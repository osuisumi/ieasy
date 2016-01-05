<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>项目详细</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<link href="<%=basePath%>/script/plugins/shCircleLoader/jquery.shCircleLoader.css" rel="stylesheet"/>
<script src="<%=basePath%>/script/plugins/shCircleLoader/jquery.shCircleLoader-min.js" type="text/javascript"></script>
<style>
#info_view_page{
	width:1000px;
	height:400px;
	margin:0 auto;
	display: table;
}
#info_view_page .title_box{
	width:1000px;
	height:80px;
	text-align: center;
	line-height: 80px;
}
#info_view_page .title_box h1{
	font-size: 26px;
}
#info_view_page .centext_box{
	width:1000px;
	height:1000px;
	border-top:1px solid #ccc;
	display: table;
}
</style>
<script type="text/javascript">
	var id = "${id}" ;
	$(function() {
		/* $('#loader').shCircleLoader({
		    color: "red",
		    dots: 24,
		    dotsRadius: 13,
		    keyframes:
		       "0%   {background: red;    {prefix}transform: scale(1)}\
		        20%  {background: orange; {prefix}transform: scale(.4)}\
		        40%  {background: red;    {prefix}transform: scale(0)}\
		        50%  {background: red;    {prefix}transform: scale(1)}\
		        70%  {background: orange; {prefix}transform: scale(.4)}\
		        90%  {background: red;    {prefix}transform: scale(0)}\
		        100% {background: red;    {prefix}transform: scale(1)}"
		}); */
		
		$.post($.webapp.root+"/admin/system/notice/get.do", {"id" : id}, function(result) {
			if (result) {
				$("title").html(result.title) ;
				$("#title").html(result.title);
				$("#content").html(result.content);
			}
		}, 'json').error(function() { $.easyui.loaded(); });
		
	});
	
	
</script>
</head>

<body>
	<div id="info_view_page">
		<div class="title_box">
			<h1 id="title"></h1>
		</div>
		<div class="centext_box" id="content"></div>
		
		
		<div id="loader"></div>
	</div>

</body>
</html>