<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>用户授权</title>
	<%@ include file="/common/header/meta.jsp"%>
	<%@ include file="/common/header/script.jsp"%>
    <script type="text/javascript">
    var $dg, $tg_menu ;
	$(function() {
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/system/user/datagrid.do",
			idField: 'id', fit: true, border: false, rownumbers: true, fitColumns: true,
			remoteSort: true, striped:true, pagination:true, pageSize: 30, singleSelect: true,
			sortName: 'created', sortOrder: 'desc',
			columns: [[
			    { field: 'id', title: 'ID', hidden: true },
			    { field: 'name', title: '姓名', width: 60 },
			    { field: 'account', title: '账号', width: 150, sortable: true, formatter:function(value,row){
			    	if(value == "未创建"){return "<font color='red'>"+value+"</font>";}else {return value ;}
			    }},
			    { field: 'created', title: '创建时间', hidden: true }
			]],
			onClickRow: getPermits,
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg.datagrid('unselectAll');$dg.datagrid('clearSelections');$dg.datagrid('unselectAll');
			}
		}) ;
		$dg.datagrid("getPager").pagination({layout:['first','prev','next','last']});
		
		allMenus() ;
	});
	
	function doSearch(value,name){
		var o = {} ; o[name] = value ; $dg.datagrid("load",o);
	}
	
	function allMenus() {
		$tg_menu = $("#tg_menu").treegrid({
			url: $.webapp.root+"/admin/system/acl/allMenuTree.do",
			idField: 'id', treeField: 'name', rownumbers: true, border: false,
			striped: true, fit: true, remoteSort: false, singleSelect: false, autoRowHeight: true, fitClomuns: true,
			onClickRow:function(row){   
				$tg_menu.treegrid('cascadeCheck',{		//级联选择   
                    id:row.id, 							//节点ID   
                    deepCascade:true 					//深度级联   
               	});   
         	},
			frozenColumns: [[
				{ field: 'ck', checkbox: true },
				{ field: 'id', title: 'ID', hidden: true }
			]],
			columns: [[
	            { field: 'name', title: '名称', width: 220},  
	            { field: 'sel', title: '选择', width: 110, align: 'center', formatter: function(val, row, index){
	            	var clk = $.string.format("<div class='href_icon' onclick='iChecked({0},\"{1}\");'><div class='button-mini'><span class='button-mini-icon icon-cancel'>&nbsp;</span></div><span>不选</span></div>", false, row.id) ;
	            	clk += $.string.format("<div class='href_icon' onclick='iChecked({0},\"{1}\");'><div class='button-mini'><span class='button-mini-icon icon-ok'>&nbsp;</span></div><span>全选</span></div>", true, row.id) ;
	            	return clk;
	            }},
	            { field: 'opers', title: '操作', formatter: function(val, row, index){
	            	var op = "<div id='icheck' class='icheck_in'>" ;
	            	if(undefined != val) {
		            	$.each(val, function(i, p){
		            		op += "<input pid='"+row.id+"' id='"+p.id+"' type='checkbox' operMenuName='"+p.name+"' href='"+p.href+"'><div class='opts'><label for='"+p.id+"'>"+p.name+"</label></div>" ;
		            	});
	            	}
	            	op + "</div>" ;
	            	return op;
	            }}
			]],
			onLoadSuccess: function(node, data) {
				$("#icheck input").iCheck({
        			checkboxClass: "icheckbox_minimal-orange"
        	   	});
			}
		});
	}
	
	function savePermits() {
		var principals = [] ;
		var dg = $dg.datagrid("getSelections") ;
		var flag = true ;
		$.each(dg, function(i, p){
			if(undefined == p.id) {
				alertify.warning("该用户账号未创建，无法授权！");
				flag = false ;
			}
			var principal = {"principalId": p.id, "principalType": "USER"} ;
			principals.push(principal) ;
		});
		if(!flag) return ;
		
		var resources = [] ;
		var tg_menu = $tg_menu.treegrid("getSelections") ;
		$.each(tg_menu, function(i, p){
			//许可菜单
			var menu = {"menuId": p.id, "menuName": p.name, "menuHref": p.href, "type": p.type, "menuPid": p.pid, "menuSort": p.sort, "menuIconCls": p.iconCls, "state": p.state} ;
			
			//许可操作
			var opers = [] ;
			var chk = $("input[pid="+p.id+"]:checked") ;
			$.each(chk, function(i,p){
				var oper = {"operMenuId": $(this).attr("id"), "operMenuName": $(this).attr("operMenuName"), "operMenuHref": $(this).attr("href"), "operIconCls": $(this).attr("iconCls"), "operSort": $(this).attr("sort")} ;
				opers.push(oper) ;
			});
			menu["opers"] = opers ;
			
			resources.push(menu) ;
		});
		
		$.easyui.loading({ msg: "角色授权中，请稍等..." });
		$.post($.webapp.root+"/admin/system/acl/grantPermits.do", {
			"principals": JSON.stringify(principals), 
			"resources": JSON.stringify(resources)
		}, function(result) {
			if (result.status) {
				alertify.success(result.msg);$.easyui.loaded();
			} else {
				alertify.warning(result.msg);
				$.easyui.loaded();
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	}
	
	
	function getPermits(rowIndex, rowData) {
		$tg_menu.treegrid("unselectAll") ;
		$("#icheck input").iCheck("uncheck") ;
		$.post($.webapp.root+"/admin/system/acl/getPermits.do", {"principalId": rowData.id, "principalType": "USER"}, function(result){
			$.each(result.menus, function(i, p){
				$tg_menu.treegrid("select", p.menuId) ;
			});
			$.each(result.opers, function(i, p){
				$("input[id="+p.operMenuId+"]").iCheck("check") ;
			});
			
		}, 'json').error(function() {$.easyui.loaded();});
	}
	
	function allChecked(chk) {
		if(chk == true) {
			$tg_menu.treegrid("selectAll");
			$("#icheck input").iCheck("check") ;
			
		} else {
			$tg_menu.treegrid("unselectAll");
			$("#icheck input").iCheck("uncheck") ;
		}
	}
	
	function allOperChecked(chk) {
		if(chk == true) {
			$("#icheck input").iCheck("check") ;
		} else {
			$("#icheck input").iCheck("uncheck") ;
		}
	}
	
	function iChecked(chk, group) {
		if(chk == true) {
			$("input[pid="+group+"]").iCheck("check") ;
		} else {
			$("input[pid="+group+"]").iCheck("uncheck") ;
		}
	}
	
    </script>
</head>

<body>

	<div class="easyui-layout" data-options="fit:true">
		
		<div data-options="region:'west',split:true" style="width:330px;border-top:none;">
			<div class="easyui-layout" data-options="fit:true"> 
				<div data-options="region: 'north', split: false, border: true" style="border-top:none;border-left:none;border-right:none;height: 33px;">
	                <div class="easyui-toolbar" style="margin-left:-5px;">
	                    <input id="topSearchbox" class="easyui-searchbox" data-options="searcher:doSearch,width: 222, height: 26, menu: '#topSearchboxMenu'" />
						<div id="topSearchboxMenu" style="width: 85px;">
							<div data-options="name:'num', iconCls: 'icon-hamburg-zoom'">编号</div>
							<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">姓名</div>
							<div data-options="name:'account', iconCls: 'icon-hamburg-zoom'">账号</div>
						</div>
						<a onClick="$dg.datagrid('load',{});$('#topSearchbox').searchbox('setValue', '');" class="easyui-linkbutton" title="取消筛选" data-options="plain: true, iconCls: 'ext_remove'">取消筛选</a>
	                </div>
	            </div>
				<div data-options="region:'center',border:false">
					<div id="dg"></div>
				</div>
			</div>
			
		</div>
		
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" data-options="fit:true">
			
				<div data-options="region: 'north', split: false, border: true" style="border-top:none;height: 33px;">
	                <div class="easyui-toolbar">
	                    <a onClick="savePermits()" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_save'">保存</a>
	                    <a onClick="$tg_menu.treegrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">刷新</a>
			            <a onClick="allChecked(true);" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_edit'">菜单全选</a>
			            <a onClick="allChecked(false);" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">菜单不选</a>
			            <a onClick="allOperChecked(true);" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_edit'">操作全选</a>
			            <a onClick="allOperChecked(false);" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">操作不选</a>
			            <a onClick="$tg_menu.treegrid('expandAll')" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-arrow-out'">展开</a>
			            <a onClick="$tg_menu.treegrid('collapseAll')" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-arrow-in'">折叠</a>
	                </div>
	            </div>
				
				<div data-options="region:'center'">
					<div id="tg_menu"></div>
				</div>
				
			</div>
		</div>
		
	</div>
    
</body>
</html>



