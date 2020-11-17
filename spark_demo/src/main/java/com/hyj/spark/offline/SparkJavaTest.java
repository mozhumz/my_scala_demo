package com.hyj.spark.offline;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;
import scala.Tuple3;

import java.util.*;

/**
 * 数据倾斜解决
 */
public class SparkJavaTest {
    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setMaster("local").setAppName("SparkFlatMapJava")
                .set("spark.sql.shuffle.partitions","300");
//        SparkContext sc = session.sparkContext();

        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaPairRDD<String, Long> rdd = sc.textFile("file:///G:\\idea_workspace\\my_scala_demo\\input\\word.txt")
                .flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        }).mapToPair(new PairFunction<String, String, Long>() {
            @Override
            public Tuple2<String, Long> call(String s) throws Exception {
                return new Tuple2<String, Long>(s, 1L);
            }
        });
        JavaPairRDD<String, Long> rdd2 = sc.textFile("file:///G:\\idea_workspace\\my_scala_demo\\input\\word2.txt")
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public Iterator<String> call(String s) throws Exception {
                        return Arrays.asList(s.split(" ")).iterator();
                    }
                }).mapToPair(new PairFunction<String, String, Long>() {
                    @Override
                    public Tuple2<String, Long> call(String s) throws Exception {
                        return new Tuple2<String, Long>(s, 1L);
                    }
                });
//        combineWithTwoSteps(rdd);
        sampleKey(rdd,rdd2);

    }

    /**
     * 使用随机前缀和扩容RDD进行join
     * @param rdd1
     * @param rdd2
     */
    private static void randomKeyAndExtend(JavaPairRDD<String, Long> rdd1, JavaPairRDD<String, Long> rdd2){
        // 首先将其中一个key分布相对较为均匀的RDD膨胀100倍。
        JavaPairRDD<String, Long> expandedRDD = rdd1.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String,Long>, String, Long>() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public Iterator<Tuple2<String, Long>> call(Tuple2<String, Long> tuple)
                            throws Exception {
                        List<Tuple2<String, Long>> list = new ArrayList<Tuple2<String, Long>>();
                        for(int i = 0; i < 100; i++) {
                            list.add(new Tuple2<String, Long>(0 + "_" + tuple._1, tuple._2));
                        }
                        return list.iterator();
                    }
                });

// 其次，将另一个有数据倾斜key的RDD，每条数据都打上100以内的随机前缀。
        JavaPairRDD<String, Long> mappedRDD = rdd2.mapToPair(
                new PairFunction<Tuple2<String,Long>, String, Long>() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, Long> tuple)
                            throws Exception {
                        Random random = new Random();
                        int prefix = random.nextInt(100);
                        return new Tuple2<String, Long>(prefix + "_" + tuple._1, tuple._2);
                    }
                });

// 将两个处理后的RDD进行join即可。
        JavaPairRDD<String, Tuple2<Long, Long>> joinedRDD = mappedRDD.join(expandedRDD);
        joinedRDD.foreach(new VoidFunction<Tuple2<String, Tuple2<Long, Long>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<Long, Long>> stringTuple2Tuple2) throws Exception {
                System.out.println(stringTuple2Tuple2);
            }
        });
    }

    /**
     * 通过采样找出rdd1数据倾斜的key 做成skewedRdd1（对key添加随机数）,找到rdd2同样的key 做成skewedRdd2（扩容n倍）
     * 两个rdd join，rdd1的普通rdd join rdd2
     *
     * @param rdd1
     * @param rdd2
     */
    private static void sampleKey(JavaPairRDD<String, Long> rdd1, JavaPairRDD<String, Long> rdd2) {
        // 首先从包含了少数几个导致数据倾斜key的rdd1中，采样10%的样本数据。
//        JavaPairRDD<String, Long> sampledRDD = rdd1.sample(false, 0.1);
        System.out.println("rdd1:");
        rdd1.foreach(new VoidFunction<Tuple2<String, Long>>() {
            @Override
            public void call(Tuple2<String, Long> stringLongTuple2) throws Exception {
                System.out.println(stringLongTuple2);
            }
        });
        JavaPairRDD<String, Long> sampledRDD=rdd1;

// 对样本数据RDD统计出每个key的出现次数，并按出现次数降序排序。
// 对降序排序后的数据，取出top 1或者top 100的数据，也就是key最多的前n个数据。
// 具体取出多少个数据量最多的key，由大家自己决定，我们这里就取1个作为示范。
        JavaPairRDD<String, Long> countedSampledRDD = sampledRDD.reduceByKey(
                new Function2<Long, Long, Long>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Long call(Long v1, Long v2) throws Exception {
                        return v1 + v2;
                    }
                });
        JavaPairRDD<Long, String> reversedSampledRDD = countedSampledRDD.mapToPair(
                new PairFunction<Tuple2<String, Long>, Long, String>() {
                    @Override
                    public Tuple2<Long, String> call(Tuple2<String, Long> tuple) throws Exception {
                        return new Tuple2<Long, String>(tuple._2, tuple._1);
                    }

                });

        final String skewedUserid = reversedSampledRDD.sortByKey(false).take(1).get(0)._2;

