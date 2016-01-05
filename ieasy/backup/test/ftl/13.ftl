<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<#macro showTopics topics titleSize>
	<dl>
		<dt><span><#nested/></span></dt>
		<#list topics as t>
		<dd><a href="#">
			<#if t.title?length gte titleSize>
				${t.title[0..titleSize]}...
			<#else>
				${t.title}
			</#if>
		</a></dd>
		</#list>
	</dl>
</#macro>

<@showTopics topics=topics["1"] titleSize=12>
	第一个栏目
</@showTopics>

<@showTopics topics=topics["2"] titleSize=5>
	哇哈哈哈哈哈
</@showTopics>
</body>
</html>