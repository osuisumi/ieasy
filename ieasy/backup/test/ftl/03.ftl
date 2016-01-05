<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<#--用来导入相应的数据，导入的路径依然是从模板所在的位置开始-->
<#include "/inc/top.ftl"/>
<#--以下定义是和root中的username有冲突，其实不是进行覆盖，而且用一个优先级比较高的对象将其隐藏了-->
<#assign username="小李"/>
<#--
	对于freemarker而言，有如下几种数据范围
	1、数据模型变量--root中的变量
	2、模板变量--在模板文件中通过assign来定义
	3、局部变量
	4、循环变量--
	
	当在模板中显示这个变量的时候，首先会找模板变量，如果模板变量不存在就会去数据模型中找
-->
${username}<#--模板中存在，所以就直接显示-->
${.globals.username}<#--.globals可以直接去数据模型中找变量-->
${emp.name}

<#list emps as emp>
<#--循环变量仅仅只是在循环中有效，出了循环马上失效，不会影响模板变量-->
	${emp.name}
</#list>

${emp.name}
</body>
</html>