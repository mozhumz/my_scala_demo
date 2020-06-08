package com.hyj.flink.redis

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

object RedisWordCountTestHyj {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val wdStream: DataStream[String] = env.socketTextStream("master",9999)
//    wdStream.print()
    val wd: DataStream[(String, Int)] = wdStream.flatMap(_.split(" ")).map((_, 1)).keyBy(0).sum(1)

    /**
      * flink流处理特性：当前流会合并之前所有相应的key对应的流数据进行汇总
      * 如 wordcount：第一次输入ww，第二次再次输入ww，此时ww为2
      */
    wd.print()
    wd
      .addSink(MyRedisUtil.getRedisSink)

    env.execute()
  }
}
