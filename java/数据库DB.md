#数据库DB

##行列转换
- 列转行(max(case..when..then))

 ```sql
 select Student as '姓名',
 max(case Subject when '语文' then Score else 0 end) as '语文' ,
 max(case Subject when '英语' then Score else 0 end ) as '英语'
 from Tbl_Scores
 group by Student
 order by Student
```

- 行转列(union all)


```sql
SELECT * FROM (
SELECT name, "语文",  score FROM Tbl_Scores
union all
SELECT name, "数学",  score FROM Tbl_Scores
union all
SELECT name, "英语", as score FROM Tbl_Scores
) tepTable
 ```

##索引
>详见: [美团技术Mysql索引原理](http://tech.meituan.com/mysql-index.html)

###MYSQL索引原理
- Mysql索引底层结构：B+树。其中非叶子结点只存储索引搜索方向数据，叶子节点则存储真实数据。
- B+树IO次数取决于树的高度h=\log(m+1)N 【m:树节点，N：数据】
- 当为联合索引的复合结构时:b+树按照从左到右的顺序来建立搜索树

###索引几个原则
- 最左前缀匹配原则，非常重要的原则，mysql会一直向右匹配直到遇到范围查询(>、<、between、like)就停止匹配，比如a = 1 and b = 2 and c > 3 and d = 4 如果建立(a,b,c,d)顺序的索引，d是用不到索引的，如果建立(a,b,d,c)的索引则都可以用到，a,b,d的顺序可以任意调整。
- =和in可以乱序，比如a = 1 and b = 2 and c = 3 建立(a,b,c)索引可以任意顺序，mysql的查询优化器会帮你优化成索引可以识别的形式
- 尽量选择区分度高的列作为索引,区分度的公式是count(distinct col)/count(*),表示字段不重复的比例，比例越大我们扫描的记录数越少，唯一键的区分度是1
- 索引列不能参与计算,比如from_unixtime(create_time) = ’2014-05-29’就不能使用到索引，原因很简单，b+树中存的都是数据表中的字段值，但进行检索时，需要把所有元素都应用函数才能比较，显然成本太大。所以语句应该写成create_time = unix_timestamp(’2014-05-29’);
- 尽量的扩展索引，不要新建索引。比如表中已经有a的索引，现在要加(a,b)的索引，那么只需要修改原来的索引即可