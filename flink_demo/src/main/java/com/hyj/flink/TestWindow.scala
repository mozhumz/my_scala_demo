package com.hyj.flink

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object TestWindow {
  def main(args: Array[String]): Unit = {
    val senv = StreamExecutionEnvironment.getExecutionEnvironment
    senv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val data = senv.socketTextStream("192.168.174.134",9999)
    val stream = data.assignTimestampsAndWatermarks(
      new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(5)) {
//      定义timestamp怎么从数据中抽取出来[1582424357000 a 2020-02-23 10:19:17]
      override def extractTimestamp(t: String): Long = {
        val eventTime = t.split(" ")(0).toLong
        println(s"$eventTime")
        eventTime
      }
    }).map(line=>(line.split(" ")(1),1L)).keyBy(0)


    stream
      .window(TumblingEventTimeWindows.of(Time.seconds(3)))
//      .window(SlidingEventTimeWindows.of(Time.seconds(3),Time.seconds(1)))
//      打印5S+3S
//      .window(EventTimeSessionWindows.withGap(Time.seconds(3)))
      .sum(1).print()

    data.map((_,1)).keyBy(0)
//      按照事件时间的翻滚
      .window(TumblingEventTimeWindows)
////    按照事件时间的滑动
////      .window(SlidingEventTimeWindows)
////      session window
////      .window(EventTimeSessionWindows.withGap(Time.seconds(3)))
////      事件窗口，按照时间数量（针对具体的key进行统计的数量）进行输出
////      .countWindow(5)
////      只有一个时间的时候是翻滚窗口，两个时间表示前面是窗口大小，后面是滑动时间
////      .timeWindow(Time.seconds(3))
//      .sum(1).print()


    senv.execute("Test Window")
  }
}
