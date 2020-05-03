package streaming.kafka

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * spark-kafka消费者：消费kafka生产的数据进行wordcount
  * kafka生产者：kafka-console-producer.sh --broker-list master:9092 --topic first
  * 消费者:streaming.KafkaTestHyj#main(java.lang.String[])
  *
  */
object KafkaTestHyj {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("myReceiverTest").setMaster("local[*]")
    //每五秒进行一次计算
    val ssc = new StreamingContext(conf, Seconds(5))
    //设置检查点目录 在进行有状态统计时，方便将前面计算的结果存到磁盘
    ssc.sparkContext.setCheckpointDir("cp")

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
    var messages: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics2, kafkaParams))


    /**
      *注意：kafka的消息是KV形式，key默认为null，value才是生产的数据
      */
      //1 无状态统计 第一个五秒的数据和下一个五秒的数据相互独立统计
    //1)解析kafka传过来的json数据
    //      val words=messages.map(x=>JSON.parseObject(x.value().toString,classOf[KafkaMsgObj]).getName)
    //        .flatMap(_.split(" "))
    //2)解析kafka传过来的字符串数据
    val words = messages.flatMap(t => t.value().toString.split(" "))
//    words.map((_, 1)).reduceByKey(_ + _).print()

    val value: DStream[(String, Int)] = words.map((_, 1)).updateStateByKey((seq: Seq[Int], option: Option[Int]) => {
      val sum = option.getOrElse(0) + seq.sum
      Option(sum)
    })
    //    2 有状态统计 当前五秒内的数据和前面所有的数据进行聚合
      val res=value
    res.print()


    ssc.start()
    ssc.awaitTermination()
  }
}
