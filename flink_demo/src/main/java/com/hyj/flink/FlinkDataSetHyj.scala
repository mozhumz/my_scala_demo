package com.hyj.flink

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.api.scala._

object FlinkDataSetHyj {
  def main(args: Array[String]): Unit = {
    val parameterTool: ParameterTool = ParameterTool.fromArgs(args)
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val txtDs: DataSet[String] = env.readTextFile(parameterTool.get("input"))
    val wordDs: AggregateDataSet[(String, Int)] = txtDs.flatMap(_.split(" ")).map((_,1)).groupBy(0).sum(1)
    wordDs.writeAsCsv(parameterTool.get("output")).setParallelism(1)

    env.execute()
    println("ok")

  }
}
