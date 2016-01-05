<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>组织机构</title>
	<%@ include file="/common/header/meta.jsp"%>
	<%@ include file="/common/header/script.jsp"%>
    <script type="text/javascript">
    var $tg ;
	$(function() {
		$tg = $("#tg").treegrid({
			url : $.webapp.root+"/static_res/org.tree.json", border: false,
			title: '<span style="font-weight:normal;color:#666;">组织机构</span>',
			idField: 'id', treeField: 'name', rownumbers: true, cascadeCheck: true, lines: true,
			toolbar: '#toolbars', striped: true, fit: true, remoteSort: false, singleSelect: true,
			frozenColumns: [[
				{ field: 'ck', checkbox: true },
				{ field: 'id', title: 'ID', hidden: true }
			]],
			columns: [[
	    		{ field: 'sort', title: '排序号', width: 50, sortable: true, align: 'center', editor: "numberbox" },
			    { field: 'type', title: '类型', width: 50, align: 'center', formatter:function(value,row){
			    	if(value == "O"){return "<font color='red'>公司</font>";}else if(value == "D"){return "<font color='#5CB811'>部门</font>";}
			    }},
	            { field: 'name', title: '组织名称', width: 320, sortable: true },
	    		{ field: 'sn', title: '唯一标示', width: 150, sortable: true },
			    { field: 'remark', title: '备注', width: 200 },
			    { field: 'modifyDate', title: '最后修改时间', width: 140 },
			    { field: 'modifyName', title: '修改者', width: 80 },
			    { field: 'created', title: '创建时间', width: 140, sortable: true }
			]],
			autoEditing: true, extEditing: true, singleEditing: true,
            onBeforeEdit: function(data) {
        		$.fn.treegrid.extensions.onBeforeEdit.apply(this, arguments);
	        	pid = data.pid ; oldSort = data.sort ;
	        },
	        onAfterEdit: function(data) {
	            $.fn.treegrid.extensions.onAfterEdit.apply(this, arguments);
	            if(oldSort != data.sort) {
	            	var o = {oldSort: oldSort, newSort: data.sort, id: data.id, pid: (undefined == data.pid?"":data.pid)};
	            	$.post($.webapp.root + "/admin/system/org/doNotNeedAuth_sort.do", o, function(result) {
	            		$tg.treegrid('reload') ; $.easyui.loaded();
	        		}).error(function() { $.easyui.loaded(); });
	            }
	        }
		});
	});
	
	function form_edit(form) {
		var form_url = $.webapp.root + "/admin/system/org/org_form_UI.do" ;
		if("E" == form) {
			var node = $tg.treegrid('getSelected');
			if (node) {
				form_url = $.webapp.root + "/admin/system/org/org_form_UI.do?id="+node.id ;
			} else {
				alertify.warning("请选择一条记录！"); return ;
			}
		}
		var $d = $.easyui.showDialog({
			href: form_url, title: "表单", iniframe: false, topMost: true, width: 740 , height: 300,
	        enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
	        buttons : [ 
			  { text : '保存并继续', iconCls : 'icon-standard-disk', handler : function() { $.easyui.parent.submitForm($d, $tg, true) ; } },
	          { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $tg) ; } },
	          { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } }
	       	]
	    });
	}
	
	function del() {
		var node = $tg.treegrid('getSelected');
		if(node){
			$.messager.confirm("该操作不可逆，您确定要进行该操作？", function (c) { 
				if(c) {
					$.easyui.loading({ msg: "数据删除中，请稍等..." });
					$.post($.webapp.root + "/admin/system/org/delete.do", {id:node.id}, function(result) {
						if (result.status) {
							$tg.treegrid('reload') ; $.easyui.loaded();
							alertify.success(result.msg);
						} else {
							$.easyui.loaded();alertify.error(result.msg);
						}
					}, 'json').error(function(){$.easyui.loaded();});
				} else {$.easyui.loaded("#westCenterLayout", true);}
			});
		} else {
			alertify.warning("请选择一条记录！");
		}
	}
    </script>
</head>

<body>

	<div id="tg">
		<div id="toolbars">
            <a onClick="form_edit('A');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
            <a onClick="form_edit('E');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_edit'">编辑</a>
            <a onClick="del();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">删除</a>
            <a onclick="$tg.treegrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
        </div>
	</div>
    
</body>
</html>



