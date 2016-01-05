<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<#--用来导入相应的数据，导入的路径依然是从模板所在的位置开始-->
<#include "/inc/top.ftl"/>
<#assign n=1/>
${n+1}
<#assign s="1"/>
${s+1}
<#assign b=true/>
<#--对于freemarker而言，只能用${}这种方式输出数字或者字符串，对于其他类型都无法输出-->
<#--${b}:是一个boolean类型，无法输出，需要将其转换为字符串才能输出-->
${b?string}<#--将boolean转换为字符串-->
${b?string("yes","no")}
<#--${.now}显示当前的日期-->
${.now}--->${.now?string("[yyyy/MM/dd]")}

<#assign d="2012-12-22 12:22:22"?date("yyyy-MM-dd HH:mm:ss")>
${d?string("yyyy/MM/dd HH:mm:ss")}
</body>
</html>