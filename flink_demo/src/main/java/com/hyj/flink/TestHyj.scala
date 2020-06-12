package com.hyj.flink


import com.alibaba.fastjson.JSON
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.json4s.native.Serialization

import scala.collection.mutable
import scala.collection.JavaConverters._

object TestHyj {
  def merge(a: mutable.Map[String, Map[String, Int]], b: mutable.Map[String, Map[String, Int]]): mutable.Map[String, Map[String, Int]] = {
    a++b
  }

  def main(args: Array[String]): Unit = {
//    var d=mutable.Map("a"->Map("a2"->1))
    ////    var d2=mutable.Map("b"->Map("a2"->1))
    ////
    ////    val res=merge(d,d2)
    ////
    ////    println(res)
//    val str: String = Array("1", "\"" + 2 + "\"", 3, 4).mkString("(", ",", ")")
//    print(str)
    implicit val formats = org.json4s.DefaultFormats
//    val map: mutable.Map[String, String] = mutable.Map[String,String]()
    val productMap:mutable.Map[String,String]=mutable.Map[String,String]()
    val product_name="1"
    val a="2"
    val c="3"
    productMap.put("1",Serialization.write(Products(product_name,a,c)))
    productMap.put("2",Serialization.write(Products(product_name,a,c)))

    val map: Map[String, AnyRef] = JSON.parseObject(productMap("1")).asScala.toMap
    println(map)

    val res: Map[String, String] = map.filter(_._1 != "product_name")
      .mapValues(_.toString)
    println(res)
  }


//  def getProductData:Unit={
//    implicit val formats = org.json4s.DefaultFormats
//    val products = statement.executeQuery("select * from products")
//    while(products.next()){
//      val product_id = ""
//      val product_name = ""
//      val aisle = ""
//      val department_id = ""
//
//      productMap.put(product_id,
//        Serialization.write(Products(product_name,aisle,department_id)))
//    }
//  }
  case class Products(product_name:String,aisle:String,department_id:String)
}
