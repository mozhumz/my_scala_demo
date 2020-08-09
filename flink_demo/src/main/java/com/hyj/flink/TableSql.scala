package com.hyj.flink

import org.apache.flink.api.scala._

object TableSql {
  case class orders(order_id:String,
                    user_id:String,
                    eval:String,
                    num:String,
                    dow:String,
                    hour:String,
                    day:String)

  def main(args: Array[String]): Unit = {
    val benv = ExecutionEnvironment.getExecutionEnvironment

    val path = "F:\\work\\scala_path\\FlinkClass14\\src\\main\\resources\\orders.txt"
    val input = benv.readCsvFile[orders](path)

//    input.print()
//    table sql的操作环境来源于benv或者senv
//    val tableEnv = TableEnvironment.getTableEnvironment(benv)
//    tableEnv.registerDataSet("orders",input)
//
////    val sqlString = args(0)
//    val sqlString = "select user_id,count(1) as cnt from orders group by user_id"
//    val res = tableEnv.sqlQuery(sqlString)
//    res.printSchema()
//    res.orderBy("cnt").fetch(2)
//    val outpath = "F:\\work\\scala_path\\FlinkClass14\\src\\main\\resources"
//    val sink = new CsvTableSink("out.txt",fieldDelim = ",")
//    res.writeToSink(sink)

  }
}
