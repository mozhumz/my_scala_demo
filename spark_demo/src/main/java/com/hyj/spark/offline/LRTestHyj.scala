package com.hyj.spark.offline

import com.hyj.spark.offline.Feature.GenerateFeature
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.{BinaryLogisticRegressionTrainingSummary, LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.feature.RFormula
import org.apache.spark.ml.linalg
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.storage.StorageLevel

/**
  * spark之多元线性回归-模型拟合
  */
object LRTestHyj {
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
    /**
      *    1. 读取对应所需的数据
      */
    val priors = spark.sql("select * from badou.priors where order_id!='order_id'")
    val orders = spark.sql("select * from badou.orders where order_id!='order_id'")
    //priors和trains表结构相同
    val trains = spark.sql("select * from badou.trains where order_id!='order_id'")

    /**
      * 2 获取用户特征和商品特征
      */
    val op: DataFrame = priors.join(orders,"order_id")
    op.persist(StorageLevel.MEMORY_AND_DISK_SER)

    val  (userFeat, prodFeat) = GenerateFeature(priors,orders,op)

    /**
      * 3 关联商品真实数据 给user_id product_id添加标签1
      */
    val user_real: DataFrame = orders.join(trains, "order_id").select("user_id", "product_id")
      .distinct().withColumn("label", lit(1))

    /**
      * 4 真实数据和商品数据取并集 添加标签0
      */
    val pt_df: DataFrame = op
      .join(user_real, Seq("user_id", "product_id"),"full")
      .select("user_id", "product_id","label").distinct()
//    pt_df.show(false)
  val train_data: DataFrame = pt_df.na.fill(0)
    train_data.show(false)
    /**
      * 5 关联用户特征和商品特征
      */
    val train_df: DataFrame = train_data.join(userFeat,"user_id").join(prodFeat,"product_id")

    /**
      * 6 模型训练
      */
    //1 将数据变为向量
    //prod: prod_sum_rod prod_cnt prod_rod_rate
    //user: u_avg_ord_prods u_prod_uni_size u_prod_size u_avg_day_gap u_ord_cnt
    val formula: RFormula = new RFormula().setFormula("label~ u_avg_ord_prods+u_prod_uni_size+u_prod_size+u_avg_day_gap+u_ord_cnt" +
      "+prod_sum_rod+prod_cnt+prod_rod_rate")
      .setFeaturesCol("features").setLabelCol("label")
    val df: DataFrame = formula.fit(train_df).transform(train_df).select("features","label")
    df.show(false)
    //创建LR
    val lr: LogisticRegression = new LogisticRegression().setMaxIter(10).setRegParam(0)
    //划分数据集
    val Array(train,test)=df.randomSplit(Array(0.7,0.3))
    val model: LogisticRegressionModel = lr.fit(train)

    val w: linalg.Vector = model.coefficients
    val b: Double = model.intercept
    println("w:\n"+w)
    println("b:\n"+b)

    val roc: DataFrame = model.binarySummary.roc
    val auc: Double = model.binarySummary.areaUnderROC

    println("roc:\n"+roc)
    println("auc:\n"+auc)

    /**
      * w:
      * [0.03213612345733875,-0.008110931845648468,4.97480733002297E-4,-0.01665463265680159,-0.01703680849706705,9.483355971022713E-7,5.893970711895661E-7,1.6774232387790167]
      * b:
      * -2.3733955053742295
      * roc:
      * [FPR: double, TPR: double]
      * auc:
      * 0.6816727577444573
      */

  }
}

