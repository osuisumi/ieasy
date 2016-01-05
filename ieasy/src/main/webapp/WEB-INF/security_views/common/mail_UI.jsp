<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<script type="text/javascript">
	var form_url = $.webapp.root+"/common/mail/doNotNeedAuth_sendMail.do" ;
	var attach = $("#attach"), u1 ;
	var simple = [[
	       		'fontset', '|', 'forecolor', 'backcolor',
	       		'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify','|', 
	       		'emotion', 'scrawl', '|', 'snapscreen', 'insertimage', 'attachment', '|', 'source', 'preview', 'fullscreen'
	       	]];
	
	$(function() {
		$("input[name=from]").val($.webapp.emp_email) ;
		u1 = new UE.ui.Editor({ toolbars : simple });
		u1.render('u1');
		
		var u = $("#attchaments").euploadify({
			required: false, border: true, width: 625,
		 	swf: $.webapp.root+'/script/plugins/uploadify/uploadify.swf',
		 	uploader: $.webapp.root+'/fileAction/doNotNeedAuth_Upload.do?uploadDir=/mail_attach',
		 	formData: {newName: 'orginalName'},
		 	onSelect: function() {
		 		if(attach.find(".attach1").length>=6) {
		 			alert("附件只允许上传6个，多个附件请压缩后上传。") ;
		 			u.euploadify("cancel", "*") ;
		 		}
		 	},
            onUploadSuccess: function(file, data, response) {
            	var o = $.parseJSON(data) ;
            	if(o.status) {
            		var at = '<div class="attach1">'+
		        			 '<span class="span1" name="'+o.savePath+'">'+o.newName+'</span>'+
		        			 '<span class="span2" name="'+o.uploadDir+o.newName+'">×</span>'+
		        		  	 '</div>';
            		attach.append(at);
            	}
            }
		});
		
		//删除附件
		$("#attach").on("click", ".span2", function() {
			var at = $(this) ;
			$.post($.webapp.root+"/common/mail/doNotNeedAuth_deleteAttach.do", {attachPath: at.attr("name")}, function(result) {
				if (result.status) {
					at.parent(".attach1").remove() ;
				} else {
					$.messager.alert("错误", "删除附件失败！", "error");
				}
			}, 'json').error(function() {});
			
		}) ;
	});
	
	//提交表单数据
	var submitNow = function($d, $dg, flag) {
		var o = $("#form").form("getData") ;
		var attachs = [] ;
		if(attach.find(".attach1").length > 0) {
			attach.find(".span1").each(function(){
				attachs.push($(this).attr("name"));
			});
			o["attachments"] = attachs.join(",") ;
		}
		$.post(form_url, o, function(result) {
			if (result.status) {
				alertify.success(result.msg);$.easyui.loaded();
				//if(flag==undefined||flag==false){$d.dialog("close") ;}
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	};
	
	//验证表单
	var submitForm = function($d, $dg, flag) { 
		if($('#form').form('validate')) {
			$.easyui.loading({ msg: "邮件发送中，请稍等..." });
			submitNow($d, $dg, flag) ;
		}
	};
	
	
</script>
<style>
.euploadify-wrapper-single .euploadify-progressbar{margin:0px;}
.euploadify-wrapper-single .euploadify-buttonbar {margin:0px;}
</style>
<form id="form" class="easyui-form form_container">
	<input type="hidden" name="from" />
	<table class="tableform">
		<tr>
			<th style="width:70px;">收件人：</th> 
			<td>
				<textarea name="to" style="width:618px;height:65px;overflow-y: auto;" class="easyui-validatebox" data-options="required: true, prompt: '多个收件人使用英文逗号“,”隔开'"></textarea>
			</td>
		</tr>
		<tr>
			<th>抄送人：</th>
			<td><textarea name="cc" style="width:618px;height:65px;overflow-y: auto;" class="easyui-validatebox"></textarea></td>
		</tr>
		<tr>
			<th>暗送人：</th>
			<td><input name="bcc" style="width:618px;" class="easyui-validatebox" type="text" /></td>
		</tr>
		<tr>
			<th>标题：</th>
			<td><input name="subject" style="width:618px;" class="easyui-validatebox" type="text" /></td>
		</tr>
		<tr>
			<th>附件：</th>
			<td><div id="attchaments"></div></td>
		</tr>
		<tr>
			<td colspan="2">
				<textarea id="u1" name="ctx" style="width:703px;height:300px;"></textarea>
			</td>
		</tr>
	</table>
	
	<div id="attach" class="attach"></div>
</form>
