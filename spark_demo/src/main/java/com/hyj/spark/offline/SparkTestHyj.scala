package com.hyj.spark.offline

import org.apache.spark.SparkContext
import org.apache.spark.ml.feature.RFormula
import org.apache.spark.sql.SparkSession

object SparkTestHyj {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()

//    val dataset = spark.createDataFrame(Seq(
//      (7, 1, 18, 1.0),
//      (8, 2, 12, 0.0),
//      (9, 3, 15, 0.0)
//    )).toDF("id", "country", "hour", "clicked")
//    val formula = new RFormula()
//      .setFormula("clicked ~ country + hour")
//      .setFeaturesCol("features")
//      .setLabelCol("label")
//    val output = formula.fit(dataset).transform(dataset)
//    output.select("features", "label").show()
    val sc: SparkContext = spark.sparkContext
    val a =sc.parallelize(Array(("1",4.0),("2",8.0),("3",9.0)))
    val b=sc.parallelize(Array(("1",2.0),("2",8.0)))

    val c=a.leftOuterJoin(b)
    c.foreach(println)
    val lines = sc.textFile("hdfs://...")
    val words = lines.flatMap(_.split(" "))
    val rdd = words.map((_, 1))
    val rdd2=sc.makeRDD(Array((1,1),(2,1)))
    //打印结果出来如下：
    //(2,(8.0,Some(8.0)))
    //(3,(9.0,None))
    //(1,(4.0,Some(2.0)))

  }
}
