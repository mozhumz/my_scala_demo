package com.hyj.spark.offline.sparksql

import java.util.Properties

import com.hyj.spark.offline.sparksql.config.MySqlConf
import org.apache.spark.sql.SparkSession

object SparkSqlHyj {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("Feature Transform")
//      .config("hive.metastore.uris","thrift://master:9083")
      //本地调试要加上 默认并行度=cpu核心数
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()
    //    spark shell [client] 实例化sc,spark
    val orders = spark.sql("select * from badou.orders where order_id!='order_id' ")
    val priors = spark.sql("select * from badou.priors where order_id!='order_id' ")

    orders.show()
    println("------------------------------------")
    priors.show()

    //将数据写入mysql-badou
    val properties: Properties = new Properties
    properties.put("user",MySqlConf.username)
    properties.put("password",MySqlConf.password)
    //每次批量写入10000条
    properties.put("batchsize","10000")
    println("------write orders start--------------")
    /**
      * 默认为SaveMode.ErrorIfExists模式，该模式下，如果数据库中已经存在该表，则会直接报异常，导致数据不能存入数据库.另外三种模式如下：
      * SaveMode.Append 如果表已经存在，则追加在该表中；若该表不存在，则会先创建表，再插入数据；
      * SaveMode.Overwrite 重写模式，其实质是先将已有的表及其数据全都删除，再重新创建该表，最后插入新的数据；
      * SaveMode.Ignore 若表不存在，则创建表，并存入数据；在表存在的情况下，直接跳过数据的存储，不会报错。
      */
    //    orders.write
    //      .mode(SaveMode.Append)
    //      .jdbc()
    orders.write.jdbc(MySqlConf.url,"orders",properties)
    println("------write orders ok--------------")
    println("------write priors start--------------")
    priors.write.jdbc(MySqlConf.url,"priors",properties)
    println("------write priors ok--------------")
  }


}
