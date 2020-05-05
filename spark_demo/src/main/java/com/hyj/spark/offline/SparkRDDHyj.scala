package com.hyj.spark.offline

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object SparkRDDHyj {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkRDDHyj").setMaster("local[*]")
    val context = new SparkContext(conf)

    val listRdd: RDD[Int] = context.parallelize(List(1, 2, 3, 4))
    /**
      * 默认集合数据的分区数量：节点的最大核心数与2比较
      * conf.getInt("spark.default.parallelism", math.max(totalCoreCount.get(), 2))
      * 指定分区数为3
      */
    //    val listRdd2: RDD[Int] = context.makeRDD(List(1,2,3,4,5),3)
    //    listRdd2.saveAsTextFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output_dir")
    val fileRDD: RDD[String] = context.textFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\input_dir",
      3)
    fileRDD.saveAsTextFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output_dir2")

  }

  def rddOperation(conf: SparkConf): Int = {
    val ssc = new SparkContext(conf)
    val rdd: RDD[Int] = ssc.makeRDD(1 to 10)

    /**
      * map算子和mapPartitions算子区别：
      * map每条数据会往多个executor均匀分发
      * mapPartitions则根据rdd的分区数num，分发到num个executor，相比map，减少网络IO，效率更高
      * 但是如果某个分区的数据过大，超过executor内存则会出现OOM
      */
    val mapRdd: RDD[Int] = rdd.map(_*2)
    rdd.mapPartitions(datas=>{
      datas.map(_*2)
    })

    return 0
  }
}
