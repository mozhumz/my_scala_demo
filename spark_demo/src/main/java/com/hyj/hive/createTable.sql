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
--1 加载本地文件到hive表
load data local inpath
'/home/badou/Documents/data/order_data/orders.csv'
into table orders_ext;
--2 加载hdfs文件到hive表
load data inpath 'hdfs:///usr/local/hive/warehouse/badou.db/orders/orders.csv' into table orders_ext;