package com.hyj.spark.offline.dataskew

import com.hyj.spark.offline.dataskew.SparkDataSkewTest.getRdd
import com.hyj.spark.util.MyUtils
import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object PartitionTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("dataSkewApp").setMaster("local")
    val sc=new SparkContext(conf)
    val wordRdd: _root_.org.apache.spark.rdd.RDD[(_root_.scala.Predef.String, Int)]
    = getRdd(sc,"file:///G:\\idea_workspace\\my_scala_demo\\input\\word.txt")
    /**
      * 分区器测试
      */
    //None
    val partitions0 = sc.textFile("file:///G:\\idea_workspace\\my_scala_demo\\input\\word.txt",3).partitioner
    //kv类型的rdd才有分区器 默认为HashPartitioner
    val partitioner1: Option[Partitioner] = wordRdd.reduceByKey(_+_).partitioner
    //    wordRdd.reduceByKey(new MyPartitioner(3),_+_).collect().foreach(println)
        wordRdd.partitionBy(new MyPartitioner(2)).collect().foreach(println)

    val urlRdd: RDD[String] = sc.textFile("file:///G:\\idea_workspace\\my_scala_demo\\input\\url.txt")
    //reduceByKey的结果并不会受分区器的影响
    urlRdd.map((_,1)).reduceByKey(new MyPartitioner(2),_+_).collect().foreach(println)
  }

  private def getRdd(sc: SparkContext,path:String): RDD[(String, Int)] = {
    val data: RDD[String] = sc.textFile(path)
    val wordRdd: RDD[(String, Int)] = data.flatMap(_.split(" ")).map((_, 1))
    MyUtils.printRdd("wordRdd:",wordRdd)
    wordRdd
  }
}
