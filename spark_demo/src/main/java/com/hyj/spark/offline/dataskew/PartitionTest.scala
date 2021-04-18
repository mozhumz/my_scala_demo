package com.hyj.spark.offline.dataskew

import java.io.PrintWriter
import java.util.Random
import java.io.File

import com.hyj.spark.offline.dataskew.SparkDataSkewTest.getRdd
import com.hyj.spark.util.MyUtils
import org.apache.spark.{HashPartitioner, Partitioner, RangePartitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import java.util.concurrent.{Callable, Executors, Future}

import org.apache.log4j.{Level, Logger}

object PartitionTest {
  def main(args: Array[String]): Unit = {
   Logger.getLogger("org.apache.spark").setLevel(Level.INFO)
    //    testPartition()
    val logger: Logger = Logger.getLogger(PartitionTest.getClass)
    logger.setLevel(Level.INFO)
    sortBigFileOfNum(logger)
  }

  def testPartition(): Unit = {
    val conf = new SparkConf().setAppName("dataSkewApp").setMaster("local")
    val sc = new SparkContext(conf)
    val wordRdd: _root_.org.apache.spark.rdd.RDD[(_root_.scala.Predef.String, Int)]
    = getRdd(sc, "file:///G:\\idea_workspace\\my_scala_demo\\input\\word.txt")
    /**
      * 分区器测试
      */
    //None
    val partitions0 = sc.textFile("file:///G:\\idea_workspace\\my_scala_demo\\input\\word.txt", 3).partitioner
    //kv类型的rdd才有分区器 默认为HashPartitioner
    val partitioner1: Option[Partitioner] = wordRdd.reduceByKey(_ + _).partitioner
    //    wordRdd.reduceByKey(new MyPartitioner(3),_+_).collect().foreach(println)
    wordRdd.partitionBy(new MyPartitioner(2)).collect().foreach(println)
    wordRdd.repartitionAndSortWithinPartitions(new RangePartitioner(3, wordRdd))

    val urlRdd: RDD[String] = sc.textFile("file:///G:\\idea_workspace\\my_scala_demo\\input\\url.txt")
    //reduceByKey的结果并不会受分区器的影响
    urlRdd.map((_, 1)).reduceByKey(new MyPartitioner(2), _ + _).collect().foreach(println)
  }

  private def getRdd(sc: SparkContext, path: String): RDD[(String, Int)] = {
    val data: RDD[String] = sc.textFile(path)
    val wordRdd: RDD[(String, Int)] = data.flatMap(_.split(" ")).map((_, 1))
    MyUtils.printRdd("wordRdd:", wordRdd)
    wordRdd
  }



  def sortBigFileOfNum(logger: Logger): Unit = {
    logger.info("sortBigFileOfNum start...")
    val conf = new SparkConf().setAppName("dataSkewApp").setMaster("local")
    conf.set("spark.yarn.am.waitTime","1000s")
    conf.set("spark.local.dir","G:\\tmp")
    val sc = new SparkContext(conf)
    //文件目录
    val path = "file:///G:\\tmp\\hive\\*\\*.txt"
    //wholeTextFiles得到的rdd key=文件名 value=文件内容
    val rdd0: RDD[(String, String)] = sc.wholeTextFiles(path)
    MyUtils.printRdd("rdd0:",rdd0)
    //读取文件目录下的所有文件
    val data: RDD[String] = sc.textFile(path)
    val rdd: RDD[(Int, Int)] = data.flatMap(x => x.split(","))
      .filter(x => x != "" && x != " ").map(x=>(x.toInt, 1))
    rdd.take(10).foreach(println)

//    val count: Long = rdd.count()
//    5950321822
//    println("count:"+count)
//    val res: RDD[(Int, Int)] = rdd.repartitionAndSortWithinPartitions(new HashPartitioner(10))
//    val res: RDD[(Int, Int)] = rdd.repartitionAndSortWithinPartitions(new HashPartitioner(10))
    val res: RDD[(Int, Int)] = rdd.repartitionAndSortWithinPartitions(new RangePartitioner(10,rdd))
//    res.saveAsTextFile("hdfs://master:9000/tmp/numTest")
    res.coalesce(1).saveAsTextFile("file:///F:\\tmp\\numTest"+Math.random()*Int.MaxValue)
    logger.info("sortBigFileOfNum done")

  }
}
