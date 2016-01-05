<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
${doc.fields.field[0].@id}--${doc.fields.field[0].@name}
<#list doc.fields.field[0].data as d>
${d.key}---${d.value}
</#list>

<#list doc["fields/field[@id='zydm']/data"] as d>
	${d.key}-----${d.value}
</#list>
</body>
</html>