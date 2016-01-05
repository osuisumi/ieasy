<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<#--在freemarker中如果出现空值，不会什么都不显示，而是会报错，要求开发人员手动处理-->
${emp.id}---${emp.group!("没有group")}
<#--如果使用了对象导航，!仅仅只会判断最后一个是否为空，如果导航中的某个对象为空依然会报错
所以在导航中最好使用括号将要判断的全部包含起来-->
<br/>
${(emp.group.name)!}

${(a.b.c)!("a.b.c没有定义")}

<#--(a.b)??表示判断a.b是否为空-->
<#if (a.b)??>
	不为空
<#else>
	a.b为空
</#if>

${(a.b)???string}
</body>
</html>