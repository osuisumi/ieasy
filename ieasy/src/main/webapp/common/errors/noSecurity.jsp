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
	
	var noSecurity = $("#noSecurity") ;
	var width =  parseInt($.webapp.getStyle(noSecurity[0],"width"));
	var height =  parseInt($.webapp.getStyle(noSecurity[0],"height"));
	
	noSecurity.css("left",((winWidth-width)/2)-50+"px");
	noSecurity.css("top",((winHeight-height)/2)-80+"px");
	var str = "权限不足，请<a href='javascript:callMe();'>联系超管</a>赋予您资源访问权限。<br/>"+
	  		  "&nbsp;${msg}";
	$("#msg_error").html(str) ;
	
});
function callMe() {
	alert("Don't call me！") ;
}
</script>



<div id="noSecurity">
	<h1>操作警告</h1>
	<div id="msg_error"></div>
</div>


