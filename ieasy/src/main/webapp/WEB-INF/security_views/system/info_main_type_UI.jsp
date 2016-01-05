<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>信息</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg;
	$(function() {
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/notice/datagrid_info.do",
			title: '系统公告管理', 
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200], rownumbers: true,
			remoteSort: false, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'created', sortOrder: 'desc', queryParams: {approve: 1, type: ("${type}"=="proj_info"?"项目管理注意事项":"质量管理注意事项")},
			frozenColumns: [[
 			    { field: 'ck', checkbox: true },
 			    { field: 'id', title: 'ID', width: 80, hidden: true }
 			]],
 			columns: [[
 			    { field: 'title', title: '标题', width: 350, tooltip: true, formatter: function(value, row, index){
			    	var open = $.string.format("<p><a href='#' onclick='javascript:open_(\"{0}\")'>{1}</a></p>", row.id, value) ;
			    	return open ;
			    } },
 			    { field: 'created', title: '创建时间', width: 140 }
 			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg.datagrid('unselectAll');$dg.datagrid('clearSelections');$dg.datagrid('unselectAll');
			}
		}) ;
	});
	
	function open_(id) {
		$.webapp.open($.webapp.root + "/admin/system/notice/info_open_UI.do?id="+id) ;
	}
	
</script>
</head>

<body>

	<div id="dg">
	</div>
	

</body>
</html>