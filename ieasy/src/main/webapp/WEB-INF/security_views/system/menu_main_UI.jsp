<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>菜单管理</title>
	<%@ include file="/common/header/meta.jsp"%>
	<%@ include file="/common/header/script.jsp"%>
    <script type="text/javascript">
    var $tg, oldSort, pid ;
	$(function() {
		$tg = $("#tg").treegrid({
			url: $.webapp.root + "/static_res/menu.tree.json",
			idField: "id", treeField: "name", rownumbers: true, cascadeCheck: true, border: false,
			toolbar: "#toolbars", striped: true, fit: true, remoteSort: false, singleSelect: true,
			frozenColumns: [[
				{ field: "ck", checkbox: true },
				{ field: "id", title: "ID", hidden: true }
			]],
			columns: [[
	            { field: "name", title: "名称", width: 220, sortable: true },
	    		{ field: "sort", title: "排序", width: 50, sortable: true, editor: "numberbox" },
			    { field: "type", title: "菜单类型", width: 70, sortable: true, formatter:function(value,row){
			    	if(value == "T"){return "导航栏目";}else if(value == "M"){return "<font color='green'>导航菜单</font>";}else{return "<font color='red'>菜单操作</font>";}
			    }},
			    { field: "href", title: "链接地址", width: 300, tooltip: true },
			    { field: "isShow", title: "是否显示", width: 60, sortable: true, align: "center", formatter:function(value,row){
			    	if(row.type != "O") {
			    		return "<div id='icheck'><input id='"+row.id+"' type='checkbox' "+(value==1?'checked':'')+"></div>" ;
			    	}
			    }},
			    { field: "remark", title: "备注", width: 200 },
			    { field: "modifyDate", title: "最后修改时间", width: 140 },
			    { field: "modifyName", title: "修改者", width: 80 },
			    { field: "created", title: "创建时间", width: 140, sortable: true }
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
	            	$.post($.webapp.root + "/admin/system/menu/doNotNeedAuth_sort.do", o, function(result) {
	            		$tg.treegrid("reload") ; $.easyui.loaded();
	        		}).error(function() { $.easyui.loaded(); });
	            }
	        },
	        onLoadSuccess: function() {
            	$.fn.treegrid.extensions.onLoadSuccess.apply(this, arguments);
            	$("#icheck input").iCheck({
        			checkboxClass: "icheckbox_minimal-blue",
        			radioClass: "iradio_square-blue"
        	   	}).on("ifChecked", function(event){
        	   		isShow($(this).attr("id"), 1) ;
            	}).on("ifUnchecked", function(event){
            		isShow($(this).attr("id"), 0) ;
            	});
	         }
		});
	});
	
	function isShow(id, chk) {
		$.post($.webapp.root+"/admin/system/menu/isShow_hide.do", {"id":id, "isShow":chk}, function(result) {
			if (result.status) {
				$tg.treegrid("clearSelections");$tg.treegrid("clearChecked");
				alertify.success(result.msg);
			} else {
				alertify.warning(result.msg);
			}
		}, "json");
	}
	
	function form_edit(form) {
		var form_url = $.webapp.root + "/admin/system/menu/menu_form_UI.do" ;
		if("E" == form) {
			var node = $tg.treegrid("getSelected");
			if (node) {
				form_url = $.webapp.root + "/admin/system/menu/menu_form_UI.do?id="+node.id ;
			} else {
				alertify.warning("请选择一条记录！"); return ;
			}
		}
		var $d = $.easyui.showDialog({
			href: form_url, title: "表单", iniframe: false, topMost: true, width: 740 , height: 400,
	        enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
	        buttons : [ 
			  { text : "保存并继续", iconCls : "icon-standard-disk", handler : function() { $.easyui.parent.submitForm($d, $tg, true) ; } },
	          { text : "确定", iconCls : "ext_save", handler : function() { $.easyui.parent.submitForm($d, $tg) ; } },
	          { text : "关闭", iconCls : "ext_cancel", handler : function() { $d.dialog("destroy"); } }
	       	]
	    });
	}
	
	function del() {
		var node = $tg.treegrid("getSelected");
		if(node){
			$.messager.confirm("您确定要进行该操作？", function (c) { 
				if(c) {
					$.easyui.loading({ msg: "数据删除中，请稍等..." });
					$.post($.webapp.root + "/admin/system/menu/delete.do", {id:node.id}, function(result) {
						if (result.status) {
							$tg.treegrid("reload") ; $.easyui.loaded();
							alertify.success(result.msg);
						} else {
							$.easyui.loaded();alertify.error(result.msg);
						}
					}, "json").error(function(){$.easyui.loaded();});
				} 
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



