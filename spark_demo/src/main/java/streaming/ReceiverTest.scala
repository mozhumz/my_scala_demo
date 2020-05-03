package streaming

import com.alibaba.fastjson.{JSON, JSONException}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{AnalysisException, SaveMode, SparkSession}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import streaming.kafka.Orders

object ReceiverTest {
  case class order(order_id:String,user_id:String)

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME","root")
    val warehouse = "hdfs://192.168.174.134:9000/warehouse"

    val spark = SparkSession.builder()
      .config("spar.sql.warehouse.dir",warehouse).master("local[2]")
      .appName("rdd2DF")
      .enableHiveSupport()
      .getOrCreate()

//    val conf = new SparkConf().setAppName("WC")
//          .setMaster("local[2]")
    val ssc = new StreamingContext(spark.sparkContext,Seconds(2))
//    ssc.checkpoint("hdfs:///streaming/checkpoint")
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
//   需要消费kafka中的topic
    val topicMap = Map{"badou"-> 2}
    val zkQuorum = "192.168.174.134:2181"

    val Dstream = KafkaUtils.createStream(ssc,zkQuorum,"group1",topicMap)
      .map(_._2)
//    Dstream.map((_,1L)).reduceByKey(_+_).print()
//    简析{"user_id": 1, "hour": 7, "order_id": 2398795, "eval_set": "prior", "order_dow": 3, "order_number": 2, "day": 15.0}
//    统计hour的分布

    import spark.implicits._

    Dstream.map{line=>
      try {
        val mess = JSON.parseObject(line, classOf[Orders])
        order(mess.order_id, mess.user_id)
      }catch {
        case e:JSONException=>
          println(e,line)
          order("wrong",line) //也需要存hive表中，为了方便进一步了解简析出问题的具体是哪些数据，
        // 以及数据量，会不会影响整体情况，看需不需要单独对某一些日志重写解析
      }
    }.foreachRDD{rdd=>
      val df= rdd.toDF("order_id","user_id")
      try {
        df.write.mode(SaveMode.Append).insertInto("badou.order_test")
        println("insertInto")
      }catch { case e: AnalysisException=>
        println(e)
        df.write.saveAsTable("badou.order_test")
        println("create table, insertInto")
      }
    }
//      .map(x=>(x.order_id,1L))
//      .reduceByKeyAndWindow((a:Long,b:Long)=>a+b,Minutes(5),Seconds(10))
//      .reduceByKey(_+_)


//      .print()



    ssc.start()
    ssc.awaitTermination()
  }

}
