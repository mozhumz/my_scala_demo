package com.hyj.flink.processFunc

import com.hyj.flink.processFunc.SideOutputHyj.SensorReading
import org.apache.flink.api.common.functions.{FlatMapFunction, RichFlatMapFunction}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
/**
 * flink状态编程
 */
object StateTestHyj {
  def main(args: Array[String]): Unit = {
    val list1=List(("haha",1,2))
    println(list1)
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val data = env.socketTextStream("192.168.147.10", 8888)
    val ds=data.map(x=>{
      val arr=x.split(" ")
      SensorReading(arr(0),arr(1).toLong,arr(2).toDouble,arr(3))
    }
    )
    //flatMap+MyProcess1实现状态编程
    val ps1=ds.keyBy(_.id).flatMap(new MyProcess1(10))
    //flatMapWithState实现状态编程 输出类型为(String,Double,Double)，要和List的元素类型一致
   val ps2= ds.keyBy(_.id).flatMapWithState[(String,Double,Double),Double]{
         //返回值类型为
      case (input:SensorReading,None)=>(List.empty,Some(input.temperature))
      case (input:SensorReading,lastTemp:Some[Double])=>{
        val diff=(input.temperature-lastTemp.get).abs
        if(diff>10){
          (List((input.id,lastTemp.get,input.temperature)),Some(input.temperature))
        }else{
          (List.empty,Some(input.temperature))
        }
      }

    }

    ds.print("data")
//    ps1.print("ps1")
    ps2.print("ps2")
    env.execute()
  }

  class MyProcess1(threshold:Double) extends RichFlatMapFunction[SensorReading,(String,Double,Double)]{
    var lastTemp:ValueState[Double]=_

    override def open(parameters: Configuration): Unit = {

      lastTemp=getRuntimeContext.getState(new ValueStateDescriptor[Double]("lastTemp",classOf[Double]))
    }
    override def flatMap(value: SensorReading, out: Collector[(String,Double,Double)]): Unit = {
      val lastValue=lastTemp.value()
      val diff=(lastValue-value.temperature).abs
      if(diff>threshold){
        out.collect(value.id,lastValue,value.temperature)
      }
      lastTemp.update(value.temperature)

    }
  }
}
