package com.hyj.spark.offline

import java.sql.{Connection, DriverManager, PreparedStatement}

import com.mysql.jdbc.Driver
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.log4j.{Level, Logger}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object SparkRDDHyj {
  def main(args: Array[String]): Unit = {
    Logger.getLogger(SparkRDDHyj.getClass).setLevel(Level.INFO)
    val conf: SparkConf = new SparkConf().setAppName("SparkRDDHyj").setMaster("local[*]")

    //    rddOperation(conf)
    //    rddTrain(conf)
    //    println("四川#3".split("#")(0))
    //    aggRdd(conf)
    //    dependencyRdd(conf)
    //    dbMysqlRdd(conf)
    //    dbHbaseRdd(conf)
    joinTest(conf)
  }

  /**
    * 分区测试
    *
    * @param conf
    */
  def testPartition(conf: SparkConf): Unit = {
    val context = new SparkContext(conf)

    val listRdd: RDD[Int] = context.parallelize(List(1, 2, 3, 4))
    /**
      * 默认集合数据的分区数量：节点的最大核心数与2比较 取最大值
      * conf.getInt("spark.default.parallelism", math.max(totalCoreCount.get(), 2))
      * 指定分区数为3
      */
    //    val listRdd2: RDD[Int] = context.makeRDD(List(1,2,3,4,5),3)
    //    listRdd2.saveAsTextFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output_dir")
    val fileRDD: RDD[String] = context.textFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\input_dir",
      3)
    fileRDD.saveAsTextFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output_dir2")

    context.stop()
  }

  /**
    * 算子测试
    * 注意：算子操作都在executor执行 如rdd.map rdd.flatmap，创建SparkContext的程序叫driver 如当前的main方法
    *
    * @param conf
    * @return
    */
  def rddOperation(conf: SparkConf): Int = {
    val ssc = new SparkContext(conf)
    /**
      * 2表示该数据集存储到2个分区 假设分区数为3 则10/3 多余的数据会根据算法均分到分区
      */
    val rdd: RDD[Int] = ssc.makeRDD(1 to 10, 2)

    /**
      * map算子和mapPartitions算子区别：
      * map每条数据会往多个executor均匀分发
      * mapPartitions则根据rdd的分区数num，分发到num个executor，相比map，减少网络IO，效率更高
      * 但是如果某个分区的数据过大，超过executor内存则会出现OOM
      */
    val mapRdd: RDD[Int] = rdd.map(_ * 2)
    rdd.mapPartitions(datas => {
      datas.map(_ * 2)
    }).collect().foreach(println)

    /**
      * 在mapPartitions算法的基础上 可以获取每条数据的分区号
      */
    rdd.mapPartitionsWithIndex {
      /**
        * num 分区号
        * datas 数据集
        */
      case (num, datas) => {
        datas.map((_, "partition:" + num))
      }
    }.collect().foreach(println)

    /**
      * glom将同分区的数据收集在一起 返回值Rdd的每个元素为一个分区数组Array
      */
    val glomRdd: RDD[Array[Int]] = rdd.glom()
    glomRdd.collect().foreach(x => println(x.mkString(",")))

    /**
      * groupBy(func) 根据func的返回值进行分组
      */
    val groupByFuncRdd: RDD[(Int, Iterable[Int])] = rdd.groupBy(_ % 2)
    println("groupByFuncRdd----------------")
    groupByFuncRdd.foreach(println)

    val filterRdd: RDD[Int] = rdd.filter(_ % 2 == 0)
    println("filterRdd----------------")
    filterRdd.foreach(println)

    /**
      * reduceByKey用于对每个key对应的多个value进行merge操作，最重要的是它能够在本地先进行merge操作，并且merge操作可以通过函数自定义
      * groupByKey也是对每个key进行操作，但只返回一个Iterable sequence 如果需要对sequence进行aggregation操作
      * （注意，groupByKey本身不能自定义操作函数），那么，选择reduceByKey/aggregateByKey更好。这是因为groupByKey
      * 不能自定义函数，我们需要先用groupByKey生成RDD，然后才能对此RDD通过map进行自定义函数操作
      */
    val mapRdd2: RDD[(Int, Int)] = ssc.makeRDD(Array(1, 1, 2, 3, 4, 4)).map((_, 1))
    val reduceByKeyRdd: RDD[(Int, Int)] = mapRdd2.reduceByKey((x, y) => x + y,1000)
    mapRdd2.groupByKey().mapValues(x=>x.toArray.sum)
    println("reduceByKeyRdd---------------")
    reduceByKeyRdd.foreach(println)
    //    mapRdd2.groupByKey()
    /**
      * aggregate函数 将每个分区里面的元素进行聚合
      * aggregateByKey 第一个参数0为:每个分区内的每个key的初始值
      * math.max表示分区内相同key中取最大值
      * _+_表示不同分区的值相加
      */
    val aggRdd = ssc.parallelize(List(("a", 3), ("a", 2), ("c", 4), ("b", 3), ("c", 6), ("c", 8)), 2)
    println("aggRdd.glom()---------------------")
    aggRdd.glom().collect().foreach(x => println(x.mkString("|")))
    println("aggRdd.aggregateByKey---------------------0")
    aggRdd.aggregateByKey(0)(math.max, _ + _).foreach(println)
    println("aggRdd.aggregateByKey---------------------10")
    aggRdd.aggregateByKey(10)(math.max, _ + _).foreach(println)
    println("aggRdd.aggregateByKey-wordcount--------------------")
    aggRdd.aggregateByKey(0)(_ + _, _ + _).foreach(println)

    /**
      * foldByKey 分区内计算和分区间计算一致时使用
      */
    println("aggRdd.foldByKey---------------------")
    aggRdd.foldByKey(0)(_ + _).foreach(println)

    /**
      * combineByKey根据key求平均值
      */
    println("aggRdd.combineByKey---------------------")
    aggRdd.combineByKey(
      (_, 1),
      (acc: (Int, Int), v) => (acc._1 + v, acc._2 + 1),
      (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
    )
      .map { case (key, value) => (key, value._1 / value._2.toDouble) }
      .foreach(println)
    println("aggRdd.combineByKey-wordcount--------------------")
    aggRdd.combineByKey(
      x => x,
      (acc: Int, v) => acc + v,
      (acc1: Int, acc2: Int) => acc1 + acc2
    ).foreach(println)

    /**
      *
      */
    aggRdd.sortByKey(true)

    ssc.stop()

    0
  }

  /**
    * 用户点击广告的记录数据样本： 省份  广告id 用户id
    * 四川 1 1
    * 四川 1 2
    * 四川 1 3
    * 河北 1 4
    * 河北 1 5
    * 山东 1 6
    * 统计每个省广告点击次数的top-N
    *
    * @param conf
    */
  def rddTrain(conf: SparkConf): Unit = {
    val ssc = new SparkContext(conf)
    val rdd = ssc.makeRDD(
      List(
        ("四川", 1, 1), ("四川", 2, 1), ("四川", 1, 2), ("四川", 3, 3), ("四川", 4, 2),
        ("河北", 1, 1), ("河北", 1, 1), ("河北", 3, 1), ("河北", 3, 1), ("河北", 2, 1),
        ("湖北", 4, 1), ("湖北", 5, 1), ("湖北", 6, 1), ("湖北", 6, 1), ("湖北", 4, 1)
      )
    )

    object ImplicitValue {

      implicit val KeyOrdering = new Ordering[Int] {
        override def compare(x: Int, y: Int): Int = {
          y.compareTo(x)
        }
      }
    }
    import ImplicitValue.KeyOrdering
    rdd.map(x => {
      (x._1 + "\001" + x._2, 1)
    }).reduceByKey(_ + _).map(x =>

      (x._1.split("\001")(0), x)
    ).groupByKey().map(
      x => {
        (x._1, x._2.toList.sortBy(_._2).take(2))
      }
    ).foreach(println)

    println("countByKey----------------")
    //    rdd.map(x=>{
    //      (x._1+"|"+x._2,1)
    //    }).countByKey().map(x=>
    //      (x._1.split("|")(0),x)
    //    ).groupBy(_._1)
    //      .map(
    //      y=>
    //        (y._1,y._2.toList.sortBy(_._2).take(2))
    //    ).foreach(println)
    ssc.stop()

  }

  def aggRdd(conf: SparkConf): Unit = {
    val ssc = new SparkContext(conf)
    val rdd = ssc.makeRDD(1 to 10, 3)
    val aggRdd = ssc.parallelize(List(("a", 3), ("a", 2), ("c", 4), ("b", 3), ("c", 6), ("c", 8)), 2)
    println("aggRdd.glom()-------------")
    aggRdd.glom().collect().foreach(x => println(x.mkString("|")))

    /**
      * aggregateByKey zeroValue表示初始值，
      * 这里分区内和分区间都是根据key求和，分区内每个key的初始值10+每个key对应的值
      */
    println("aggRdd.aggregateByKey()-------------")
    aggRdd.aggregateByKey(10)(_ + _, _ + _).foreach(println)

    /**
      * aggregate 初始值会在分区内和分区间参与计算
      * 这里分区为2个，初始值加两次 10+10，分区间加一次初始值+10
      */
    val rddSum: Int = rdd.aggregate(10)(_ + _, _ + _)
    println("rddSum:" + rddSum)

    aggRdd.saveAsTextFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output1")
    aggRdd.saveAsSequenceFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output2")
    aggRdd.saveAsObjectFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output3")

    ssc.stop()
  }

  /**
    * 宽依赖和窄依赖
    *
    * @param conf
    */
  def dependencyRdd(conf: SparkConf): Unit = {
    val ssc = new SparkContext(conf)
    ssc.setCheckpointDir("dependencyRdd")
    val rdd: RDD[Int] = ssc.makeRDD(1 to 10, 2)
    val rdd1: RDD[(Int, Int)] = rdd.map((_, 1))
    //    rdd1.checkpoint()
    val res: RDD[(Int, Int)] = rdd1.reduceByKey(_ + _)

    /**
      * (2) ShuffledRDD[2] at reduceByKey at SparkRDDHyj.scala:246 []
      * |  ReliableCheckpointRDD[3] at foreach at SparkRDDHyj.scala:248 []
      * checkpoint 将当前调用checkpoint的rdd的父级rdd保存到磁盘，切断血缘关系，
      * 这样调用者依赖于checkpoint的rdd（磁盘保存的rdd）
      */
    res.checkpoint()

    /**
      * cache缓存rdd 不能切断血缘关系
      */
    res.cache()
    res.foreach(println)

    //打印rdd之间的依赖关系（血缘关系）
    println(res.toDebugString)

    /**
      * 释放
      */
    ssc.stop()
  }

  /**
    * rdd对接数据库mysql
    *
    * @param sparkConf
    */
  def dbMysqlRdd(sparkConf: SparkConf): Unit = {
    val sc = new SparkContext(sparkConf)
    val url = "jdbc:mysql://master:3306/db_rdd"
    val driver = "com.mysql.jdbc.Driver"
    val username = "root"
    val passwd = "Mz.52610"
    //查询
    val sql = "select name,age from t_user where id>=? and id <=?"

    val insertSql = "insert into t_user (name,age) values (?,?)"

    //    val jdbcRdd = new JdbcRDD(
    //      sc,
    //      () => {
    //        Class.forName(driver)
    //        DriverManager.getConnection(url,username,passwd)
    //      },
    //      sql, 1, 3, 2, (rs) => {
    //        println(rs.getString(1) + "," + rs.getInt(2))
    //      }
    //    )
    //    jdbcRdd.collect()

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(("zhangsan", 20), ("lisi", 30), ("wangwu", 40)))

    //效率低的插入 每次新建数据库连接
    //    rdd.foreach(x=>{
    //      Class.forName(driver)
    //      val connection: Connection = DriverManager.getConnection(url,username,passwd)
    //      val statement: PreparedStatement = connection.prepareStatement(insertSql)
    //      statement.setString(1,x._1)
    //      statement.setInt(2,x._2)
    //      statement.execute()
    //      statement.close()
    //      connection.close()
    //    })

    /**
      * 效率高的插入 重复利用数据库连接
      * 由于数据库连接Connection不能序列化，只能在本地创建连接 本地使用
      * foreachPartition 每个分区的数据只会存在于一个节点，不存在网络传输，因此不需要序列化Connection就可以使用
      */
    rdd.foreachPartition(p => {

      Class.forName(driver)
      val connection: Connection = DriverManager.getConnection(url, username, passwd)
      p.foreach(x => {
        val statement: PreparedStatement = connection.prepareStatement(insertSql)
        statement.setString(1, x._1)
        statement.setInt(2, x._2)
        statement.execute()
        statement.close()
      })
      connection.close()
    })

    sc.stop()
  }


  /**
    * rdd对接hbase
    *
    * @param sparkConf
    */
  def dbHbaseRdd(sparkConf: SparkConf): Unit = {
    val sc = new SparkContext(sparkConf)
    val configuration: Configuration = HBaseConfiguration.create()
    //查询
    configuration.set(TableInputFormat.INPUT_TABLE, "student")
    val hbaseRdd: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      configuration, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result]
    )
    hbaseRdd.foreach(x => {
      val cells: Array[Cell] = x._2.rawCells()
      for (cell <- cells) {
        println(Bytes.toString(CellUtil.cloneValue(cell)))
      }
    })

    //新增
    val rdd: RDD[(String, String)] = sc.makeRDD(List(("1002", "lisi"), ("1003", "w5"), ("1004", "x6")))
    val putRdd: RDD[(ImmutableBytesWritable, Put)] = rdd.map(x => {
      val put = new Put(Bytes.toBytes(x._1))
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(x._2))
      (new ImmutableBytesWritable(Bytes.toBytes(x._1)), put)
    })
    val jobConf = new JobConf(configuration)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "student")
    putRdd.saveAsHadoopDataset(jobConf)

    sc.stop()
  }

  def joinTest(sparkConf: SparkConf): Unit = {
    val sc = new SparkContext(sparkConf)
    val rdd1 = sc.makeRDD(List((1, 1), (2,1)), 2)
    val list = List((1, 3), (2, 2), (3, 4),(1,4))
    val rdd2 = sc.makeRDD(list, 2)
    rdd1.join(rdd2).foreach(println)
  }

  /**
    * spark-broadcast 广播变量调优
    *
    * @param sparkConf
    */
  def broadcast(sparkConf: SparkConf): Unit = {
    val sc = new SparkContext(sparkConf)
    val rdd1: RDD[(Int, String)] = sc.makeRDD(List((1, "a"), (2, "b"), (3, "c")), 2)
    val list = List((1, 1), (2, 2), (3, 3))
    val rdd2 = sc.makeRDD(list, 2)

    /**
      * 广播变量的好处，不需要每个task带上一份变量副本，而是变成每个节点的executor才一份副本。
      * 这样的话， 就可以让变量产生的副本大大减少
      */
    //    rdd1.join(rdd2).foreach(println)
    val bRdd: Broadcast[List[(Int, Int)]] = sc.broadcast(list)
    rdd2.map(x => {
      var v: Any = null
      for (y <- bRdd.value) {
        if (y._1 == x._1) {
          v = y._2
        }
      }
       (x._1, (x._2, v))

    }).foreach(println)




    sc.stop()
  }

}
