package com.hyj.spark.test

import com.alibaba.fastjson.{JSON, JSONArray}

import scala.collection.mutable

object TestCase {
  def main(args: Array[String]): Unit = {
    case class Book(title: String, pages: Int)
    val books = Seq( Book("Future of Scala developers", 85),
      Book("Parallel algorithms", 240),
      Book("Object Oriented Programming", 130),
      Book("Mobile Development", 495) )
    //Book(Mobile Development,495)
    val b1=books.maxBy(_.pages)
//    println(b1)
    //Book(Future of Scala developers,85)
    val b2=books.minBy(book => book.pages)
//    println(b2)
    val set = mutable.Set[String]()
    set.add("1")
    set.add("2")

//    println(set.contains("1"))
    val map: mutable.Map[String, Double] = mutable.Map[String,Double]()
    map.put("1",1);
    println(map)

//    var map=Map[Int,String]()
//    map+=(1->"one",2->"two")
//    println(map.getOrElse(1,"default"))
//    testArr
  }

  def testArr:Unit={
    val str="[[328, 3.0], [345, 3.0], [311, 4.0], [902, 5.0], [900, 3.0], [690, 4.0], [319, 4.0], [321, 4.0], [303, 4.0], [305, 4.0], [315, 4.0], [316, 4.0], [340, 3.0], [272, 4.0], [347, 4.0], [313, 3.0], [304, 3.0], [307, 2.0], [286, 3.0], [268, 5.0], [896, 4.0], [338, 3.0]] "
    val array: JSONArray = JSON.parseArray(str)
    println(array.toArray())
  }



}
