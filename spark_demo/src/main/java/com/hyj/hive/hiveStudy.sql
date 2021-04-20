--1 word count
art_ext2: sentence
select regexp_extract(word,'[A-Za-z]+',0) as word,count(1) as cnt from (
    select explode(split(sentence,' ')) as word from art_ext2
) t group by word
order by cnt desc
limit 10;

-- 测试正则 过滤得到英文字母
select regexp_extract('2Certainly,”','[A-Za-z]+',0); --Certainly

--partition
--1.创建分区表（表在创建过程要指定是分区表）
create table art_dt(sentence string)
partitioned by (dt string)
row format delimited fields terminated by '\n';
--2.插入数据
insert into table art_dt partition(dt='20191109')
select * from art_ext limit 100;

select * from art_dt where dt='20191110' limit 10;

--重写当前分区的数据
insert overwrite table art_dt partition(dt='20191110')
select * from art_ext limit 100;

--orders
--order_id,订单编号
--user_id,用户id
--eval_set,标识订单是否为历史数据
--order_number,用户购买订单的编号（优先级：1表示第一个购买，2表示第二个购买）
--order_dow,dow（day of week）星期几
--order_hour_of_day,一天中哪一个小时产生的订单
--days_since_prior_order,下一个订单距离上一个订单间隔时间（按天）
load data local inpath
'/home/badou/Documents/data/order_data/orders.csv'
into table orders;

--priors
--order_id,订单id
--product_id,商品id
--add_to_cart_order,订单支付先后的位置
--reordered,当前商品是否为重复下单的行为
create table priors(
order_id string,
product_id string,
add_to_cart_order string,
reordered string
)
row format delimited fields terminated by ',';

--orders：订单与用户的关系
--priors：订单中商品详情
--1.每个订单有多少商品
select order_id,count(1) as prod_cnt from priors
group by order_id
order by prod_cnt desc
limit 10;
--2. 每个用户有多个订单，每个用户所有商品数量
 --每个用户有多个订单
(select user_id,count(1) as order_cnt from orders
group by user_id
order by order_cnt desc)

 --每个用户所有商品数量
select user_id,sum(x.prod_cnt) as user_prod_cnt from orders o
join
(
select order_id,count(1) as prod_cnt from priors p
group by order_id
) x
on o.order_id=x.order_id
group by user_id
order by user_prod_cnt desc
limit 10;

--作业：
1. 将orders和priors建表入hive
2. 每个用户有多少个订单
select user_id,count(1) as order_count from orders
3. 每个用户平均每个订单是多少商品
select
4. 每个用户在一周中的购买订单的分布情况 --case when
user_id dow_0 dow_1 dow_2 dow_3 dow_4 dow_5 dow_6
1111      0    3      2    2      3     0     0

2539329,1,prior,1,2
2398795,1,prior,2,3
473747,1,prior,3,3
2254736,1,prior,4,4
431534,1,prior,5,4
3367565,1,prior,6,2
550135,1,prior,7,1
3108588,1,prior,8,1
2295261,1,prior,9,1
2550362,1,prior,10,4
5. 一个用户平均每个月购买多少个商品（30天一个月）平均每30天
6.每个用户最喜爱购买的三个product是什么。
user_id product_id1,product_id2,product_id3
			top1     top2        top3