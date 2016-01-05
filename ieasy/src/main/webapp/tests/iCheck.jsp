<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%String webroot = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();%>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>iEasy</title>
	
    
    <link href="<%=basePath%>/script/bootstrap/plugins/iCheck/skins/all.css" rel="stylesheet" />
    <script src="<%=basePath%>/script/jquery/jquery-1.11.0.js" type="text/javascript"></script>
    <script src="<%=basePath%>/script/bootstrap/plugins/iCheck/icheck.js"></script>
    <script type="text/javascript">
   	$(function(){
   		$('input').iCheck({
   			//checkboxClass: 'icheckbox_minimal-red',
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-blue'
          });
   	});
    </script>
</head>

<body>


	<input tabindex="1" type="checkbox" id="input-1" />
	<label for="input-3">攥着, <span>#input-3</span></label>
	
	<input tabindex="2" type="checkbox" id="input-2" checked />
	<label for="input-3">攥着, <span>#input-3</span></label>
	
	<input type="radio" id="input-3" name="demo-radio" />
	<label for="input-3">攥着, <span>#input-3</span></label>
	
	<input tabindex="4" type="radio" id="input-4" name="demo-radio" checked />
	<label for="input-4">Radio button, <span>#input-4</span></label>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
</body>
</html>



