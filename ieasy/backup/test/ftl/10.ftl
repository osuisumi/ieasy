<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<#import "/inc/select.ftl" as my/>

<#list filterDoc["filters/filter[@id='${obj}']/field"] as f>
	<@my.xmlselect cid="${f.@cid}" id="${f.@id}"/>
</#list>
</body>
</html>