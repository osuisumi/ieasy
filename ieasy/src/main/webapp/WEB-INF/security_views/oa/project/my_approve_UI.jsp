<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>项目审批</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg, params={"status": 0, "person_id": $.webapp.emp_id};
	$(function() {
		$dg = $("#dg").datagrid({
			title: '项目审批', url: $.webapp.root+"/admin/oa/project/get_myApprove_datagrid.do",
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200], rownumbers: true,
			remoteSort: false, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			queryParams: params,
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'approve_id', title: 'approve_id', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'proj_name', title: '项目名称', width: 350, tooltip: true, formatter: function(value, row, index){
			    	var open = $.string.format("<p><a href='#' onclick='javascript:open_project(\"{0}\")'>{1}</a></p>", row.id, (undefined == value?"-":value)) ;
			    	return open ;
			    } },
			    { field: 'proj_status', title: '项目状态', width: 80, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "0"){return "<font color='red'>立项中</font>";}
			    	else if(value == "1"){return "<font color='red'>审批中</font>";}
			    	else if(value == "2"){return "<font color='#00CC00'>办理中</font>";}
			    	else if(value == "3"){return "<font color='red'>挂起中</font>";}
			    	else if(value == "4"){return "<font color='#B91D47'>已办结</font>";}
			    }},
			    { field: 'approve_status', title: '审批状态', width: 80, align: 'center', formatter:function(value,row){
			    	if(value == 0){return "<font color='red'>未审批</font>";} 
			    	else {return "<font color='#00CC00'>已审批</font>" ;}
			    }},
			    { field: 'proj_owner_dept_name', title: '所属部门', width:150 },
			    { field: 'proj_level', title: '项目级别', width:80, align: 'center', sortable: true, formatter: function(value, row, index){
			    	return (undefined != value && "" != value? value + " 级":"-") ;
			    } },
			    { field: 'proj_start_time', title: '开始日期', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
			    	return $.date.format(value, "yyyy-MM-dd") ;
			    } },
			    { field: 'cyc', title: '工期', width:60, align: 'center' },
			    { field: 'proj_end_time', title: '结束日期', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
			    	return $.date.format(value, "yyyy-MM-dd") ;
			    } }
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg.datagrid('unselectAll');$dg.datagrid('clearSelections');
			}
		}) ;
	});
	
	function doSearch(value,name){
		params[name] = value ; $dg.datagrid("load",params);
	}
	function clearSearch(){
		$('#topSearchbox').searchbox('setValue','');
		params={"status": 0, "person_id": $.webapp.emp_id} ;
		$dg.datagrid('load', params);
	}
	function findByStatus(status) {
		$('#topSearchbox').searchbox('setValue','');
		params={"status": status, "person_id": $.webapp.emp_id} ;
		$dg.datagrid('load', params);
	}
	
	//打开项目详细信息
	function open_project(projectId) {
		$.webapp.open($.webapp.root + "/admin/oa/project/open_project_UI.do?projectId="+projectId) ;
	}
	
	//提供子窗口调用刷新，当删除项目时调用
	function parentCloseRefered() {
		params={"status": 0, "person_id": $.webapp.emp_id} ;
		$dg.datagrid('load', params);
	}
	
</script>
</head>

<body>

	<div id="dg">
		<div id="toolbars">
			<a onClick="findByStatus(0);" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-customers'">未审批</a>
			<a onClick="findByStatus(1);" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-issue'">已审批</a>
            <a onclick="$dg.datagrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
            <input id="topSearchbox" class="easyui-searchbox" data-options="searcher:doSearch,width: 220, height: 26, menu: '#topSearchboxMenu'" />
			<div id="topSearchboxMenu" style="width: 85px;">
				<div data-options="name:'proj_num', iconCls: 'icon-hamburg-zoom'">项目编号</div>
				<div data-options="name:'proj_name', iconCls: 'icon-hamburg-zoom'">项目名称</div>
			</div>
			<a onClick="clearSearch();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置过滤</a>
        </div>
	</div>
	

</body>
</html>