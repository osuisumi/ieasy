<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/system/user/doNotNeedAuth_batchLockAccount.do" ;
	$(function() {
		$("#lockAccount a").click(function() {
			$("#lockAccount a").removeClass("select") ;
			$(this).addClass("select");
			$("input[name=status]").val($(this).attr("lang")) ;
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

<div id="lockAccount">
	<a href="javascript:;" class="select" lang="1">锁定</a>
	<a href="javascript:;" lang="0">解锁</a>
</div>

<form id="form" class="easyui-form form_container" style="display: none;">
	<input type="hidden" name="ids" id="ids"/>
	<input type="hidden" name="status" value="1" />
</form>
