<!--[if lt IE 10]><script>window.location.href="/ieasy/common/browse/";</script><![endif]-->
<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/common/header/mytags.jsp"%>
<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
	<meta charset="UTF-8">
	<title>iEasy</title>
	<link rel="shortcut icon" href="${webRoot}/images/sys_img/favicon.ico">
	<link href="${webRoot}/login/login.css" rel="stylesheet">
	<script type="text/javascript" charset="UTF-8" src="${webRoot}/login/prefixfree.min.js"></script>
	<script src="script/jquery/jquery-1.11.0.js" type="text/javascript"></script>
	<script src="${webRoot}/script/plugins/jquery/jquery.cookie.js" type="text/javascript"></script>
	<script src="${webRoot}/script/tools/jquery.ieasy.core.js" type="text/javascript"></script>
	<link href="${webRoot}/script/bootstrap/plugins/iCheck/skins/all.css" rel="stylesheet" />
	<script src="${webRoot}/script/bootstrap/plugins/iCheck/icheck.js"></script>
	<script src="${webRoot}/login/login.js"></script>
</head>

<body>

	<div class="content">
		<form id="login-form" class="login-form">
			<div id="tx"><img id="tx_img" src="${webRoot}/login/profilepicture.jpg"></div>
			<div class="account">
				<input type="text" name="account" placeholder="账号" autocomplete="on" />
				<span class="user-icon icon">U</span>
			</div>
			<div class="password">
				<input type="password" name="password" placeholder="密码" />
				<span class="password-icon icon">P</span>
			</div>
			<div class="password" id="valid_box" style="display:none;">
				<input type="text" name="validCode" style="float:left;width:100px;" placeholder="验证码" />
				<img id="valid_img" style="float:left;border:1px solid #ccc;margin-left:1px;margin-top:1px;" src="${webRoot}/validCodeServlet">
				<span class="password-icon icon">V</span>
			</div>
			<div class="account-control">
				<div id="icheck" class="memember">
					<input type="checkbox" id="remember" name="remember" value="false">
					<label class="lableTxt" for="remember">记住账号？</label>&nbsp;&nbsp;
				</div>
				<div class="login"><button type="button" name="login">Login</button></div>
			</div>
			<p class="not-registered"> <!-- not-registered-b -->
				欢迎您！请输入账号密码
			</p>
		</form>
	</div>
   
</body>
</html>