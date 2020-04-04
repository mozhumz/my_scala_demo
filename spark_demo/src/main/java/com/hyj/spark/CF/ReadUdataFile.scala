package com.hyj.spark.CF

import org.apache.spark.sql.SparkSession

object ReadUdataFile {
  def main(args: Array[String]): Unit = {
//    System.setProperty("HADOOP_USER_NAME","root")
//    val warehouse = "hdfs://192.168.174.134:9000/warehouse"

//    val spark = SparkSession.builder()
////      .config("spar.sql.warehouse.dir",warehouse)
//      .master("local[2]")
//      .appName("User Base")
////      .enableHiveSupport()
//      .getOrCreate()

//    val df = spark.read.("C:\\Users\\zheng\\PycharmProjects\\14mr\\data\\u.data")
//    df.show()

//    val a = Array(("a",2),("b",1),("c",4))
//    print(a)

    val s = """52_4, 302_3, 168_5, 153_4, 98_4, 100_4, 6_3"""
    val s_seq = s.split(",").toSeq
    print(s_seq.map(_.split("_")))








  }

}
