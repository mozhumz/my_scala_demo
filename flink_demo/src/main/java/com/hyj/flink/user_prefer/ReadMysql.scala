package com.hyj.flink.user_prefer

import com.hyj.flink.config.MysqlConf
import org.json4s.native.Serialization

import scala.collection.mutable

object ReadMysql {
  val statement = MysqlConf.statment
  val productMap:mutable.Map[String,String]=mutable.Map[String,String]()
  getProductData

  def getProductData:Unit={
    implicit val formats = org.json4s.DefaultFormats
    val products = statement.executeQuery("select * from products")
    while(products.next()){
      val product_id = products.getString("product_id")
      val product_name = products.getString("product_name")
      val aisle = products.getString("aisle")
      val department_id = products.getString("department_id")

      productMap.put(product_id,
        Serialization.write(Products(product_name,aisle,department_id)))
    }
  }
  case class Products(product_name:String,aisle:String,department_id:String)
}
