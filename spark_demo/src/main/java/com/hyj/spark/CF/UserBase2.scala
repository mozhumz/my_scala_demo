package com.hyj.spark.CF

import breeze.numerics.{pow, sqrt}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._


object UserBase2 {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")
    //    val warehouse = "hdfs://master:9000/warehouse"

    val session = SparkSession.builder()
      //      .config("spar.sql.warehouse.dir", warehouse)
      .master("local[*]")
      .appName("User Base")
      .enableHiveSupport()
      .getOrCreate()
    val spark = session


    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    //    val df = spark.read.option("sep","\t").csv("C:\\Users\\zheng\\PycharmProjects\\14mr\\data\\u.data")
    val scoreStrage = "sum"
    val ITEMNUM = 50

    val df = spark.sql("select user_id,item_id,rating from badou.udata")
    //    df.show()
    //    1. 计算相似用户  相似度度量cosine = a*b/(|a|*|b|)   dot
    //    选取的用户和相似用户都是在user_id中

    //    每一个用户的分母
    //    df.filter("user_id='196'").selectExpr("*","pow(cast(rating as double),2) as pow_rating").show()
    //    用sql逻辑进行计算
    val userScoreSum = df.selectExpr("*", "pow(cast(rating as double),2) as pow_rating").groupBy("user_id").agg(sum("pow_rating").as("sum_pow_rating")).selectExpr("*", "sqrt(sum_pow_rating) as sqrt_rating")
    //  用rdd偏底层的方式进行计算
    import spark.implicits._
    //    val userScoreSum_rdd = df.rdd.map(x=>(x(0).toString,x(2).toString)).groupByKey().mapValues(x=>sqrt(x.toArray.map(rating=>pow(rating.toDouble,2)).sum)).toDF("user_id","sqrt_rating")

    // 1.1倒排表item->users
    val df_v = df.selectExpr("user_id as user_v", "item_id", "rating as rating_v")

    val df_decare = df.join(df_v, "item_id").filter("cast(user_id as long)<>cast(user_v as long)")
    //    df_decare.show()
    //    dot计算两个用户在同一个item下的评分的乘积，cosine公式的分子中的一部分
    val df_product = df_decare.selectExpr("user_id", "user_v", "item_id", "cast(rating as double)*cast(rating_v as double) as prod")
    //    相同的两个用户会有在不同item上的打分
    //    df_product.filter("user_id='196' and user_v='721'").show()
    //    求和，计算完整的分子部分
    val df_sim_group = df_product.groupBy("user_id", "user_v").agg(sum("prod").as("rating_dot"))
    //    df_sim_group.filter("user_id='196' and user_v='721'").show()

    //  user_v的分母
    val userScoreSum_v = userScoreSum.selectExpr("user_id as user_v", "sqrt_rating as sqrt_rating_v")
    //    带入user_id的分母和user_v分母，然后进行cosine计算公式
    val df_sim = df_sim_group.join(userScoreSum, "user_id").join(userScoreSum_v, "user_v").selectExpr("user_id", "user_v", "rating_dot/(sqrt_rating*sqrt_rating_v) as consine_sim")
    //计算公式
    //    df_sim.filter("user_id='196'").show()

    //    2. 获取相似用户的物品集合
    //    2.1 取得前n个相似用户 row_number over

    val tmp_df = df_sim.rdd.map(x => (x(0).toString, (x(1).toString, x(2).toString))).groupByKey().mapValues(_.toArray.sortWith((x, y) => x._2.toDouble > y._2.toDouble).slice(0, 5))

    val df_nsim = tmp_df.flatMapValues(x => x).toDF("user_id", "user_v_sim").selectExpr("user_id", "user_v_sim._1 as user_v", "user_v_sim._2 as sim")
    //          df_nsim.show(false)

    //      2.2 获取用户的物品集合进行过滤
    //    获取user_id物品集合（同样能把user_v的物品集合取到）     //df user_id,item_id_rating
    val df_user_item = df.rdd.map(x => (x(0).toString, x(1).toString + "_" + x(2).toString)).groupByKey().mapValues(_.toArray).toDF("user_id", "item_rating_arr")
    //    df_user_item.show(1,false)
    //    表示user_v对于user_id的列名区别
    val df_user_item_v = df_user_item.selectExpr("user_id as user_v", "item_rating_arr as item_rating_arr_v")

    //    将user_id的物品集合关联进来，同时关联相似用户user_v的物品集合
    val df_get_item = df_nsim.join(df_user_item, "user_id").join(df_user_item_v, "user_v")
    //    df_get_item.show()
    //          df_get_item.show(1,false)
    //    2.3 用一个udf过滤相似用户user_v中包含user_id已经打过分的电影
    val filter_udf = udf { (items: Seq[String], item_v: Seq[String]) => val fMap = items.map { item_rating =>  val l = item_rating.split("_");  (l(0), l(1))      }.toMap;  item_v.filter { iv =>      val l = iv.split("_");        fMap.getOrElse(l(0), -1) == -1      }    }

    //    使用去除user_id中出现过的物品，①用户相似度分值sim，②相似用户对物品打分filtered_item
    val df_filter_item = df_get_item.withColumn("filtered_item", filter_udf(col("item_rating_arr"),col("item_rating_arr_v")))      .select("user_id","sim","filtered_item")
    //    df_filter_item.where("user_id=385").show(false)

    //    2.4最终打分：①*②
    val simRatingUDF = udf{(sim:Double,items:Seq[String])=>  items.map{item_rating=>   val l = item_rating.split("_");  l(0)+"_"+l(1).toDouble*sim }}

    val itemSimRating = df_filter_item.withColumn("item_prod", simRatingUDF(col("sim"),col("filtered_item"))) .select("user_id","item_prod")

    //    将item和打分从item_prod数组里面打平取出来
    val userItemScore = itemSimRating.select(itemSimRating("user_id"), explode(itemSimRating("item_prod"))).toDF("user_id","item_prod") .selectExpr("user_id","split(item_prod,'_')[0] as item_id","cast(split(item_prod,'_')[1] as double) as score")
//        userItemScore.show()
    /**
      * +-------+-------+-------------------+
      * |user_id|item_id|              score|
      * +-------+-------+-------------------+
      * |     71|    256| 1.6914477316307988|
      * |     71|     19| 1.6914477316307988|
      * |     71|    297|  1.353158185304639|
      * |     71|    301| 1.6914477316307988|
      * |     71|    315| 1.6914477316307988|
      * |     71|      1| 1.6914477316307988|
      * |     71|      7| 1.6914477316307988|
      * |     71|    483| 1.6914477316307988|
      */

    val u_item_score_df = userItemScore.groupBy("user_id","item_id").agg(sum("score").as("score_sum"))
    u_item_score_df.where("user_id=385").where("item_id=11").show(false)

    /**
      * +-------+-------+-----------------+
      * |user_id|item_id|score_sum        |
      * +-------+-------+-----------------+
      * |385    |11     |10.83455940330164|
      * +-------+-------+-----------------+
      */
  }

}
