package streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ConnectException, Socket}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver

object MyReceiverTestHyj {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    val conf=new SparkConf().setAppName("myReceiverTest").setMaster("local[*]")
    val ssc=new StreamingContext(conf,Seconds(5))
    val receiverStream = ssc.receiverStream(new MyReceiver("master",9999))
    val words=receiverStream.flatMap(_.split(" "))
    val counts=words.map((_,1))
    val countsRes: DStream[(String, Int)] = counts.reduceByKey(_+_)
    countsRes.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
class MyReceiver(host:String,port:Int) extends Receiver[String](StorageLevel.MEMORY_AND_DISK_SER_2){
  private var socket: Socket = _

  override def onStart(): Unit = {
    try {
      socket = new Socket(host, port)
    } catch {
      case e: ConnectException =>
        restart(s"Error connecting to $host:$port", e)
        return
    }

    // Start the thread that receives data over a connection
    new Thread("Socket Receiver") {
      setDaemon(true)
      override def run() { receive() }
    }.start()
  }

  override def onStop(): Unit = {
    if(socket!=null){
      socket.close()
    }
  }

  def receive() {
    val reader = new BufferedReader(new InputStreamReader(socket.getInputStream))
    var line:String=null
    while ((line=reader.readLine())!=null){
      if("end".eq(line)){
        return
      }
      this.store(line)
    }
  }
}
