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