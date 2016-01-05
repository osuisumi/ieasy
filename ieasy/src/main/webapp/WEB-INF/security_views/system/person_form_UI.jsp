<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/system/person/add.do" ;
	var tabsContainer, org, position, $person_select_dg ;
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
		
		if($('input[name=id]').val().length > 0) {
			loadData($('input[name=id]').val()) ;
			view_person_datagrid() ;
		}  else {
			selectField() ;
		}
	});
	
	function loadData(personId) {
		selectField() ;
		
		$.easyui.loading({ msg: "数据加载中，请稍等...", locale: "#contentPanel" }) ;
		$("#emp_tx_view").attr("class", "imgDef") ;
		$("#emp_tx_view").attr("src", $.webapp.root+"/images/sys_img/loading6.gif") ;
		form_url = $.webapp.root+"/admin/system/person/update.do" ;
		$.post($.webapp.root+"/admin/system/person/get.do", {id: personId}, function(result) {
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
				$('#jdl_form').form('load', {
					'dbmType' : result.dbmType,
					'dbmDate' : result.dbmDate,
					'lbmType' : result.lbmType,
					'lbmDate' : result.lbmDate
				});
			}
		}, 'json').error(function() { $.easyui.loaded(); });
		$.easyui.loaded("#contentPanel", true);
	}
	
	function selectField() {
		org = $("#org").combotree({
			url : $.webapp.root+"/static_res/org.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true, autoShowPanel: false
		});
		
		position = $("#position").combotree({
			url : $.webapp.root+"/static_res/position.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, required:false, lines:true
		});
		
		var data = {"dictCode": "XB,HYZK,ZZMM,ZC,JKZK,ZJLX,HKXZ,ZCDJ,YGZT,YGLX,JDL_DBMTYPE,JDL_LBMTYPE,MZ"} ;
		$.post($.webapp.root+"/admin/system/dict/doNotNeedSession_dictAttrMaps.do", data, function(result) {
			$.each(result, function(i, p) {
				if("YGZT" == i || "JDL-LBMTYPE" == i) {
					return ;
				}
				$("#"+i).combobox({valueField: 'text', textField: 'text', panelHeight: 'auto', data: p}) ;
			});
			$("#YGZT").combobox({
				valueField: 'text', textField: 'text', panelHeight: 'auto', data: result.YGZT,
				onSelect: function(record) {
					if("离职" == record.text) {
						$("#JDL_LBMTYPE").combobox("setValue", record.text);
					} else if("在职" == record.text) {
						$("#JDL_LBMTYPE").combobox("setValue", "");
						$("#lbmDate").val("");
					}
				}
			}) ;
			
			$("#JDL_LBMTYPE").combobox({
				valueField: 'text', textField: 'text', panelHeight: 'auto', data: result.JDL_LBMTYPE	,
				onSelect: function(record) {
					if("离职" == record.text) {
						$("#YGZT").combobox("setValue", record.text);
					}
				}
			}) ;
		}, 'json');
	}
	
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
	var submitNow = function($d, $dg, flag) {
		var o = $.webapp.serializeObject("#basic_info_form") ;
		var jdl_form = $.webapp.serializeObject("#jdl_form") ;
		$.each(jdl_form, function(n, v){
			o[n] = v ;
		});
		$.post(form_url, o, function(result) {
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
	
	
	function view_person_datagrid() {
		$person_select_dg = $("#person_select_dg").datagrid({
			url: $.webapp.root+"/admin/system/person/datagrid.do",
			idField: 'id', fit: true, border: false, pageSize: 24, pageList: [24], rownumbers: true,
			remoteSort: true, toolbar: '#psd_search_bar', striped:true, pagination: true, singleSelect: true,
			sortName: 'num', sortOrder: 'asc',
			columns: [[
				{ field: 'id', title: 'ID', width: 80, hidden: true },
				{ field: 'num', title: '编号', width: 60 },
				{ field: 'name', title: '编号', width: 90 }
			]],
			onClickRow: function(rowIndex, rowData) {
				loadData(rowData.id) ;
			}
		});
		
		$person_select_dg.datagrid("getPager").pagination({layout:['first','prev','next','last'], displayMsg: ""});
	}
	function sp_doSearch(value,name){
		var o = {} ; o[name] = value ; $person_select_dg.datagrid("load",o);
	}
	
</script>

<div class="easyui-layout" data-options="fit: true">
	
	<div id="contentPanel" data-options="region: 'center', border: true" style="border-left:0px;border-top:0px;border-bottom:0px;overflow: hidden;">
		<div id="tabsContainer">
			<div class="panel-container" data-options="title: '基本信息', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
				<form id="basic_info_form">
					<div class="form_container">
						<input type="hidden" name="id" value="${id}" />
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
							<tr>
								<th>日语级别：</th>
								<td colspan="3"><input name="ryjb" class="easyui-validatebox" type="text" /></td>
							</tr>
						</table>
						
						<table class="tableform" style="margin-top:10px;">
							<tr>
								<td colspan="6" style="background: #006699;border:1px solid #006699; color:#fff;font-weight: bold;">
								部门岗位信息
								</td>
							</tr>
							<tr>
								<th>员工状态：</th>
								<td>
									<input id="YGZT" name="empState" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
								<th>员工类型：</th>
								<td>
									<input id="YGLX" name="empType" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
								<th>职称等级：</th>
								<td>
									<input id="ZCDJ" name="empLevel" type="text" class="easyui-combobox" style="width:219px;height:25px;"/>
								</td>
							</tr>
							<tr>
								<th>入职时间：</th>
								<td>
									<input id="enterDate" name="enterDate" class="Wdate" style="height:23px;width:217px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									maxDate:'#F{$dp.$D(\'changeDate\')||\'2020-10-01\'}',
									onpicked:function(){
										$('#dbmDate').val($(this).val()) ; 
									}
									})"/>								
								</td>
								<th>转正日期：</th>
								<td>
									<input id="changeDate" name="changeDate" class="Wdate" style="height:23px;width:217px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'enterDate\')}',
									maxDate:'2120-10-01'
									})"/>									
								</td>
								<th>离职日期：</th>
								<td>
									<input id="dimissionDate" name="dimissionDate" class="Wdate" style="height:23px;width:217px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'changeDate\')}',
									maxDate:'2120-10-01',
									onpicked:function(){
										$('#lbmDate').val($(this).val()) ;
									}
									})"/>									
								</td>
							</tr>
							<tr>
								<th>机构部门：</th>
								<td colspan="3"><input id="org" name="org_id" style="width:506px;height:25px;" /><a onClick="org.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
								<th>分配岗位：</th> 
								<td><input id="position" name="position_id" style="width:219px;height:25px;" /><a onClick="position.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
							</tr>
						</table>
						
						<table class="tableform" style="margin-top:10px;">
							<tr>
								<td colspan="6" style="background: #006699;border:1px solid #006699; color:#fff;font-weight: bold;">
								联系信息
								</td>
							</tr>
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
									<input name="address" class="easyui-validatebox" style="width:773px;" type="text" />&nbsp;
									<input name="zipCode" style="width:53px;" class="easyui-validatebox" data-options="prompt: '邮编'" type="text" />
								</td>
							</tr>
							<tr>
								<th>其他操作：</th>
								<td colspan="7">
									<div id="icheck">
										<input id="input-1" type="checkbox" name="createAccount" checked="checked" value="1"> <label class="irl" for="input-1">创建账号</label>
										<input id="input-2" type="checkbox" name="isSendMailNotity" checked="checked" value="1"> <label class="irl" for="input-2">邮件通知</label>
									</div>
								</td>
							</tr>
						</table>
						
						<textarea name="remark" class="easyui-validatebox" data-options="prompt: '备注...'" rows="5" cols="5" style="margin-top:10px;width:99.3%; height:100px;border:1px solid #ccc; overflow: auto;"></textarea>
					</div>
				</form>
			</div>
			
			
		    <div id="code" class="panel-container" data-options="title: '稼动率相关', iconCls: 'icon-standard-layout-header', refreshable: false">
		    	<div class="form_container">
		    		<form id="jdl_form">
						<table class="tableform">
							<tr>
								<th>到部门类型：</th>
								<td><input id="JDL_DBMTYPE" name="dbmType" type="text" class="easyui-combobox" style="width:219px;height:25px;"/></td>
								<th>到部门日期：</th>
								<td>
									<input id="dbmDate" name="dbmDate" class="Wdate" style="height:23px;width:217px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'enterDate\')}',
									maxDate: new Date()
									})"/>
								</td>
							</tr>
							<tr>
								<th>离部门类型：</th>
								<td><input id="JDL_LBMTYPE" name="lbmType" type="text" class="easyui-combobox" style="width:219px;height:25px;"/></td>
								<th>离部门日期：</th>
								<td>
									<input id="lbmDate" name="lbmDate" class="Wdate" style="height:23px;width:217px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'changeDate\')}',
									maxDate:'2120-10-01',
									onpicked:function(){
										if('离职' == $('#JDL_LBMTYPE').combobox('getValue')) {
											$('#YGZT').combobox('setValue', '离职');
											$('#dimissionDate').val($(this).val()) ;
										}
									}
									})"/>
								</td>
							</tr>
						</table>
					</form>
				</div>
		    </div>
		    <!-- 
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
		     -->
		</div>
	</div>
	
	<div style="width:200px;" data-options="region: 'east', title: '人员列表', border: false" style="overflow: hidden;">
		<div id="person_select_dg">
			<div id="psd_search_bar" style="display: none;">
            	<input class="easyui-searchbox" data-options="searcher:sp_doSearch,width: 200, height: 25, menu: '#ssp'" />
				<div id="ssp" style="width: 85px;">
					<div data-options="name:'num', iconCls: 'icon-hamburg-zoom'">编号</div>
					<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">姓名</div>
				</div>
            </div>
		</div>
	</div>

</div>


