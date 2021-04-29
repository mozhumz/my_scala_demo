create external table orders_ext (
order_id string,
user_id string,
eval_set string,
order_number string,
order_dow string,
order_hour_of_day string,
days_since_prior_order string
)
row format delimited fields terminated by ',';
--如果从本地导入后本地的原数据文件依然存在，相当于复制过去；如果是从hdfs导入，则原数据文件不存在，相当于剪切过去
--1 加载本地文件到hive表
load data local inpath
'/home/badou/Documents/data/order_data/orders.csv'
into table orders_ext;
--2 加载hdfs文件到hive表
load data inpath 'hdfs:///usr/local/hive/warehouse/badou.db/orders/orders.csv' into table orders_ext;

--用户日志表
create table trace_log(
    user_id string,--用户id
    do_type int,--操作类型
    do_time string --操作时间
)
row format delimited fields terminated by ',';

load data local inpath
'/usr/local/src/hadoop_demo/hive/test20210428.txt'
into table trace_log;

字符串转时间戳 unix_timestamp('2021-01-01 00:00:00')
时间戳转字符串 from_unixtime(bigint,'yyyy-MM-dd HH:mm:ss')
字符串转日期（具体到某天） to_date('2021-01-01 00:00:00')
-- lag与lead函数是跟偏移量相关的两个分析函数，通过这两个函数可以在一次查询中取出同一字段的前N行的数据(lag)
-- 和后N行的数据(lead)作为独立的列,从而更方便地进行进行数据过滤。这种操作可以代替表的自联接，并且LAG和LEAD有更高的效率。
-- over()表示 lag()与lead()操作的数据都在over()的范围内，over里面可以使用partition by 语句（用于分组）
--  order by 语句（用于排序）。partition by a order by b表示以a字段进行分组，再 以b字段进行排序，对数据进行查询
-- 统计每天a用户后面是b用户的记录数
select do_date,count(1) as cnt from
(select t.*,lead(t.user_id,1) over(partition by do_date order by do_time) as user_id2 from
(select user_id,do_type,unix_timestamp(do_time) as do_time,to_date(do_time) as do_date
from trace_log) t
) x
where
user_id!=user_id2
group by do_date;

--统计每天符合以下条件的用户数：A操作之后是B操作，AB操作必须相邻