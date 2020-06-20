package com.hyj.spark.offline

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.{BinaryLogisticRegressionTrainingSummary, LogisticRegression}
import org.apache.spark.ml.feature.RFormula
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.storage.StorageLevel
import com.hyj.spark.offline.Feature.GenerateFeature

/**
  * spark之多元线性回归-模型拟合
  */
object LRTest {
  def main(args: Array[String]): Unit = {

    //    System.setProperty("HADOOP_USER_NAME","root")
//    val warehouse = "hdfs://master:9000/warehouse"

    val spark = SparkSession
      .builder()
      //      .config("spar.sql.warehouse.dir",warehouse)
      .appName("LR test")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    //    1. 读取对应所需的数据
    val priors = spark.sql("select * from badou.priors where order_id!='order_id'")
    val orders = spark.sql("select * from badou.orders where order_id!='order_id'")
    //priors和trains表结构相同
    val trains = spark.sql("select * from badou.trains where order_id!='order_id'")

    //      orders.show()
    //    priors.show()
    //    trains.show()
    //    2. 获取生成的特征
    val op = orders.join(priors, "order_id")
      .persist(StorageLevel.MEMORY_AND_DISK) // prior product_ids
    /**
      *userFeat:用户购买的商品数，平均每个订单商品数
      * prodFeature: 商品销量 商品再次被购买数和再次被购买比率 [1,1,0,1,0] 3/5
      */
    val (userFeat, prodFeat) = GenerateFeature(priors, orders, op)

    val opTrain = orders.join(trains, "order_id") //train product_ids

    //    train的数据标签为1
    val user_real = opTrain.select("product_id", "user_id").distinct()
      .withColumn("label", lit(1))
//outer,full,fullouter 表示取并集
    val pt_df: DataFrame = op.join(user_real, Seq("product_id", "user_id"), "full")
//    pt_df.show(false)
    /**
      * +----------+-------+--------+--------+------------+---------+-----------------+----------------------+-----------------+---------+-----+
      * |product_id|user_id|order_id|eval_set|order_number|order_dow|order_hour_of_day|days_since_prior_order|add_to_cart_order|reordered|label|
      * +----------+-------+--------+--------+------------+---------+-----------------+----------------------+-----------------+---------+-----+
      * |1         |105565 |null    |null    |null        |null     |null             |null                  |null             |null     |1    |
      * |1         |142357 |2624791 |prior   |4           |6        |14               |11.0                  |3                |0        |null |
      * |1         |142357 |442972  |prior   |10          |6        |11               |30.0                  |15               |1        |null |
      * |1         |162795 |388491  |prior   |8           |5        |15               |4.0                   |1                |1        |null |
      */
    //    priors表中的标签为0，train中的标签为1，合并在一起
    val trainData = pt_df
      .select("user_id", "product_id", "label").distinct().na.fill(0)
//    trainData.show(false)
    /**
      * +-------+----------+-----+
      * |user_id|product_id|label|
      * +-------+----------+-----+
      * |15207  |10032     |0    |
      * |205096 |1004      |0    |
      * |100259 |10151     |0    |
      * |110942 |10167     |0    |
      * |76323  |10199     |0    |
      * |121860 |1025      |0    |
      * |45782  |10339     |0    |
      * |50274  |10644     |0    |
      * |111893 |10671     |0    |
      * |16854  |10749     |0    |
      * |25275  |10753     |0    |
      * |42398  |10960     |0    |
      * |236    |11123     |0    |
      * |136460 |11261     |1    |
      * |182962 |1127      |0    |
      * |22095  |11707     |0    |
      * |1656   |11988     |0    |
      * |168165 |12013     |0    |
      * |120222 |12033     |1    |
      * |2555   |12211     |0    |
      * +-------+----------+-----+
      */
    //
//    //    关联特征：根据user_id关联用户特征，根据product_id关联product特征
    val train = trainData.join(userFeat, "user_id")
      .join(prodFeat, "product_id")
//
//    //    rformula 将对应值（不管离散还是连续）转变成向量形式放入到features这一列中
    val rformula = new RFormula().setFormula("label~ u_avg_day_gap + u_ord_cnt + u_prod_size " +
      "+ u_prod_uni_size + u_avg_ord_prods + prod_sum_rod + prod_rod_rate + prod_cnt")
      .setFeaturesCol("features").setLabelCol("label")
//    //  formula的数据转换
    val df = rformula.fit(train).transform(train)
//      .selectExpr("*")
      .select("features", "label")
    df.show(false)
    /**
      * +-------------------------------------------------------------------------------------+-----+
      * |features                                                                             |label|
      * +-------------------------------------------------------------------------------------+-----+
      * |[8.538461538461538,13.0,100.0,39.0,8.333333333333334,503.0,0.63510101010101,792.0]   |0    |
      * |[6.0,7.0,72.0,57.0,12.0,503.0,0.63510101010101,792.0]                                |0    |
      * |[16.25,4.0,40.0,34.0,13.333333333333334,503.0,0.63510101010101,792.0]                |0    |
      * |[3.2,100.0,683.0,105.0,6.898989898989899,503.0,0.63510101010101,792.0]               |0    |
      * |[7.384615384615385,39.0,617.0,189.0,16.236842105263158,503.0,0.63510101010101,792.0] |0    |
      * |[8.473684210526315,19.0,159.0,87.0,8.833333333333334,503.0,0.63510101010101,792.0]   |0    |
      * |[11.666666666666666,6.0,54.0,46.0,10.8,503.0,0.63510101010101,792.0]                 |0    |
      * |[16.41176470588235,17.0,88.0,53.0,5.5,503.0,0.63510101010101,792.0]                  |0    |
      * |[6.638297872340425,47.0,372.0,116.0,8.08695652173913,503.0,0.63510101010101,792.0]   |0    |
      * |[12.23076923076923,26.0,181.0,68.0,7.24,503.0,0.63510101010101,792.0]                |0    |
      * |[5.671875,64.0,484.0,131.0,7.682539682539683,503.0,0.63510101010101,792.0]           |1    |
      * |[5.0,73.0,477.0,160.0,6.625,503.0,0.63510101010101,792.0]                            |0    |
      * |[16.0,13.0,253.0,109.0,21.083333333333332,503.0,0.63510101010101,792.0]              |0    |
      */
    //    ################################LR model###############################
    //    实例化LR模型 setElasticNetParam 0:L2,1:L1 通过正则解决过拟合问题 0-1之间是L1和L2正则的组合
    val lr = new LogisticRegression().setMaxIter(10).setRegParam(0) //具体参数值
    //    将训练集70%作为训练，30%作为test
    val Array(trainingData, testData) = df.randomSplit(Array(0.7, 0.3))
    //    训练模型
    val lrModel = lr.fit(trainingData)
    //    打印参数(权重w,截距b)
    print(s"weights : ${lrModel.coefficients} intercept : ${lrModel.intercept}")
    //    weights : [-0.01623261812237973,-0.01705715241719569,4.75002368381124E-4,-0.008066900402337377,0.03201556250886345,9.126310752565968E-7,1.6691532815019365,5.62221181248999E-7] intercept : -2.3711237931175457
    //    收集日志
    val trainingSummary = lrModel.summary
    val objectHistory = trainingSummary.objectiveHistory
    //    打印loss
    objectHistory.foreach(loss => println(loss))
    /**
      *0.32493643035795355
      *0.3144145131457065
      *0.31314820933977033
      *0.31095074294845115
      *0.31069472754120697
      *0.31033208710810756
      *0.3092607378269133
      *0.30833865777020764
      *0.30735449291274686
      *0.3068926473576491
      *0.3066430053972412
      * */
    //  用auc来评价模型训练结果 auc是roc曲线下的面积
    val binarySummary = trainingSummary.asInstanceOf[BinaryLogisticRegressionTrainingSummary]
    val roc = binarySummary.roc
    roc.show()
    println(binarySummary.areaUnderROC) //0.6814142835249827

  }
}

