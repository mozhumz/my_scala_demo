package com.hyj.spark.offline.dataskew

import org.apache.spark.{HashPartitioner, Partitioner}

/**
  * 自定义分区器
  * @param num
  */
class MyPartitioner(num: Int) extends Partitioner {
  /**
    * 获取分区数量
    * @return
    */
  override def numPartitions(): Int = num

  /**
    * 根据key 获取所在的分区号（分区号从0开始）
    * 此处可自定义分区逻辑
    * @param key
    * @return
    */
  override def getPartition(key: Any): Int = {
//    val code = key.hashCode() % numPartitions()
//    if (code < 0) {
//      code + numPartitions
//    } else {
//      code
//    }
    val domain=new java.net.URL(key.toString).getHost
    val code=domain.hashCode % numPartitions()
    if(code<0){
      code+numPartitions()
    }else{
      code
    }

  }

  override def equals(other: Any): Boolean = other match {
    case m: MyPartitioner =>
      m.numPartitions == numPartitions
    case _ =>
      false

//    case h: HashPartitioner =>
//      h.numPartitions == numPartitions
//    case _ =>
//      false
  }
}
