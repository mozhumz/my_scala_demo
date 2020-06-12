package com.hyj.flink

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.{AggregateDataSet, DataSet, ExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object FlinkDataSetHyj {
  def main(args: Array[String]): Unit = {
//    csvCase(args)

//    streamCase
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val data: DataStream[String] = env.socketTextStream("master",9999)
    data
    .map(line=>(line.split(" ")(1),1L)).keyBy(0)
      .sum(1)
      .print()

    env.execute()
  }

  private def csvCase(args: Array[String]): Unit = {
    val parameterTool: ParameterTool = ParameterTool.fromArgs(args)
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val txtDs: DataSet[String] = env.readTextFile(parameterTool.get("input"))
    val wordDs: AggregateDataSet[(String, Int)] = txtDs.flatMap(_.split(" ")).map((_, 1)).groupBy(0).sum(1)
    //    wordDs.writeAsCsv(parameterTool.get("output")).setParallelism(1)
    //
    //    env.execute()
    println("ok")
    wordDs.print()
  }

  def streamCase:Unit={
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val data: DataStream[String] = env.socketTextStream("master",9999)
    data.map(line=>(line.split(" ")(1),1L)).keyBy(0)
      .sum(1)
      .print()
  }


}
