<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户管理</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg;
	$(function() {
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/user/datagrid.do",
			title: '<span style="font-weight:normal;color:#666;">用户管理</span>', 
			idField: 'emp_id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200,1000	], rownumbers: true,
			remoteSort: true, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'num', sortOrder: 'asc',
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true },
			    { field: 'account', title: '登录账号', width: 100, sortable: true, formatter:function(value,row){
			    	if(value == "未创建"){return "<font color='red'>"+value+"</font>";}else {return value ;}
			    }},
			    { field: 'password', title: '登录密码', width: 80, align: 'center', formatter:function(value,row){
			    	if(value == "未创建"){return "<font color='red'>"+value+"</font>";}else {return value ;}
			    }},
			    { field: 'status', title: '账号状态', width: 80, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == 0){return "<font color='#00CC00'>正常</font>";}else if(value == 1){return "<font color='red'>锁定</font>";}else if(value == 2){return "<font color='red'>未创建</font>";}
			    }}
			]],
			columns: [[
				{ field: 'num', title: '编号', width: 80, sortable: true },
			    { field: 'name', title: '姓名', width: 100 },
			    { field: 'sex', title: '性别', width:50, sortable: true, align: 'center'},
			    { field: 'org_name', title: '机构/部门', width: 180, tooltip: true }, 
			    { field: 'empState', title: '员工状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "在职"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "离职"){return "<font color='red'>"+value+"</font>";}
			    	else if(value == "退休"){return "<font color='#B91D47'>"+value+"</font>";}
			    }},
			    { field: 'role_names', title: '角色', width:100, tooltip: true },
			    { field: 'mobile', title: '手机号码', width:100 },
			    { field: 'email', title: '邮箱地址', width: 200, formatter: function(value, row) {
			    	return $.string.format("<a href='javascript:alert(\"因功能太多，或该功能优先级别低，暂时忘了实现，请提醒我。thk\");' class='clickLInk ext_email'>{0}</a>", value) ;
			    } },
			    { field: 'lastAcceccTime', title: '最后访问', width: 140, sortable: true },
			    { field: 'diffDatetime', title: '离线', width: 180, sortable: true },
			    { field: 'created', title: '创建时间', width: 140 },
 			    { field: 'createName', title: '创建者', width: 140 },
 			    { field: 'modifyDate', title: '最后修改时间', width: 140 },
 			    { field: 'modifyName', title: '修改者', width: 140 }
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg.datagrid('unselectAll');$dg.datagrid('clearSelections');
			}
		}) ;
		org_search = $("#org_search").combotree({
			url : $.webapp.root+"/static_res/org.tree.json",
			editable: false, lines:true,
			onChange: function(newValue, oldValue) {
				$dg.datagrid("load",{org_id: newValue});
				
            }
	    });
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
	
	function search_dialog() {
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/user/doNotNeedAuth_search.do", title: "搜索", iniframe: false, topMost: true,
			width: 400, height: 550,modal: false, collapsible: true,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '搜索', iconCls : 'ext_save', handler : function() { searchGroup(); } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	]
        });
	}
	
	function form_edit(form) {
		var form_url = $.webapp.root+"/admin/system/user/user_form_UI.do" ;
		if("E" == form) {
			var node = $dg.datagrid('getSelected');
			if (node) {
				if(node.id == undefined || "" == node.id) {
					alertify.warning("未创建账号，不可以编辑！"); return ;
				}
				form_url = $.webapp.root+"/admin/system/user/user_form_UI.do?id="+node.id ;
			} else {
				alertify.warning("请选择一条记录！"); return ;
			}
		}
		var $d = $.easyui.showDialog({
			href: form_url, title: "表单", iniframe: false, topMost: true,
			width: 750, height: 405, maximizable: true,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '保存并继续', iconCls : 'icon-standard-disk', handler : function() { $.easyui.parent.submitForm($d, $dg, true) ; } },
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg) ; } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	]
        });
	}
	
	function createAccount() {
		var node = $dg.datagrid('getSelected');
		if (node) {
			if(node.id != undefined && "" != node.id) {
				alertify.warning("账号已创建，编辑即可！"); return ;
			}
		} else {
			alertify.warning("请选择一条记录！"); return ;
		}
		var form_url = $.webapp.root+"/admin/system/user/user_account.do?emp_id="+node.emp_id ;
		var $d = $.easyui.showDialog({
			href: form_url, title: "表单", iniframe: false, topMost: true,
			width: 650, height: 405, maximizable: true,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
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
				if(undefined != rows[i].id && ""!=rows[i].id) {
					ids.push(rows[i].id);
				}
			}
		} else {
			alertify.warning("请选择一条记录！");
			return false;
		}
		if(ids.length<1) { alertify.warning("请选择已创建账号的记录！"); return ;}
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据删除中，请稍等..."});
				$.post($.webapp.root+"/admin/system/user/delete.do", {"ids" : ids.join(',')}, function(result) {
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
	
	
	function batch_role() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				if(undefined != rows[i].id && ""!=rows[i].id) {
					ids.push(rows[i].id);
				}
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		if(ids.length<1) { alertify.warning("请选择已创建账号的记录！"); return ;}
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/user/user_role.do", 
			title: "批量添加角色", iniframe: false, topMost: true, width: 600, height: 530,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg); } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	],
           	onLoad: function(){
           		parent.$("#ids").val(ids.join(","));
           	}
        });
	}
	
	function lockAccount() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				if(undefined != rows[i].id && ""!=rows[i].id) {
					ids.push(rows[i].id);
				}
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		if(ids.length<1) { alertify.warning("请选择已创建账号的记录！"); return ;}
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/user/user_lock_account.do", 
			title: "锁定账号", iniframe: false, topMost: true, width: 340, height: 147,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg); } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	],
           	onLoad: function(){
           		parent.$("#ids").val(ids.join(","));
           	}
        });
	}
	
	function clearPwd() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				if(undefined != rows[i].id && ""!=rows[i].id) {
					ids.push(rows[i].id);
				}
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		if(ids.length<1) { alertify.warning("请选择已创建账号的记录！"); return ;}
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据添加中，请稍等..."});
				$.post($.webapp.root+"/admin/system/user/batchClearPwd.do", {ids: ids.join(',')}, function(result) {
					if (result.status) {
						$dg.datagrid("reload");
						alertify.success(result.msg);$.easyui.loaded();
					} else {
						$.easyui.loaded();warning.success(result.msg);
					}
				}, 'json').error(function() { $.easyui.loaded(); });
			}
		});
	}
	
	function resetPwd() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				if(undefined != rows[i].id && ""!=rows[i].id) {
					ids.push(rows[i].id);
				}
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		if(ids.length<1) { alertify.warning("请选择已创建账号的记录！"); return ;}
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/user/user_reset_pwd.do", 
			title: "重设密码", iniframe: false, topMost: true, width: 340, height: 147,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg); } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	],
           	onLoad: function(){
           		parent.$("#ids").val(ids.join(","));
           	}
        });
		
	}
	
	function notifyNullPwd() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				if(undefined != rows[i].id && ""!=rows[i].id) {
					ids.push(rows[i].id);
				}
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		if(ids.length<1) { alertify.warning("请选择已创建账号的记录！"); return ;}
		alert("提醒空密码用户，修改密码！");
	}
	
	function export_user_data() {
		$("#download").attr("src", $.webapp.root+"/admin/system/user/exportUserData.do");
	}
	
	function import_user_data() {
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/user/import_user_data.do",  
			title: "导入Excel数据", iniframe: false, topMost: true, width: 800, height: 530,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	]
        });
	}
	
	function mail() {
		var tos = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				if(undefined != rows[i].email &&"" != rows[i].email) {
					tos.push(rows[i].email);
				}
			}
		}
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/common/mail/mail_UI.do", title: "邮件", iniframe: false, topMost: true,
			width: 750, height: 765, maximizable: true,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '发送', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg); } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	],
           	onLoad: function(){
           		parent.$("textarea[name=to]").val(tos.join(","));
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
            <div id="search_bar">
            	<form id="searchForm">
	            	<div class="s_box">
	            		<div class="gr">
		            		<div class="si">
			            		<input class="easyui-searchbox" data-options="searcher:doSearch,width: 220, height: 25, menu: '#topSearchboxMenu'" />
								<div id="topSearchboxMenu" style="width: 85px;">
									<div data-options="name:'num', iconCls: 'icon-hamburg-zoom'">编号查询</div>
									<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">真实姓名</div>
									<div data-options="name:'account', iconCls: 'icon-hamburg-zoom'">登录账号</div>
									<div data-options="name:'email', iconCls: 'icon-hamburg-zoom'">邮件地址</div>
								</div>
		            		</div>
	            		</div>
						<div class="gr">
							<div class="st">性别</div>
							<div class="si">
								<select name="sex" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="height:25px;">
									<option value="">请选择</option>
									<option value="男">男</option>
									<option value="女">女</option>
								</select>
							</div>
						</div>
						<div class="gr">
							<div class="st">日期查询</div>
							<div class="si1">
								<input id="d4311" name="startDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'
									})"/>
								至
								<input id="d4312" name="endDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'d4311\')}',
									maxDate:'2120-10-01'
									})"/>
		            		</div>
		            	</div>
	            		<div class="gr">
		            		<div class="st">公司</div>
		            		<div class="si1"><input id="org_search" name="org_id" style="width:250px;height:25px;" /></div>
	            		</div>
	            		<div class="gr">
		            		<div class="st">
			            		<a onClick="searchBox();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">查询</a>
			            		<a onClick="searchClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置</a>
			            		<a onClick="$dg.datagrid('load',{});" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-in'">复杂查询</a>
		            		</div>
	            		</div>
	            	</div>
	            	
	            	<div class="s_box">
	            		<div class="gr">
		            		<a onClick="createAccount();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-credit-card'">分配账号</a>
		            		<a onclick="lockAccount();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-lock'">锁定账号</a>
		            		<a onclick="clearPwd();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-standard-textfield'">清空密码</a>
		            		<a onclick="resetPwd();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-standard-textfield-key'">重设密码</a>
		            		<a onclick="notifyNullPwd();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-standard-email-go'">提醒空密码用户</a>
		            		<a onclick="batch_role();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-hire-me'">批量添加角色</a>
		            		<!-- 
		            		<a onclick="import_user_data();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-up'">导入数据</a>
		            		 -->
		            		<a onclick="mail();" class="easyui-linkbutton" data-options="plain: true, iconCls:'ext_email'">发送邮件</a>
		            		<a onclick="export_user_data();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-down'">导出数据</a>
	            		</div>
	            	</div>
            	</form>
            </div>
        </div>
	</div>
	<iframe id="download" style="display:none"></iframe> 
</body>
</html>