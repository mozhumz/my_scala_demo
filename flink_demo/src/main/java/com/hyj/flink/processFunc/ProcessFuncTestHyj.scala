package com.hyj.flink.processFunc

import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.{KeyedProcessFunction, ProcessFunction}
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object ProcessFuncTestHyj {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val data = env.socketTextStream("192.168.147.10", 8888)
    data.assignTimestampsAndWatermarks(
      new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(1)) {
        override def extractTimestamp(element: String): Long = {
          element.split(" ")(0).toLong
        }
      })

    //    val stream=data.map(x=>(x.split(" ")(0),1))
    data.keyBy(_.split(" ")(0)).process(new MyProcess()).print("process")
    data.print("data")
    env.execute()
  }

  /**
   * KeyedProcessFunction函数使用
   */
  class MyProcess extends KeyedProcessFunction[String, String, String] {
    lazy val valState = getRuntimeContext.getState(new ValueStateDescriptor[Double]("valState", classOf[Double]))
    lazy val timerState = getRuntimeContext.getState(new ValueStateDescriptor[Long]("timerState", classOf[Long]))

    override def processElement(value: String, ctx: KeyedProcessFunction[String, String, String]#Context, out: Collector[String]):
    Unit = {
      val preValue = valState.value()
      val preTimer = timerState.value()
      val curValue = value.split(" ")(2).toDouble

      //本次温度比上次高 则设置定时器,进行报警
      if (curValue > preValue && preTimer == 0 && preValue != 0.0) {
        val curTimer = ctx.timerService().currentProcessingTime() + 3000L
        ctx.timerService().registerProcessingTimeTimer(curTimer)
        //更新state参数
        timerState.update(curTimer)
      } else if (curValue < preValue || preTimer == 0) {
        ctx.timerService().deleteProcessingTimeTimer(preTimer)
        timerState.clear()
      }
      valState.update(curValue)
    }

    override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, String, String]#OnTimerContext, out: Collector[String]): Unit = {

      out.collect(ctx.getCurrentKey + "温度上升")
      //清空报警state
      timerState.clear()
    }

  }

}


