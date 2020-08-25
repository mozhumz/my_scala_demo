package com.hyj.flink

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
object CommonTestHyj {
  def main(args: Array[String]): Unit = {
    batchWordCount

    //    env.execute("fromCollection job Test")
//    streamWordCount
  }

  private def batchWordCount: Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val ds = env.fromCollection(List(1, 2, 3, 3, 4, 4, 5, 5, 5, 6, 66, 6, 7, 7, 8, 8, 8))

    ds.map((_, 1)).filter(_._1 > 1).groupBy(0).sum(1).print()
  }

  private def streamWordCount: Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val ds = env.fromCollection(List(1, 2, 3, 3, 4, 4, 5, 5, 5, 6, 66, 6, 7, 7, 8, 8, 8))

    ds.map((_, 1)).filter(_._1 > 1).keyBy(0).sum(1).print()
    env.execute("streamWordCount test")
  }
}
