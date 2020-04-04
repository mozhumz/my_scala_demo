package com.hyj.spark.offline

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, IDF, StringIndexer}
import org.apache.spark.sql.SparkSession

object TextPredictBayes {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME","root")
    val warehouse = "hdfs://192.168.174.134:9000/warehouse"

    val spark = SparkSession.builder()
      .config("spar.sql.warehouse.dir",warehouse).master("local[2]")
      .appName("User Base")
      .enableHiveSupport()
      .getOrCreate()

    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)

    val df = spark.sql("select * from badou.news_seg")
      .selectExpr("split(sentence,'##@@##')[0] as sentence","split(sentence,'##@@##')[1] as label")
      .selectExpr("split(sentence,' ') as sentence","label")
//      df.show()
//    setBinary(true)   bernoulli伯努利分布统计出来的单词只表示出现或者没出现。一个单词在文章中出现了10次，这个单词就是1，没有出现0
//    setBinary(false)  multinomial正常词频统计，一个单词出现10次，值为10,这种就是多项式分布
    val coltf = "feature_tf"
    val hashingTF = new HashingTF()
      .setBinary(false)  //涉及到统计的数据分布式伯努利分布（0,1）还是*多项式分布*（词频分布）
      .setInputCol("sentence")
      .setOutputCol(coltf)
      .setNumFeatures(1<<18) //表示词表大小262144，对应是文本向量的大小

    val df_tf = hashingTF.transform(df).select("feature_tf","label")
//    df_tf.show(1,false)
    /** sparseVector (向量大小，[index],[value])   [0,1,0,2,0,0,0,0,0,3] (10,[1,3,9],[1,2,3])
      * (262144,
      * [25092,30776,32170,37868,41381,46345,47509,52820,53214,54571,56356,59063,68415,85364,101501,111202,115360,119175,122747,137701,145507,146391,152497,154668,156037,158988,161609,170555,172050,174205,181458,183965,184919,195842,197957,198177,205710,210829,226542,236972,238938,241664,242670,243109,243937,249040,249180,250396,254957],
      * [1.0,1.0,1.0,7.0,5.0,1.0,1.0,1.0,1.0,13.0,1.0,1.0,1.0,1.0,8.0,1.0,9.0,1.0,7.0,1.0,1.0,1.0,3.0,5.0,1.0,1.0,2.0,2.0,1.0,2.0,1.0,1.0,8.0,1.0,1.0,1.0,1.0,11.0,6.0,3.0,1.0,1.0,2.0,1.0,2.0,1.0,1.0,7.0,1.0])|auto |
      * */
    val idf = new IDF().setInputCol(coltf)
      .setOutputCol("feature_tfidf")
      .setMinDocFreq(2) // 取文档频率>=2

//    idf需要fit，需要统计doc-freq map
//    val idfModel = idf.fit(df_tf)
//    val df_tfIdf = idfModel.transform(df_tf).select("feature_tfidf","label")
//    df_tfIdf.show(1,false)
      /**(262144,
        * [25092,30776,32170,37868,41381,46345,47509,52820,53214,54571,56356,59063,68415,85364,101501,111202,115360,119175,122747,137701,145507,146391,152497,154668,156037,158988,161609,170555,172050,174205,181458,183965,184919,195842,197957,198177,205710,210829,226542,236972,238938,241664,242670,243109,243937,249040,249180,250396,254957],
        * [2.2223857314469218,0.7803437255901005,3.5863743350525144,5.351292635036672,12.414360305451064,4.915510282332456,3.399162792964368,2.730708224994794,6.707269751560512,37.70789440327249,3.5717755356313616,2.1054384668379345,1.9887708802654167,0.028927636906179426,11.766622309844497,1.9536795604541466,34.691744894824744,4.670387824299471,5.332944000677553,1.4139649268360188,0.8304681127726692,4.092309973524313,5.254328081877752,6.485564516284695,2.1639749692905075,3.060949911865371,12.39288825558904,5.316775126830336,6.707269751560512,5.3401672063567185,0.0,5.859971891173307,0.0032593196621134475,3.8546383216471938,0.377548846037815,1.9365851270948466,3.0522918491222564,2.9120254360154387,10.508656163755504,1.0052647981006768,2.8714081070979294,4.340146137428895,2.788127545037448,2.08229693827624,7.143551071262723,4.438586210242147,0.0,21.80173910599634,1.0431525332967415])|auto |
      */

//        Column label must be of type numeric but was actually of type string

    val stringIndex = new StringIndexer()
        .setInputCol("label")
        .setOutputCol("indexed")
        .setHandleInvalid("error")

//    收集label：auto-> index(double)
//    val df_tfidf_lab = stringIndex.fit(df_tfIdf).transform(df_tfIdf)
      val df_tfidf_lab = stringIndex.fit(df_tf).transform(df_tf)
//    df_tfidf_lab.show()

    val Array(train,test) = df_tfidf_lab.randomSplit(Array(0.8,0.2))

//    对bayes模型进行实例化定义参数
    val nb = new NaiveBayes()
      .setModelType("multinomial")
//      .setModelType("bernoulli")
      .setSmoothing(1)
//      .setFeaturesCol("feature_tfidf")
      .setFeaturesCol("feature_tf")
      .setLabelCol("indexed")
      .setPredictionCol("pre_label")
      .setProbabilityCol("prob")
      .setRawPredictionCol("rawPred")

//    模型训练
    val nbModel = nb.fit(train)

    val pred = nbModel.transform(test)

    //  param for metric name in evaluation (supports `"f1"` (default), `"weightedPrecision"`,
//    * `"weightedRecall"`, `"accuracy"`)
    val eval = new MulticlassClassificationEvaluator()
      .setLabelCol("indexed")
      .setPredictionCol("pre_label")
      .setMetricName("accuracy")
    val accuracy = eval.evaluate(pred)

    println("Test accuracy："+accuracy)


  }

}
