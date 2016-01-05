<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/system/person/doNotNeedAuth_batchChangeOrg.do" ;
	var org ;
	$(function() {
		//$("input[name=ids]").val($("iframe").contents().find("#ids_seri").val()) ;
		//console.info($("iframe").contents().find("#ids_seri").val()) ;
		//console.info($("iframe")) ;
		org = $("#org").combotree({
			url : $.webapp.root+"/static_res/org.tree.json",
			editable: false, lines:true,
			onLoadSuccess: function(node, data) {
				if(data && data.length>0){
					org.combotree("setValue", data[0].id) ; 
				}
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
	<input type="hidden" name="ids" id="ids"/>
	<table class="tableform">
		<tr>
			<th>组织机构：</th>
			<td><input id="org" name="org_id" style="width:218px;height:30px;" /><a onClick="org.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
		</tr>
	</table>
</form>
