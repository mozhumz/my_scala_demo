package streaming

import com.alibaba.fastjson.JSON
import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
//import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
//import org.apache.spark.streaming.kafka._

object DirectKafaTest {
  def main(args: Array[String]): Unit = {
    val Array(brokers, groupId, topics) = Array("192.168.174.134:9092","group1","badou")
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount")
      .setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(2))



    // Create direct kafka stream with brokers and topics

    val topicsSet = topics.split(",").toSet
    //    kafka 0-8
    //    val kafkaParams = Map[String,String]("metadata.broker.list"->brokers,"group.id"->groupId)
    //    kafka 0-10
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer])
    val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))
    //    kafka 0-8
    //    val km = new KafkaManager(kafkaParams)
    //    val messages = KafkaUtils.createDirectStream[
    //      String,
    //      String,
    //      StringDecoder,
    //      StringDecoder](ssc,kafkaParams,topicsSet)


    //    offset获取，打印
    //    var offsetRanges = Array[OffsetRange]()
    //    messages.foreachRDD{rdd=>
    //      offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
    //      for(offsize<- offsetRanges){
    //        km.commitOffsetsToZK(offsetRanges)
    //        print(s"${offsize.topic}, ${offsize.partition},${offsize.fromOffset},${offsize.untilOffset}")
    //      }
    //    }

    // Get the lines, split them into words, count the words and print
    val lines = messages.map(x=>JSON.parseObject(x.value.toString,classOf[Orders]).order_dow)
    val words = lines.flatMap(_.split(" "))
    words.map(x => (x, 1L)).reduceByKey(_ + _).print()
    //    messages.map(_.value).print()

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }

}
