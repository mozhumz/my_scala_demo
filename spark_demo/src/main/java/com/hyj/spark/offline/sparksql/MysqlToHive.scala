package com.hyj.spark.offline.sparksql


import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import com.hyj.spark.util.{ConnectUtils, SparkUtils}

object MysqlToHive {
  def main(args: Array[String]): Unit = {
    //创建session
    val spark: SparkSession = SparkUtils.sparkSessionWithHive

    //连接mysql
    //TODO 修改表名
    val dataDF: DataFrame = ConnectUtils.mysqlSource("orders_copy1")

    //TODO 具体业务逻辑处理
    //通过调用API的方式保存到hive
    dataDF
//        .repartition(1)
        .write.mode(SaveMode.Append).insertInto("badou.orders2")


    //方式二：利用API自动创建表再插入数据
    //dataDF.write.mode(SaveMode.Append).saveAsTable("test_xsh_0401")
    //方式三：利用SQL插入已存在的表
    //    dataDF.createTempView("qiaoJie")
    //    sql("insert into table ods_xsh_0330 select * from qiaoJie")

    /**
      * spark读取hive数据进行重分区Overwrite原表，从而合并hive的小文件
      * 另外一种方式：sql读取数据后先重分区存储到hdfs，再从hdfs读取写入到原表所在文件夹
      */
        val df: DataFrame = spark.sql("select * from badou.orders2")
//    val path = "hdfs://master:9000/tmp/20210603"
//    df.rdd.coalesce(1).saveAsTextFile(path)
//    val tablePath = "hdfs://master:9000/user/hive/warehouse/badou.db/orders2"
//    spark.read.textFile(path).write.mode(SaveMode.Overwrite).save(tablePath)
    df.repartition(1).write.mode(SaveMode.Overwrite).insertInto("badou.orders2")
    println("OK。。。。。")

    //释放资源
    spark.stop()
  }
}


