<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<#--自定义指令，第一个参数是指令的名称，接着的参数就是指令的参数
可以为参数增加默认值，有默认值的参数必须放在最后，当一个参数有了默认值之后
在调用的时候可以省略这个参数-->
<#macro hello world num=3>
	<#list 1..num as n>
		hello:${username}[${world}]-->${n}
	</#list>
</#macro>
<#--调用hello这个指令，传入参数必须写参数名-->
<@hello world="老张"/>

<#--可以使用nested标签嵌入调用自定义指令中的内容-->
<#macro test num=3>
	<ul>
	<#list 1..num as n>
		<#nested n/>
	</#list>
	</ul>
</#macro>

<@test;n>
	<li>${n}.abc</li>
</@test>

<#macro helloworld>
	<#--当函数执行之后，使用assign所定义的模板变量会被覆盖，这是一个有风险的行为，
	所以在函数中如果没有特殊的需求，一般不适应assign来定义相应的变量，而是使用
	local来定义变量，用local来定义的变量就是局部变量-->
	<#--<#assign username="小李"/>-->
	
	<#local username="小李"/>
	macro:${username}
</#macro>
<@helloworld/>
${username}
</body>
</html>