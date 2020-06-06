package com.hyj.spark.offline.sparksql

import org.apache.spark.sql.SparkSession

object SparkSqlHyj {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("Feature Transform")
//      .config("hive.metastore.uris","thrift://master:9083")
      //本地调试要加上
//      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()
    //    spark shell [client] 实例化sc,spark
    val orders = spark.sql("select * from badou.orders limit 10")
    val priors = spark.sql("select * from badou.priors limit 10")

    orders.show()
    println("------------------------------------")
    priors.show()
  }
}
