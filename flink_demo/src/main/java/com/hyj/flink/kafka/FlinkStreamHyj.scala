package com.hyj.flink.kafka

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08
import org.apache.flink.api.scala._

object FlinkStreamHyj {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)

    val properties: Properties = new Properties
    properties.put("bootstrap.servers","master:9092")
    properties.put("zookeeper.connect","master:2181")
    properties.put("group.id","test_flink")

    val stream: DataStream[String] = env.addSource(
      new FlinkKafkaConsumer08[String]("badou",new SimpleStringSchema(),properties))



//    wordCount(stream)
    /**
      * 从topic badou获取数据 sink到 topic badou2
      */
    stream.addSink(MyKafkaUtil.getKafkaSink)
    println("start----------------")
    env.execute()
  }

  def wordCount(stream: DataStream[String]): Unit = {
    val wordStream: DataStream[(String, Int)] = stream.setParallelism(10)
      .flatMap(_.split(" ")).map((_, 1)).keyBy(0).sum(1)
  }

}
