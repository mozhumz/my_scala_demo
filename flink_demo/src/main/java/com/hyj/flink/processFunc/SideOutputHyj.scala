package com.hyj.flink.processFunc

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
/**
 * 侧输出流
 */
object SideOutputHyj {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val data = env.socketTextStream("192.168.147.10", 8888)
//    data.assignTimestampsAndWatermarks(
//      new BoundedOutOfOrdernessTimestampExtractor[String](Time.seconds(1)) {
//        override def extractTimestamp(element: String): Long = {
//          element.split(" ")(0).toLong
//        }
//      })
    val ds=data.map(x=>{
      val arr=x.split(" ")
      SensorReading(arr(0),arr(1).toLong,arr(2).toDouble,arr(3))
    }
    )
    val ps=ds.process(new FreezingAlert)
    //主输出流
    ps.print("mainPs")
    ps.getSideOutput(new OutputTag[String]("FreezingAlert")).print("side out")

    env.execute()

  }

  case class SensorReading(id:String,time:Long,temperature:Double,desc:String)

  //温度小于32则放入侧输出流
  class FreezingAlert extends ProcessFunction[SensorReading,SensorReading]{
    val sideOutTag=new OutputTag[String]("FreezingAlert")
    override def processElement(value: SensorReading, ctx: ProcessFunction[SensorReading, SensorReading]#Context, out: Collector[SensorReading]): Unit =
      {
        if(value.temperature<32.0){
          ctx.output(sideOutTag,value.id)
        }else{
          out.collect(value)
        }
      }
  }
}
