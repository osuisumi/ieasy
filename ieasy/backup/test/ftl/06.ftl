<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
${test?html}
<#assign nums=[1,23,22,33,2,44]/>
<#assign nums=3..100/>
<#list nums[0..9] as num>
	${num}
</#list>
${"这个世界怎么样！！！！"[0..3]}...

<#--freemarker最为特殊的一个问题就是对于map而言，支持String类型作为key-->
<#assign user={"1":"老张","2":"小张"}/>

${user["1"]}

<#--map的遍历首先要获取key-->
<#assign keys=user?keys/>
<#list keys as key>
	${key}---${user["${key}"]}
</#list>
</body>
</html>