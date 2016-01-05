<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<#--如果使用include来导入多个ftl文件，这些ftl文件中所定义的模板变量可能会完成覆盖，所以如果要引入
相应的由ftl编写的函数等模块不建议使用include,而是用import来代替，include一般用于引入模板的公共部分
在这些公共部分中不加入任何变量定义-->
<#include "/inc/inc1.ftl"/>
<#include "/inc/inc2.ftl"/>

<#--使用import可以加入相应的名称空间，以下代码将inc1.ftl引入并且加入到了inc1的名称空间中-->
<#import "/inc/inc1.ftl" as inc1/>
<#import "/inc/inc2.ftl" as inc2/>
${inc1.username}---${inc2.username}
</body>
</html>