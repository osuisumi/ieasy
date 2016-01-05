<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>人员管理</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg;
	$(function() {
		
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/person/datagrid.do",
			title: '人员管理', 
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200,500,1000], rownumbers: true,
			remoteSort: true, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'num', sortOrder: 'asc',
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'num', title: '员工编号', width: 80, sortable: true, formatter:function(value,row){
			    	if(row.logout == -1){
			    		return "<s style='color:red;'>"+value+"</s>";
			    	} else { return value;} 
			    } },
			    { field: 'name', title: '员工姓名', width: 80 },
			    { field: 'sex', title: '性别', width:50, sortable: true, align: 'center'},
			    { field: 'email', title: '邮箱地址', width: 200, formatter: function(value, row) {
			    	return $.string.format("<a href='javascript:alert(\"因功能太多，或该功能优先级别低，暂时忘了实现，请提醒我。thk\");' class='clickLInk ext_email'>{0}</a>", value) ;
			    } },
			    { field: 'org_name', title: '组织机构', width: 100, tooltip: true }, 
			    { field: 'position_name', title: '岗位', width: 100 },
			    { field: 'ryjb', title: '日语级别', width:60, align: 'center' }, 
			    { field: 'enterDate', title: '入职日期', align: 'center', width:80, sortable: true },
			    { field: 'bysj', title: '毕业时间', width:100, sortable: true },
			    { field: 'positionRecord', title: '岗位变更记录', width: 200, tooltip: true, formatter: function(value, row) {
			    	if(undefined != value && "" != value) {
			    		var str = "" ;
			    		var j = $.parseJSON("["+value+"]") ;
			    		$.each(j, function(i,p){
			    			str += "["+p.name+"#"+p.date+"]" + "==>>" ;
			    		});
			    		return str ;
			    	} else {
			    		return "-" ;
			    	}
			    } },
			    { field: 'empState', title: '员工状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "在职"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "离职"){return "<font color='red'>"+value+"</font>";}
			    	else if(value == "退休"){return "<font color='#B91D47'>"+value+"</font>";}
			    }},
			    { field: 'dimissionDate', title: '离职日期', align: 'center', width:80, sortable: true },
			    { field: 'dbmType', title: '到部门类型', width: 100, align: 'center', rowspan:2, formatter:function(value, row, index){
					if(value != undefined) {
						return value ;
					} else {return "-" ;}  
				}},
				{ field: 'dbmDate', title: '到部门日期', width: 90, rowspan:2, align: 'center', formatter:function(value, row, index){
					if(undefined != value) {
						return $.date.format(value, "yyyy-MM-dd") ;
					} else {
						return "-" ;
					}
				}},
				{ field: 'lbmType', title: '离部门类型', width: 110, rowspan:2, align: 'center', formatter:function(value, row, index){
					if(value != undefined) {
						return value ;
					} else {return "-" ;}  
				}},
				{ field: 'lbmDate', title: '离部门日期', width: 90, align: 'center', rowspan:2, formatter:function(value, row, index){
					if(undefined != value) {
						return $.date.format(value, "yyyy-MM-dd") ;
					} else {
						return "-" ;
					}
				}},
			    /*
			    { field: 'archivesStatus', title: '建档状态', width: 80, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == 0){return "<font color='red'>未建档</font>";}else if(value == 1){return "<font color='#00CC00'>已建档</font>";}
			    }},
			    */
			    { field: 'created', title: '创建时间', width: 140 },
 			    { field: 'createName', title: '创建者', width: 80 },
 			    { field: 'modifyDate', title: '最后修改时间', width: 140 },
 			    { field: 'modifyName', title: '修改者', width: 80 }
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
		
		position = $("#position_search").combotree({
			url : $.webapp.root+"/static_res/position.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true, autoShowPanel: false,
			onChange: function(newValue, oldValue) {
				$dg.datagrid("load",{position_id: newValue});
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
	
	function form_edit(form) {
		var form_url = $.webapp.root+"/admin/system/person/person_form_UI.do" ;
		if("E" == form) {
			var node = $dg.datagrid('getSelected');
			if (node) {
				form_url = $.webapp.root+"/admin/system/person/person_form_UI.do?id="+node.id ;
			} else {
				alertify.warning("请选择一条记录！"); return ;
			}
		}
		var $d = $.easyui.showDialog({
			href: form_url, title: "表单", iniframe: false, topMost: true, maximizable: true,
			width: (1200 >= parent.$.webapp.getInner().width? parent.$.webapp.getInner().width-80:1200),
			height: (800 > parent.$.webapp.getInner().height? parent.$.webapp.getInner().height-60:800),
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '保存并继续', iconCls : 'icon-standard-disk', handler : function() { $.easyui.parent.submitForm($d, $dg, true); } },
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg); } },
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
				$.post($.webapp.root+"/admin/system/person/delete.do", {"ids" : ids.join(',')}, function(result) {
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
	
	
	function dimission() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/person/person_batch_dimission.do",
			title: "批量离职", iniframe: false, topMost: true, width: 400, height: 330,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg) ; } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	],
           	onLoad: function(){
           		//person_main_UI.jsp是在iframe中，dialog在父窗口中，如下获取父窗口的元素
           		//设置父窗口中的Dialog中的ids的值
           		parent.$("#ids").val(ids.join(","));
           	}
       	});
	}
	
	function logout() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
				if(rows[i].empState != "离职") {
					alertify.warning("所选人员中有未离职人员，请先设置为离职！");
					return;
				}
			}
		} else {
			alertify.warning("请选择一条记录！");
			return false;
		}
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据注销中，请稍等..."});
				$.post($.webapp.root+"/admin/system/person/batchOrgLogout.do", {"ids" : ids.join(',')}, function(result) {
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
	
	function batch_org() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/person/person_batch_org.do",
			title: "批量调换部门", iniframe: false, topMost: true, width: 400, height: 330,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg) ; } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	],
           	onLoad: function(){
           		//person_main_UI.jsp是在iframe中，dialog在父窗口中，如下获取父窗口的元素
           		//设置父窗口中的Dialog中的ids的值
           		parent.$("#ids").val(ids.join(","));
           	}
       	});
		
	}
	
	function batch_pos() {
		var ids = [];
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/person/person_batch_position.do", 
			title: "批量设置岗位", iniframe: false, topMost: true, width: 400, height: 330,method: "post",
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, $dg) ; } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	],
           	onLoad: function(){
           		parent.$("#ids").val(ids.join(","));
           	}
        });
	}
	
	function export_emp_data() {
		var p = handlerParams(); 
		$("#download").attr("src", $.webapp.root+"/admin/system/person/exportPersonExcelData.do?1=1"+p);
	}
	function handlerParams() {
		var params = "" ;
		var data = $("#searchForm").form("getData") ;
		$.each(data, function(p, v){
			if("" != v) {
				params += "&"+p+"="+encodeURI(v) ;
			}
		});
		return params ;
	}
	
	function import_emp_data() {
		var $d = $.easyui.showDialog({
			href: $.webapp.root+"/admin/system/person/person_import_excel_data.do",  
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
            <div id="search_bar">
            	<form id="searchForm">
	            	<div class="s_box">
	            		<div class="gr">
		            		<div class="st">公司部门&nbsp;</div>
		            		<div class="si1"><input id="org_search" name="org_id" style="width:220px;height:25px;" /></div>
	            		</div>
	            		<div class="gr">
		            		<div class="st">岗位&nbsp;</div>
		            		<div class="si1"><input id="position_search" name="position_id" style="width:200px;height:25px;" /></div>
	            		</div>
	            		<div class="gr">
		            		<div class="si">
			            		<input class="easyui-searchbox" data-options="searcher:doSearch,width: 150, height: 25, menu: '#topSearchboxMenu'" />
								<div id="topSearchboxMenu" style="width: 85px;">
									<div data-options="name:'num', iconCls: 'icon-hamburg-zoom'">编号</div>
									<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">姓名</div>
									<div data-options="name:'email', iconCls: 'icon-hamburg-zoom'">邮件</div>
								</div>
		            		</div>
	            		</div>
	            		<div class="gr">
							<div class="st">性别&nbsp;</div>
							<div class="si">
								<select name="sex" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="height:25px;width:67px;">
									<option value="">请选择</option>
									<option value="男">男</option>
									<option value="女">女</option>
								</select>
							</div>
						</div>
	            		<div class="gr">
							<div class="st">状态&nbsp;</div>
							<div class="si">
								<select name="empState" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="height:25px;width:67px;">
									<option value="">请选择</option>
									<option value="在职">在职</option>
									<option value="离职">离职</option>
									<option value="退休">退休</option>
								</select>
							</div>
						</div>
	            		<div class="gr">
							<div class="st">注销&nbsp;</div>
							<div class="si">
								<select name="logout" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="height:25px;width:67px;">
									<option value="">请选择</option>
									<option value="0">正常</option>
									<option value="-1">注销</option>
								</select>
							</div>
						</div>
	            	</div>
	            	<div class="s_box">
						<div class="gr">
							<div class="st">入职日期&nbsp;</div> 
							<div class="si1">
								<input id="rz1" name="rz_startDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									maxDate:'#F{$dp.$D(\'rz2\')||\'2020-10-01\'}'
									})"/>
								至
								<input id="rz2" name="rz_endDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'rz1\')}',
									maxDate:'2120-10-01'
									})"/>
		            		</div>
							<div class="st">离职日期</div> 
							<div class="si1">
								<input id="lz1" name="lz_startDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									maxDate:'#F{$dp.$D(\'lz2\')||\'2020-10-01\'}'
									})"/>
								至
								<input id="lz2" name="lz_endDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'lz1\')}',
									maxDate:'2120-10-01'
									})"/>
		            		</div>
		            	</div>
	            		<div class="gr">
		            		<div class="st">
			            		<a onClick="searchBox();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">查询</a>
			            		<a onClick="searchClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置</a>
		            		</div>
	            		</div>
	            	</div>
	            	
	            	<div class="s_box">
	            		<div class="gr">
		            		<a onclick="dimission();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-sitemap'">批量离职</a>
		            		<a onclick="logout();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-sitemap'">批量注销</a>
		            		<a onclick="batch_org();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-sitemap'">批量调换部门</a>
		            		<a onclick="batch_pos();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-sitemap'">批量设置岗位</a>
		            		<!-- 
		            		<a onclick="import_emp_data();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-up'">导入数据</a>
		            		 -->
		            		<a onclick="mail();" class="easyui-linkbutton" data-options="plain: true, iconCls:'ext_email'">发送邮件</a>
		            		<a onclick="export_emp_data();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-down'">导出数据</a>
	            		</div>
	            	</div>
            	</form>
            </div>
        </div>
	</div>
	<iframe id="download" style="display:none"></iframe> 
</body>
</html>