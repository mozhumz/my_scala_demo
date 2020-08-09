package com.hyj.flink

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
object CommonTestHyj {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val ds: DataStream[Int] = env.fromCollection(List(1,2,3))
    ds.print("ds1")

    env.execute("fromCollection job Test")

  }
}
