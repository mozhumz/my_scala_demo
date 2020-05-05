package com.hyj.spark.offline

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object SparkRDDHyj {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkRDDHyj").setMaster("local[*]")

    rddOperation(conf)
  }

  /**
    * 分区测试
    * @param conf
    */
  def testPartition(conf: SparkConf): Unit = {
    val context = new SparkContext(conf)

    val listRdd: RDD[Int] = context.parallelize(List(1, 2, 3, 4))
    /**
      * 默认集合数据的分区数量：节点的最大核心数与2比较 取最大值
      * conf.getInt("spark.default.parallelism", math.max(totalCoreCount.get(), 2))
      * 指定分区数为3
      */
    //    val listRdd2: RDD[Int] = context.makeRDD(List(1,2,3,4,5),3)
    //    listRdd2.saveAsTextFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output_dir")
    val fileRDD: RDD[String] = context.textFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\input_dir",
      3)
    fileRDD.saveAsTextFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output_dir2")
  }

  /**
    * 算子测试
    * 注意：算子操作都在executor执行 如rdd.map rdd.flatmap，创建SparkContext的程序叫driver 如当前的main方法
    * @param conf
    * @return
    */
  def rddOperation(conf: SparkConf): Int = {
    val ssc = new SparkContext(conf)
    /**
      * 2表示该数据集存储到2个分区 假设分区数为3 则10/3 多余的数据会根据算法均分到分区
      */
    val rdd: RDD[Int] = ssc.makeRDD(1 to 10,2)

    /**
      * map算子和mapPartitions算子区别：
      * map每条数据会往多个executor均匀分发
      * mapPartitions则根据rdd的分区数num，分发到num个executor，相比map，减少网络IO，效率更高
      * 但是如果某个分区的数据过大，超过executor内存则会出现OOM
      */
    val mapRdd: RDD[Int] = rdd.map(_ * 2)
    rdd.mapPartitions(datas => {
      datas.map(_ * 2)
    }).collect().foreach(println)

    /**
      * 在mapPartitions算法的基础上 可以获取每条数据的分区号
      */
    rdd.mapPartitionsWithIndex {
      /**
        * num 分区号
        * datas 数据集
        */
      case (num, datas) => {
        datas.map((_,"partition:"+num))
      }
    }.collect().foreach(println)

    /**
      * glom将同分区的数据收集在一起 返回值Rdd的每个元素为一个分区数组Array
      */
    val glomRdd: RDD[Array[Int]] = rdd.glom()
    glomRdd.collect().foreach(x=>println(x.mkString(",")))

    0
  }
}
