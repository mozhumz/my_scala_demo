package streaming

import com.alibaba.fastjson.JSON
import streaming.kafka.Orders

object JsonTest {
  def main(args: Array[String]): Unit = {
    val s =
      """
        |{"user_id": 1, "hour": 7, "order_id": 2398795, "eval_set": "prior", "order_dow": 3, "order_number": 2, "day": 15.0}
      """.stripMargin
//    "order_id": 2398795
//    "order_dow": 3
    val mess = JSON.parseObject(s,classOf[Orders])
    println(mess.order_id,mess.order_dow)
  }

}
