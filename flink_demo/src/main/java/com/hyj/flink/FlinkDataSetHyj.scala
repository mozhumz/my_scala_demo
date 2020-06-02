package com.hyj.flink

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.api.scala._

object FlinkDataSetHyj {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val txtDs: DataSet[String] = env.readTextFile("G:\\idea_workspace\\my_scala_demo\\input\\word.txt")
    val wordDs: AggregateDataSet[(String, Int)] = txtDs.flatMap(_.split(" ")).map((_,1)).groupBy(0).sum(1)
    wordDs.print()
  }
}
