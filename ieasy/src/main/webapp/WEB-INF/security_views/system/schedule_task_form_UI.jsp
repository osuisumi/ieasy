<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var form_url = $.webapp.root + "/admin/system/schedule/add.do" ; 
	$(function() {
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});		
		
		//编辑，加载表单数据
		if($('input[name=id]').val().length > 0) {
			form_url = $.webapp.root + "/admin/system/schedule/update.do" ;
			$.post($.webapp.root + "/admin/system/schedule/get.do", {id:$('input[name=id]').val()}, function(result) {
				if (result.id != undefined) {
					$('#form').form('load', {
						'task_name' : result.task_name,
						'task_job_class' : result.task_job_class,
						'cron_expression' : result.cron_expression,
						'task_code' : result.task_code,
						'task_remark' : result.task_remark
					});
					$("input[name=task_enable][value="+result.task_enable+"]").iCheck('check') ;
				}
			}, 'json').error(function(){$.easyui.loaded();});
		} 
		
	});
	
	//提交表单数据
	var submitNow = function($d, $dg, flag) {
		$.post(form_url, $("#form").form("getData"), function(result) {
			if (result.status) {
				$dg.datagrid("reload");$.easyui.loaded(); 
				alertify.success(result.msg);
				if(flag==undefined||flag==false){$d.dialog("close") ;}
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function(){$.easyui.loaded();});
	};
	
	//验证表单
	var submitForm = function($d, $dg, flag) {
		if($('#form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			submitNow($d, $dg, flag) ;
		} 
	};
</script>

<form id="form" class="easyui-form form_container">
	<input type="hidden" name="id" value="${id}" />
	<input type="hidden" name="task_code" />
	<input type="hidden" name="modifyName" value="${USER_SESSION.user.emp_name}" />
	<table class="tableform">
		<tr>
			<th>作业名称：</th>
			<td><input name="task_name" class="easyui-validatebox i400" type="text" data-options="required: true" /></td>
		</tr>
		<tr>
			<th>执行动作类：</th>
			<td><input name="task_job_class" value="com.ieasy.module.common.executors.ResourceBackupJob" class="easyui-validatebox i400" type="text" data-options="required: true, prompt: '具体的操作类'" /></td>
		</tr>
		<tr>
			<th>作业时间：</th>
			<td>
				<input name="cron_expression" value="0/3 * * * * ?" class="easyui-validatebox i400" type="text" data-options="required: true" />
			</td>
		</tr>
		<tr>
			<th>作业状态：</th>
			<td>
				<div id="icheck">
					<input id="task_enable1" type="radio" name="task_enable" value="Y" checked="checked">
					<label for="task_enable1">启动</label>
					
					<input id="task_enable2" type="radio" name="task_enable" value="N">
					<label for="task_enable2">停止</label>
				</div>
			</td>
		</tr>
		<tr>
			<th>作业备注：</th>
			<td>
				<textarea name="task_remark" rows="5" cols="5" style="width:408px;height:65px;"></textarea>
			</td>
		</tr>
	</table>
</form>