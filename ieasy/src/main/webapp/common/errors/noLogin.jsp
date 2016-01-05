<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%String webroot = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();%>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>

<script src="<%=basePath%>/script/jquery/jquery-1.11.0.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/tools/jquery.ieasy.core.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=basePath%>/common/errors/errors.css" />
<script>
$(function() {
	var winWidth = parseInt($.webapp.getInner().width) ;
	var winHeight = parseInt($.webapp.getInner().height) ;
	
	var noLoggin = $("#noLoggin") ;
	var width =  parseInt($.webapp.getStyle(noLoggin[0],"width"));
	var height =  parseInt($.webapp.getStyle(noLoggin[0],"height"));
	
	noLoggin.css("left",((winWidth-width)/2)-50+"px");
	noLoggin.css("top",((winHeight-height)/2)-80+"px");
});

function relogin() {
	window.location.replace("<%=basePath%>/login.jsp") ;
}
</script>
<style type="text/css">
body{color: #666;text-align: center;font-family: Helvetica, 'microsoft yahei', Arial, sans-serif;margin:0;width: 800px;margin: auto;font-size: 14px;}
h1{font-size: 56px;line-height: 100px;font-weight: normal;color: #456;}
h2{font-size: 24px;color: #666;line-height: 1.5em;}
h3{color: #456;font-size: 20px;font-weight: normal;line-height: 28px;}
hr{margin: 18px 0;border: 0;border-top: 1px solid #EEE;border-bottom: 1px solid white;}
a{color: #17bc9b;text-decoration: none;}
</style>

<body cz-shortcut-listen="true" ryt13070="1">
	<p id="noLoggin" style="position: absolute;">
		你还未登陆或登录超时， <a href="javascript:relogin();">点击这里</a>
		回到登陆页面.
	</p>
</body>


