<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var form_url = $.webapp.root + "/admin/system/org/add.do" ; 
	var parent_box, org_type ;
	$(function() {
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});
		
		parent_box = $("#parent_box").combotree({
			url : $.webapp.root+"/static_res/org.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true, autoShowPanel: false,
			formatter:function(node){
				var type = "" ;
				if(node.type == "O"){type="<font color='red'>公司</font>";}
				else if(node.type == "D"){type="<font color='green'>部门</font>";}
                return "["+type+"]--"+node.text;
            }
		}) ;
		org_type = $("#org_type").combobox({
			valueField: 'label', textField: 'value', value: 'O',
			data: [{ label: 'O', value: '公司' },{ label: 'D', value: '部门' }],
			panelHeight:'auto', editable:false, autoShowPanel: true
		}) ;
		
		//编辑，加载表单数据
		if($('input[name=id]').val().length > 0) {
			form_url = $.webapp.root + "/admin/system/org/update.do" ;
			$.post($.webapp.root + "/admin/system/org/get.do", {id:$('input[name=id]').val()}, function(result) {
				if (result.id != undefined) {
					$('#form').form('load', {
						'id' : result.id,
						'name' : result.name,
						'sn' : result.sn,
						'type' : result.type,
						'iconCls' : result.iconCls,
						'pid' : result.pid
					});
					$("input[name=state][value="+result.state+"]").iCheck('check') ;
					if(result.sumJdl) {
						$("input[name=sumJdl]").iCheck('check') ;
					}
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
			<th>组织名称：</th>
			<td><input name="name" class="easyui-validatebox i300 i300" type="text" data-options="required: true,prompt: '组织名称'" /></td>
		</tr>
		<tr>
			<th>唯一标示：</th>
			<td><input name="sn" class="easyui-validatebox i300" type="text" data-options="required: true,prompt: '序列号：HZKJ'" /></td>
		</tr>
		<tr>
			<th>父组织：</th>
			<td><input id="parent_box" name="pid" style="width:309px;height:25px;" class="easyui-validatebox" type="text" /><a onClick="parent_box.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
		</tr>
		<tr>
			<th>组织类型：</th>
			<td><input id="org_type" name="type" style="width:219px;height:25px;" class="easyui-validatebox" type="text" /><a onClick="org_type.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
		</tr>
		<tr>
			<th>图标：</th>
			<td><input name="iconCls" class="easyui-comboicons" data-options="autoShowPanel: false, multiple: false, size: '16', value: 'icon-standard-application-view-list'" style="width:219px;height:25px;" /></td>
		</tr>
		<tr>
			<th>其他：</th>
			<td>
				<div id="icheck">
					<input id="status1" type="radio" name="state" value="open" checked="checked">
					<label class="irl" for="status1">展开</label>
					<input id="status2" type="radio" name="state" value="closed">
					<label class="irl" for="status2">折叠</label>
					
					<input id="access1" type="checkbox" name="sumJdl" value="false">
					<label class="irl" for="access1">计算稼动率</label>
				</div>
			</td>
		</tr>
	</table>
</form>