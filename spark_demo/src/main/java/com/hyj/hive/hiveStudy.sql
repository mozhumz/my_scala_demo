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
create table orders(
order_id string,
user_id string
)
row format delimited fields terminated by ',';

2. 每个用户有多少个订单
select user_id,count(1) as order_count from orders
group by user_id
order by order_count desc
limit 20;
3. 每个用户平均每个订单是多少商品
select o.user_id,avg(t.prod_cnt) as avg_prod_cnt from orders o
left join
(select order_id,count(1) as prod_cnt from priors group by order_id) t
on o.order_id=t.order_id
group by user_id
order by avg_prod_cnt desc
limit 20;
4. 每个用户在一周中的购买订单的分布情况 --case when
user_id dow_0 dow_1 dow_2 dow_3 dow_4 dow_5 dow_6
1111      0    3      2    2      3     0     0
 --方法一
select user_id,collect_set(dow_cnt) as dow_cnt_set
 from (
select a.user_id,concat(a.order_dow,'_',a.dow_cnt) as dow_cnt
from
(select user_id,order_dow,count(1) as dow_cnt from orders
group by user_id,order_dow
order by user_id,dow_cnt desc
) a) b
group by user_id
limit 20;

--方法二
select user_id,
sum(case order_dow when '0' then 1 else 0 end) as dow_0,
sum(case order_dow when '1' then 1 else 0 end) as dow_1 ,
sum(case order_dow when '2' then 1 else 0 end) as dow_2 ,
sum(case order_dow when '3' then 1 else 0 end) as dow_3,
sum(case order_dow when '4' then 1 else 0 end) as dow_4 ,
sum(case order_dow when '5' then 1 else 0 end) as dow_5 ,
sum(case order_dow when '6' then 1 else 0 end) as dow_6
from orders
group by user_id
limit 20;

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
 --先查询每个用户商品数，再查询每个用户购买订单的间隔月份数，两者相除
select user_id,sum(prod_cnt) as total_prod_cnt ,
sum(cast(if(days_since_prior_order='','0',days_since_prior_order) as float))/30 as mon_cnt,
sum(prod_cnt)/(sum(cast(if(days_since_prior_order='','0',days_since_prior_order) as float))/30) as prod_cnt_per_mon
from orders a
join (
select order_id,count(1) as prod_cnt from priors
where order_id!='order_id'
group by order_id
) b
on  a.order_id=b.order_id
where a.order_id!='order_id'
group by user_id
limit 10;

6.每个用户最喜爱购买的三个product是什么。
user_id product_id1,product_id2,product_id3
			top1     top2        top3
select *,
 row_number() over(partition by user_id order by prod_cnt desc) as rno
 from
(select a.user_id,b.product_id,count(1) as prod_cnt
 from orders a
join priors b on a.order_id=b.order_id
group by a.user_id,b.product_id) t
limit 20;
 --优化
 select user_id,collect_set(product_id) as prod_set
( select *,
 row_number() over(distribute by user_id sort by prod_cnt desc) as rno
 from
(select a.user_id,b.product_id,count(1) as prod_cnt
 from orders a
join priors b on a.order_id=b.order_id
group by a.user_id,b.product_id) t
) x
where rno<=3
group by user_id
;

-- 7 现有一个订单表，含有以下几个字段：order_info(order_id,user_id,pay_amount,pay_time,goods_level_1,goods_level_2)，
-- 分别为订单ID、用户ID、成交金额、成交时间、一级类目，二级类目。

-- 7.1 求最近7天内每一个类目下成交总金额排名前三的二级类目
select goods_level_1,collect_list(goods_level_2) as amount_level2_set
(select goods_level_1,goods_level_2,
 row_number() over(partition by goods_level_1,goods_level_2 sort by amount2 desc) as rno
from
(select goods_level_1,goods_level_2,sum(pay_amount) as amount2
from order_info
where
unix_timestamp()-from_unixtime(pay_time)<=7*24*3600*1000
group by  goods_level_1,goods_level_2) t
) x
where
rno<=3
group by goods_level_1;

-- 8 有订单详情表和活动报名表
-- 订单详情表order_info(user_id,pay_amount,pay_time）
-- 活动报名表act_apply(act_id,user_id,act_time)
-- 8.1 统计每个活动对应所有用户在报名后产生的总订单金额、总订单数
select act_id,sum(pay_amount),count(1) as total_cnt from order_info a
join act_apply b on a.user_id=b.user_id
where a.pay_time>b.act_time
group by act_id;
