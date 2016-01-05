<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>系统公告管理</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg;
	$(function() {
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/notice/datagrid.do",
			title: '系统公告管理', 
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200], rownumbers: true,
			remoteSort: false, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'created', sortOrder: 'desc',
			frozenColumns: [[
 			    { field: 'ck', checkbox: true },
 			    { field: 'id', title: 'ID', width: 80, hidden: true }
 			]],
 			columns: [[
 			    { field: 'title', title: '公告标题', width: 170 },
 			    { field: 'created', title: '创建时间', width: 140 },
 			    { field: 'createName', title: '创建者', width: 140 },
 			    { field: 'modifyDate', title: '最后修改时间', width: 140 },
 			    { field: 'modifyName', title: '修改者', width: 140 },
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
	
	function form_edit(form) {
		var form_url = $.webapp.root+"/admin/system/notice/notice_form_UI.do" ;
		if("E" == form) {
			var node = $dg.datagrid('getSelected');
			if (node) {
				form_url = $.webapp.root+"/admin/system/notice/notice_form_UI.do?id="+node.id ;
			} else {
				alertify.warning("请选择一条记录！"); return ;
			}
		}
		var $d = $.easyui.showDialog({
			href: form_url, title: "表单", iniframe: false, topMost: true,
			width: 750, height: 610, maximizable: true,
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
				$.post($.webapp.root+"/admin/system/notice/delete.do", {"ids" : ids.join(',')}, function(result) {
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
            <input id="topSearchbox" class="easyui-searchbox" data-options="searcher:doSearch,width: 220, height: 26, menu: '#topSearchboxMenu'" />
			<div id="topSearchboxMenu" style="width: 85px;">
				<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">名称</div>
				<div data-options="name:'sn', iconCls: 'icon-hamburg-zoom'">序列号</div>
			</div>
			<a onClick="$dg.datagrid('load',{});$('#topSearchbox').searchbox('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置过滤</a>
        </div>
	</div>
	

</body>
</html>