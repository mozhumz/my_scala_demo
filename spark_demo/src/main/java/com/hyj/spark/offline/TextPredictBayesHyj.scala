package com.hyj.spark.offline

import org.apache.spark.sql.SparkSession

object TextPredictBayesHyj {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME","root")
//    val warehouse = "hdfs://192.168.163.130:9000/user/hive/warehouse"

    val spark = SparkSession.builder()
      .master("local[2]")
      .appName("User Base")
      .enableHiveSupport()
      .getOrCreate()

    println(spark)

    spark.sql("select * from badou.orders").show()
  }
}
