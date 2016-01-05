<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	$(function() {
		
	});
	
	//提交表单数据
	var submitNow = function($d, $dg, flag) {
		$.post($.webapp.root+"/admin/system/backup_res/doNotNeedAuth_backup.do", $("#form").form("getData"), function(result) {
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
			$.easyui.loading({ msg: "数据备份中，请稍等..." });
			submitNow($d, $dg, flag) ;
		}
	};
	
	
</script>

<form id="form" class="easyui-form form_container">
	<input type="hidden" name="createName" value="${USER_SESSION.user.emp_name}" />
	<input type="hidden" name="status" value="0" />
	<input type="hidden" name="backup_type" value="0" />
	<table class="tableform">
		<tr>
			<th>文件名称：</th>
			<td><input name="backup_name" style="width:228px;" class="easyui-validatebox" type="text" data-options="prompt: '默认为：未命名'" /></td>
		</tr>
	</table>
</form>
