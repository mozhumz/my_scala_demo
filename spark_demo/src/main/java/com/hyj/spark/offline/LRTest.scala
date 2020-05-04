package com.hyj.spark.offline

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.{BinaryLogisticRegressionTrainingSummary, LogisticRegression}
import org.apache.spark.ml.feature.RFormula
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.storage.StorageLevel
import com.hyj.spark.offline.Feature.GenerateFeature

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
    val (userFeat, prodFeat) = GenerateFeature(priors, orders, op)

    val opTrain = orders.join(trains, "order_id") //train product_ids

    //    train的数据标签为1
    val user_real = opTrain.select("product_id", "user_id").distinct()
      .withColumn("label", lit(1))

    //    priors表中的标签为0，train中的标签为1，合并在一起
    val trainData = op.join(user_real, Seq("product_id", "user_id"), "outer")
      .select("user_id", "product_id", "label").distinct().na.fill(0)
//
//    //    关联特征：根据user_id关联用户特征，根据product_id关联product特征
//    val train = trainData.join(userFeat, "user_id")
//      .join(prodFeat, "product_id")
//
//    //    rformula 将对应值（不管离散还是连续）转变成向量形式放入到features这一列中
//    val rformula = new RFormula().setFormula("label~ u_avg_day_gap + u_ord_cnt + u_prod_size " +
//      "+ u_prod_uni_size + u_avg_ord_prods + prod_sum_rod + prod_rod_rate + prod_cnt")
//      .setFeaturesCol("features").setLabelCol("label")
//    //  formula的数据转换
//    val df = rformula.fit(train).transform(train).select("features", "label")
//    df.show()
    //    ################################LR model###############################
    //    实例化LR模型 setElasticNetParam 0:L2,1:L1 通过正则解决过拟合问题 0-1之间是L1和L2正则的组合
   /* val lr = new LogisticRegression().setMaxIter(10).setRegParam(0) //具体参数值
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
      *0.3066430053972412*/
    //  用auc来评价模型训练结果 auc是roc曲线下的面积
    val binarySummary = trainingSummary.asInstanceOf[BinaryLogisticRegressionTrainingSummary]
    val roc = binarySummary.roc
    roc.show()
    println(binarySummary.areaUnderROC) //0.6814142835249827*/

  }
}

