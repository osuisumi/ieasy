<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>hello:${emp.name}---${emp.id}----${emp.age}</h1>

<#--以下显示了如何使用判断结构，注意在判断的标签中不用加入${}-->
<#if emp.age lt 18>
	${emp.name}是童工
<#elseif emp.age gt 60>
	${emp.name}应该退休了
<#else>
	${emp.name}好好工作
</#if>

<#list emps as emp>
	${emp.id}-----${emp.name}---${emp.age}<br/>
</#list>
</body>
</html>