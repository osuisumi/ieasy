<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户操作日志</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg;
	$(function() {
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/log/datagrid.do",
			title: '用户操作日志', 
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200], rownumbers: true,
			remoteSort: true, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'operDateTime', sortOrder: 'desc',
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'name', title: '用户名称', width: 80 },
			    { field: 'userAccount', title: '用户账号', width: 80 },
			    { field: 'ip', title: 'IP地址', width: 120 }, 
			    { field: 'title', title: '操作标题', width: 200 },
			    { field: 'detail', title: '操作明细', width: 250 },
			    { field: 'remark', title: '备注', width: 300 },
			    { field: 'operDateTime', title: '操作时间', width: 140, sortable: true }
			]]
		}) ;
	});
	
	function doSearch(value,name){
		var o = {} ; o[name] = value ; $dg.datagrid("load",o);
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
				$.post($.webapp.root+"/admin/system/log/delete.do", {"ids" : ids.join(',')}, function(result) {
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
            <a onClick="del();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">删除</a>
            <a onclick="$dg.datagrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
            <input id="topSearchbox" class="easyui-searchbox" data-options="searcher:doSearch,width: 220, height: 26, menu: '#topSearchboxMenu'" />
			<div id="topSearchboxMenu" style="width: 85px;">
				<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">名称</div>
			</div>
			<a onClick="$dg.datagrid('load',{});$('#topSearchbox').searchbox('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置过滤</a>
        </div>
	</div>
	

</body>
</html>