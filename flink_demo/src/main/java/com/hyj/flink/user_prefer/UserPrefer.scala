package com.hyj.flink.user_prefer

import com.alibaba.fastjson.JSON
import com.badou.config.MysqlConf
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

import scala.collection.JavaConverters._
import scala.collection.mutable

object UserPrefer {
  def main(args: Array[String]): Unit = {

    val senv = StreamExecutionEnvironment.getExecutionEnvironment
    val data = senv.socketTextStream("192.168.174.134",9999)

    val productMap = ReadMysql.productMap

//    user_id,product_id (orders.join(priors))
    data.map(_.split(",")).map{ x=>
        val map = JSON.parseObject(productMap(x(1))).asScala.toMap.filter(_._1!="product_name")
          .mapValues(_.toString)
        (x(0),map)
    }
      .keyBy(_._1)
      .timeWindow(Time.seconds(2))
//    统计{aisle：{aisle_val:cnt},department_id:{department_val:cnt}}
      .aggregate(new UserProductPreferAggregate).print() //user_id没有带进来

//    Map(department_id -> Map(12 -> 1, 7 -> 1, 4 -> 1, 16 -> 1, 13 -> 1), aisle -> Map(24 -> 1, 89 -> 1, 106 -> 1, 120 -> 1, 31 -> 1))
//    Map(department_id -> Map(4 -> 1, 16 -> 2), aisle -> Map(24 -> 1, 86 -> 1, 91 -> 1))
//    Map(department_id -> Map(4 -> 1, 16 -> 1), aisle -> Map(24 -> 1, 91 -> 1))
//    Map(department_id -> Map(16 -> 1), aisle -> Map(91 -> 1))
//    Map(department_id -> Map(12 -> 1, 4 -> 1, 13 -> 1), aisle -> Map(24 -> 1, 89 -> 1, 106 -> 1))
//    Map(department_id -> Map(4 -> 1), aisle -> Map(24 -> 1))

    senv.execute("User Prefer")
  }

  class UserProductPreferAggregate
    extends AggregateFunction[(String,Map[String,String]),mutable.Map[String,mutable.Map[String,Int]],mutable.Map[String,mutable.Map[String,Int]]]{

    override def add(value: (String, Map[String, String]), accumulator: mutable.Map[String, mutable.Map[String, Int]]): mutable.Map[String, mutable.Map[String, Int]] = {
      accumulator.getOrElseUpdate("aisle",mutable.Map[String,Int]())
      accumulator.getOrElseUpdate("department_id",mutable.Map[String,Int]())
      //Map(department_id -> Map(), aisle -> Map())

      value._2.map{attribute=> //aisle,department_id  (aisle,10) (department_id,17)
        val attributeValueCntMap = accumulator.get(attribute._1).get
        attributeValueCntMap+=(attribute._2->(attributeValueCntMap.getOrElse(attribute._2,0)+1))
      }
      accumulator
    }

    override def createAccumulator(): mutable.Map[String, mutable.Map[String, Int]] = {
      mutable.Map[String,mutable.Map[String,Int]]()
    }

    override def getResult(accumulator: mutable.Map[String, mutable.Map[String, Int]]): mutable.Map[String, mutable.Map[String, Int]] = {
      accumulator
    }

    override def merge(a: mutable.Map[String, mutable.Map[String, Int]], b: mutable.Map[String, mutable.Map[String, Int]]): mutable.Map[String, mutable.Map[String, Int]] = {
//      具体数据流进来不知道哪个里面有数据，哪个没有数据，对a，b分别都进行初始化
      a.getOrElseUpdate("aisle",mutable.Map[String,Int]())
      a.getOrElseUpdate("department_id",mutable.Map[String,Int]())

//      b.getOrElseUpdate("aisle",mutable.Map[String,Int]())
//      b.getOrElseUpdate("department_id",mutable.Map[String,Int]())

//      主要合并的逻辑，将b中的统计数据（对b中数据进行扫一遍）和里面的值进行更新，更新到a中
      a.map{attributeAndValueCnt=>
        val bValueCntMap = b(attributeAndValueCnt._1)
        //      (17,2)或者(11,1), (10,1)
        bValueCntMap.map{valueCnt=>
          attributeAndValueCnt._2 += (valueCnt._1->(attributeAndValueCnt._2.getOrElse(valueCnt._1,0)+valueCnt._2))}
      }
      a
    }
  }

}
