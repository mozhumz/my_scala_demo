package com.hyj.flink

import scala.io.Source


object TestMysql extends App {
  val statment = MysqlConf.statment
  //  去product.csv取数据，存到mysql

  val input = "D:\\data\\data\\products.csv"
  val sourceFile = Source.fromFile(input, "utf-8")

  //  数据处理 product_name 有引号
  val data = sourceFile.getLines().toList
    //    有列名column name表头需要去掉
    .filter(!_.startsWith("product_id")).map { line =>
    val arr = line.split(",")
    //    6777,Original Gluten Free Rice Thins,78,19=>(6777,"Original Gluten Free Rice Thins",78,19)
    if (arr.length <= 4 && !arr(1).startsWith("\"")) {
      Array(arr(0), "\"" + arr(1) + "\"", arr(2), arr(3)).mkString("(", ",", ")")
    } else "(" + line.replaceAll("""\\\"\"""", "") + ")" // \""
  }

  //  插入数据到mysql
  data.foreach { line =>
    try {
//      写入数据
      statment.executeUpdate("insert into products values " + line)
    }catch {case e:Exception=>println(e,line)}
  }

//  建表语句
//  create table products(product_id int(11),product_name varchar(255),aisle int(4),department_id int(4))DEFAULT CHARACTER SET  'UTF8';


}
