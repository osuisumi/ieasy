<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<#import "/inc/select.ftl" as my/>

<@my.select id="choice" datas=["张学友","刘德华","沙和尚"] value="沙和尚"/>
<@my.select id="address" datas=["北京","上海","天津","昭通"] headtext="选择地址"/>

<@my.select id="emp" datas=emps key="id" text="name" value="3" headkey="-1" headtext="请选择用户"/>

<@my.select id="emp" datas={"0":"男","1":"女"} headkey="-1" headtext="请选择性别"/>

<@my.select id="stu" datas=stus key="id" text="name" headkey="-1" headtext="请选择学生"/>
</body>
</html>