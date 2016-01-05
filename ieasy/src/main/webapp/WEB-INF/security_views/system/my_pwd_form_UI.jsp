<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
//提交表单数据
var submitNow = function() {
	var data = $("#form").form("getData") ;
	data["id"] = $.webapp.user_id ;
	$.post($.webapp.root+"/admin/system/myinfo/doNotNeedAuth_modifyPwd.do", data, function(result) {
		if (result.status) {
			$.easyui.loaded();
			$.messager.alert("提示", result.msg, "info");
		} else {
			$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
		}
	}, 'json');
};

//验证表单
var submitForm = function() { 
	if($('#form').form('validate')) {
		$.easyui.loading({ msg: "数据提交中，请稍等..." });
		submitNow() ;
	}
};
</script>

<form id="form" class="easyui-form form_container">
	<input type="hidden" name="id" />
	<table class="tableform">
		<tr>
			<th>旧密码：</th>
			<td><input name="oldPwd" class="easyui-validatebox" type="password" data-options="prompt: '旧密码'" /></td>
		</tr>
		<tr>
			<th>新密码：</th>
			<td><input id="password" name="password" class="easyui-validatebox" type="password" data-options="required:true, prompt: '新密码'" /></td>
		</tr>
		<tr>
			<th>确认密码：</th>
			<td><input validType="equals[$('#password').val()]" invalidMessage="两次输入密码不匹配" class="easyui-validatebox" type="password" data-options="required:true, prompt: '确认密码'" /></td>
		</tr>
	</table>
</form>
