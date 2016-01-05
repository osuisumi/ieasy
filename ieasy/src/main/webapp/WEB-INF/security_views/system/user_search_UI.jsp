<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var company, dept, group, sex, tabsContainer ;
	$(function() {
		
		tabsContainer = $("#tabsContainer").tabs({
			fit: true, border: false
		}) ;
		
		sex = $("#sex").combobox({
			valueField: 'label', textField: 'value', value: '',
			panelHeight:'auto', editable:false, autoShowPanel: true,
			data: [{ label: '男', value: '男' },{ label: '女', value: '女' }],
		});
		
		company = $("#company").combotree();
		dept = $("#dept").combotree();
		
		group = $("#group").combogrid({
			url: $.webapp.root+"/admin/system/group/datagrid.do",
			idField:'id', textField:'name', pagination: true, panelWidth: 400, panelHeight: 200,
			columns: [[
			    { field: 'name', title: '组名称', width: 130, sortable: true },
			    { field: 'remark', title: '组描述', width: 200, sortable: true }
			]]
		});
		
		company.combotree({
			url : $.webapp.root+"/admin/system/company/tree.do",
			panelHeight:'auto', editable: false, lines:true,
			onChange: function(newValue, oldValue) { 
				dept.combotree({
        			url : $.webapp.root+"/admin/system/dept/tree.do?company_id="+newValue,
        			editable: false, lines:true,
        			onLoadSuccess: function(node, data) {
    					if(data && data.length>0){
    						dept.combotree("setValue", data[0].id) ; 
    					}
    				},
        	    });
            }
	    });
	});
</script>

<div id="tabsContainer">
	<div class="panel-container" data-options="title: '基本信息', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
		<form id="form" class="easyui-form form_container">
			<input type="hidden" name="id" value="${id}" />
			<table class="tableform">
				<tr>
					<th>用户编号：</th>
					<td><input name="num" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>性别：</th>
					<td><input id="sex" name="sex" style="width:218px;height:30px;" /><a onClick="sex.combobox('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
				</tr>
				<tr>
					<th>登录账号：</th>
					<td><input name="account" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>登录密码：</th>
					<td><input name="password" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>真实姓名：</th>
					<td><input name="truename" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>手机号码：</th>
					<td><input name="mobile" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>邮箱地址：</th>
					<td><input name="email" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>手机号码：</th>
					<td><input name="a" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>隶属机构：</th>
					<td><input id="company" name="company_id" style="width:218px;height:30px;" /><a onClick="company.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
				</tr>
				<tr>
					<th>所属部门：</th>
					<td><input id="dept" name="dept_id" style="width:218px;height:30px;" /><a onClick="dept.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
				</tr>
				<tr>
					<th>用户组：</th>
					<td>
						<input id="group" name="group_id" style="width:218px;height:30px;" /><a onClick="group.combogrid('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div class="panel-container" data-options="title: '其他信息', iconCls: 'icon-standard-layout-header', refreshable: false">
		
	</div>
</div>