// 从rdd1中分拆出导致数据倾斜的key，形成独立的RDD。
        JavaPairRDD<String, Long> skewedRDD = rdd1.filter(
                new Function<Tuple2<String, Long>, Boolean>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Boolean call(Tuple2<String, Long> tuple) throws Exception {
                        return tuple._1.equals(skewedUserid);
                    }
                });
// 从rdd1中分拆出不导致数据倾斜的普通key，形成独立的RDD。
        JavaPairRDD<String, Long> commonRDD = rdd1.filter(
                new Function<Tuple2<String, Long>, Boolean>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Boolean call(Tuple2<String, Long> tuple) throws Exception {
                        return !tuple._1.equals(skewedUserid);
                    }
                });

// rdd2，就是那个所有key的分布相对较为均匀的rdd。
// 这里将rdd2中，前面获取到的key对应的数据，过滤出来，分拆成单独的rdd，并对rdd中的数据使用flatMap算子都扩容100倍。
// 对扩容的每条数据，都打上0～100的前缀。
        JavaPairRDD<String, Long> skewedRdd2 = rdd2.filter(
                new Function<Tuple2<String, Long>, Boolean>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Boolean call(Tuple2<String, Long> tuple) throws Exception {
                        return tuple._1.equals(skewedUserid);
                    }
                }).flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Long>, String, Long>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Iterator<Tuple2<String, Long>> call(
                            Tuple2<String, Long> tuple) throws Exception {
                        List<Tuple2<String, Long>> list = new ArrayList<Tuple2<String, Long>>();
                        for (int i = 0; i < 100; i++) {
                            list.add(new Tuple2<String, Long>(i + "_" + tuple._1, tuple._2));
                        }
                        return list.iterator();
                    }

                });

// 将rdd1中分拆出来的导致倾斜的key的独立rdd，每条数据都打上100以内的随机前缀。
// 然后将这个rdd1中分拆出来的独立rdd，与上面rdd2中分拆出来的独立rdd，进行join。
        JavaPairRDD<String, Tuple2<Long, Long>> join = skewedRDD.mapToPair(
                new PairFunction<Tuple2<String, Long>, String, Long>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, Long> tuple)
                            throws Exception {
                        Random random = new Random();
                        int prefix = random.nextInt(100);
                        return new Tuple2<String, Long>(prefix + "_" + tuple._1, tuple._2);
                    }
                })
                .join(skewedRdd2);

        System.out.println("join rdd:");
        join.foreach(new VoidFunction<Tuple2<String, Tuple2<Long, Long>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<Long, Long>> tuple) throws Exception {
                System.out.println(tuple);
            }
        });
        //去除前缀
        JavaPairRDD<String, Tuple2<Long, Long>> joinedRDD1 = join
                .mapToPair(
                        new PairFunction<Tuple2<String, Tuple2<Long, Long>>, String, Tuple2<Long, Long>>() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public Tuple2<String, Tuple2<Long, Long>> call(
                                    Tuple2<String, Tuple2<Long, Long>> tuple)
                                    throws Exception {
                                String key = String.valueOf(tuple._1.split("_")[1]);
                                return new Tuple2<String, Tuple2<Long, Long>>(key, tuple._2);
                            }
                        }
                );

// 将rdd1中分拆出来的包含普通key的独立rdd，直接与rdd2进行join。
        JavaPairRDD<String, Tuple2<Long, Long>> joinedRDD2 = commonRDD.join(rdd2);
        System.out.println("joinedRDD2:");
        joinedRDD2.foreach(new VoidFunction<Tuple2<String, Tuple2<Long, Long>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<Long, Long>> stringTuple2Tuple2) throws Exception {
                System.out.println(stringTuple2Tuple2);
            }
        });

