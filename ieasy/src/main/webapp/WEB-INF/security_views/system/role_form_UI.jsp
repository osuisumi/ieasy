<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/system/role/add.do" ;
	$(function() {
		$('#icheck input').iCheck({
			radioClass: 'iradio_square-blue'
	   	});
		
		//编辑，加载表单数据
		if($('input[name=id]').val().length > 0) {
			form_url = $.webapp.root+"/admin/system/role/update.do" ;
			$.post($.webapp.root+"/admin/system/role/get.do", {id:$('input[name=id]').val()}, function(result) {
				if (result.id != undefined) {
					$('#form').form('load', {
						'name' : result.name,
						'sn' : result.sn,
						'remark' : result.remark
					});
					$("input[name=defaultRole][value="+result.defaultRole+"]").iCheck('check') ;
				}
			}, 'json').error(function() { $.easyui.loaded(); });
		} 
	});
	
	
	//提交表单数据
	var submitNow = function($d, $dg, flag) {
		var o = $("#form").form("getData") ;
		if(o.defaultRole == true) {o["defaultRole"] = 1;} 
		else{o["defaultRole"] = 0;}
		$.post(form_url, o, function(result) {
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

<form id="form" class="easyui-form form_container">
	<input type="hidden" name="id" value="${id}" />
	<table class="tableform">
		<tr>
			<th>角色名称：</th>
			<td><input name="name" class="easyui-validatebox" type="text" data-options="required: true" /></td>
		</tr>
		<tr>
			<th>唯一标示：</th>
			<td><input name="sn" class="easyui-validatebox" type="text" data-options="required: true, prompt: '序列号：HZKJ'" /></td>
		</tr>
		<tr>
			<th>默认分配：</th>
			<td>
				<div id="icheck">
					<input id="defaultRole" type="radio" name="defaultRole" value="1">
				</div>
			</td>
		</tr>
		<tr>
			<th>备注：</th>
			<td>
				<textarea name="remark" rows="5" cols="5" style="width:217px;height:70px;max-width:217px;min-width:217px;max-height:70px;min-height:70px;"></textarea>
			</td>
		</tr>
	</table>
</form>
