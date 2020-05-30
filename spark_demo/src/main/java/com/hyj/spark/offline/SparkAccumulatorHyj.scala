package com.hyj.spark.offline


import java.util

import org.apache.hadoop.hbase.util.CollectionUtils
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}
import shapeless.ops.nat.GT.>

/**
  * spark-
  */
object SparkAccumulatorHyj {
  def main(args: Array[String]): Unit = {
    Logger.getLogger(SparkRDDHyj.getClass).setLevel(Level.INFO)
    val conf: SparkConf = new SparkConf().setAppName("SparkRDDHyj").setMaster("local[*]")
    val sc = new SparkContext(conf)



//    longAccumulator(sc)
    myWordAccumulator(sc)
    sc.stop()

  }
  def longAccumulator(sc: SparkContext): Unit = {
    val rdd: RDD[Long] = sc.makeRDD(1L to 5L, 2)
    //    val sum: Int = rdd.reduce(_+_)
    //    var sum = 0
    //    rdd.map(x => sum += x)
    //    println(sum)
    val accumulator: LongAccumulator = sc.longAccumulator
    rdd.foreach(accumulator.add)
    println(accumulator.value)
  }

  def myWordAccumulator(sc: SparkContext):Unit={
    val rdd: RDD[String] = sc.makeRDD(List("hive","hbase","word","math","zero"),2)
    val accumulator: WordAccumulator = new WordAccumulator
    sc.register(accumulator)
    rdd.foreach(accumulator.add)
    println(accumulator.value)

  }

  class WordAccumulator extends AccumulatorV2[String, util.ArrayList[String]] {
    val  list = new util.ArrayList[String]()

    /**
      * 是否为初始值
      * @return
      */
    override def isZero: Boolean = {
      list.isEmpty
    }

    /**
      * 复制累加器
      * @return
      */
    override def copy(): AccumulatorV2[String, util.ArrayList[String]] = {
      this
    }

    /**
      * 重置累加器
      */
    override def reset(): Unit = list.clear()

    /**
      * 同分区的进行累加
      * @param v
      */
    override def add(v: String): Unit = {
      if(v!=null&&v.contains("h")){
        list.add(v)
      }
    }

    /**
      * 不同分区的累加器合并
      * @param other
      */
    override def merge(other: AccumulatorV2[String, util.ArrayList[String]]): Unit = {
      if(!CollectionUtils.isEmpty(other.value)){
        list.addAll(other.value)
      }
//      list.add("ww")
    }

    /**
      * 获取累加器的值
      * @return
      */
    override def value: util.ArrayList[String] = list
  }

}
