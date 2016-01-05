<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>定时作业管理</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg;
	$(function() {
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/schedule/datagrid.do",
			title: '<span style="font-weight:normal;color:#666;">定时作业管理</span>', 
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200], rownumbers: true,
			remoteSort: false, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			frozenColumns: [[
 			    { field: 'ck', checkbox: true },
 			    { field: 'id', title: 'ID', width: 280, hidden: true },
 			    { field: 'task_code', title: 'task_code', width: 80, hidden: true }
 			]],
 			columns: [[
 			    { field: 'task_name', title: '任务名称', width: 380, tooltip: true },
 			    { field: 'run_count', title: '运行次数', width: 60, align: 'center' },
 			    { field: 'task_enable', title: '状态',align:'center', width: 80, sortable: true, formatter:function(value,row){
 			    	if(value == "Y"){return "<font color='#5CB811'>运行中</font>";}else{return "<font color='red'>已暂停</font>";}
 			    }},
 			    { field: 'operation', title: '操作', width: 150, formatter:function(value,row,index){
 			    	var op = "<div id='icheck' class='icheck_in'>" + 
            				 "<input id='"+row.id+"' name='"+row.id+"' value='"+index+"' type='radio' "+(row.task_enable=="Y"?"checked":"")+"><div class='opts'><label for='"+row.id+"'>启动</label></div>" +
            				 "<input id='"+row.id+"' name='"+row.id+"' value='"+index+"' type='radio' "+(row.task_enable=="N"?"checked":"")+"><div class='opts'><label for='"+row.id+"'>暂停</label></div>" +
	            			 "</div>" ;
	            	return op;
 			    }},
 			    { field: 'cron_expression', title: 'cron表达式', width: 200 },
 			    { field: 'task_datetime', title: '定时时间', width: 150, sortable: true },
 			    { field: 'task_remark', title: '备注', width: 250, tooltip: true },
 			    { field: 'createName', title: '创建者', width: 80 },
 			    { field: 'created', title: '创建时间', width: 140, sortable: true },
 			    { field: 'modifyName', title: '修改者', width: 80 },
 			    { field: 'modifyDate', title: '修改时间', width: 140 }
 			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg.datagrid('unselectAll');$dg.datagrid('clearSelections');$dg.datagrid('unselectAll');
				$("#icheck input").iCheck({
        			radioClass: "iradio_square-blue"
        	   	}).on('ifChecked', function(event){
        	   		engine($(this).attr("value")) ;
            	});
			}
		}) ;
		
		$("#task_enable").combobox({
			onSelect: function(node){
				$dg.datagrid("load", {"task_enable": node.value}) ;
			}
		});
	});
	
	function doSearch(value,name){
		var o = {} ; o[name] = value ; $dg.datagrid("load",o);
	}
	function searchBox() {
		var search_form = $("#searchForm").form("getData") ;
		console.info(search_form) ;
		$dg.datagrid("load",search_form);
	}
	function searchClear() {
		$("#searchForm").form("clear") ;
		$dg.datagrid("load",{});
	}
	
	function form_edit(form) {
		var form_url = $.webapp.root+"/admin/system/schedule/schedule_task_form_UI.do" ;
		if("E" == form) {
			var node = $dg.datagrid('getSelected');
			if (node) {
				form_url = $.webapp.root+"/admin/system/schedule/schedule_task_form_UI.do?id="+node.id ;
			} else {
				alertify.warning("请选择一条记录！"); return ;
			}
		}
		var $d = $.easyui.showDialog({
			href: form_url, title: "表单", iniframe: false, topMost: true,
			width: 600, height: 300, maximizable: true,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '保存并继续', iconCls : 'icon-standard-disk', handler : function() { $.easyui.parent.submitForm($d, $dg, true) ; } },
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg) ; } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	]
        });
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
				$.post($.webapp.root+"/admin/system/schedule/delete.do", {"ids" : ids.join(',')}, function(result) {
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
	
	function engine(index) {
		var o = {} ; o=$dg.datagrid("getRowData", index); o["task_enable"]=(o.task_enable=="Y"?"N":"Y") ;
		$.post($.webapp.root+"/admin/system/schedule/update.do?d"+new Date().getTime(), o, function(result) {
			if (result.status) {
				$dg.datagrid('clearSelections');$dg.datagrid('clearChecked');$dg.datagrid('reload') ;
				if(o.task_enable == "Y") {
					$.easyui.messager.show({ icon: "info", msg: "任务已重新启动。" });
				} else {
					$.easyui.messager.show({ icon: "info", msg: "任务已停止。" });
				}
			} else {
				$.easyui.messager.show({ icon: "info", msg: "编辑失败。" });
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	}
	
	
	function ref_project_timer() {
		$.easyui.loading({ msg: "正在刷新项目定时器，请稍等..."});
		$.post($.webapp.root+"/admin/oa/project/refProjectTimer.do?d"+new Date().getTime(), {proj_status: 2, dev_work_status: 1}, function(result) {
			$dg.datagrid('reload') ;
			$.easyui.loaded();
		}).error(function() { $.easyui.loaded(); });
	}
	
</script>
</head>

<body>

	<div id="dg">
		<div id="toolbars">
			
            <a onClick="form_edit('A');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
            <a onClick="form_edit('E');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_edit'">编辑</a>
            <a onClick="del();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">删除</a>
            <a onclick="$dg.datagrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
            <a onClick="$dg.datagrid('unselectAll');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-application-xp'">取消选中</a>
				
			<div id="search_bar">
				<form id="searchForm">
					<div class="s_box">
						<div class="si1">
				            <input id="topSearchbox" class="easyui-searchbox" data-options="searcher:doSearch,width: 220, height: 26, menu: '#topSearchboxMenu'" />
							<div id="topSearchboxMenu" style="width: 85px;">
								<div data-options="name:'task_name', iconCls: 'icon-hamburg-zoom'">名称</div>
								<div data-options="name:'run_count', iconCls: 'icon-hamburg-zoom'">运行次数</div>
							</div>
						</div>
						<div class="st">状态</div>
						<div class="si1">
							 <select id="task_enable" data-options="panelHeight:'auto',editable:false" style="height:25px;">
								<option value="">请选择</option>
								<option value="Y">运行</option>
								<option value="N">暂停</option>
							</select>
						</div>
						<div class="st">定时时间</div> 
						<div class="si1">
							<input name="task_datetime_start" style="width:160px;height:25px;" class="easyui-my97" data-options="isShowClear:true" />
							至
							<input name="task_datetime_end" style="width:160px;height:25px;" class="easyui-my97" data-options="isShowClear:true" />
		           		</div>
		           		<div class="gr">
		            		<a onclick="ref_project_timer();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-sitemap'">刷新项目定时器</a>
		           		</div>
		           		<div class="gr">
		            		<div class="st">
			            		<a onClick="searchBox();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">查询</a>
			            		<a onClick="searchClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置</a>
		            		</div>
		           		</div>
		           	</div>
				</form>
			</div>
        </div>
	</div>
	

</body>
</html>