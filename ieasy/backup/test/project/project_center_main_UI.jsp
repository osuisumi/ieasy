<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>项目中心</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $project_dg, project_state_menu, org_search, status, params={} ;
	$(function() {
		//刷新状态
		refreshStatusFun() ;
		//项目中心菜单添加点击事件
		project_state_menu = $("#project_state_menu li").click(function(){
			project_state_menu.css("background-color", "#F2F2EA").css("color", "#000") ;
			$(this).css("background-color", "blue").css("color", "#FFFFFF").addClass($(this).attr("class")) ;
			params = {} ;
			projectViewer($(this).children("button").attr("id")) ;
		});
		//打开页面后加载立项中的项目列表
		projectViewer(project_state_menu.first().css("background-color", "blue").css("color", "#FFFFFF").addClass($(this).attr("class")).children("button").attr("id")) ;
		
		$project_dg = $("#project_dg").datagrid({
			title: '项目中心', 
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200,500,1000], rownumbers: true,
			remoteSort: false, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'created', sortOrder: 'desc',
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'proj_num', title: '项目编号', width: 150, tooltip: true }, 
			    { field: 'proj_name', title: '项目名称', width: 250, tooltip: true, formatter: function(value, row, index){
			    	var open = $.string.format("<p><a href='#' onclick='javascript:open_project(\"{0}\")'>{1}</a></p>", row.id, (undefined == value?"-":value)) ;
			    	return open ;
			    } },
			    { field: 'proj_level', title: '项目级别', width:80, align: 'center' },
			    { field: 'proj_start_time', title: '开始日期', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
			    	return $.date.format(value, "yyyy-MM-dd") ;
			    } },
			    { field: 'proj_end_time', title: '结束日期', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
			    	return $.date.format(value, "yyyy-MM-dd") ;
			    } },
			    { field: 'proj_owner_dept_name', title: '所属部门', width:150 },
			    { field: 'proj_approve_person_names', title: '审批人', width:150, tooltip: true },
			    { field: 'proj_const_money', title: '总预算资金', width:80, align: 'center', sortable: true},
			    { field: 'proj_owner_name', title: '项目负责人', width:80, align: 'center'},
			    { field: 'proj_manager_name', title: '项目经理', width:80, align: 'center'},
			    { field: 'created', title: '新建项目时间', width: 140 },
			    { field: 'modifyName', title: '修改者', width: 80 },
			    { field: 'modifyDate', title: '最后修改时间', width: 140 }
			]],
			enableHeaderContextMenu: false, enableRowContextMenu: false,
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$project_dg.datagrid('unselectAll');$project_dg.datagrid('clearSelections');$project_dg.datagrid('unselectAll');
			}
		});
		
		org_search = $("#org_search").combotree({
			url : $.webapp.contextPath+"/static_res/org.tree.json",
			editable: false, lines:true
	    });
		
	});
	
	//刷新项目各个状态数量
	var refreshStatusFun = function() {
		$.post($.webapp.contextPath + "/admin/oa/project/getProjectStatusCount.do", function(result){
			$.each(result, function(i, p){
				$("#"+i).html(p.status_count) ;
			}); 
		},"JSON").error(function(){});
	} ;
	
	//获取项目列表数据
	function projectViewer(menuType){
		status = menuType ;
		if(undefined != menuType) {
			params["proj_status"] = menuType ;
			loadProjectList(params) ;
		}
	}
	//加载项目列表数据
	function loadProjectList(params) {
		$.post($.webapp.contextPath + "/admin/oa/project/findByProjectStatusList.do", params, function(result){
			$project_dg.datagrid("loadData", result) ;
		},"JSON").error(function(){}) ;
	}
	
	//搜索
	function searchBox() {
		var search_form = $("#projectSearchForm").form("getData") ;
		$.each(search_form, function(p,v){
			params[p] = v ;
		}) ;
		loadProjectList(params) ;
	}
	function searchClear() {
		params = {"proj_status": params.proj_status} ;
		$("#projectSearchForm").form("clear") ;
		loadProjectList(params) ;
	}
	
	//新建项目
	function addProject() {
		var $d = $.easyui.showDialog({
			href: $.webapp.contextPath + "/admin/oa/project/project_add_page.do", title: "表单", iniframe: false, topMost: true, maximizable: true,
			width: 1000,
			height: (800 > parent.$.webapp.getInner().height? parent.$.webapp.getInner().height-60:800),
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '确认审批', iconCls : 'icon-hamburg-issue', handler : function() { $.easyui.parent.submitProjectForm($d, 1, refreshStatusFun) ; } },
              { text : '保存', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitProjectForm($d, 0, refreshStatusFun) ; } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	],
			onResize: function() {
				$("#jdl_dg").datagrid("resize",{
			        width:function(){return document.body.clientWidth*0.9;},
			        height:300
			    });
			}
        });
	}
	
	//打开项目详细信息
	function open_project(projectId) {
		$.webapp.open($.webapp.contextPath + "/admin/oa/project/open_project_UI.do?projectId="+projectId+"&status="+status) ;
	}
	
