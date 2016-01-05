<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>系统运行信息</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});
		
		$.post($.webapp.root+"/admin/system/global/getWebAppCtxGlobal.do", function(result) {
			console.info(result) ;
			if (result) {
				$('#form').form('load', {
					'id' : result.id,
					'pass_length1' : result.pass_length1,
					'pass_length2' : result.pass_length2,
					'retry_ban' : result.retry_ban,
					'retry_times' : result.retry_times,
					'ban_time' : result.ban_time,
					'retrieve_mail_pwd' : result.retrieve_mail_pwd
				});
				$("input[name=retry_ban][value="+result.retry_ban+"]").iCheck('check') ;
				$("input[name=retrieve_mail_pwd][value="+result.retrieve_mail_pwd+"]").iCheck('check') ;
				$("input[name=only_user_login][value="+result.only_user_login+"]").iCheck('check') ;
			} else {
				$.easyui.loaded();$.messager.alert("错误", "发生错误！", "error");
			}
		}, 'json').error(function(){$.easyui.loaded();});
		
	});
	function submit() {
		$.easyui.loading({ msg: "数据提交中，请稍等..." });
		$.post($.webapp.root+"/admin/system/global/update.do", $("#form").form("getData"), function(result) {
			if (result.status) {
				alertify.success(result.msg);
				$.easyui.loaded();
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function(){$.easyui.loaded();});
	}
	
</script>
</head> 

<body>

<form id="form" class="easyui-form form_container">
	<input type="hidden" name="id"/>
	<input type="hidden" name="modifyName" value="${USER_SESSION.user.emp_name}" />
	<table class="tableform">
		<tr>
			<td colspan="3" style="height:30px;background: #006699;border:1px solid #006699; color:#fff;font-weight: bold;">系统安全设置</td>
		</tr>
		<tr>
			<td style="text-align:center;height:40px;">密码强度：</td>
			<td style="text-align:left;">
				<table class="noTabBorder">
					<tr>
						<td>
							最少<input name="pass_length1" class="easyui-numberbox" type="text" data-options="value: '2', width: 30" /> 位 ~
							最大<input name="pass_length2" class="easyui-numberbox" type="text" data-options="value: '6', width: 30" />位
						</td>
					</tr>
				</table>
			</td>
			<td style="text-align:left;">设置密码强度，以保证密码的安全性。</td>
		</tr>
		<tr>
			<td style="text-align:center;height:40px;">登录错误次数限制：</td>
			<td style="text-align:left;">
				<div id="icheck">
					<input id="pass_flag1" type="radio" name="retry_ban" value="0" checked="checked" />
					<label for="pass_flag1">是</label>
					<input id="pass_flag2" type="radio" name="retry_ban" value="1"/>
					<label for="pass_flag2">否</label>
					
					登录错误重试<input name="retry_times" style="text-align: center;" class="easyui-numberbox validatebox-text-small" data-options="value: '3', width: 30" />次后，
					<input name="ban_time" style="text-align: center;" class="easyui-numberbox validatebox-text-small" data-options="value: '10', width: 30" maxlength="2" />分钟内禁止再次登录
				</div>
			</td>
			<td style="text-align:left;">如果选择“是”，则登录错误重试数次后会被限制数分钟内不能登录。</td>
		</tr>
		<tr>
			<td style="text-align:center;height:40px;">是否开启密码找回：</td>
			<td style="text-align:left;">
				<div id="icheck">
					<input id="retrieve_mail_pwd1" type="radio" name="retrieve_mail_pwd" value="0" checked="checked" />
					<label for="retrieve_mail_pwd1">是</label>
					<input id="retrieve_mail_pwd2" type="radio" name="retrieve_mail_pwd" value="1"/>
					<label for="retrieve_mail_pwd2">否</label>
				</div>
			</td>
			<td style="text-align:left;">在登录页面中是否显示找回密码链接。</td>
		</tr>
		<tr>
			<td style="text-align:center;height:40px;">一帐号同时登录：</td>
			<td style="text-align:left;">
				<div id="icheck">
					<input id="only_user_login1" type="radio" name="only_user_login" value="0" checked="checked" />
					<label for="only_user_login1">是</label>
					<input id="only_user_login2" type="radio" name="only_user_login" value="1"/>
					<label for="only_user_login2">否</label>
				</div>
			</td>
			<td style="text-align:left;">如果允许同一账号在同一时间允许多人登陆选择是，不允许选择否。</td>
		</tr>
	</table>
	<table class="tableform" style="margin-top:10px;">
		<tr>
			<td colspan="3" style="height:30px;background: #006699;border:1px solid #006699; color:#fff;font-weight: bold;">邮箱信息设置</td>
		</tr>
		<tr>
			<td style="text-align:center;height:40px;">邮件服务：</td>
			<td style="text-align:left;">
				<table class="noTabBorder">
					<tr>
						<td>IP地址：</td><td><input name="title" class="easyui-validatebox" type="text" /></td>
					</tr>
					<tr>
						<td>端口：</td><td><input name="title" class="easyui-validatebox" type="text" /></td>
					</tr>
					<tr>
						<td>邮件收取间隔：</td><td><input name="title" class="easyui-validatebox" type="text" /></td>
					</tr>
				</table>
			</td>
			<td style="text-align:left;">
				Web服务通过该地址和端口连接邮件服务，如果设置不正确，会导致不能发送外部邮件。修改端口后需重启服务才会生效。<br/>
				自动收取外部邮件间隔建议10-60分钟，最短不能小于3分钟，考虑收取外部邮件对服务器带宽的占用，建议超过2-5MB的邮件不自动收取。
			</td>
		</tr>
	</table>
	
	<table style="width:100%;">
		<tr>
			<td colspan="3" style="text-align: center;height: 50px;">
				<a onClick="submit();" class="easyui-linkbutton" data-options="plain: false, iconCls: 'ext_save'">修改系统全局参数</a>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
