package com.hyj.spark.offline

import com.huaban.analysis.jieba.JiebaSegmenter.SegMode
import com.huaban.analysis.jieba.{JiebaSegmenter, SegToken}
import com.hyj.algorithm.demo.CommonUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.udf
import org.apache.spark.{SparkConf, SparkContext}

object SparkWordCountHyj {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkWordCountHyj")
    conf.registerKryoClasses(Array(classOf[JiebaSegmenter]))
    val ssc = new SparkContext(conf)
    //    val inputFile = "hdfs://master:9000/usr/local/hive/warehouse/badou.db/art_ext2/The_Man_of_Property.txt"
    val inputDir = "hdfs://master:9000/usr/local/hive/warehouse/badou.db/news_seg/"
    //如果直接写input_dir或者hdfs://master:9000开头 则从hdfs查找 hdfs://master:9000/user/root/input_dir
    val lines: RDD[String] = ssc.textFile(inputDir)
    //    val lines: RDD[String] = ssc.textFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\input_dir")
    val segmenter = new JiebaSegmenter()
    val broadcastSeg = ssc.broadcast(segmenter)
//    val jiebaUdf = udf { sentence: String =>
//      val exeSegmenter = broadcastSeg.value
//      exeSegmenter.process(sentence, SegMode.INDEX)
//        .toArray().map(_.asInstanceOf[SegToken].word)
//        //返回值为String
//        .filter(_.length > 1)
//    }
    val wordType=2
    val word_count_rdd: RDD[(String, Int)] = lines.flatMap(
//      _.split(" ")
      x=> {
        val exeSegmenter = broadcastSeg.value
        val words: Array[String] = exeSegmenter.process(x, SegMode.INDEX)
          .toArray().map(_.asInstanceOf[SegToken].word)
          .filter(_.length > 1)
        words
      }
    )
      .filter(x => {
        //          CommonUtil.trimWord(x,1)!=null
        CommonUtil.trimWord(x, wordType) != null
      }
      )
      .map(x => (CommonUtil.trimWord(x, wordType), 1)).reduceByKey(_ + _)
    word_count_rdd.take(5).foreach(println)
    val k = 10
    val topK: Array[(String, Int)] = word_count_rdd.sortBy(tuple => tuple._2, ascending = false).take(k)
    topK.foreach(println)
  }
}
