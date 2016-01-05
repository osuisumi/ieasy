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
	
	var pro_error = $("#pro_error") ;
	var width =  parseInt($.webapp.getStyle(pro_error[0],"width"));
	var height =  parseInt($.webapp.getStyle(pro_error[0],"height"));
	
	pro_error.css("left",((winWidth-width)/2)-50+"px");
	pro_error.css("top",((winHeight-height)/2)-80+"px");
	
});
</script> 


<div id="pro_error">
	<h1>程序出错</h1>
	<div id="msg_error">程序发生错误，请联系管理员！</div>
</div>


