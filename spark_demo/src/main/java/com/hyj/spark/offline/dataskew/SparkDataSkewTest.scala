package com.hyj.spark.offline.dataskew

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import java.util.Random

import com.hyj.spark.util.MyUtils
import org.apache.spark.broadcast.Broadcast

import scala.collection.mutable
/**
 * spark数据倾斜解决
 *
 */
object SparkDataSkewTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("dataSkewApp").setMaster("local")
    val sc=new SparkContext(conf)

    val wordRdd: _root_.org.apache.spark.rdd.RDD[(_root_.scala.Predef.String, Int)]
      = getRdd(sc,"file:///G:\\idea_workspace\\my_scala_demo\\input\\word.txt")
    val wordRdd2: _root_.org.apache.spark.rdd.RDD[(_root_.scala.Predef.String, Int)]
    = getRdd(sc,"file:///G:\\idea_workspace\\my_scala_demo\\input\\word2.txt")



//    combineWith2Steps(wordRdd,sc)
    reducejoinToMapjoin(wordRdd,wordRdd2,sc)
  }

  private def getRdd(sc: SparkContext,path:String): RDD[(String, Int)] = {
    val data: RDD[String] = sc.textFile(path)
    val wordRdd: RDD[(String, Int)] = data.flatMap(_.split(" ")).map((_, 1))
    MyUtils.printRdd("wordRdd:",wordRdd)
    wordRdd
  }

  /**
   * 1 随机key-两阶段聚合
    *
    * @param rdd
   */
  def combineWith2Steps(rdd:RDD[(String, Int)],sc:SparkContext):Unit={
    //使用广播变量 使得每个key对应的随机数均匀分发，即每个key对应的数据量拆分为x份
    val wordMap=mutable.Map[String,Int]()
    val bWordMap: Broadcast[mutable.Map[String, Int]] = sc.broadcast(wordMap)
    //对rdd每个key加上随机前缀
    val randomRdd: RDD[(String, Int)] = rdd.map(x => {
      val map=bWordMap.value
      val pre: Int = map.getOrElse(x._1,0)
      map.put(x._1,pre+1)
      ( pre%3 + "_" + x._1, x._2)

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

  /**
    * 2 将reduce join转换为mapJoin 数据较小的rdd广播出去
    * 注意：join操作是将2个rdd中具有相同key的数据进行连接，
    * 如果rdd1 rdd2的某个key数据分别有6和2条，则连接后为12条。比如rdd1 hello有6个，rdd2 hello有2个，连接后为12个hello
    * 故join操作不适合做wordcount
    */
  def reducejoinToMapjoin(rdd1:RDD[(String, Int)],rdd2:RDD[(String, Int)],sc:SparkContext):Unit={
    //连接后 hello有12个
//    val rjoinRdd: RDD[(String, (Int, Int))] = rdd1.join(rdd2)
//    MyUtils.printRdd("rjoinRdd",rjoinRdd)
//    val res2: RDD[(String, Int)] = rjoinRdd.map(x => {
//      (x._1, x._2._1 + x._2._1)
//    }).reduceByKey(_ + _)
//    MyUtils.printRdd("res2:",res2)

    //对较小的rdd1先进行聚合 然后广播出去
    val map1=mutable.Map[String,Int]()
    for(t<-rdd1.collect()){
      val count=map1.getOrElse(t._1,0)
      map1.put(t._1,t._2+count)
    }
    val bMap1: Broadcast[mutable.Map[String, Int]] = sc.broadcast(map1)
    //对rdd2进行聚合，然后和广播出去的rdd1进行map join
    val res: RDD[(String, Int)] = rdd2.reduceByKey(_+_).map(x => {
      val rdd1Map = bMap1.value
      val count = rdd1Map.getOrElse(x._1, 0)
      (x._1, x._2 + count)
    })
    MyUtils.printRdd("res:",res)


  }
}
