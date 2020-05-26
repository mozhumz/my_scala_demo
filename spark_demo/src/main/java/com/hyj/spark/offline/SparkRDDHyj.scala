package com.hyj.spark.offline

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
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
    println("四川#3".split("#")(0))
//    aggRdd(conf)
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
      * groupByKey也是对每个key进行操作，但只生成一个sequence 如果需要对sequence进行aggregation操作
      * （注意，groupByKey本身不能自定义操作函数），那么，选择reduceByKey/aggregateByKey更好。这是因为groupByKey
      * 不能自定义函数，我们需要先用groupByKey生成RDD，然后才能对此RDD通过map进行自定义函数操作
      */
    val mapRdd2: RDD[(Int, Int)] = ssc.makeRDD(Array(1, 1, 2, 3, 4, 4)).map((_, 1))
    val reduceByKeyRdd: RDD[(Int, Int)] = mapRdd2.reduceByKey((x, y) => x + y)
    println("reduceByKeyRdd---------------")
    reduceByKeyRdd.foreach(println)
    //    mapRdd2.groupByKey()
    /**
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
      *foldByKey 分区内计算和分区间计算一致时使用
      */
    println("aggRdd.foldByKey---------------------")
    aggRdd.foldByKey(0)(_ + _).foreach(println)

    /**
      *combineByKey根据key求平均值
      */
    println("aggRdd.combineByKey---------------------")
    aggRdd.combineByKey(
      (_,1),
      (acc:(Int,Int),v)=>(acc._1+v,acc._2+1),
      (acc1:(Int,Int),acc2:(Int,Int))=>(acc1._1+acc2._1,acc1._2+acc2._2)
    )
      .map{case (key,value)=>(key,value._1/value._2.toDouble)}
      .foreach(println)
    println("aggRdd.combineByKey-wordcount--------------------")
    aggRdd.combineByKey(
      x=>x,
      (acc:Int,v)=> acc+v,
      (acc1:Int,acc2:Int)=> acc1+acc2
    ).foreach(println)

    /**
      *
      */
    aggRdd.sortByKey(true)

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
    * @param conf
    */
  def rddTrain(conf: SparkConf):Unit={
    val ssc = new SparkContext(conf)
    val rdd=ssc.makeRDD(
      List(
      ("四川",1,1),("四川",2,1),("四川",1,2),("四川",3,3),("四川",4,2),
      ("河北",1,1),("河北",1,1),("河北",3,1),("河北",3,1),("河北",2,1),
        ("湖北",4,1),("湖北",5,1),("湖北",6,1),("湖北",6,1),("湖北",4,1)
      )
    )

    object ImplicitValue {

      implicit val KeyOrdering = new Ordering[Int] {
        override def compare(x: Int, y: Int) : Int = {
          y.compareTo(x)
        }
      }
    }
    import ImplicitValue.KeyOrdering
    rdd.map(x=>{
      (x._1+"\001"+x._2,1)
    }).reduceByKey(_+_).map(x=>

      (x._1.split("\001")(0),x)
    ).groupByKey().map(
      x=>{
        (x._1,x._2.toList.sortBy(_._2).take(2))
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

  }

  def aggRdd(conf: SparkConf):Unit={
    val ssc = new SparkContext(conf)
    val rdd=ssc.makeRDD(1 to 10,3)
    val aggRdd = ssc.parallelize(List(("a", 3), ("a", 2), ("c", 4), ("b", 3), ("c", 6), ("c", 8)), 2)
    println("aggRdd.glom()-------------")
    aggRdd.glom().collect().foreach(x=>println(x.mkString("|")))

    /**
      * aggregateByKey zeroValue表示初始值，
      * 这里分区内和分区间都是根据key求和，分区内每个key的初始值10+每个key对应的值
      */
    println("aggRdd.aggregateByKey()-------------")
    aggRdd.aggregateByKey(10)(_+_,_+_).foreach(println)

    /**
      * aggregate 初始值会在分区内和分区间参与计算
      * 这里分区为2个，初始值加两次 10+10，分区间加一次初始值+10
      */
    val rddSum: Int = rdd.aggregate(10)(_+_,_+_)
    println("rddSum:"+rddSum)

    aggRdd.saveAsTextFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output1")
    aggRdd.saveAsSequenceFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output2")
    aggRdd.saveAsObjectFile("file:///G:\\idea_workspace\\my_scala_demo\\spark_demo\\output3")
  }
}
