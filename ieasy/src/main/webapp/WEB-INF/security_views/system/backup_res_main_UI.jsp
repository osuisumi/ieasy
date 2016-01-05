<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>系统资源备份</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg;
	$(function() {
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/backup_res/datagrid.do",
			title: '系统资源备份', 
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200], rownumbers: true,
			remoteSort: false, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: true,
			sortName: 'backup_time', sortOrder: 'desc',
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'backup_name', title: '文件名称', width: 280 }, 
			    { field: 'backup_type', title: '备份类型', width: 100, },
			    { field: 'size', title: '数据大小', width: 100, }, 
			    { field: 'db_script', title: '数据库脚本', width: 200, }, 
			    { field: 'backup_time', title: '备份时间', width: 140, sortable: true },
			    { field: 'status', title: '状态', width: 60, align: 'center', formatter:function(value,row){
			    	if(value == "备份中"){return "<font color='#CC9900'>"+value+"...</font>";}
			    	else if(value == "成功"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "失败"){return "<font color='red'>"+value+"</font>";}
			    }},
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg.datagrid('unselectAll');$dg.datagrid('clearSelections');$dg.datagrid('unselectAll');
			}
		}) ;
	});
	
	function doSearch(value,name){
		var o = {} ; o[name] = value ; $dg.datagrid("load",o);
	}
	function searchBox() {
		var search_form = $("#searchForm").form("getData") ;
		$dg.datagrid("load",search_form);
	}
	function searchClear() {
		$("#searchForm").form("clear") ;
		$dg.datagrid("load",{});
	}
	
	function del() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
		} else {
			alertify.warning("请选择一条记录！");
			return false;
		}
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据删除中，请稍等..."});
				$.post($.webapp.root+"/admin/system/backup_res/delete.do", {"ids" : ids.join(',')}, function(result) {
					if (result.status) {
						$dg.datagrid('unselectAll');$dg.datagrid('reload') ;
						alertify.success(result.msg);
						$.easyui.loaded();
					} else {
						alertify.warning(result.msg);
						$.easyui.loaded();
					}
				}, 'json').error(function() { $.easyui.loaded(); });
			}
		});
	}
	
	function backup() {
		var isBackup = false ;
		var rowData = $dg.datagrid("getData") ;
		$.each(rowData.rows, function(i,p) {
			if(p.status == "备份中") {
				isBackup = true ;
				return false;
			}
		});
		//判断列表中是否有正在备份中的数据，如果有则不能进行备份
		if(!isBackup) {
			var $d = $.easyui.showDialog({
				href: $.webapp.root+"/admin/system/backup_res/backup_res_form.do", title: "自定义文件名称", iniframe: false, topMost: true,
				width: 355, height: 160, maximizable: true,
	            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
	            buttons : [ 
	              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg) ; } },
	              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
	           	]
	        });
		} else {
			alertify.warning("数据备份中，暂时无法进行操作！<br/>刷新查看是否备份完成");
		}
	}
	
	function resume() {
		var node = $dg.datagrid('getSelected');
		if (node) {
			$.easyui.loading({ msg: "数据库恢复中，请稍等..."});
			$.post($.webapp.root+"/admin/system/backup_res/resume.do", {"id": node.id, "backup_name": node.backup_name, "db_script": node.db_script}, function(result) {
				if (result.status) {
					$dg.datagrid('unselectAll');$dg.datagrid('reload') ;
					alertify.success(result.msg);
					$.easyui.loaded();
				} else {
					alertify.warning(result.msg);
					$.easyui.loaded();
				}
			}, 'json').error(function() { $.easyui.loaded(); });
		} else {
			alertify.warning("请选择一条记录！"); return ;
		}
	}
</script>
</head>

<body>

	<div id="dg">
		<div id="toolbars">
            <a onClick="backup();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-database-save'">备份</a>
            <a onClick="resume();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-database-table'">恢复</a>
            <a onClick="del();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">删除</a>
            <a onClick="form_edit('A');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-full-time'">新建计划备份</a>
            <a onclick="$dg.datagrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
			
			<div id="search_bar">
            	<form id="searchForm">
	            	<div class="s_box">
						<div class="gr">
							<div class="st">备份日期：</div>
							<div class="si">
								<input name="backup_time" class="easyui-my97" type="text" data-options="height: 23, isShowClear:false, maxDate:new Date()" />
							</div>
						</div>
	            		<div class="gr">
		            		<div class="st">
			            		<a onClick="searchBox();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">查询</a>
			            		<a onClick="searchClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置查询</a>
		            		</div>
	            		</div>
	            	</div>
            	</form>
            </div>
			
        </div>
	</div>

</body>
</html>