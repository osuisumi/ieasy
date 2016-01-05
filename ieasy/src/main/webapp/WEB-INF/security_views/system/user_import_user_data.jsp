<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var dg_import ;	
	$(function() {
		dg_import = $("#import_data_dg").datagrid({
			fit: true, border: false, rownumbers: true,
			columns: [[
			    { field: 'num', title: '编号', width: 100 },
			    { field: 'name', title: '姓名', width: 100 },
			    { field: 'account', title: '登录账号', width: 100 }, 
			    { field: 'password', title: '&nbsp;登录密码', width: 80 },
			    { field: 'sex', title: '&nbsp;性别', width: 50, align: 'center' },
			    { field: 'mobile', title: '&nbsp;手机号码', width: 100 },
			    { field: 'email', title: '&nbsp;邮件地址', width: 150 },
			    { field: 'state', title: '状态', width: 50, align: 'center', sortable: true, formatter:function(value,row){
			    	if(value){return "<font color='green'>成功</font>";}else {return "<font color='red'>失败</font>";}
			    }},
			    { field: 'msg', title: '提示信息', width: 150, sortable: true } 
			]]
		});
		
		$("#import_user_data").euploadify({
			required: true, border: false, fileTypeExts : '*.xls; *.xlsx',
		 	swf: $.webapp.root+'/script/plugins/uploadify/uploadify.swf',
		 	uploader: $.webapp.root+'/fileAction/doNotNeedAuth_Upload.do',
		 	formData: {newName: '用户账号基本信息'},
		 	onUploadStart : function(file) {
		 		$.easyui.loading({ msg: "文件正在上传...", locale: "#uploadLayout" }) ;
	        },
            onUploadSuccess: function(file, data, response) {
            	var o = $.parseJSON(data) ;
            	if(o.status) {
            		$.easyui.loading({ msg: "正在解析数据，并导入数据库...", locale: "#layout_dg" }) ;
            		$.post($.webapp.root+'/admin/system/user/doNotNeedAuth_parseExcelData.do', {"datafile": o.savePath}, function(result) {
            			if (result.status) {
            				dg_import.datagrid("loadData", {"rows":result.obj});
            				$.easyui.loaded("#layout_dg", true);
            			} else {
            				$.messager.alert(result.msg); $.easyui.loaded("#layout_dg", true);
            			}
            		}, 'json').error(function() { $.easyui.loaded("#layout_dg", true); });
            	}
            },
            onUploadError: function(file, errorCode, errorMsg, errorString){
            	$.easyui.loaded("#uploadLayout", true);
            }
		});
	});
</script>
<div id="uploadLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north', border:false" style="overflow: hidden;">
		<div id="import_user_data" style="border:1px solid red;"></div>
	</div>
	<div id="layout_dg" data-options="region:'center', border:false" style="border-top:1px solid #ccc;">
		<div id="import_data_dg"></div>
	</div>
</div>

