<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var form_url = $.webapp.root + "/admin/system/position/add.do" ; 
	var parent_box ;
	$(function() {
		parent_box = $("#parent").combotree({
			url : $.webapp.root + "/static_res/position.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true, autoShowPanel: false
		}) ;
		
		//编辑，加载表单数据
		if($('input[name=id]').val().length > 0) {
			form_url = $.webapp.root + "/admin/system/position/update.do" ;
			$.post($.webapp.root + "/admin/system/position/get.do", {id:$('input[name=id]').val()}, function(result) {
				if (result.id != undefined) {
					$('#form').form('load', {
						'id' : result.id,
						'name' : result.name,
						'sort' : result.sort,
						'sn' : result.sn,
						'pid' : result.pid
					});
				}
			}, 'json').error(function(){$.easyui.loaded();});
		} 
	});
	
	//提交表单数据
	var submitNow = function($d, $tg, flag) {
		$.post(form_url, $("#form").form("getData"), function(result) {
			if (result.status) {
				$tg.treegrid("reload");$.easyui.loaded(); 
				alertify.success(result.msg);
				if(flag==undefined||flag==false){$d.dialog("close") ;}
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function(){$.easyui.loaded();});
	};
	
	//验证表单
	var submitForm = function($d, $tg, flag) {
		if($('#form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			submitNow($d, $tg, flag) ;
		} 
	};
</script>

<form id="form" class="easyui-form form_container">
	<input type="hidden" name="id" value="${id}" />
	<input type="hidden" name="modifyName" value="${USER_SESSION.user.emp_name}" />
	<table class="tableform">
		<tr>
			<th>职位名称：</th>
			<td><input name="name" class="easyui-validatebox" type="text" data-options="required: true, prompt: '职位名称'" /></td>
		</tr>
		<tr>
			<th>唯一标示：</th>
			<td><input name="sn" class="easyui-validatebox" type="text" data-options="required: true, prompt: '序列号：HZKJ'" /></td>
		</tr>
		<tr>
			<th>父菜单：</th>
			<td><input id="parent" name="pid" style="width:219px;height:30px;"></input><a onClick="parent_box.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
		</tr>
		<tr>
			<th>备注：</th>
			<td>
				<textarea name="remark" rows="5" cols="5" style="width:300px;height:65px;max-width:300px;min-width:300px;max-height:65px;min-height:65px;"></textarea>
			</td>
		</tr>
	</table>
</form>