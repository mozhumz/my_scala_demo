package com.hyj.spark.util


import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkUtils {

  /**
    * 创建批处理配置对象
    * setMaster：设置运行模式 local:单线程模式，local[n]:以n个线程运行，local[*]:以所有CPU核数的线程运行
    * setAppName：设置应用名称
    * set：各种属性(spark.testing.memory分配4G内存来测试)
    */

  //TODO 本地运行用这个
  lazy val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("TestHive").set("spark.testing.memory", "4294967296")

  //TODO 打包运行用这个
  //  lazy val sparkConf: SparkConf = new SparkConf().setAppName("SparkJob")

  //创建session
  //  lazy val sparkSession: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

  //创建session，并启用hive
  lazy val sparkSessionWithHive: SparkSession = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()

}


