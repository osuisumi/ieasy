<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var form_url = $.webapp.root + "/admin/system/menu/add.do" ; 
	var type_box, parent_box ;
	$(function() {
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});
		type_box = $("#type").combobox({
			valueField: 'label', textField: 'value', value: 'T',
			data: [{ label: 'T', value: '导航栏目' },{ label: 'M', value: '导航菜单' },{ label: 'O', value: '菜单操作' }],
			panelHeight:'auto', editable:false, autoShowPanel: true,
			onSelect: function(node){
				if("T" == node.label) {
					$.messager.alert("错误", "只能有一个根导航栏目！<br>请创建[导航菜单]/[菜单操作]", "error");
					return ;
					parent_box.combotree({ value: '', disabled: true }) ;
					$("#href").validatebox({required: false});
					$("#href").attr("disabled",true) ;
				} else if("M" == node.label) {
					parent_box.combotree({disabled: false, required:true, autoShowPanel: true }) ;
					$("#href").validatebox({required: false});
					$("#href").attr("disabled",false) ;
				} else if("O" == node.label) {
					parent_box.combotree({ disabled: false, required:true, autoShowPanel: true }) ;
					$("#href").validatebox({required: true});
					$("#href").attr("disabled",false) ;
				}
			}
		}) ;
		parent_box = $("#parent").combotree({
			url : $.webapp.root + "/admin/system/menu/combo_tree.do",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true, autoShowPanel: false
		}) ;
		
		//编辑，加载表单数据
		if($('input[name=id]').val().length > 0) {
			form_url = $.webapp.root + "/admin/system/menu/update.do" ;
			$.post($.webapp.root + "/admin/system/menu/get.do", {id:$('input[name=id]').val()}, function(result) {
				if (result.id != undefined) {
					$('form').form('load', {
						'id' : result.id,
						'name' : result.name,
						'href' : result.href,
						'sort' : result.sort,
						'type' : result.type,
						'iconCls' : result.iconCls,
						'pid' : result.pid,
						'remark' : result.remark
					});
					
					$("input[name=isShow][value="+result.isShow+"]").iCheck('check') ;
					$("input[name=state][value="+result.state+"]").iCheck('check') ;
					
					if("T" == result.type) {
						type_box.combobox("loadData", [{ label: 'T', value: '导航栏目' }]);
						parent_box.combotree({ value: '', disabled: true }) ;
						$("#href").validatebox({required: false});
						$("#href").attr("disabled",true) ;
					} else if("M" == result.type) {
						type_box.combobox("loadData", [{ label: 'M', value: '导航菜单' },{ label: 'O', value: '菜单操作' }]);
						parent_box.combotree({disabled: false, required:true, autoShowPanel: true, value: result.pid }) ;
						$("#href").validatebox({required: false});
						$("#href").attr("disabled",false) ;
					} else if("O" == result.type) {
						type_box.combobox("loadData", [{ label: 'M', value: '导航菜单' },{ label: 'O', value: '菜单操作' }]);
						parent_box.combotree({ disabled: false, required:true, autoShowPanel: true, value: result.pid }) ;
						$("#href").validatebox({required: true});
						$("#href").attr("disabled",false) ;
					}
				}
			}, 'json').error(function(){$.easyui.loaded();});
		} else {
			type_box.combobox("loadData", [{ label: 'M', value: '导航菜单' },{ label: 'O', value: '菜单操作' }]);
			type_box.combobox("setValue", "M") ;
			parent_box.combotree({ disabled: true, required:false }) ;
			$("#href").validatebox({required: false});
			$("#href").attr("disabled",true) ;
		}
	});
	
	//提交表单数据
	var submitNow = function($d, $tg, flag) {
		if(form_url == $.webapp.root + "/admin/system/menu/add.do") {
			if(type_box.combobox('getValue') == "T") {
				$.easyui.loaded();
				$.messager.alert("错误", "只能有一个根导航栏目！<br>请创建[导航菜单]/[菜单操作]", "error");
				return ;
			}
		}
		
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
			<th>菜单名称：</th>
			<td><input name="name" class="easyui-validatebox" type="text" data-options="required: true, prompt: '菜单名称'" /></td>
			<th>菜单类型：</th>
			<td><input id="type" name="type" style="width:219px;height:25px;"></input></td>
		</tr>
		<tr>
			<th>URL地址：</th>
			<td colspan="3"><input id="href" name="href" class="easyui-validatebox" style="width:580px;" type="text" data-options="required: false, prompt: '菜单地址'" /></td>
		</tr>
		<tr>
			<th>父菜单：</th>
			<td><input id="parent" name="pid" style="width:219px;height:25px;"></input><a onClick="parent_box.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
			<th>图标：</th>
			<td><input name="iconCls" class="easyui-comboicons" data-options="autoShowPanel: false, multiple: false, size: '16', value: 'icon-standard-application-view-list'" style="width:219px;height:25px;" /></td>
		</tr>
		<tr>
			<th>其他：</th>
			<td colspan="3">
				<div id="icheck">
					<input id="isShow1" type="radio" name="isShow" value="1" checked>&nbsp;<label for="isShow1">显示</label>
					<input id="isShow2" type="radio" name="isShow" value="0">&nbsp;<label for="isShow2">隐藏</label>
					
					<input id="state1" type="radio" name="state" value="open" checked>&nbsp;<label for="state1">展开</label>
					<input id="state2" type="radio" name="state" value="closed">&nbsp;<label for="state2">折叠</label>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<textarea name="remark" rows="5" cols="5" style="width:689px;height:85px;max-width:689px;min-width:689px;max-height:85px;min-height:85px;"></textarea>
			</td>
		</tr>
	</table>
</form>