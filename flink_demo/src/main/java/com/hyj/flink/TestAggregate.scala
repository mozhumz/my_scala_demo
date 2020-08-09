package com.hyj.flink

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

import scala.collection.mutable

object TestAggregate {
  def main(args: Array[String]): Unit = {
    val senv = StreamExecutionEnvironment.getExecutionEnvironment
    val data = senv.socketTextStream("192.168.174.134",9999)

    val keyedStream = data.map((_,1)).keyBy(0)

    val window3 = keyedStream.timeWindow(Time.seconds(1),Time.seconds(1)).sum(1).map(x=>(x._1,x._2,"1"))
    val window5 = keyedStream.timeWindow(Time.seconds(5),Time.seconds(1)).sum(1).map(x=>(x._1,x._2,"5"))
    val window10 = keyedStream.timeWindow(Time.seconds(10),Time.seconds(1)).sum(1).map(x=>(x._1,x._2,"10"))

//    (a,1,10)(a,1,3)(a,1,5)=> {window_size:{key:value}} {3:{a:1},5:{a:1},10:{a:1}}
    window3.union(window5,window10)
      .countWindowAll(3)
      .aggregate(new ConcatAggregateFunction)
      .filter(_.size==3)
      .print()

//      .timeWindow(Time.seconds(3))
//      .aggregate(new SumAggregateFunction)
//      .print()

    senv.execute("Aggregate Test")
  }


  class SumAggregateFunction extends AggregateFunction[(String,Int),(String,Int),(String,Int)] {
//    在一个节点上的累加 value数据流中的数据，accumulator是tmp，累加数据中间存储
    override def add(value: (String, Int), accumulator: (String, Int)): (String, Int) = {
      (value._1,accumulator._2+value._2)
    }

    override def createAccumulator(): (String, Int) = {
      ("",0)
    }

    override def getResult(accumulator: (String, Int)): (String, Int) = {
      accumulator
    }
//  在不同节点数据汇总合并
    override def merge(a: (String, Int), b: (String, Int)): (String, Int) = {
      val key = if(a._1=="") b._1 else a._1
      (key,a._2+b._2)
    }
  }

  class ConcatAggregateFunction extends AggregateFunction[(String,Int,String),mutable.Map[String,Map[String,Int]],mutable.Map[String,Map[String,Int]]]{
    override def add(value: (String, Int,String), accumulator: mutable.Map[String, Map[String, Int]]): mutable.Map[String, Map[String, Int]] = {
      accumulator += (value._3->Map(value._1->value._2))
    }

    override def createAccumulator(): mutable.Map[String, Map[String, Int]] = {
      mutable.Map[String,Map[String,Int]]()
    }

    override def getResult(accumulator: mutable.Map[String, Map[String, Int]]): mutable.Map[String, Map[String, Int]] = {
      accumulator
    }

    override def merge(a: mutable.Map[String, Map[String, Int]], b: mutable.Map[String, Map[String, Int]]): mutable.Map[String, Map[String, Int]] = {
      a++b
    }
  }

}
