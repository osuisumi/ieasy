<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%String webroot = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();%>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>iEasy</title>
	
	
	<link href="<%=basePath%>/script/bootstrap/css/bootstrap.css" rel="stylesheet" />
	
	<link href="<%=basePath%>/script/bootstrap/plugins/switch/dist/css/bootstrap2/bootstrap-switch.css" rel="stylesheet" />
	<script src="<%=basePath%>/script/jquery/jquery-1.11.0.js" type="text/javascript"></script>
    
	<script src="<%=basePath%>/script/bootstrap/plugins/switch/dist/js/bootstrap-switch.js"></script>
	 
	
    <script type="text/javascript"> 
   	$(function(){
   		$("input").bootstrapSwitch();
   	});
    </script>
</head>

<body>

	<input type="checkbox" name="s" checked data-size="small" data-off="success" data-on-text="开" data-off-text="关" />
	<input type="checkbox" name="x" checked />
	<hr/>
	
	<div class="switch" data-on-label="dd" data-off-label="<i class='icon-remove'></i>">
	    <input type="checkbox" checked />
	</div>	
	
	
</body>
</html>



