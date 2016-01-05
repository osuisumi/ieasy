<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/system/user/add.do" ;
	var org, role, tabsContainer ;
	$(function() {
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});
		tabsContainer = $("#tabsContainer").tabs({
			fit: true, border: false
		}) ;
		
		role = $("#role").combogrid({
			url : $.webapp.root+"/admin/system/role/combo_datagrid.do", multiple:true, 
			idField:'id', textField:'name', pagination: true, panelWidth: 400, panelHeight: 200,
			columns: [[
			    { field: 'name', title: '角色名称', width: 130, sortable: true },
			    { field: 'remark', title: '角色描述', width: 200, sortable: true }
			]]
		});
		
		//编辑，加载表单数据
		if($('input[name=id]').val().length > 0) {
			form_url = $.webapp.root+"/admin/system/user/update.do" ;
			$.post($.webapp.root+"/admin/system/user/get.do", {id:$('input[name=id]').val()}, function(result) {
				if (result.id != undefined) {
					$('#form').form('load', {
						'account' : result.account,
						'name' : result.name,
						'mobile' : result.mobile,
						'email' : result.email,
						'org_id' : result.org_id,
						'role_ids' : (undefined !=result.role_ids?result.role_ids.split(","):"")
					});
					
					$("input[name=status][value="+result.status+"]").iCheck('check') ;
					$("input[name=sex][value="+result.sex+"]").iCheck('check') ;
					$("input[name=password]").prop("disabled", true).val("密码不可编辑") ;
				}
			}, 'json').error(function() { $.easyui.loaded(); });
		} 
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

<div id="tabsContainer">
	<div class="panel-container" data-options="title: '基本信息', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
		<form id="form" class="easyui-form form_container">
			<input type="hidden" name="id" value="${id}" />
			<table class="tableform">
				<tr>
					<th>真实姓名：</th>
					<td><input name="name" class="easyui-validatebox" type="text" /></td>
					<th>性别：</th>
					<td>
						<div id="icheck">
							<input id="sex1" type="radio" name="sex" value="男" checked="checked">
							<label class="irl" for="sex1">男</label>
							<input id="sex2" type="radio" name="sex" value="女">
							<label class="irl" for="sex2">女</label>
						</div>
					</td>
				</tr>
				<tr>
					<th>登录账号：</th>
					<td><input name="account" class="easyui-validatebox" type="text" /></td>
					<th>登录密码：</th>
					<td><input name="password" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>邮箱地址：</th>
					<td><input name="email" class="easyui-validatebox" type="text" /></td>
					<th>手机号码：</th>
					<td><input name="mobile" class="easyui-validatebox" type="text" /></td>
				</tr>
				<tr>
					<th>权限角色：</th>
					<td colspan="3">
						<input id="role" name="role_ids" style="width:420px;height:25px;" /><a onClick="role.combogrid('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a>
					</td>
				</tr>
				<tr>
					<th>访问控制：</th>
					<td colspan="3">
						<div id="icheck">
							<input id="status1" type="radio" name="status" value="0" checked="checked">
							<label class="irl" for="status1">允许登录</label>
							<input id="status2" type="radio" name="status" value="1">
							<label class="irl" for="status2">禁止登录</label>
							
							<input id="input-2" type="checkbox" name="sendMailNotity" checked="checked" value="true"> <label class="irl" for="input-2">邮件通知</label>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div class="panel-container" data-options="title: '其他信息', iconCls: 'icon-standard-layout-header', refreshable: false">
		
	</div>
</div>
