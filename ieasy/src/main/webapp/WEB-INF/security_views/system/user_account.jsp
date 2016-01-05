<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/system/user/doNotNeedAuth_userCreateAccount.do" ;
	var role ;
	$(function() {
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});
		
		role = $("#role").combogrid({
			url : $.webapp.root+"/admin/system/role/combo_datagrid.do", multiple:true, 
			idField:'id', textField:'name', pagination: true, panelWidth: 400, panelHeight: 200,
			columns: [[
			    { field: 'name', title: '角色名称', width: 130, sortable: true },
			    { field: 'remark', title: '角色描述', width: 200, sortable: true }
			]]
		});
	});
	
	
	//提交表单数据
	var submitNow = function($d, $dg, flag) {
		$.post(form_url, $("#form").form("getData"), function(result) {
			if (result.status) {
				$dg.datagrid('reload');alertify.success(result.msg);$.easyui.loaded();
				if(flag==undefined||flag==false){$d.dialog("close") ;}
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	};
	
	//验证表单
	var submitForm = function($d, $dg, flag) { 
		if($('#form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			submitNow($d, $dg, flag) ;
		}
	};
	
	
</script>

<div class="panel-container" data-options="title: '基本信息', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
	<form id="form" class="easyui-form form_container">
		<input type="hidden" name="id" value="${id}" />
		<input type="hidden" name="emp_id" value="${emp_id}" />
		<input type="hidden" name="modifyName" value="${USER_SESSION.user.emp_name}" />
		<table class="tableform">
			<tr>
				<th>登录账号：</th>
				<td><input name="account" class="easyui-validatebox" type="text" /></td>
			</tr>
			<tr>
				<th>登录密码：</th>
				<td><input name="password" class="easyui-validatebox" type="text" /></td>
			</tr>
			<tr>
				<th>权限角色：</th>
				<td>
					<input id="role" name="role_ids" style="width:420px;height:30px;" /><a onClick="role.combogrid('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a>
				</td>
			</tr>
			<tr>
				<th>访问控制：</th>
				<td colspan="3">
					<div id="icheck">
						<input id="status1" type="radio" name="status" value="0" checked="checked">
						<label for="status1">允许登录</label>
						<input id="status2" type="radio" name="status" value="1">
						<label for="status2">禁止登录</label>
						
						<input id="access1" type="checkbox" name="access" value="1">
						<label for="access1">OA首页</label>
						<input id="access2" type="checkbox" name="access" value="1">
						<label for="access2">人员列表</label>
						<input id="access3" type="checkbox" name="access" value="1">
						<label for="access3">邮件发送</label>
					</div>
				</td>
			</tr>
		</table>
	</form>
</div>
