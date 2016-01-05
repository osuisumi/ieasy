<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>我的项目</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $project_dg, project_state_menu, status, params={} ;
	
	$(function() {
		$("#project_state_menu li").first().addClass("click") ;
		status = $("#project_state_menu li").first().children("button").attr("id") ;
		if("undefined" != status && "" != status) {
			params["proj_owner_id"] = $.webapp.emp_id ;
			params["proj_status"] = status ;
		}
		
		$project_dg = $("#project_dg").datagrid({
			url: $.webapp.root + "/admin/oa/project/get_myProject_list.do",
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200,500,1000], rownumbers: true,
			remoteSort: true, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'proj_level', sortOrder: 'asc', queryParams: params,
			frozenColumns: [[
 			    { field: 'ck', checkbox: true },
 			    { field: 'id', title: 'ID', width: 80, hidden: true },
 			    { field: 'proj_num', title: '项目编号', width: 120, tooltip: true }, 
 			    { field: 'proj_name', title: '项目名称', width: 350, tooltip: true, formatter: function(value, row, index){
 			    	var open = $.string.format("<p><a href='#' onclick='javascript:open_project(\"{0}\")'>{1}</a></p>", row.id, (undefined == value?"-":value)) ;
 			    	return open ;
 			    } }
 			]],
 			columns: [[
 			    { field: 'proj_owner_dept_name', title: '所属部门', width:150 },
 			    { field: 'proj_level', title: '项目级别', width:80, align: 'center', sortable: true, formatter: function(value, row, index){
 			    	return (undefined != value && "" != value? value + " 级":"-") ;
 			    } },
 			    { field: 'proj_start_time', title: '开始日期', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
 			    	return $.date.format(value, "yyyy-MM-dd") ;
 			    } },
 			    { field: 'cyc', title: '工期', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
 			    	return value + " 天" ;
 			    } },
 			    { field: 'proj_end_time', title: '结束日期', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
 			    	return $.date.format(value, "yyyy-MM-dd") ;
 			    } },
 			    { field: 'proj_status', title: '项目状态', width: 80, sortable: true, align: 'center', formatter:function(value,row){
 			    	if(value == "0"){return "<font color='red'>立项中</font>";}
 			    	else if(value == "1"){return "<font color='red'>审批中</font>";}
 			    	else if(value == "2"){return "<font color='#00CC00'>进行中</font>";}
 			    	else if(value == "3"){return "<font color='red'>挂起中</font>";}
 			    	else if(value == "4"){return "<font color='#B91D47'>已结束</font>";}
 			    }},
 			    
 			    { field: 'proj_type', title: '项目类型', width: 90, align: 'center', sortable: true}, 
 			    { field: 'proj_zyfw', title: '作业范围', width: 120, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value?value:"-") ;
 			    } },
 			    { field: 'proj_gm', title: '项目规模', width: 80, sortable: true, tooltip: true, align: 'center', formatter:function(value,row){
 			    	return (undefined != value&&0 != value?value + "&nbsp;Ks":"-") ;
 			    } },
 			    { field: 'proj_gjjb', title: '跟进级别', width: 60, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value?value:"-") ;
 			    } },
 			    { field: 'proj_buglv', title: '顾客反馈BUG率目标', width: 120, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value && "" != value?value + "&nbsp;件/Ks":"-") ;
 			    } },
 			    { field: 'proj_bjzry', title: '报价总人月数', width: 90, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value && "" != value?value:"-") ;
 			    } },
 			    { field: 'proj_yjtrzry', title: '预计投入总人月数', width: 120, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value && "" != value?value:"-") ;
 			    } },
 			    { field: 'proj_bjscx', title: '报价生产性', width: 80, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value?value + "&nbsp;Ks/人月":"-") ;
 			    } },
 			    { field: 'proj_ydscx', title: '预定生产性', width: 80, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value?value + "&nbsp;Ks/人月":"-") ;
 			    } },
 			    { field: 'proj_clrl', title: '初始粗利润率', width: 80, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value?value + "&nbsp;%":"-") ;
 			    } },
 			    { field: 'proj_cclrl', title: '当前粗利润率', width: 80, sortable: true, align: 'center', tooltip: true, formatter:function(value,row){
 			    	return (undefined != value?value + "&nbsp;%":"-") ;
 			    } },
 			    { field: 'proj_htpjzt', title: '合同评审状态', width: 100, sortable: true, align: 'center', formatter:function(value,row){
 			    	if(undefined == value || value == ""){ 
 			    		return "-" ; 
 			    	} else { 
 			    		if(value == '未评审'){ 
 				    		return "<font color='blue'>未评审</font>" ; 
 				    	} else  if(value == '已评审') {
 				    		return "<font color='green'>已评审</font>" ;
 				    	}
 			    	}
 			    } },
 			    { field: 'proj_shouzhu', title: '合同受注状态', width: 100, sortable: true, align: 'center', formatter:function(value,row){
 			    	if(value == '未受注'){ 
 			    		return "<font color='red'>未受注</font>" ; 
 			    	} else if(value == '已受注') { 
 			    		return "<font color='green'>已受注</font>" ; 
 			    	} else {
 			    		return "-"
 			    	}
 			    } },
 			    { field: 'proj_jxzt', title: '结项状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
 			    	if(undefined == value || value == ""){ 
 			    		return "-" ; 
 			    	} else {
 			    		if(value == '未结项'){ 
 				    		return "<font color='blue'>未结项</font>" ; 
 				    	} else  if(value == '已结项') {
 				    		return "<font color='red'>已结项</font>" ;
 				    	}
 			    	}
 			    } },
 			    { field: 'proj_cwydwcjssj', title: '财务预定完成结算时间', width: 140, align: 'center', sortable: true, formatter:function(value,row){
 			    	return (undefined != value?value:"-") ;
 			    } },
 			    { field: 'proj_cwjszt', title: '财务结算状态', width: 100, align: 'center', sortable: true, formatter:function(value,row){
 			    	if(undefined == value || value == ""){ 
 			    		return "-" ; 
 			    	} else { 
 			    		if(value == '未结算'){ 
 				    		return "<font color='blue'>未结算</font>" ; 
 				    	} else  if(value == '结算中') {
 				    		return "<font color='green'>结算中</font>" ;
 				    	} else  if(value == '已结算') {
 				    		return "<font color='orange'>已结算</font>" ;
 				    	}
 			    	}
 			    } },
 			    { field: 'proj_xmjsnf', title: '项目结算年份', width: 140, align: 'center', formatter:function(value,row){
 			    	return (undefined != value?value:"-") ;
 			    } },
 			    { field: 'proj_cwwcjssj', title: '财务完成结算时间', width: 140, align: 'center', formatter:function(value,row){
 			    	return (undefined != value?value:"-") ;
 			    } },
 			    
 			    
 			    { field: 'proj_approve_person_names', title: '审批人', width:150, tooltip: true },
 			    { field: 'proj_const_money', title: '总预算资金', width:80, align: 'center', sortable: true, formatter: function(value, row, index){
 			    	return (undefined != value && "" != value? value + " 元":"-") ;
 			    } },
 			    { field: 'proj_owner_name', title: '项目负责人', width:80, align: 'center'},
 			    { field: 'proj_manager_names', title: '项目经理', width:80, align: 'center'},
 			    { field: 'proj_creator_name', title: '创建人', width: 80 },
 			    { field: 'created', title: '新建时间', width: 140 },
 			    { field: 'modifyName', title: '修改者', width: 80 },
 			    { field: 'modifyDate', title: '最后修改时间', width: 140 }
 			]],
			enableHeaderContextMenu: false, enableRowContextMenu: false,
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$project_dg.datagrid('unselectAll');$project_dg.datagrid('clearSelections');$project_dg.datagrid('unselectAll');
			}
		});
		
		//刷新状态
		refreshStatusFun() ;
		
		project_state_menu = $("#project_state_menu li").click(function(){
			params = {} ;
			params["proj_owner_id"] = $.webapp.emp_id ;
			
			$("#project_state_menu li").removeClass("click") ;
			$(this).addClass("click") ;
			status = $(this).children("button").attr("id") ;
			openOther = $(this).children("b").attr("id") ;
			if("undefined" != status && "" != status && undefined == openOther) {
				params["proj_status"] = status ;
				projectViewer() ;
			}
		});
		
	});
	
	//刷新项目各个状态数量
	var refreshStatusFun = function() {
		$("#project_state_menu li button").text(0) ;
		$.post($.webapp.root + "/admin/oa/project/getProjectStatusCount.do",{"proj_owner_id": $.webapp.emp_id}, function(result){
			$.each(result, function(i, p){
				$("#"+p.proj_status).html(p.status_count) ;
			}); 
		},"JSON").error(function(){});
	} ;
	//获取项目列表数据
	function projectViewer(){
		$project_dg.datagrid("load", params) ;
	}
	
	//搜索
	function searchBoxProj() {
		var search_form = $("#projectSearchForm").form("getData") ;
		$.each(search_form, function(p,v){
			params[p] = v ;
		}) ;
		
		//如果点击部门项目之后,在点击查询,不加入以下条件。
		if(undefined != params.proj_owner_dept_id && "" != params.proj_owner_dept_id) {
			params["proj_status"] = null ;
			params["proj_owner_id"] = null ;
		}
		
		$project_dg.datagrid("load", params) ;
	}
	function searchClearProj() {
		if(undefined != params.proj_status) {
			params = {"proj_status": status} ;
		} else {
			params = {} ;
		}
		$("#projectSearchForm").form("clear") ;
		//重置之后以以下条件为基础
		params["proj_owner_id"] = $.webapp.emp_id ;
		params["proj_status"] = status ;
		$project_dg.datagrid("load", params) ;
	}
	
	//新建项目
	function addProject() {
		var $d = $.easyui.showDialog({
			href: $.webapp.root + "/admin/oa/project/project_add_page.do", title: "表单", iniframe: false, topMost: true, maximizable: true,
			width: 1100,
			height: (900 > parent.$.webapp.getInner().height? parent.$.webapp.getInner().height-60:800),
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '确认审批', iconCls : 'icon-hamburg-issue', handler : function() { $.easyui.parent.submitProjectForm($d, 1, refreshStatusFun) ; } },
              { text : '保存', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitProjectForm($d, 0, refreshStatusFun) ; } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	]
        });
	}
	
	//提供子窗口调用刷新，当删除项目时调用
	function parentCloseRefered() {
		refreshStatusFun() ;
		projectViewer() ;
	}
	
	//打开项目详细信息
	function open_project(projectId) {
		$.webapp.open($.webapp.root + "/admin/oa/project/open_project_UI.do?projectId="+projectId+"&status="+status) ;
	}
	
	function project_by_dept() {
		$("#projectSearchForm").form("clear") ;
		params = {} ;
		params["proj_status"] = null ;
		params["proj_owner_id"] = null ;
		params["proj_owner_dept_id"] = $.webapp.org_id;
		$project_dg.datagrid("load", params) ;
	}
	function project_by_pm() {
		$("#projectSearchForm").form("clear") ;
		params = {} ;
		params["proj_status"] = null ;
		params["proj_owner_id"] = null ;
		params["proj_manager_ids"] = $.webapp.emp_id;
		$project_dg.datagrid("load", params) ;
	}
	function project_by_dev() {
		$("#projectSearchForm").form("clear") ;
		params = {} ;
		params["proj_status"] = null ;
		params["proj_owner_id"] = null ;
		params["my_dev_project_personId"] = $.webapp.emp_id;
		$project_dg.datagrid("load", params) ;
	}
	function project_by_testing() {
		$("#projectSearchForm").form("clear") ;
		params = {} ;
		params["proj_status"] = null ;
		params["proj_owner_id"] = null ;
		params["proj_testing_engine_ids"] = $.webapp.emp_id;
		$project_dg.datagrid("load", params) ;
	}
	function project_by_sqa() {
		$("#projectSearchForm").form("clear") ;
		params = {} ;
		params["proj_status"] = null ;
		params["proj_owner_id"] = null ;
		params["proj_sqa_member_ids"] = $.webapp.emp_id;
		$project_dg.datagrid("load", params) ;
	}
	function project_by_viewer() {
		$("#projectSearchForm").form("clear") ;
		params = {} ;
		params["proj_status"] = null ;
		params["proj_owner_id"] = null ;
		params["proj_viewer_member_names"] = $.webapp.emp_id;
		$project_dg.datagrid("load", params) ;
	}
	
