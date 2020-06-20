package com.hyj.spark.offline

import org.apache.spark.ml.feature.RFormula
import org.apache.spark.sql.SparkSession

object SparkTestHyj {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()

    val dataset = spark.createDataFrame(Seq(
      (7, 1, 18, 1.0),
      (8, 2, 12, 0.0),
      (9, 3, 15, 0.0)
    )).toDF("id", "country", "hour", "clicked")
    val formula = new RFormula()
      .setFormula("clicked ~ country + hour")
      .setFeaturesCol("features")
      .setLabelCol("label")
    val output = formula.fit(dataset).transform(dataset)
    output.select("features", "label").show()
  }
}
