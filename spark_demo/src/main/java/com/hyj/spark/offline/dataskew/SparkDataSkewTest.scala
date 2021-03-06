package com.hyj.spark.offline.dataskew

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import java.util.Random
import com.hyj.spark.util.MyUtils
/**
 * spark数据倾斜解决
 */
object SparkDataSkewTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("dataSkewApp").setMaster("local")
    val sc=new SparkContext(conf)

    val data: RDD[String] = sc.textFile("file:///G:\\idea_workspace\\my_scala_demo\\input\\word.txt")

    val wordRdd: RDD[(String, Int)] = data.flatMap(_.split(" ")).map((_, 1))


    MyUtils.printRdd("wordRdd:",wordRdd)
    combineWith2Steps(wordRdd)
  }

  /**
   * 随机key-两阶段聚合
   * @param rdd
   */
  def combineWith2Steps(rdd:RDD[(String, Int)]):Unit={
    //对rdd每个key加上随机前缀
    val randomRdd: RDD[(String, Int)] = rdd.map(x => {
      val r = new Random()
      (r.nextInt(5) + "_" + x._1, x._2)
    })
    MyUtils.printRdd("randomRdd:",randomRdd)
    //进行第一次聚合 numPartitions=2表示shuffle read task数量=2
    val firstCombineRdd: RDD[(String, Int)] = randomRdd.reduceByKey((v1, v2) => {
      v1 + v2
    },2)
//    randomRdd.reduce((x1,x2)=>{(x1._1,x1._2+x2._2)})
    //对聚合后的每个数据的key，去除随机前缀
    val rddWithoutPre: RDD[(String, Int)] = firstCombineRdd.map(x => (x._1.split("_")(1), x._2))
    //进行第二次聚合
    val res: RDD[(String, Int)] = rddWithoutPre.reduceByKey(_ + _,2)
    MyUtils.printRdd("res",res)
  }
}
