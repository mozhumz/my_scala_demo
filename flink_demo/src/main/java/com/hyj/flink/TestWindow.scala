package com.hyj.flink

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Watermark @ -9223372036854775808
  * Watermark @ 1582424358000
  * Watermark @ 1582424359000
  * Watermark @ 1582424360000
  * Watermark @ 1582424361000
  * origin> (1582424363000 a3 23,1)
  * origin> (1582424364000 a4 24,1)
  * origin> (1582424365000 a5 25,1)
  * origin> (1582424366000 a6 26,1)
  * origin> (1582424367000 a7 27,1)
  * Watermark @ 1582424362000
  * origin> (1582424368000 a8 28,1)
  * Watermark @ 1582424363000
  * origin> (1582424369000 a 29,1)
  * reduce> (a3,1)
  * Watermark @ 1582424364000
  * origin> (1582424370000 a 30,1)
  * Watermark @ 1582424365000
  * origin> (1582424371000 a 31,1)
  * Watermark @ 1582424366000
  * origin> (1582424372000 a 32,1)
  * reduce> (a4,1)
  * reduce> (a6,1)
  * reduce> (a5,1)
  *
  * 如上，设置event时间戳，延时bound=5s windowSize=3s后，
  * 第一次输入23-28s的流数据，窗口不会关闭，输入29s的数据时窗口关闭，此时watermark为29s-bound=24s,表示小于24s的数据要被处理:23
  * 输入30-31s的数据，窗口不会关闭，输入32s的数据时窗口关闭，此时watermark为32-bound=27s，表示小于27s的数据被处理：24-26
  * 总结：假设延时为bound，窗口大小为windowSize，第一条数据时间戳为t1,当前水位线now_watermark，上一次聚合的水位线pre_watermark
  * 则第一次执行聚合的条件为：t1 < now_watermark，第一次的窗口范围为[now_watermark-bound,now_watermark)
  * 后续执行聚合的条件为: 窗口中有数据的时间戳 < now_watermark && now_watermark - pre_watermark >= bound
  * 窗口范围为[now_watermark-bound,now_watermark)
  */
object TestWindow {
  def main(args: Array[String]): Unit = {
    val senv = StreamExecutionEnvironment.getExecutionEnvironment
    senv.setParallelism(1)
    //设置时间语义为event的时间戳
    senv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val data = senv.socketTextStream("192.168.147.10",8899)
    val stream = data.assignTimestampsAndWatermarks(
      new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(5)) {
//      定义timestamp怎么从数据中抽取出来[1582424357000 a 2020-02-23 10:19:17]
      override def extractTimestamp(t: String): Long = {
        val eventTime = t.split(" ")(0).toLong
        println(this.getCurrentWatermark)
        eventTime
      }
    }).map(line=>(line.split(" ")(1),1L)).keyBy(0)


    stream
        .timeWindow(Time.seconds(3))
//      .window(TumblingEventTimeWindows.of(Time.seconds(3)))
//      .window(SlidingEventTimeWindows.of(Time.seconds(3),Time.seconds(1)))
//      打印5S+3S
//      .window(EventTimeSessionWindows.withGap(Time.seconds(3)))
      .sum(1).print("reduce")

    data.map((_,1)).keyBy(0)
//      按照事件时间的翻滚
//      .window(TumblingEventTimeWindows)
////    按照事件时间的滑动
////      .window(SlidingEventTimeWindows)
////      session window
////      .window(EventTimeSessionWindows.withGap(Time.seconds(3)))
////      事件窗口，按照时间数量（针对具体的key进行统计的数量）进行输出
////      .countWindow(5)
////      只有一个时间的时候是翻滚窗口，两个时间表示前面是窗口大小，后面是滑动时间
////      .timeWindow(Time.seconds(3))
      .sum(1).print("origin")


    senv.execute("Test Window")
  }
}