</script>
</head>

<body>
	<div class="easyui-layout" data-options="fit:true">
			
		<div data-options="region:'west',split:true" style="width:200px;border-top:none;">
			<div class="easyui-layout" data-options="fit:true"> 
				<div data-options="region:'center',border:false" class="project_state_left">
					<c:if test="${icf:hasPermit('/admin/oa/project/project_add_page.do')}">
					<div class="createProject">
						<button onclick="javascript:addProject();">新建项目</button>
					</div>
            		</c:if> 
					<ul id="project_state_menu">
						<li class="icon_lxz"><b>立项中</b><button id="0">0</button></li> 
						<li class="icon_spz"><b>审批中</b><button id="1">0</button></li>
						<li class="icon_blz"><b>进行中</b><button id="2">0</button></li>
						<li class="icon_gqz"><b>挂起中</b><button id="3">0</button></li>
						<li class="icon_ybj"><b>已结束</b><button id="4">0</button></li>
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
									<div data-options="name:'proj_manager_names', iconCls: 'icon-hamburg-zoom'">项目经理名称</div>
									<div data-options="name:'proj_owner_name', iconCls: 'icon-hamburg-zoom'">项目拥有者名称</div>
								</div>
			            	
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
							            		<a onClick="searchBoxProj();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">查询</a>
							            		<a onClick="searchClearProj();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置</a>
						            		</div>
						            	</div>
					            	</div> <!-- 提交查询 -->
					            	
					            	<div class="s_box">
					            		<div class="gr">
					            			<c:if test="${icf:hasPermit('project_by_dept')}">
						            		<a onClick="project_by_dept();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-bremen-sitemap'">部门项目</a>
						            		</c:if> 
						            		<c:if test="${icf:hasPermit('project_by_pm')}">
						            		<a onclick="project_by_pm();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-cologne-customers'">我是PM</a>
						            		</c:if>
						            		<a onclick="project_by_dev();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-product'">我参与开发的</a>
						            		<!-- 
						            		<a onclick="project_by_testing();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-product'">我参与测试的</a>
						            		 -->
						            		<a onclick="project_by_sqa();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-product'">我是SQA</a>
						            		<a onclick="project_by_viewer();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-product'">我可查看的</a>
					            		</div> 
					            	</div><!-- 功能按钮 -->
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