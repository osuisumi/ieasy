<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var dg_role ;
	$(function() {
		dg_role = $("#dg_role").datagrid({
			url: $.webapp.root+"/admin/system/role/combo_datagrid.do",
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200],
			remoteSort: false, toolbar: '#dialogBar', striped:true, pagination: true, singleSelect: false,
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'name', title: '名称', width: 180, tooltip: true }, 
			    { field: 'sn', title: '序列号', width: 100, },
			    { field: 'remark', title: '备注', width: 100, }, 
			    { field: 'created', title: '创建时间', width: 140, sortable: true },
			    { field: 'modifyDate', title: '最后修改时间', width: 140, sortable: true },
			    { field: 'modifyName', title: '修改者', width: 80, sortable: true }
			]]
		});
	});
	
	//验证表单
	var submitForm = function($d, $dg, flag) { 
		var ids = [];
		var rows = dg_role.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id);
			}
		}
		var user_ids = $("#ids").val() ;
		$.easyui.loading({ msg: "数据添加中，请稍等..."});
		$.post($.webapp.root+"/admin/system/user/doNotNeedAuth_batchRole.do", {ids: user_ids, role_ids: ids.join(',')}, function(result) {
			if (result.status) {
				alertify.success(result.msg);$.easyui.loaded();$dg.datagrid("reload") ;
				if(flag==undefined||flag==false){$d.dialog("close") ;}
			} else {
				$.easyui.loaded();warning.success(result.msg);
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	};
	function doSearch(value,name){
		var o = {} ;
		o[name] = value ;
		dg_role.datagrid("load",o);
	}
	
</script>
<div id="dg_role">
	<input type="hidden" name="ids" id="ids"/>
	<div id="dialogBar">
		<a onclick="dg_role.datagrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
		<a onClick="dg_role.datagrid('unselectAll');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-application-xp'">取消选中</a>
		<input class="easyui-searchbox" data-options="prompt:'请输入关键字',menu:'#mm',searcher:doSearch" style="width:300px"></input>
		<div id="mm">
			<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">角色名称</div>
		</div>
		<a onClick="dg_role.datagrid('load',{});" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'">取消筛选</a>
	</div>
</div>
