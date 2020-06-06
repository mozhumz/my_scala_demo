package com.hyj.spark.offline.sparksql.config

import java.sql.{Connection, DriverManager, Statement}

object MySqlConf {
  val url="jdbc:mysql://master:3306/badou?useUnicode=true&characterEncoding=utf-8&useSSL=false" +
  //开启批量写入
    "&rewriteBatchedStatements=true"
  val username="root"
  val password="123456"
  val driver="com.mysql.jdbc.Driver"

  Class.forName(driver)

  val connection: Connection = DriverManager.getConnection(url,username,password)
  val statement: Statement = connection.createStatement()


}
