package streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ConnectException, Socket}

import com.alibaba.fastjson.JSON
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaTestHyj {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    val conf=new SparkConf().setAppName("myReceiverTest").setMaster("local[*]")
    val ssc=new StreamingContext(conf,Seconds(5))
    val Array(brokers, groupId, topics) = Array("master:9092","group1","first")
    val topics2=topics.split(",").toSet
//    val brokers=Array("master:9092")
//    val groupId=Array("group1")
    val kafkaParams=Map[String,Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    val messages = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics2, kafkaParams))

    messages.map(x=>JSON.parseObject(x.value().toString,classOf[KafkaMsgObj]).getName)
      .flatMap(_.split(" ")).print()


    ssc.start()
    ssc.awaitTermination()
  }
}
