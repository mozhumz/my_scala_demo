package com.hyj.flink.redis

import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

object MyRedisUtil {
  val conf: FlinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder()
    .setHost("master").setPort(6379).setPassword("123456").build()

  def getRedisSink(): RedisSink[(String, Int)] = {
    new RedisSink[(String, Int)](conf, new MyRedisMapper)
  }

  class MyRedisMapper extends RedisMapper[(String, Int)] {
    override def getCommandDescription: RedisCommandDescription = {
      new RedisCommandDescription(RedisCommand.HSET, "wordcount")
    }

    override def getKeyFromData(t: (String, Int)): String = t._1

    override def getValueFromData(t: (String, Int)): String = t._2.toString
  }

}
