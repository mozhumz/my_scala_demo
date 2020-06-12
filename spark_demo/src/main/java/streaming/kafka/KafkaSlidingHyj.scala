package streaming.kafka

import java.util

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies, LocationStrategy}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * spark滑动窗口：
  * 消费kafka的数据，进行窗口滑动
  */
object KafkaSlidingHyj {
  def main(args: Array[String]): Unit = {
    /**
      * scala自带的滑动窗口
      */
        val iterator: Iterator[List[Int]] = List(1,2,3,4,5,6).sliding(3,1)
        for(list<-iterator){
          println(list)
        }

//    kafkaSlide

  }

  private def kafkaSlide: Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    val sparkConf: SparkConf = new SparkConf().setAppName("KafkaSlidingHyj").setMaster("local[*]")
    //解决报错 object not serializable (class: org.apache.kafka.clients.consumer.ConsumerRecord
    sparkConf.registerKryoClasses(util.Arrays.asList(classOf[ConsumerRecord[_, _]]).toArray.asInstanceOf[Array[Class[_]]])
    //Seconds(3)采集周期为3s
    val context = new StreamingContext(sparkConf, Seconds(3))
    context.sparkContext.setCheckpointDir("cp")
    //    Array(brokers,gropuId,topics)=
    val Array(brokers, groupId, topics) = Array("master:9092", "group1", "first")
    val topicsSet = topics.split(",").toSet
    val kafkaConf: Map[String, Object] = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    val dStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(context,
      LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaConf))
    /**
      * Seconds(6) 窗口大小为6s：两个采集周期（6/3）的数据进行合并
      * Seconds(3)步长为3s：窗口每次滑动3s的间隔，且每隔3s计算一次
      */
    val msgs: DStream[ConsumerRecord[String, String]] = dStream.window(Seconds(6), Seconds(3))


    val words = msgs.flatMap(_.value().toString.split(" "))
    words.map((_, 1)).reduceByKey(_ + _).print()
    //     words.map((_,1)).reduceByKeyAndWindow((a,b)=>a+b,Seconds(6)).print()

    context.start()
    context.awaitTermination()
  }
}
