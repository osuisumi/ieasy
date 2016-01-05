<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/system/myinfo/doNotNeedAuth_updateMyInfo.do" ;
	var tabsContainer, org, position ;
	var comboxDef = {panelHeight: "auto"} ;
	$(function() {
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});
		
		$("#emp_tx_view").attr("class", "imgNO") ;
		$("#emp_tx_view").attr("src", $.webapp.root+"/images/sys_img/person.png") ;
		
		tabsContainer = $("#tabsContainer").tabs({
			fit: true, border: false
		}) ;
		
		org = $("#org").combotree({
			url : $.webapp.root+"/static_res/org.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true, autoShowPanel: false
		});
		
		position = $("#position").combotree({
			url : $.webapp.root+"/static_res/position.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true
		});
		
		var data = {"dictCode": "XB,HYZK,ZZMM,ZC,JKZK,ZJLX,HKXZ,ZCDJ,YGZT,YGLX,MZ"} ;
		$.post($.webapp.root+"/admin/system/dict/doNotNeedSession_dictAttrMaps.do", data, function(result) {
			$.each(result, function(i, p) {
				$("#"+i).combobox({valueField: 'text', textField: 'text', panelHeight: 'auto', data: p}) ;
			});
		}, 'json');
		
		$("#emp_tx_view").attr("class", "imgDef") ;
		$("#emp_tx_view").attr("src", $.webapp.root+"/images/sys_img/loading6.gif") ;
		$.post($.webapp.root+"/admin/system/myinfo/get_my_info.do", {user_id:$.webapp.user_id}, function(result) {
			if (result.id != undefined) {
				//头像预加载
				if(undefined == result.txPicPath || "" == result.txPicPath) {
					$("#emp_tx_view").attr("class", "imgNO") ;
					loadImg("#emp_tx_view", $.webapp.root+"/images/sys_img/person.png") ;
				} else {
					$("#emp_tx_view").attr("class", "img") ;
					loadImg("#emp_tx_view", $.webapp.root+"/"+result.txPicPath.substring(1).replaceAll("\\","/")) ;
				}
				$('#basic_info_form').form('load', result);
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	});
	
	function openTouXiangUpload() {
		var $dc = $.easyui.showDialog({
			href: $.webapp.root + "/admin/system/person/person_touxiang_UI.do", title: "表单", iniframe: false, topMost: true,
			width: 700, height: 550, maximizable: true,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '完成', iconCls : 'ext_save', handler : function() { finishPic(); $dc.dialog('destroy'); } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $dc.dialog('destroy'); } } 
           	]
        });
	}
	
	function deleteTx() {
		$.messager.confirm("该操作不可逆，您确定要进行该操作？", function (c) { 
			if(c) {
				$('input[name=txPicPath]').val('') ;
				$("#emp_tx_view").attr("class", "imgNO") ;
				$("#emp_tx_view").attr("src", $.webapp.root+"/images/sys_img/person.png") ;
			} else {$.easyui.loaded("#westCenterLayout", true);}
		});
	}
	
	
	//预加载图片
	function loadImg(id, imgPath) {
		var temp_img = new Image();
    	$(temp_img).bind('load', function () {
    		$(id).attr("src", temp_img.src) ;
		});
    	$(temp_img).bind('error', function () {
    		$(id).attr("class", "img").attr("alt", "图片加载失败") ;
    		$(id).attr("src", temp_img.src) ;
		});
    	temp_img.src = imgPath ;
	}
	//提交表单数据
	var submitNow = function($d, flag) {
		var o = $.webapp.serializeObject("#basic_info_form") ;
		console.info(o) ;
		$.post(form_url, o, function(result) {
			if (result.status) {
				$.messager.alert("提示", result.msg, "info");$.easyui.loaded();
				if(flag==undefined||flag==false){$d.dialog("close") ;}
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	};
	
	//验证表单
	var submitForm = function($d, flag) { 
		if($('#form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			submitNow($d, flag) ;
		}
	};
	
</script>

<div class="easyui-layout" data-options="fit: true">
	
	<div data-options="region: 'center', border: false" style="overflow: hidden;">
		<div id="tabsContainer">
			<div class="panel-container" data-options="title: '基本信息', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
				<form id="basic_info_form">
					<div class="form_container">
						<input type="hidden" name="id" />
						<input type="hidden" name="modifyName" value="${USER_SESSION.user.emp_name}" />
						<table class="tableform">
							<tr>
								<th>编号：</th>
								<td><input name="num" class="easyui-validatebox" type="text" /></td>
								<th>姓名：</th>
								<td><input name="name" class="easyui-validatebox" type="text" /></td>
								
								<td rowspan="6" style="width:170px;" valign="top">
									<input type="hidden" name="txPicPath">
									<p id="emp_pic"><img id="emp_tx_view" src=""></p>
									<p style="margin:0 auto ;text-align: center;">
										<a onClick="deleteTx();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'"></a>
										<a onClick="openTouXiangUpload();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-pictures'">选择照片</a>
									</p>
								</td>
							</tr>
							<tr>
								<th>性别：</th>
								<td>
									<input id="XB" name="sex" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
								<th>民族：</th>
								<td>
									<select class="easyui-combobox" name="stirps" data-options="panelHeight:'auto', editable:false" style="width:219px;height:25px;">
										<option value="汉族">汉族</option>
	       								<option value="藏族">藏族</option>
									</select>
								</td>
							</tr>
							<tr>
								<th>工资卡号：</th>
								<td><input name="salNum" class="easyui-validatebox" type="text" /></td>
								<th>社保号：</th>
								<td><input name="ssNum" class="easyui-validatebox" type="text" /></td>
							</tr>
							<tr>
								<th>国籍：</th>
								<td><input name="country" class="easyui-validatebox" type="text" /></td> 
								<th>籍贯：</th>
								<td>
									<select class="easyui-combobox" name="place" data-options="panelHeight:'auto', editable:false" style="width:219px;height:25px;">
										<option value="广东">广东</option>
	       								<option value="浙江">浙江</option>
									</select>
								</td>
							</tr>
							<tr>
								<th>出生日期：</th>
								<td>
									<input name="birth" style="width:219px;height:25px;" class="easyui-my97" data-options="isShowClear:true, maxDate:new Date()" />
								</td>
								<th>婚姻状况：</th>
								<td>
									<input id="HYZK" name="marriage" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
							</tr>
							<tr>
								<th>政治面貌：</th>
								<td>
									<input id="ZZMM" name="political" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
								<th>健康状况：</th>
								<td>
									<input id="JKZK" name="health" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
							</tr>
							<tr>
								<th>毕业时间：</th>
								<td><input name="bysj" style="width:219px;height:25px;" class="easyui-my97" data-options="isShowClear:true, maxDate:new Date()" /></td> 
								<th>证件类型：</th>
								<td colspan="3">
									<input id="ZJLX" name="zjlx" type="text" class="easyui-combobox" style="width:100px;height:25px;"/>
									<input name="zjhm" class="easyui-validatebox" style="width:296px;" type="text" />
								</td>
							</tr>
							<tr>
								<th>最高学位：</th>
								<td><input name="highest" class="easyui-validatebox" type="text" /></td>
								<th>专业：</th>
								<td colspan="3"><input name="profession" class="easyui-validatebox i400" type="text" /></td>
							</tr>
							<tr>
								<th>职称：</th>
								<td>
									<input id="ZC" name="posTitle" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
								<th>毕业院校：</th>
								<td colspan="3"><input name="school" class="easyui-validatebox i400" type="text" /></td>
							</tr>
							<tr>
								<th>参加工作日期：</th>
								<td><input name="cjgzrq" style="width:219px;height:25px;" class="easyui-my97" data-options="isShowClear:true, maxDate:new Date()" /></td>
								<th>空闲工龄：</th>
								<td colspan="3"><input name="kxgl" class="easyui-validatebox i400" type="text" /></td>
							</tr>
							<tr>
								<th>身高：</th>
								<td><input name="height" class="easyui-numberbox" style="width:219px;" data-options="prompt: '单位：厘米'" type="text" /></td>
								<th>体重：</th>
								<td colspan="3"><input name="weight" class="easyui-numberbox" style="width:408px;" data-options="prompt: '单位：公斤'" type="text" /></td>
							</tr>
							<tr>
								<th>户口性质：</th>
								<td>
									<input id="HKXZ" name="hkxz" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
								<th>户口地址：</th>
								<td colspan="3"><input name="hkdz" class="easyui-validatebox i400" type="text" /></td>
							</tr>
						</table>
						
						<table class="tableform" style="margin-top:10px;">
							<tr>
								<th>员工状态：</th>
								<td>
									<input id="YGZT" disabled="disabled" name="empState" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
								<th>员工类型：</th>
								<td>
									<input id="YGLX" disabled="disabled" name="empType" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
								<th>职称等级：</th>
								<td>
									<input id="ZCDJ" disabled="disabled" name="empLevel" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
							</tr>
							<tr>
								<th>入职时间：</th>
								<td><input name="enterDate" disabled="disabled" style="width:219px;height:25px;" class="easyui-my97" data-options="isShowClear:false, maxDate:new Date()" /></td>
								<th>转正日期：</th>
								<td><input name="changeDate" disabled="disabled" style="width:219px;height:25px;" class="easyui-my97" data-options="isShowClear:false, maxDate:new Date()" /></td>
								<th>离职日期：</th>
								<td><input name="dimissionDate" disabled="disabled" style="width:219px;height:25px;" class="easyui-my97" data-options="isShowClear:false, maxDate:new Date()" /></td>
							</tr>
							<tr>
								<th>机构部门：</th>
								<td colspan="3"><input id="org" name="org_id" disabled="disabled" style="width:530px;height:25px;" /></td>
								<th>分配岗位：</th> 
								<td><input id="position" name="position_id" disabled="disabled" style="width:219px;height:25px;" /></td>
							</tr>
						</table>
						
						<table class="tableform" style="margin-top:10px;">
							<tr>
								<th>手机号码：</th>
								<td><input name="mobile" class="easyui-numberbox" style="width:219px;" type="text" /></td>
								<th>住宅号码：</th>
								<td><input name="phone" class="easyui-numberbox" style="width:219px;" type="text" /></td>
								<th>电子邮件：</th>
								<td><input name="email" class="easyui-validatebox" type="text" /></td>
							</tr>
							<tr>
								<th>联系地址：</th>
								<td colspan="7">
									<input name="address" class="easyui-validatebox" style="width:770px;" type="text" />&nbsp;
									<input name="zipCode" style="width:58px;" class="easyui-validatebox" data-options="prompt: '邮编'" type="text" />
								</td>
							</tr>
						</table>
					</div>
				</form>
			</div>
			
			
		    <div id="code" class="panel-container" data-options="title: '外语水平', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '资格资历', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '工作经历', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '社会关系', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '健康信息', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '奖惩信息', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '任职信息', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '调动信息', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '面谈管理', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '酬薪定级', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '调薪记录', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '培训经历', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '考核成绩', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '工伤管理', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '社保公积金', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '合同信息', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		    <div id="code" class="panel-container" data-options="title: '薪资信息', iconCls: 'icon-standard-layout-header', refreshable: false">
		    </div>
		</div>
	</div>

</div>


