<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/system/notice/add.do" ;
	var simple = [[
		       		'fontset', '|', 'forecolor', 'backcolor',
		       		'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify','|', 
		       		'emotion', 'scrawl', '|', 'snapscreen', 'insertimage', 'attachment', '|', 'source', 'preview'
		       	]];
	
	$(function() {
		u1 = new UE.ui.Editor({ toolbars : simple });
		u1.render('u1');
		u1.addListener('ready', function(e) {
			if($('input[name=id]').val().length > 0) {
				form_url = $.webapp.root+"/admin/system/notice/update.do" ;
				$.post($.webapp.root+"/admin/system/notice/get.do", {id:$('input[name=id]').val()}, function(result) {
					$('form').form('load', {
						'id' : result.id,
						'title' : result.title
					});
					u1.setContent(result.content);
				}, 'json').error(function() { $.easyui.loaded(); });
			} 
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

<form id="form" class="easyui-form form_container">
	<input type="hidden" name="id" value="${id}" />
	<input type="hidden" name="modifyName" value="${USER_SESSION.user.emp_name}" />
	<input type="hidden" name="type" value="系统公告" />
	<table class="tableform">
		<tr>
			<th>公告标题：</th>
			<td><input name="title" class="easyui-validatebox" style="width:559px;" type="text" /></td>
		</tr>
		<tr>
			<th>公告内容：</th>
			<td><textarea id="u1" name="content" style="width:565px;height:420px;"></textarea></td>
		</tr>
	</table>
</form>
