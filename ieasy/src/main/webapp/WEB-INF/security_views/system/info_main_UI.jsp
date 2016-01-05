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
		//数据字典
		var data = {"dictCode": "XTXXGG"} ;
		$.post($.webapp.root+"/admin/system/dict/doNotNeedSession_dictAttrMaps.do", data, function(result) {
			$.each(result, function(i, p) {
				$("#S-"+i).combobox({valueField: 'text', textField: 'text', panelHeight: 'auto', data: p}) ;
			});
		}, 'json');
		
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/notice/datagrid_info.do",
			title: '系统公告管理', 
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200], rownumbers: true,
			remoteSort: false, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'created', sortOrder: 'desc',
			frozenColumns: [[
 			    { field: 'ck', checkbox: true },
 			    { field: 'id', title: 'ID', width: 80, hidden: true }
 			]],
 			columns: [[
 			    { field: 'title', title: '标题', width: 350, tooltip: true, formatter: function(value, row, index){
			    	var open = $.string.format("<p><a href='#' onclick='javascript:open_(\"{0}\")'>{1}</a></p>", row.id, value) ;
			    	return open ;
			    } },
 			    { field: 'type', title: '类型', width: 120, align: 'center' },
 			    { field: 'approve', title: '状态', width: 80, align: 'center', formatter: function(value, row) {
 			    	if(value == -1) {
 			    		return "未审批"; 
 			    	} else {
 			    		return "已审批" ;
 			    	}
			   	} },
 			    { field: 'oper', title: '操作', width: 150, align: 'center', formatter: function(value, row) {
 			    	var s = "" ;
			    	s += $.string.format("<a href='javascript:void(0);' onclick='changeAp(\"{0}\", \"{1}\")' class='clickLInk ext_project'>不通过</a>", row.id, -1) ;
			    	s += $.string.format("<a href='javascript:void(0);' onclick='changeAp(\"{0}\", \"{1}\")' class='clickLInk ext_project'>通过</a>", row.id, 1) ;
			    	return s ;
			    } },
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
	
	function form_edit(form) {
		var form_url = $.webapp.root+"/admin/system/notice/info_form_UI.do" ;
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
			width: 950, height: 610, maximizable: true,
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
	
	function changeAp(id, s) {
		$.easyui.loading({ msg: "数据处理中，请稍等..."});
		$.post($.webapp.root + "/admin/system/notice/doNotNeedAuth_approve.do", {"id": id, "approve": s}, function(result){
			if(result.status) {
				$dg.datagrid("reload");
				$.easyui.loaded();
			}
		},"JSON").error(function(){$.easyui.loaded();});
	}
	
	//打开详细信息
	function open_(id) {
		$.webapp.open($.webapp.root + "/admin/system/notice/info_open_UI.do?id="+id) ;
	}
	
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
	
</script>
</head>

<body>

	<div id="dg">
		<div id="toolbars">
            <a onClick="form_edit('A');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
            <a onClick="form_edit('E');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_edit'">编辑</a>
            <a onClick="del();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">删除</a>
            <a onclick="$dg.datagrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
            <div id="search_bar">
            	<form id="searchForm">
	            	<div class="s_box">
	            		<div class="gr">
		            		<div class="st">类型&nbsp;</div>
		            		<div class="si1"><input id="S-XTXXGG" name="type" style="width:125px;height:25px;" /></div>
	            		</div>
	            		<div class="gr">
							<div class="st">状态&nbsp;</div>
							<div class="si">
								<select name="approve" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="height:25px;width:67px;">
									<option value="">请选择</option>
									<option value="-1">未审批</option>
									<option value="1">已审批</option>
								</select>
							</div>
						</div>
	            		<div class="gr">
		            		<div class="si">
			            		<input class="easyui-searchbox" data-options="searcher:doSearch,width: 250, height: 25, menu: '#topSearchboxMenu'" />
								<div id="topSearchboxMenu" style="width: 200px;">
									<div data-options="name:'title', iconCls: 'icon-hamburg-zoom'">标题</div>
								</div>
		            		</div>
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