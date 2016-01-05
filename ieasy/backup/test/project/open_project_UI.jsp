<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>项目详细</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var projectId = "${projectId}", status = "${status}", layoutContainer, project_state_menu, centerPanel ;
	$(function() {
		layoutContainer = $("#layoutContainer");
		centerPanel = layoutContainer.layout("panel", "center") ;
		
		
		//项目中心菜单添加点击事件
		project_state_menu = $("#project_state_menu li").click(function(){
			project_state_menu.css("background-color", "#F2F2EA").css("color", "#000") ;
			$(this).css("background-color", "blue").css("color", "#FFFFFF").addClass($(this).attr("class")) ;
			open($(this).children("b").attr("url")) ;
		});
		//打开页面后加载立项中的项目列表
		open(project_state_menu.first().css("background-color", "blue").css("color", "#FFFFFF").addClass($(this).attr("class")).children("b").attr("url")) ;
	});
	
	function open(url) {
		centerPanel.panel("refresh", $.webapp.contextPath + url) ;
	}
</script>
</head>

<body>
<div id="layoutContainer" class="easyui-layout" data-options="fit:true">
		
	<div data-options="region:'west',split:false" style="width:200px;border-top:none;">
		<div class="easyui-layout" data-options="fit:true"> 
			<div data-options="region:'center',border:false" class="project_state_left">
				<ul id="project_state_menu">
					<li class="icon_xxxx detail"><b url="/admin/oa/project/project_detail_view.do">详细信息</b></li>
					<li class="icon_xmjd detail"><b url="/admin/oa/project/project_center_main_UI.do">项目进度</b></li>
					<li class="icon_ysjcb detail"><b url="/admin/oa/project/project_center_main_UI.do">预算及成本</b></li>
					<li class="icon_cloud detail"><b url="/admin/oa/project/project_center_main_UI.do">资源管理</b></li>
					<li class="icon_file detail"><b url="/admin/oa/project/project_center_main_UI.do">汇总报表</b></li>
				</ul>
			</div>
		</div>
	</div>
	
	<div id="detailCenter" data-options="region:'center',border:false"></div>
	
</div>

</body>
</html>