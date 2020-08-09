package com.hyj.flink.config

import java.sql.{Connection, DriverManager}

object MysqlConf {
  //  访问mysql服务器，通过3306端口访问
  val url = "jdbc:mysql://192.168.174.134:3306/badou?useUnicode=true&characterEncoding=utf-8&useSSL=false"
  //  驱动名称
  val driver = "com.mysql.jdbc.Driver"
  //  用户，密码
  val username = "root"
  val password = "123456"

  var connection : Connection = _
  //  注册
  Class.forName(driver)
  //  得到连接

  connection = DriverManager.getConnection(url, username, password)
  val statment = connection.createStatement()

}
