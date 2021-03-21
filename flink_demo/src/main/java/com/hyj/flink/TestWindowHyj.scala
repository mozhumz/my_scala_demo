package com.hyj.flink

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.scala._
object TestWindowHyj {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val stream = env.socketTextStream("192.168.147.10", 8899)

//    val (data, reduceData) = testByProcessTime(stream)
    testByEventTime(stream,env)

//    data.print("intput data")
//    reduceData.print("reduce data")
    env.execute("testWin")


  }

  private def testByProcessTime(stream: DataStream[String]) = {

    val data = stream.map(x => {
      (x, 1)
    })


    val reduceData = data.keyBy(x => x._1.split(" ")(1))
      .timeWindow(Time.seconds(10))
      //2条流数据做聚合 d1表示之前聚合的数据 d2表示新来的数据
      .reduce((d1, d2) => (d1._1, d1._2 + d2._2))
    (data, reduceData)
  }

  private def testByEventTime(stream: DataStream[String],env:StreamExecutionEnvironment) = {
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val data = stream
      //按照event的时间戳处理
      .assignTimestampsAndWatermarks(
      new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(5)) {
        override def extractTimestamp(element: String): Long = {
          val eventTime = element.split(" ")(0).toLong
//          println(s"$eventTime")
          println(this.getCurrentWatermark)
          eventTime
        }
      }
//      new AssignerWithPeriodicWatermarks[(String, Int)] {
//        //当前流中最大的时间戳
//        val maxTimeStamp = Long.MinValue
//        //延时5s
//        val bound = 5 * 1000L
//        //假设当前maxTimeStamp为10点，则表示9:59分之前的数据都到了,< 9:59的数据要被处理了
//        override def getCurrentWatermark: Watermark = new Watermark(maxTimeStamp-bound)
//
//        override def extractTimestamp(element: (String, Int), previousElementTimestamp: Long): Long = {
//          //当前数据的时间戳-毫秒值
//          val msTimestamp=element._1.split(" ")(0).toLong
//          //更新event时间戳的最大值
//          maxTimeStamp.max(msTimestamp)
//        }
//      }
    )
      .map(x=>(x.split(" ")(1),1L)).keyBy(0)

    val reduceData = data
      .timeWindow(Time.seconds(3))
      //2条流数据做聚合 d1表示之前聚合的数据 d2表示新来的数据
      .reduce((d1, d2) => (d1._1, d1._2 + d2._2)).print("reduce data")

    stream.map((_,1)).keyBy(0).sum(1).print("origin data")

  }
}
