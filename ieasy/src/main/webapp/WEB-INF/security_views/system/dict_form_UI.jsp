<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var form_url = $.webapp.root + "/admin/system/dict/add.do" ; 
	var parent_box, dict_type ;
	$(function() {
		dict_type = $("#dict_type").combobox({
			valueField: 'label', textField: 'value', value: 'SZX',
			data: [{ label: 'SZX', value: '设置项' },{ label: 'LX', value: '类型' },{ label: 'LB', value: '列表' }],
			panelHeight:'auto', editable:false, autoShowPanel: true,
			onSelect: function(rec) {
            	if(rec.label == "SZX" || rec.label == "LB") {
            		$("#dictCode").validatebox({required: false}) ;
            		$("#dictCode").attr("disabled", true) ;
            	}
            	if(rec.label == "LX") {
            		$("#dictCode").validatebox({required: true}) ;
            		$("#dictCode").attr("disabled", false) ;
            	}
            	if(rec.label == "SZX" || rec.label == "LX") {
            		$("input[name=selected]").attr("disabled", "disabled") ;
            		$("input[name=state]").removeAttr("disabled") ;
            	}
            	if(rec.label == "LB") {
            		$("input[name=selected]").removeAttr("disabled") ;
            		$("input[name=state]").attr("disabled", "disabled") ;
            	}
            }
		}) ;
		
		parent_box = $("#parent_box").combotree({
			url : $.webapp.root+"/static_res/dict.combotree.json",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true, autoShowPanel: false,
			formatter:function(node){
				var type = "" ;
				if(node.type == "SZX"){type="<font color='red'>设置项</font>";}
				else if(node.type == "LX"){type="<font color='green'>类型</font>";}
				else if(node.type == "LB"){type="<font color='green'>列表</font>";}
                return "["+type+"]--"+node.text;
            }
		}) ;
		
		//编辑，加载表单数据
		if($('input[name=id]').val().length > 0) {
			form_url = $.webapp.root + "/admin/system/dict/update.do" ;
			$.post($.webapp.root + "/admin/system/dict/get.do", {id:$('input[name=id]').val()}, function(result) {
				if (result.id != undefined) {
					$('#form').form('load', {
						'id' : result.id,
						'dictName' : result.dictName,
						'dictCode' : result.dictCode,
						'type' : result.type,
						'pid' : result.pid
					});
					$("input[name=selected][value="+result.selected+"]").attr("checked", "checked") ;
					$("input[name=state][value="+result.state+"]").attr("checked", "checked") ;
					if(result.type == "SZX" || result.type == "LB") {
	            		$("#dictCode").validatebox({required: false}) ;
	            		$("#dictCode").attr("disabled", true) ;
	            	}
					if(result.type == "LB") {
						$("input[name=state]").attr("disabled", "disabled") ;
	            		$("input[name=selected]").removeAttr("disabled") ;
	            	}
				}
			}, 'json').error(function(){$.easyui.loaded();});
		} else {
			$("#dictCode").validatebox({required: false}) ;
    		$("#dictCode").attr("disabled", true) ;
		}
	});
	
	//提交表单数据
	var submitNow = function($d, $tg, SZXag) {
		$.post(form_url, $("#form").form("getData"), function(result) {
			if (result.status) {
				$tg.treegrid("reload");$.easyui.loaded(); 
				alertify.success(result.msg);
				if(SZXag==undefined||SZXag==false){$d.dialog("close") ;}
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function(){$.easyui.loaded();});
	};
	
	//验证表单
	var submitForm = function($d, $tg, SZXag) {
		if($('#form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			submitNow($d, $tg, SZXag) ;
		} 
	};
</script>

<form id="form" class="easyui-form form_container">
	<input type="hidden" name="id" value="${id}" />
	<input type="hidden" name="modifyName" value="${USER_SESSION.user.emp_name}" />
	<table class="tableform">
		<tr>
			<th>父字典：</th>
			<td><input id="parent_box" name="pid" style="width:219px;height:25px;" class="easyui-validatebox" type="text" /><a onClick="parent_box.combotree('setValue','');dict_type.combobox('setValue', 'SZX');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
		</tr>
		<tr>
			<th>字典类型：</th>
			<td><input id="dict_type" name="type" style="width:219px;height:25px;" class="easyui-validatebox" type="text" /></td>
		</tr>
		<tr>
			<th>字典名称：</th>
			<td><input name="dictName" class="easyui-validatebox" type="text" data-options="required: true,prompt: '字典名称'" /></td>
		</tr>
		<tr>
			<th>字典编码：</th>
			<td><input id="dictCode" name="dictCode" class="easyui-validatebox" type="text" data-options="prompt: '字典编码：ZDBM'" /></td>
		</tr>
		<tr>
			<th>默认值：</th>
			<td>
				<div id="radio">
					<input type="radio" name="selected" disabled="disabled" value="false" checked /><span>否</span>
					<input type="radio" name="selected" disabled="disabled" value="true" /><span>是</span>
					|&nbsp;&nbsp;
					<input type="radio" name="state" value="open" /><span>展开</span>
					<input type="radio" name="state" value="closed" /><span>折叠</span>
				</div>
			</td>
		</tr>
		<tr>
			<th>备注：</th>
			<td>
				<textarea name="remark" rows="5" cols="5" style="width:289px;height:85px;"></textarea>
			</td>
		</tr>
	</table>
</form>