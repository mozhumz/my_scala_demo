package com.hyj.spark.offline.algorithm

import com.huaban.analysis.jieba.JiebaSegmenter.SegMode
import com.huaban.analysis.jieba.{JiebaSegmenter, SegToken}
import com.hyj.spark.offline.SparkRDDHyj
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

object Kmeans {
  def main(args: Array[String]): Unit = {
    Logger.getLogger(Kmeans.getClass).setLevel(Level.INFO)
    //    定义结巴分词类的序列化
    val conf = new SparkConf()
      .registerKryoClasses(Array(classOf[JiebaSegmenter]))
      .set("spark.rpc.message.maxSize","800")
    //    建立sparkSession,并传入定义好的Conf
    val spark = SparkSession
      .builder()
      .appName("Jieba UDF")
      .master("local[*]")
      .enableHiveSupport()
      .config(conf)
      .getOrCreate()

    // 定义结巴分词的方法，传入的是DataFrame，输出也是DataFrame多一列seg（分好词的一列）
    def jieba_seg(df:DataFrame,colname:String): DataFrame ={
      val segmenter = new JiebaSegmenter()
      val seg= spark.sparkContext.broadcast(segmenter)
      val jieba_udf = udf{(sentence:String)=>
        val segV = seg.value
        segV.process(sentence.toString,SegMode.INDEX)
          .toArray().map(_.asInstanceOf[SegToken].word)
          .filter(
            x=>{
              x
              x.length>1
            }
          )
//          .mkString(" ")
      }
      df.withColumn("seg",jieba_udf(col(colname)))
    }

    val df = spark.sql("select sentence from badou.news_seg ")

//    val df_seg = df.selectExpr("split(sentence,' ') as seg")
//    调用udf进行结巴切词
    val df_seg = jieba_seg(df,"sentence")

//    实例化tf
    val TF = new HashingTF().setBinary(false)
      .setInputCol("seg")
      .setOutputCol("rawFeatures")
      .setNumFeatures(1<<18)
    val df_tf = TF.transform(df_seg).select("rawFeatures")

//    对word进行idf加权
    val idf = new IDF()
      .setInputCol("rawFeatures")
      .setOutputCol("features")
      .setMinDocFreq(1)

    val idfModel = idf.fit(df_tf)
    val df_tfidf = idfModel.transform(df_tf).select("features")

//    实例化kmeans
    val kmeans = new KMeans()
      .setFeaturesCol("features")
      .setInitMode("k-means||")
      .setInitSteps(5)
      .setK(5)
      .setMaxIter(30)
      .setPredictionCol("pred")
      .setSeed(2020)

    val model = kmeans.fit(df_tfidf)
    val WCSS = model.computeCost(df_tfidf)
    val loss=model.summary.trainingCost
    println("loss:"+loss)
    println(s"Set Sum of Squared Errors= $WCSS")
//    5.797699325240116E7
//    打印中心点

    println("cluster Centers:"+model.clusterCenters.length)

//    sparsevector （size,[所有不为0单词index]，[values]）
    model.clusterCenters.map(v=>v.toSparse).foreach(println)

//    为什么中心点会有这么多值
//    [0,0,0,1] [1,0,0,0] [0,2,0,0] [0,0,4,0]=> [0.25,0.5,1,0.25]
  }

}
