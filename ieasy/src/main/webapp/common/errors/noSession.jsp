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
	
	var noSession = $("#noSession") ;
	var width =  parseInt($.webapp.getStyle(noSession[0],"width"));
	var height =  parseInt($.webapp.getStyle(noSession[0],"height"));
	
	noSession.css("left",((winWidth-width)/2)-50+"px");
	noSession.css("top",((winHeight-height)/2)-80+"px");
	var str = "您还没有登录或登录已超时。<br/>"+
			  "&nbsp;请重新<a href='javascript:relogin();'>登录</a>，然后再刷新本功能！";
	$("#msg_error").html(str) ;
});

function relogin() {
	parent.window.location.href="<%=basePath%>/login.jsp" ;
}
</script>


<div id="noSession">
	<h1>操作警告</h1>
	<div id="msg_error"></div>
</div>