// 将倾斜key join后的结果与普通key join后的结果，uinon起来。
// 就是最终的join结果。
        JavaPairRDD<String, Tuple2<Long, Long>> joinedRDD = joinedRDD1.union(joinedRDD2);
        System.out.println("res:");
        joinedRDD.foreach(new VoidFunction<Tuple2<String, Tuple2<Long, Long>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<Long, Long>> stringTuple2Tuple2) throws Exception {
                System.out.println(stringTuple2Tuple2);
            }
        });
        System.out.println("res0:");
        rdd1.join(rdd2).foreach(new VoidFunction<Tuple2<String, Tuple2<Long, Long>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<Long, Long>> stringTuple2Tuple2) throws Exception {
                System.out.println(stringTuple2Tuple2);
            }
        });
    }

    /**
     * 将reduce join转换为mapJoin 数据较小的rdd广播出去
     *
     * @param rdd
     * @param rdd2
     * @param sc
     */
    private static void reducejoinToMapjoin(JavaPairRDD<String, Long> rdd,
                                            JavaPairRDD<String, Long> rdd2, JavaSparkContext sc) {
        // 首先将数据量比较小的RDD的数据，collect到Driver中来。
        List<Tuple2<String, Long>> rdd1Data = rdd.collect();
// 然后使用Spark的广播功能，将小RDD的数据转换成广播变量，这样每个Executor就只有一份RDD的数据。
// 可以尽可能节省内存空间，并且减少网络传输性能开销。
        final Broadcast<List<Tuple2<String, Long>>> rdd1DataBroadcast = sc.broadcast(rdd1Data);

// 对另外一个RDD执行map类操作，而不再是join类操作。
        JavaPairRDD<String, Long> joinedRdd = rdd2.mapToPair(
                new PairFunction<Tuple2<String, Long>, String, Long>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, Long> tuple)
                            throws Exception {
                        // 在算子函数中，通过广播变量，获取到本地Executor中的rdd1数据。
                        List<Tuple2<String, Long>> rdd1Data = rdd1DataBroadcast.value();
                        // 可以将rdd1的数据转换为一个Map，便于后面进行join操作。
                        Map<String, Long> rdd1DataMap = new HashMap<String, Long>();
                        for (Tuple2<String, Long> data : rdd1Data) {
                            //如果rdd1有重复key 那么value叠加 rdd1DataMap.put(data._1, data._2+value);
                            rdd1DataMap.put(data._1, data._2);
                        }
                        // 获取当前RDD数据的key以及value。
                        String key = tuple._1;
                        Long value = tuple._2;
                        // 从rdd1数据Map中，根据key获取到可以join到的数据。
                        Long rdd1Value = rdd1DataMap.get(key);
                        return new Tuple2<>(key, value + rdd1Value);
                    }
                });

// 这里得提示一下。
// 上面的做法，仅仅适用于rdd1中的key没有重复，全部是唯一的场景。
// 如果rdd1中有多个相同的key，那么就得用flatMap类的操作，在进行join的时候不能用map，而是得遍历rdd1所有数据进行join。
// rdd2中每条数据都可能会返回多条join后的数据。
    }

    /**
     * 两阶段聚合（局部聚合+全局聚合）
     *
     * @param rdd
     */
    private static void combineWithTwoSteps(JavaPairRDD<String, Long> rdd) {
        // 第一步，给RDD中的每个key都打上一个随机前缀。
        JavaPairRDD<String, Long> randomPrefixRdd = rdd.mapToPair(
                new PairFunction<Tuple2<String, Long>, String, Long>() {
                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, Long> tuple) throws Exception {
                        Random random = new Random();
                        int prefix = random.nextInt(10);
                        return new Tuple2<String, Long>(prefix + "_" + tuple._1, tuple._2);
                    }
                }

        );

// 第二步，对打上随机前缀的key进行局部聚合。
        JavaPairRDD<String, Long> localAggrRdd = randomPrefixRdd.reduceByKey(
                new Function2<Long, Long, Long>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Long call(Long v1, Long v2) throws Exception {
                        return v1 + v2;
                    }
                });

// 第三步，去除RDD中每个key的随机前缀。
        JavaPairRDD<Long, Long> removedRandomPrefixRdd = localAggrRdd.mapToPair(
                new PairFunction<Tuple2<String, Long>, Long, Long>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Tuple2<Long, Long> call(Tuple2<String, Long> tuple)
                            throws Exception {
                        long originalKey = Long.valueOf(tuple._1.split("_")[1]);
                        return new Tuple2<Long, Long>(originalKey, tuple._2);
                    }
                });

// 第四步，对去除了随机前缀的RDD进行全局聚合。
        JavaPairRDD<Long, Long> globalAggrRdd = removedRandomPrefixRdd.reduceByKey(
                new Function2<Long, Long, Long>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Long call(Long v1, Long v2) throws Exception {
                        return v1 + v2;
                    }
                });
    }
}
