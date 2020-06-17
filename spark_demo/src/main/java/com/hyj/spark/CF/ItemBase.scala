package com.hyj.spark.CF

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, RelationalGroupedDataset, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.mutable

/**
  *
  * item ij相似度itemCos ij=  同时喜欢物品1 2 的用户数/（喜欢物品1的用户数+喜欢物品2的用户数）
  * item j打分=itemCos*用户对i的rating
  * 注意：如果同一候选物品j和用户多个物品都相似则得分叠加
  */
object ItemBase {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[*]").appName("itemCF")
      .enableHiveSupport().getOrCreate()

    /**
      * 1 计算物品相似度
      */
    val df: DataFrame = spark.sql("select user_id,item_id, rating from badou.udata")
    val df_j: DataFrame = df.selectExpr("user_id","item_id as item_id_j","rating as rating_j")
    //获取item i j
    val item_ij_df: RelationalGroupedDataset = df.join(df_j,"user_id")
      .groupBy("item_id","item_id_j")
    //计算喜欢物品i的user_id和人数
    //      val item_count_df: DataFrame = df.groupBy("item_id").count()
    import spark.implicits._
    val item_users_df: DataFrame = df.rdd.map(row => {
      (row.getAs[String]("item_id"),
        (row.getAs[String]("user_id"), row.getAs[Double]("rating"))
      )
    })
      .groupByKey().mapValues(v => {
      (v.toArray, v.toArray.length)
    }).toDF("item_id", "arr_count")
    //

    //计算同时喜欢物品i j 的人数和user_id
    //物品相似度map i,(j,cos)
   /* val map = mutable.Map[String, mutable.Map[String, Double]]()

    item_count_rdd.foreach(i => {
      val item_id_i=i._1

      item_count_rdd.foreach(j => {
        val item_id_j=j._1
        if (!item_id_i.equals(item_id_j)) {
          //找出共同的user_id
          val i_set = mutable.Set[String]()
          i._2._1.foreach(t=>{
            i_set.add(t._1)
          })
          val j_set = mutable.Set[String]()
          j._2._1.foreach(t=>{
            j_set.add(t._1)
          })
          val count=(i_set&j_set).size.toDouble
          //ij相似度=共同的user_id数量/（分别喜欢ij的用户数之和）
          val cos: Double = count/(i._2._2+j._2._2)
          //i的相似物品map
          val cos_i_map: mutable.Map[String, Double] = map.getOrElse(item_id_i,mutable.Map[String,Double]())
          cos_i_map.put(item_id_j,cos)

          map.put(item_id_i,cos_i_map)
        }

      })
    })

    /**
      * 2 物品打分 计算user_id=385的推荐物品
      */
    val rec_map: mutable.Map[String, Double] = mutable.Map[String,Double]()

    df.where("user_id=385").groupBy("user_id")
      //获取user_id=385的item集合
      .agg(collect_list(concat_ws("_",col("item_id"),col("rating"))).as("item_arr"))
      .selectExpr("item_arr").foreach(row=>{
      row.getAs[mutable.WrappedArray[String]]("item_arr")
        .foreach(item_rating=>{
          val arr: Array[String] = item_rating.split("_")
          //用户物品id
          val id=arr(0)
          val rating=arr(1).toDouble
          if(map.contains(id)){
            val rec_item_cos= map.get(id).get
            rec_item_cos.foreach(e=>{
              //根据推荐物品id获取相似度，没有则为0
              val score: Double = rec_map.getOrElse(e._1,0)
              //同一推荐物品可能和用户的多个item相似，此时得分相加
              rec_map.put(e._1,e._2*rating+score)
            })
          }
        })
    })

    //排序输出
    println(rec_map.toArray.sortWith((t1,t2)=>t1._2.compareTo(t2._2)>0).take(10))*/


  }

}
