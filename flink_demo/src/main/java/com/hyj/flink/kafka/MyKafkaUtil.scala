package com.hyj.flink.kafka

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer08, FlinkKafkaProducer08}

object MyKafkaUtil {
  val properties: Properties = new Properties
  properties.put("bootstrap.servers","master:9092")
  properties.put("zookeeper.connect","master:2181")
  properties.put("group.id","test_flink")

  def getKafkaSource:FlinkKafkaConsumer08[String]={
    new FlinkKafkaConsumer08[String]("badou",new SimpleStringSchema(),properties)
  }

  def getKafkaSink:FlinkKafkaProducer08[String]={
    new FlinkKafkaProducer08[String]("master:9092","badou2",new SimpleStringSchema())
  }

}
