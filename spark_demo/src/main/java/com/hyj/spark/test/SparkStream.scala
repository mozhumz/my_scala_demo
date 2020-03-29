package com.hyj.spark.test

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStream {


  def main(args: Array[String]): Unit = {
     val conf: SparkConf = new SparkConf().setAppName("wc").setMaster("local[2]")
    val context: StreamingContext = new StreamingContext(conf, Seconds(3))
    val streamingContext = context
    val socketLineDstream: ReceiverInputDStream[String] = streamingContext.socketTextStream("master", 9999)
    val wordDstream = socketLineDstream.flatMap(line => line.split(" "))


  }

}
