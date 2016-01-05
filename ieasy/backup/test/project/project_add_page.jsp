<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.contextPath+"/admin/oa/project/add.do" ;
	var tabsContainer, u1, owner_dept, partake_dept ;
	
	$(function() {
		tabsContainer = $("#tabsContainer").tabs({
			fit: true, border: false
		}) ;
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});
		u1 = new UE.ui.Editor({ toolbars : $.webapp.ue_simple() });
		u1.render('u1');
		
		owner_dept = $("#owner_dept").combotree({
			url: $.webapp.contextPath+"/static_res/org.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, 
			required:false, lines:true, autoShowPanel: false
		});
		partake_dept = $("#partake_dept").combotree({
			url: $.webapp.contextPath+"/static_res/org.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, 
			required:false, multiple:true, lines:true, autoShowPanel: false
		});
		
		pro_role = $("#pro_role").combobox({
			textField: 'text', valueField: 'value',
			data: [{ text: '项目经理', value: '项目经理' },{ text: '系统工程师', value: '系统工程师' },{ text: '程序员', value: '程序员' }],
			panelHeight:'auto', editable:false, autoShowPanel: true
		});
		
		jdl_dg = $("#jdl_dg").datagrid({
			title: '开发人员作业日期', iconCls: 'icon-hamburg-date',
			idField: 'id', fit: false, border: false, rownumbers: true, width:function(){return document.body.clientWidth*0.1;}, height:395,
			remoteSort: false, toolbar: '#jdl_dg_toolbars', striped:true, pagination: false, singleSelect: false,
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'org_name', title: '组织机构', width: 170, tooltip: true }, 
			    { field: 'position_name', title: '岗位', width: 100 },
			    { field: 'num', title: '员工编号', width: 80 },
			    { field: 'name', title: '员工姓名', width: 100 },
			    { field: 'sex', title: '性别', width:50, align: 'center'},
			    { field: 'projectRole', title: '项目角色', width:100 },
			    { field: 'startDate', title: '开始日期', width:100 },
			    { field: 'endDate', title: '结束日期', width:100 },
			    { field: 'workStatus', title: '作业状态', width:100 }
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				jdl_dg.datagrid('unselectAll');jdl_dg.datagrid('clearSelections');jdl_dg.datagrid('unselectAll');
			}
		}) ;
		
		//初始化项目周期为一个月
		$("#psd").val(new Date().format('yyyy-MM-dd')) ;
		$("#ped").val($.date.add(new Date(), "d", 30).format("yyyy-MM-dd")) ;
		
		//数字字典，暂时未用
		var data = {"dictCode": "XB,HYZK,ZZMM,ZC,JKZK,ZJLX,HKXZ,ZCDJ,YGZT,YGLX"} ;
		$.post($.webapp.contextPath+"/admin/system/dict/doNotNeedSession_dictAttrMaps.do", data, function(result) {
			$.each(result, function(i, p) {
				$("#"+i).combobox({valueField: 'text', textField: 'text', panelHeight: 'auto', data: p}) ;
			});
		}, 'json');
	});
	
	//打开费用明细
	function moneyDetail() {
		if($("#costDetail").is(":hidden")) {
			$("#costDetail").show();
		} else {
			$("#costDetail").hide();
		}
	}
	//计算费用明细
	function totalMoney() {
		var consts = $.webapp.serializeObject("#costDetail input") ;
		var total = 0 ;
		$.each(consts, function(i,p){
			total += parseFloat(p) ;
		});
		$("#tm").numberbox("setValue", total) ;
	}
	//清除费用
	function costDetailClear() {
		$("#tm").numberbox("setValue", 0) ;
		$("#costDetail input").val(0) ;
	}
	
	/**
		打开双表选择
		inputIds 隐藏域的input，用于存放所选人员的id(name属性至)
		inputNames 存放所选人员的名称(name属性至)
		grid 所选的人员是否存放到datagrid表格中
		ansyToDatagrid 是否同步，如选择人员到textarea框中的同时同步数据到datagrid表格中(参数值为datagrid的ID)
		ansyToInput 是否同步，如选择人员到Datagrid表格的同时同步数据到input框中(参数值为input的name属性值)
	*/
	function personDblgridFunc(inputIds, inputNames, grid, ansyToDatagrid, ansyToInput) {
		var $d = $.easyui.showDialog({
			href: $.webapp.contextPath + "/common/person_dbl_grid_UI.do?inputIds="+inputIds+"&inputNames="+inputNames+"&grid="+grid, 
			title: "待选列表", iniframe: false, topMost: true, maximizable: true,
			width: 900,height: 600,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '清空', iconCls : 'icon-metro-remove', handler : function() { $.easyui.parent.dblgridForm($d, true, inputIds, inputNames, grid, ansyToDatagrid, ansyToInput) ; } },
              { text : '确定', iconCls : 'ext_save', handler : function() { $.easyui.parent.dblgridForm($d, false, inputIds, inputNames, grid, ansyToDatagrid, ansyToInput) ; } },
              { text : '取消', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } }
           	]
        });
	}
	function personDblgridClearFunc(inputIds, inputNames, grid, ansyToDatagrid, ansyToInput) {
		//判断是否input
		if($("input[name="+inputNames+"]").length > 0) {
			$("input[name="+inputNames+"]").val("") ;
		} else {
			$("textarea[name="+inputNames+"]").val("") ;
		}
		$("input[name="+inputIds+"]").val("") ;
		if(undefined != ansyToDatagrid && "" != ansyToDatagrid.trim()) {
			$("#"+ansyToDatagrid).datagrid("loadData", {rows:0}) ;
		}
	}
	
	//设置开发人员进入项目的开始和结束作业日期
	function addJDLParam() {
		var projectRole = (pro_role.combobox("getValue")==""?"-":pro_role.combobox("getValue")) ;
		var startDate = $("#sd").val() ;
		var endDate = $("#ed").val() ;
		if(startDate == "" || endDate == "") {$.easyui.messager.show("起止日期不能为空！"); return;}
		
		var rows = jdl_dg.datagrid("getSelections"), data = $.array.clone(rows) ;
		if(rows.length == 0){$.easyui.messager.show("您未选择任何数据。"); return;}
		$.each(data, function (i, val) { setRowCell(val, projectRole, startDate, endDate); });
	}
	//更新列数据
	function setRowCell(row, projectRole, startDate, endDate){
		jdl_dg.datagrid("updateRow",{
			index: jdl_dg.datagrid("getRowIndex", row),
			row: {
				projectRole: projectRole,
				startDate: startDate,
				endDate: endDate
			}
		});
	}
	//删除列内容
	function removeRowCell(){
		var rows = jdl_dg.datagrid("getSelections"), data = $.array.clone(rows) ;
		if(rows.length == 0){$.easyui.messager.show("您未选择任何数据。"); return;}
		$.each(data, function (i, val) {
			jdl_dg.datagrid("updateRow",{
				index: jdl_dg.datagrid("getRowIndex", val),
				row: {
					projectRole: '',
					startDate: '',
					endDate: ''
				}
			});
		});
	}
	//删除数据行
	function deleteJDLRow(inputIds, inputNames, grid, ansyToDatagrid, ansyToInput) {
		var rows = jdl_dg.datagrid("getSelections"), data = $.array.clone(rows) ;
		if(rows.length == 0){$.easyui.messager.show("您未选择任何数据。"); return;}
		$.each(data, function (i, val) {
			var index = jdl_dg.datagrid("getRowIndex", val);
	        if (index > -1) { jdl_dg.datagrid("deleteRow", index); }
		});
		
		if(undefined != ansyToInput && "" != ansyToInput.trim()) {
			var new_ids = [] ;
			var new_names = [] ;
			var allRows = jdl_dg.datagrid("getRows");
			if(allRows.length > 0) {
				$.each(allRows, function(i,p){
					new_ids.push(p.id) ;
					new_names.push(p.name) ;
				});
				console.info(new_names) ;
				//判断是input还是textarea
				if($("input[name="+ansyToInput+"]").length > 0) {
					$("input[name="+ansyToInput+"]").val(new_names.join(",")) ;
				} else {
					$("textarea[name="+ansyToInput+"]").val(new_names.join(",")) ;
				}
				$("input[name="+inputIds+"]").val(new_ids.join(",")) ;
			} else {
				//判断是否input
				if($("input[name="+ansyToInput+"]").length > 0) {
					$("input[name="+ansyToInput+"]").val("") ;
				} else {
					$("textarea[name="+ansyToInput+"]").val("") ;
				}
				$("input[name="+inputIds+"]").val("") ;
			}
		}
	}
	
	//获取开发人员作业日期
	function getRowsCellField() {
		var jdl_develops_work = [] ;
		var rows = jdl_dg.datagrid("getRows") ;
		$.each(rows, function (i, row) {
			if(row.startDate == undefined || "" == row.startDate || row.endDate == undefined || "" == row.endDate) {
				jdl_develops_work = [] ;
				return false ;
			}
			jdl_develops_work.push(row.id+","+row.num+","+row.name+","+row.projectRole+","+row.startDate+","+row.endDate) ;
		});
		return jdl_develops_work.join("|") ;
	}
	
	//提交表单数据
	var submitNow = function($d, submitStatus, refreshStatusFun) {
		var basicform = $.webapp.serializeObject("#basic_pro_form") ;
		if(undefined == basicform.proj_start_time || "" == basicform.proj_start_time || undefined == basicform.proj_end_time || "" == basicform.proj_end_time) {
			$.easyui.loaded(); $.messager.alert("警告", "项目周期不能为空！", "error");
			return ;
		}
		
		var relationform = $.webapp.serializeObject("#relation_pro_form") ;
		var jdlform = $.webapp.serializeObject("#jdl_form") ;
		
		//组装数据
		var o = {} ;
		o["submitStatus"] = submitStatus ;
		o["proj_status"] = submitStatus ;
		o["proj_owner_dept_name"] = owner_dept.combotree("getText") ;
		o["proj_partake_dept_names"] = partake_dept.combotree("getText") ;
		addAttr(o, basicform) ;
		addAttr(o, relationform) ;
		addAttr(o, jdlform) ;
		//开发人员的作业日期
		o["jdl_develops_work"] = getRowsCellField() ;
		
		$.post(form_url, o, function(result) {
			if (result.status) {
				refreshStatusFun();
				$.easyui.loaded();
				alertify.success(result.msg);
				//$d.dialog("close") ;
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	};
	
	//验证表单
	var submitProjectForm = function($d, submitStatus, refreshStatusFun) { 
		if($('#basic_pro_form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			submitNow($d, submitStatus, refreshStatusFun) ;
		}
	};
	
	//添加数据
	function addAttr(datas, obj){
		$.each(obj, function(p, v){
			datas[p] = v ;
		}) ;
	}
	
</script>

<div class="easyui-layout" data-options="fit: true">
	
	<div data-options="region: 'center', border: true" style="border-left:0px;border-top:0px;border-right:0px;border-bottom:0px;overflow: hidden;">
		<div id="tabsContainer">
			<div id="basic" class="panel-container" data-options="title: '立项及预算', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
				<form id="basic_pro_form">
					<div class="form_container">
						<table class="tableform">
							<tr>
								<th>项目编号：</th>
								<td><input name="proj_num" class="easyui-validatebox i300" type="text" /></td>
								<th>项目级别：</th>
								<td>
									<div id="icheck">
										<input id="level1" type="radio" name="proj_level" value="A" checked="checked">
										<label class="irl irl-10" for="level1">A</label>&nbsp;&nbsp;
										<input id="level2" type="radio" name="proj_level" value="B">
										<label class="irl irl-10" for="level2">B</label>&nbsp;&nbsp;
										<input id="level3" type="radio" name="proj_level" value="C">
										<label class="irl irl-10" for="level3">C</label>&nbsp;&nbsp;
										<input id="level4" type="radio" name="proj_level" value="D">
										<label class="irl irl-10" for="level4">D</label>
									</div>
								</td>
							</tr>
							<tr>
								<th>项目名称：</th>
								<td><input name="proj_name" class="easyui-validatebox i300" type="text" /></td>
								<th>项目周期：</th>
								<td>
									<input id="psd" name="proj_start_time" value="" class="Wdate" style="height:23px;width:142px;" type="text" onFocus="WdatePicker({
										isShowClear:true,
										readOnly:true,
										maxDate:'#F{$dp.$D(\'ped\')||\'2020-10-01\'}'
										})"/>
									至
									<input id="ped" name="proj_end_time" class="Wdate" style="height:23px;width:142px;" type="text" onFocus="WdatePicker({
										isShowClear:true,
										readOnly:true,
										minDate:'#F{$dp.$D(\'psd\')}',
										maxDate:'2120-10-01'
										})"/>
								</td>
							</tr>
							<tr>
								<th>所属部门：</th>
								<td><input id="owner_dept" name="proj_owner_dept_id" style="width:308px;height:25px;" /><a onClick="owner_dept.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
								<th>参与部门：</th>
								<td><input id="partake_dept" name="proj_partake_dept_ids" style="width:308px;height:25px;" /><a onClick="partake_dept.combotree('setValue','');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_clear'"></a></td>
							</tr>
							<tr>
								<th>项目审批人：</th>
								<td colspan="3">
									<input name="proj_approve_person_names" value="${USER_SESSION.user.emp_name}" readonly="readonly" class="easyui-validatebox" style="width:660px;" type="text" />
									<input name="proj_approve_person_ids" value="${USER_SESSION.user.emp_id}" type="hidden" />
									<a onClick="personDblgridFunc('proj_approve_person_ids', 'proj_approve_person_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="personDblgridClearFunc('proj_approve_person_ids', 'proj_approve_person_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th valign="top">项目描述：</th>
								<td colspan="3">
									<textarea id="u1" name="proj_description" style="width:99.8%;height:300px;"></textarea>
								</td>
							</tr>
						</table>
						
						<p style="border-bottom: 1px solid #e6e6e6;font-size: 18px;font-weight:bold;">资金预算信息</p>
						<div class="moneyDetail">
							总预算资金：<input id="tm" name="proj_const_money" readonly="readonly" value="0" class="easyui-numberbox" type="text" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" />&nbsp;元&nbsp;&nbsp;
							<a href="javascript:moneyDetail();">添加明细</a>
						</div>
						
						<table id="costDetail" class="tableform" style="margin-top:10px;display: none">
							<tr>
								<th colspan="6" style="text-align: left;">基本支出：</th>
							</tr>
							<tr>
								<th>人员工资：</th>
								<td><input name="proj_budget_amount1" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
								<th>差旅费：</th>
								<td><input name="proj_budget_amount2" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
								<th>设备消耗：</th>
								<td><input name="proj_budget_amount3" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
							</tr>
							<tr>
								<th colspan="6" style="text-align: left;">项目支出：</th>
							</tr>
							<tr>
								<th>项目维护：</th>
								<td><input name="proj_budget_amount4" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
								<th>资源采购：</th>
								<td><input name="proj_budget_amount5" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
								<th>活动支出：</th>
								<td><input name="proj_budget_amount6" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
							</tr>
							<tr>
								<th colspan="6" style="text-align: left;">项目费用：</th>
							</tr>
							<tr>
								<th>差旅费：</th>
								<td><input name="proj_budget_amount7" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
								<th>加班费：</th>
								<td><input name="proj_budget_amount8" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
								<th>奖金：</th>
								<td><input name="proj_budget_amount9" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:',',suffix:'￥'" type="text" />&nbsp;元</td>
							</tr>
							<tr>
								<th colspan="6" style="text-align: center;background: none;">
									<a onClick="totalMoney();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-bank'">计算</a>
									<a onClick="costDetailClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">取消</a>
								</th>
							</tr>
						</table>
						
					</div>
				</form>
			</div>
			
		    <div id="member" class="panel-container" data-options="title: '设置干系人', iconCls: 'icon-standard-layout-header', refreshable: false">
		    	<form id="relation_pro_form">
			    	<div class="form_container">
				    	<table class="tableform">
							<tr>
								<th style="width:150px;">项目创建人：</th>
								<td colspan="2">
									<input name="proj_creator_name" class="easyui-validatebox i300" type="text" readonly="readonly" value="${USER_SESSION.user.emp_name}" />
									<input name="proj_creator_id" type="hidden" value="${USER_SESSION.user.emp_id}" />
								</td>
							</tr>
							<tr>
								<th>项目负责人：</th>
								<td colspan="2">
									<input name="proj_owner_name" class="easyui-validatebox i300" type="text" readonly="readonly" value="${USER_SESSION.user.emp_name}" />
									<input name="proj_owner_id" type="hidden" value="${USER_SESSION.user.emp_id}" />
									<a onClick="personDblgridFunc('proj_owner_id', 'proj_owner_name');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="personDblgridClearFunc('proj_owner_id', 'proj_owner_name');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>项目经理：</th>
								<td colspan="2">
									<input name="proj_manager_name" class="easyui-validatebox i300" type="text" readonly="readonly" value="${USER_SESSION.user.emp_name}" />
									<input name="proj_manager_id" type="hidden" value="${USER_SESSION.user.emp_id}" />
									<a onClick="personDblgridFunc('proj_owner_id', 'proj_owner_name');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="personDblgridClearFunc('proj_owner_id', 'proj_owner_name');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>项目查看人：</th>
								<td style="width:310px;border-right:0px;">
									<textarea name="proj_viewer_member_names" readonly="readonly" style="width:306px;height:80px;"></textarea>
									<input name="proj_viewer_member_ids" type="hidden" />
								</td>
								<td style="border-left:0px;">
									<a onClick="personDblgridFunc('proj_viewer_member_ids', 'proj_viewer_member_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="personDblgridClearFunc('proj_viewer_member_ids', 'proj_viewer_member_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>开发工程师：</th>
								<td style="width:310px;border-right:0px;">
									<textarea name="proj_develop_engine_names" readonly="readonly" style="width:306px;height:80px;"></textarea>
									<input name="proj_develop_engine_ids" type="hidden" />
								</td>
								<td style="border-left:0px;">
									<a onClick="personDblgridFunc('proj_develop_engine_ids', 'proj_develop_engine_names','','jdl_dg');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="personDblgridClearFunc('proj_develop_engine_ids', 'proj_develop_engine_names','','jdl_dg');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>测试工程师：</th>
								<td style="width:310px;border-right:0px;">
									<textarea name="proj_testing_engine_names" readonly="readonly" style="width:306px;height:80px;"></textarea>
									<input name="proj_testing_engine_ids" type="hidden" />
								</td>
								<td style="border-left:0px;">
									<a onClick="personDblgridFunc('proj_testing_engine_ids', 'proj_testing_engine_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="personDblgridClearFunc('proj_testing_engine_ids', 'proj_testing_engine_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>质量保障工程师：</th>
								<td style="width:310px;border-right:0px;">
									<textarea name="proj_sqa_member_names" readonly="readonly" style="width:306px;height:80px;"></textarea>
									<input name="proj_sqa_member_ids" type="hidden" />
								</td>
								<td style="border-left:0px;">
									<a onClick="personDblgridFunc('proj_sqa_member_ids', 'proj_sqa_member_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="personDblgridClearFunc('proj_sqa_member_ids', 'proj_sqa_member_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
						</table>
			    	</div>
			    </form>
		    </div>
		    
		    <div id="jdl_param" class="panel-container" data-options="title: '稼动率', iconCls: 'icon-standard-layout-header', refreshable: false">
		    	<form id="jdl_form">
					<div class="form_container">
						<table class="tableform">
							<tr>
								<th>案件编号：</th>
								<td><input name="proj_ajbh" class="easyui-validatebox i300" type="text" /></td>
								<th>作业范围：</th>
								<td><input name="proj_zyfw" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>项目规模：</th>
								<td><input name="proj_xwgm" class="easyui-validatebox i300" type="text" /></td>
								<th>顾客返回BUG率目标：</th>
								<td><input name="proj_gkfhbuglmb" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>报价总人月：</th>
								<td><input name="proj_bjzry" class="easyui-validatebox i300" type="text" /></td>
								<th>预计投入总人月数：</th>
								<td><input name="proj_yjtrzrys" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>报价生产性：</th>
								<td><input name="proj_bjscx" class="easyui-validatebox i300" type="text" /></td>
								<th>初始粗利润率：</th>
								<td><input name="proj_csclrl" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>当前粗利润率：</th>
								<td><input name="proj_dqclyl" class="easyui-validatebox i300" type="text" /></td>
								<th>预定生产性：</th>
								<td><input name="proj_ydscx" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>合同评审状态：</th>
								<td><input name="proj_htpszt" class="easyui-validatebox i300" type="text" /></td>
								<th>财务结算状态：</th>
								<td><input name="proj_cwjszt" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>跟进级别：</th>
								<td><input name="proj_gjjb" class="easyui-validatebox i300" type="text" /></td>
								<th>财务预定完成结算时间：</th>
								<td><input name="proj_cwydwcjssj" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>财务完成结算时间：</th>
								<td><input name="proj_cwwcjssj" class="easyui-validatebox i300" type="text" /></td>
								<th>项目结算年份：</th>
								<td><input name="proj_xmjsnf" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>合同受注状态：</th>
								<td><input name="proj_htszzt" class="easyui-validatebox i300" type="text" /></td>
								<th>结项状态：</th>
								<td><input name="proj_jszt" class="easyui-validatebox i300" type="text" /></td>
							</tr>
						</table>
					</div>
				</form>
				<div id="jdl_dg">
					<div id="jdl_dg_toolbars">
		            	<div class="s_box">
							<div class="gr">
								<div class="st">项目角色：</div>
								<div class="si1"><input id="pro_role" name="projectRole" style="width:160px;height:25px;"></input></div>
								<div class="st">起止日期：</div> 
								<div class="si1">
									<input id="sd" name="startDate" class="Wdate" style="width:100px;height:23px;" type="text" onFocus="WdatePicker({
										isShowClear:true,
										readOnly:true,
										maxDate:'#F{$dp.$D(\'ed\')||\'2020-10-01\'}'
										})"/>
									至
									<input id="ed" name="endDate" class="Wdate" style="width:100px;height:23px;" type="text" onFocus="WdatePicker({
										isShowClear:true,
										readOnly:true,
										minDate:'#F{$dp.$D(\'sd\')}',
										maxDate:'2120-10-01'
										})"/>
			            		</div>
			            	</div>
		            		<div class="gr">
			            		<div class="st">
				            		<a style="float:left;" onClick="addJDLParam();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-settings'">设置</a>
				            		<a style="float:left;" onClick="removeRowCell();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-remove'">清空</a>
				            		<div class="datagrid-btn-separator"></div>
				            		<a style="float:left;" onClick="personDblgridFunc('proj_develop_engine_ids', '', 'jdl_dg', '', 'proj_develop_engine_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加数据</a>
									<a style="float:left;" onClick="deleteJDLRow('proj_develop_engine_ids', '', 'jdl_dg', '', 'proj_develop_engine_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">删除</a>
			            		</div>
		            		</div>
		            	</div>
		            </div>
				</div>
		    </div>
		    
		</div>
	</div>
</div>


