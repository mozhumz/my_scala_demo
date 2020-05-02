package streaming


import com.alibaba.fastjson.JSON
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * spark-kafka消费者：消费kafka生产的数据进行wordcount
  */
object KafkaTestHyj {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("myReceiverTest").setMaster("local[*]")
    //每五秒进行一次计算
    val ssc = new StreamingContext(conf, Seconds(5))
    val Array(brokers, groupId, topics) = Array("master:9092", "group1", "first")
    val topics2 = topics.split(",").toSet
    //    val brokers=Array("master:9092")
    //    val groupId=Array("group1")
    //从kafka采集数据
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    val messages = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics2, kafkaParams))

    /**
      *注意：kafka的消息是KV形式，key默认为null，value才是生产的数据
      */
    //1)解析kafka传过来的json数据
    //      val words=messages.map(x=>JSON.parseObject(x.value().toString,classOf[KafkaMsgObj]).getName)
    //        .flatMap(_.split(" "))
    //2)解析kafka传过来的字符串数据
    val words = messages.flatMap(t => t.value().toString.split(" "))

    words.map((_, 1)).reduceByKey(_ + _).print()


    ssc.start()
    ssc.awaitTermination()
  }
}
