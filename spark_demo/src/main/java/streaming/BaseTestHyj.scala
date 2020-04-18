package streaming

object BaseTestHyj {
  def main(args: Array[String]): Unit = {
    val Array(brokers, groupId, topics) = Array("192.168.174.134:9092","group1","badou")
    println(topics)
  }
}
