<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>事务通知</title>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="shortcut icon" href="${domain}/images/sys_img/favicon.ico" />
	<style>
		body,h1,h2,h3,ul,li{margin:0px;padding:0px;}body{width:1000px;margin:0 auto;font-size:14px;}
		ul{list-style:none;}#top{height:80px;border-bottom:6px solid #3F9BCA;position:relative;}
		#top h2{line-height:80px;font-size:26px;text-align:center;}#top .pd{position:absolute;right:0px;bottom:5px;display:inline-block;}
		#footer{margin-top:10px;border-top:3px solid #3F9BCA;height:50px;line-height:20px;}#footer ul{margin-top:5px;float:right;}#footer li{text-align:right;}
		#footer li a{text-decoration:none;color:red;}
		a{text-decoration: none; color: red;} 
	</style>
</head>

<body>

	<div id="top">
		<h2>${title}</h2>
		<span class="pd">打印日期：${.now?string("yyyy/MM/dd")}</span>
	</div>
	<div id="center">
		您好 <br/><br/>
    		
   		&nbsp;&nbsp;&nbsp;您有以下项目需审批，请及时登录系统进行审批。<br/>
   		&nbsp;&nbsp;&nbsp;项目名称：${project.proj_name} <br/>
   		&nbsp;&nbsp;&nbsp;登录地址：<a href="${domain}/login.jsp" target="_blank">登录</a> <br/><br/><br/>
   		
   		<br/><br/>
	
	</div>
	<div id="footer">
		<ul>
   			<li>广州华智科技有限公司&copy;项目管理部</li>
   			<li>如无法正常显示，请点击<a href="${link}">&nbsp;这里&nbsp;</a>仅华智内部使用</li>
   		</ul>
	</div>
    
</body>
</html>
