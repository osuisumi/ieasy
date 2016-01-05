<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>数据模板</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/sys_img/favicon.ico" />
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
		<h2>华智项目管理系统登录账号和密码</h2>
		<span class="pd">打印日期：2017-05-04</span>
	</div>
	<div id="center">
		您好：${name} <br/><br/>
    		
   		&nbsp;&nbsp;&nbsp;如下是你在华智项目管理系统的登录账号和密码。<br/>
   		&nbsp;&nbsp;&nbsp;登录账号：${account} <br/>
   		&nbsp;&nbsp;&nbsp;登录密码：${password} <br/>
   		&nbsp;&nbsp;&nbsp;访问地址：<a href="${reportURL}" target="_blank">&nbsp;请点击这里&nbsp;</a> <br/><br/><br/>
   		
   		<font>
    		相关事项：<br/>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		以上密码为你的初始化密码，请登陆系统后进行修改。建议修改为你个人电脑的登陆密码。<br/>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		请使用  IE(Version 10以上版本)/ Chrome/ Firefox  系列浏览器<br/>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		系统不支持IE10以下的浏览器，推荐您使用最新版的 Chrome、Firefox 及以上版本的浏览器，以获取最佳的浏览效果<br/>
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		已共享 Chrome、Firefox浏览器，地址：\\192.168.2.206\浏览器\
   		</font>
   		<br/><br/>
	
	</div>
	<div id="footer">
		<ul>
   			<li>广州华智科技有限公司&copy;项目管理部</li>
   			<li>如无法正常显示，请点击<a href="">&nbsp;这里&nbsp;</a>仅华智内部使用</li>
   		</ul>
	</div>
    
</body>
</html>