</script>
</head>

<body>
<div class="easyui-layout" data-options="fit:true">
		
	<div data-options="region:'west',split:true" style="width:200px;border-top:none;">
		<div class="easyui-layout" data-options="fit:true"> 
			<div data-options="region:'center',border:false" class="project_state_left">
				<div class="createProject">
					<button onclick="javascript:addProject();">新建项目</button>
				</div>
				<ul id="project_state_menu">
					<li class="icon_lxz"><b>立项中</b><button id="0">0</button></li> 
					<li class="icon_spz"><b>审批中</b><button id="1">0</button></li>
					<li class="icon_blz"><b>办理中</b><button id="2">0</button></li>
					<li class="icon_gqz"><b>挂起中</b><button id="3">0</button></li>
					<li class="icon_ybj"><b>已办结</b><button id="4">0</button></li>
					<li class="icon_qbxm"><b id="qbxm">全部项目</b></li>
					<li class="icon_tbhz"><b id="tbhz">图表汇总</b></li>
				</ul>
			</div>
		</div>
	</div>
	
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
		
			<div data-options="region:'center'" style="border-top:0px;">
				<div id="project_dg">
					<div id="toolbars">
						<form id="projectSearchForm">
				            <input class="easyui-searchbox" data-options="width: 332, height: 25, menu: '#projectSearchboxMenu'" />
							<div id="projectSearchboxMenu" style="width: 180px;">
								<div data-options="name:'proj_num', iconCls: 'icon-hamburg-zoom'">项目编号</div>
								<div data-options="name:'proj_name', iconCls: 'icon-hamburg-zoom'">项目名称</div>
								<div data-options="name:'proj_manager_name', iconCls: 'icon-hamburg-zoom'">项目经理名称</div>
								<div data-options="name:'proj_owner_name', iconCls: 'icon-hamburg-zoom'">项目拥有者名称</div>
							</div>
			            	部门：<input id="org_search" name="proj_owner_dept_id" style="width:296px;height:25px;" /> 
		            	
			            	<div id="search_bar">
				            	<div class="s_box">
									<div class="gr">
										<div class="st">开始日期范围查询：</div>
										<div class="si1">
											<input id="ss1" name="proj_begin_startDate" class="Wdate" style="width:100px;height:23px;" type="text" onFocus="WdatePicker({
												isShowClear:true,
												readOnly:true,
												maxDate:'#F{$dp.$D(\'ss2\')||\'2020-10-01\'}'
												})"/>
											至
											<input id="ss2" name="proj_begin_endDate" class="Wdate" style="width:100px;height:23px;" type="text" onFocus="WdatePicker({
												isShowClear:true,
												readOnly:true,
												minDate:'#F{$dp.$D(\'ss1\')}',
												maxDate:'2120-10-01'
												})"/>
					            		</div>
					            		<div class="st">结束日期范围查询：</div>
										<div class="si1">
											<input id="ee1" name="proj_finish_startDate" class="Wdate" style="width:100px;height:23px;" type="text" onFocus="WdatePicker({
												isShowClear:true,
												readOnly:true,
												maxDate:'#F{$dp.$D(\'ee2\')||\'2020-10-01\'}'
												})"/>
											至
											<input id="ee2" name="proj_finish_endDate" class="Wdate" style="width:100px;height:23px;" type="text" onFocus="WdatePicker({
												isShowClear:true,
												readOnly:true,
												minDate:'#F{$dp.$D(\'ee1\')}',
												maxDate:'2120-10-01'
												})"/>
					            		</div>
					            		<div class="si1">
						            		<a onClick="searchBox();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">组合查询</a>
						            		<a onClick="searchClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置查询</a>
					            		</div>
					            	</div>
				            	</div> <!-- 提交查询 -->
				            	<!--
				            	<div class="s_box">
				            		<div class="gr">
					            		<a onClick="" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-credit-card'">分配账号</a>
					            		<a onclick="" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-lock'">锁定账号</a>
					            		<a onclick="" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-standard-textfield'">清空密码</a>
					            		<a onclick="" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-standard-textfield-key'">重设密码</a>
					            		<a onclick="" class="easyui-linkbutton" data-options="plain: true, iconCls:'ext_email'">发送邮件</a>
				            		</div>
				            	</div> 功能按钮 -->
				            	
			            	</div>
		            	</form>
			        </div>
				</div>
			</div>
			
		</div>
	</div>
	
</div>

</body>
</html>