<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">
	var form_url = $.webapp.root+"/admin/oa/project/add.do" ;
	var tabsContainer, u1, owner_dept, partake_dept, developsDetail ;
	
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
		
		//数据字典
		var data = {"dictCode": "KFRYJS,XM_LXQF,XM_PSZT,XM_SZZT,XM_JXZT,XM_JSZT"} ;
		$.post($.webapp.root+"/admin/system/dict/doNotNeedSession_dictAttrMaps.do", data, function(result) {
			$.each(result, function(i, p) {
				$("#"+i).combobox({valueField: 'text', textField: 'text', panelHeight: 'auto', data: p}) ;
			});
		}, 'json');
		
		owner_dept = $("#owner_dept").combotree({
			url: $.webapp.root+"/static_res/org.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, 
			required:false, lines:true, autoShowPanel: false
		});
		partake_dept = $("#partake_dept").combotree({
			url: $.webapp.root+"/static_res/org.tree.json",
			idFiled:'pid', textFiled:'name', editable: false, 
			required:false, multiple:true, lines:true, autoShowPanel: false
		});
		
		developsDetail = $("#developsDetail").datagrid({
			title: '开发人员作业日期明细设置', iconCls: 'icon-hamburg-date',
			idField: 'id', fit: true, border: false, rownumbers: true,
			remoteSort: false, toolbar: '#developsDetail_toolbars', striped:true, pagination: false, singleSelect: false,
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
			    { field: 'endDate', title: '结束日期', width:100 }
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				developsDetail.datagrid('unselectAll');developsDetail.datagrid('clearSelections');developsDetail.datagrid('unselectAll');
			}
		}) ;
		
		//初始化项目周期为一个月
		$("#psd").val(new Date().format('yyyy-MM-dd')) ;
		$("#ped").val($.date.add(new Date(), "d", 30).format("yyyy-MM-dd")) ;
		
		
	});
	
	//打开费用明细
	function moneyDetail() {
		if($("#costDetail").is(":hidden")) {
			$("#costDetail").show();
			$("#costDetail input").blur(function(){
				totalMoney() ;
			});
		} else {
			$("#costDetail").hide();
		}
	}
	//计算费用明细
	function totalMoney() {
		var consts = $.webapp.serializeObject("#costDetail input") ;
		var total = 0 ;
		$.each(consts, function(i,p){
			total += parseFloat((undefined != p && "" != p?p:0)) ;
		});
		$("#tm").numberbox("setValue", total) ;
	}
	//清除费用
	function costDetailClear() {
		$("#tm").numberbox("setValue", 0) ;
		$("#costDetail input").val(0) ;
	}
	
	
	function addMembers(showId, hideInPutIds, hideInputNames) {
		var $d = $.easyui.showDialog({
			href: $.webapp.root + "/common/person_dbl_grid_UI.do", 
			title: "待选列表", iniframe: false, topMost: true, maximizable: true,
			width: 900,height: 600,
	        enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
	        buttons : [ 
	          { text : '确定', iconCls : 'ext_save', handler : function() {
	        	  var rows = $.easyui.parent.getDataGrid() ;
	        	  setInputData(rows, showId, hideInPutIds, hideInputNames) ;
	        	  $d.dialog('destroy');
	          } },
	          { text : '取消', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } }
	       	]
	    });
	}
	function setInputData(rows, showId, hideInPutIds, hideInputNames){
		var _ul = $("#"+showId) ;
		var input1 = $("input[name="+hideInPutIds+"]") ;
		var input2 = $("input[name="+hideInputNames+"]") ;
		
		var input1s = [] ;
		var input2s = [] ;
		
		$.each(rows, function(p, v){
			if(!$.string.contains(input1.val(), v.id)) {
				_ul.append($.string.format("<li id='{0}'><span class='name'>{1}</span><span class='remove' onClick='removeMember(\"{2}\",\"{3}\",\"{4}\",\"{5}\")'>X</span></li>", v.id, v.name, v.id, showId, hideInPutIds, hideInputNames));
				input1s.push(v.id) ;
				input2s.push(v.name) ;
			}
		});
		if(input1s.length>0) {
	 		input1.val((""!=input1.val().trim()?input1.val()+",":"")+input1s) ;
			input2.val((""!=input2.val().trim()?input2.val()+",":"")+input2s) ;
		} 
	}
	function removeMember(id, showId, hideInPutIds, hideInputNames) {
		$("#"+showId).children("#"+id).remove();
		var input1s = [] ;
		var input2s = [] ;
		var _lis = $("#"+showId+" li") ;
		$.each(_lis, function(i,p){
			input1s.push($(this).attr("id"));
			input2s.push($(this).children("span.name").text());
		});
		$("input[name="+hideInPutIds+"]").val(input1s) ;
		$("input[name="+hideInputNames+"]").val(input2s) ;
	}
	function removeAllMembers(showId, hideInPutIds, hideInputNames) {
		$("#"+showId + " li").remove();
		$("input[name="+hideInPutIds+"]").val("") ;
		$("input[name="+hideInputNames+"]").val("") ;
	}
	
	function addDevMembers() {
		var $d = $.easyui.showDialog({
			href: $.webapp.root + "/common/person_dbl_grid_UI.do", 
			title: "待选列表", iniframe: false, topMost: true, maximizable: true,
			width: 900,height: 600,
	        enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
	        buttons : [ 
	          { text : '确定', iconCls : 'ext_save', handler : function() {
	        	  var rows = $.easyui.parent.getDataGrid() ;
	        	  setRowsToDatagrid(rows) ;
	        	  $d.dialog('destroy');
	          } },
	          { text : '取消', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } }
	       	]
	    });
	}
	
	function setRowsToDatagrid(rows) {
		$.each(rows, function(p, v){
			sRow(v) ;
		});
	}
	function sRow(row) {
        if (!row) { return; }
        var isExists = developsDetail.datagrid("getRowIndex", row["id"] ? row["id"] : row) > -1;
        if (!isExists) { 
        	var nr = {
					id: row.id,
					org_name: row.org_name,
					position_name: row.position_name,
					num: row.num,
					name: row.name,
					sex: row.sex
			};
        	developsDetail.datagrid("appendRow", nr); 
       	}
    }
	
	
	//设置开发人员作业周期
	function setDevWorkDate() {
		var projectRole = ($("#KFRYJS").combobox("getValue")==""?"-":$("#KFRYJS").combobox("getValue")) ;
		var startDate = $("#sd").val() ;
		var endDate = $("#ed").val() ;
		if(startDate == "" || endDate == "") {$.easyui.messager.show("起止日期不能为空！"); return;}
		
		var rows = developsDetail.datagrid("getSelections"), data = $.array.clone(rows) ;
		if(rows.length == 0){$.easyui.messager.show("您未选择任何数据。"); return;}
		$.each(data, function (i, val) { setRowCell(val, projectRole, startDate, endDate); });
	}
	//更新列数据
	function setRowCell(row, projectRole, startDate, endDate){
		developsDetail.datagrid("updateRow",{
			index: developsDetail.datagrid("getRowIndex", row),
			row: {
				projectRole: projectRole,
				startDate: startDate,
				endDate: endDate
			}
		});
	}
	//删除列内容
	function removeRowCell(){
		var rows = developsDetail.datagrid("getSelections"), data = $.array.clone(rows) ;
		if(rows.length == 0){$.easyui.messager.show("您未选择任何数据。"); return;}
		$.each(data, function (i, val) {
			developsDetail.datagrid("updateRow",{
				index: developsDetail.datagrid("getRowIndex", val),
				row: {
					projectRole: '',
					startDate: '',
					endDate: ''
				}
			});
		});
	}
	//删除数据行
	function deleteDevMemberRow() {
		var rows = developsDetail.datagrid("getSelections"), data = $.array.clone(rows) ;
		if(rows.length == 0){$.easyui.messager.show("您未选择任何数据。"); return;}
		$.each(data, function (i, val) {
			var index = developsDetail.datagrid("getRowIndex", val);
	        if (index > -1) { developsDetail.datagrid("deleteRow", index); }
		});
	}
	
	//获取开发人员作业日期
	function getRowsCellField() {
		var jdl_develops_work = [] ;
		var rows = developsDetail.datagrid("getRows") ;
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
	var submitNow = function($d, proj_status, refreshStatusFun) {
		if("1" == proj_status) {
			var approve = $("input[name=proj_approve_person_ids]").val();
			if(!$.string.contains(approve, "${USER_SESSION.user.emp_id}")) {
				$.easyui.loaded();$.easyui.loaded(); $.messager.alert("警告", "你不在审批人员列表中，无法以审批方式提交！", "warning");
				return ;
			}
		}
		var projectOwner = $("#selectList_owner li") ;
		if(projectOwner.length == 0 || projectOwner.length > 1) {
			$.easyui.loaded();$.easyui.loaded(); $.messager.alert("警告", "项目负责人必须和只能设置一人！", "warning");
			return ;
		}
		
		var basicform = $.webapp.serializeObject("#basic_pro_form") ;
		if(undefined == basicform.proj_start_time || "" == basicform.proj_start_time || undefined == basicform.proj_end_time || "" == basicform.proj_end_time) {
			$.easyui.loaded(); $.messager.alert("警告", "项目周期不能为空！", "error");
			return ;
		}
		
		var relationform = $.webapp.serializeObject("#relation_pro_form") ;
		
		//组装数据
		var o = {} ;
		o["proj_status"] = proj_status ;
		o["proj_owner_dept_name"] = owner_dept.combotree("getText") ;
		o["proj_partake_dept_names"] = partake_dept.combotree("getText") ;
		addAttr(o, basicform) ;
		addAttr(o, relationform) ;
		//开发人员的作业日期
		o["develops_worktime"] = getRowsCellField() ;
		
		$.post(form_url, o, function(result) {
			if (result.status) {
				refreshStatusFun();
				$.easyui.loaded();
				alertify.success(result.msg);
				$d.dialog("close") ;
			} else {
				$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	};
	
	//验证表单
	var submitProjectForm = function($d, proj_status, refreshStatusFun) { 
		if($('#basic_pro_form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			submitNow($d, proj_status, refreshStatusFun) ;
		} else {
			$.messager.alert("提示", "有必填项未填！", "warning");
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
								<th style="width:150px;">项目编号：</th> 
								<td><input name="proj_num" class="easyui-validatebox i300" type="text" data-options="required:true" /></td>
								<th style="width:150px;">跟进级别：</th>
								<td>
									<div id="icheck">
										<input id="level1" type="radio" name="proj_level" value="A">
										<label class="irl irl-10" for="level1">A</label>&nbsp;&nbsp;
										<input id="level2" type="radio" name="proj_level" value="B">
										<label class="irl irl-10" for="level2">B</label>&nbsp;&nbsp;
										<input id="level3" type="radio" name="proj_level" value="C">
										<label class="irl irl-10" for="level3">C</label>&nbsp;&nbsp;
										<input id="level4" type="radio" name="proj_level" value="D">
										<label class="irl irl-10" for="level4">D</label>
										<input id="level5" type="radio" name="proj_level" value="-" checked="checked">
										<label class="irl irl-10" for="level5">未知</label>
									</div>
								</td>
							</tr>
							<tr>
								<th>项目名称：</th>
								<td><input name="proj_name" class="easyui-validatebox i300" data-options="required:true" type="text" /></td>
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
								<th>ID类型区分：</th>
								<td><input id="XM_LXQF" name="distinguish" style="width:308px;height:25px;" /></td>
								<th>合同项目类型：</th>
								<td><input name="proj_type" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>合同受注状态：</th>
								<td><input id="XM_SZZT" name="proj_shouzhu" style="width:308px;height:25px;" type="text" /></td>
								<th>作业范围：</th>
								<td><input name="proj_zyfw" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>项目规模：</th>
								<td><input name="proj_gm" class="easyui-validatebox i300" type="text" /></td>
								<th>顾客返回BUG率目标：</th>
								<td><input name="proj_buglv" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>报价总人月：</th>
								<td><input name="proj_bjzry" class="easyui-validatebox i300" type="text" /></td>
								<th>预计投入总人月数：</th>
								<td><input name="proj_yjtrzry" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>报价生产性：</th>
								<td><input name="proj_bjscx" class="easyui-validatebox i300" type="text" /></td>
								<th>初始粗利润率：</th>
								<td><input name="proj_clrl" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>当前粗利润率：</th>
								<td><input name="proj_cclrl" class="easyui-validatebox i300" type="text" /></td>
								<th>预定生产性：</th>
								<td><input name="proj_ydscx" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>合同评审状态：</th>
								<td><input id="XM_PSZT" name="proj_htpjzt" style="width:308px;height:25px;" type="text" /></td>
								<th>财务结算状态：</th>
								<td><input id="XM_JSZT" name="proj_cwjszt" style="width:308px;height:25px;" type="text" /></td>
							</tr>
							<tr>
								<th>财务完成结算时间：</th>
								<td><input name="proj_cwwcjssj" class="easyui-validatebox i300" type="text" /></td>
								<th>结项状态：</th>
								<td><input id="XM_JXZT" name="proj_jxzt" style="width:308px;height:25px;" type="text" /></td>
							</tr>
							<tr>
								<th>财务预定完成结算时间：</th>
								<td><input name="proj_cwydwcjssj" class="easyui-validatebox i300" type="text" /></td>
								<th>项目系数：</th>
								<td><input name="proj_quot" class="easyui-numberbox validatebox-text i300" style="height:25px;" data-options="precision:1" type="text" /></td>
							</tr>
							<tr>
								<th>项目结算年份：</th>
								<td><input name="proj_xmjsnf" class="easyui-validatebox i300" type="text" /></td>
							</tr>
							<tr>
								<th>项目备注：</th>
								<td colspan="3">
									<textarea name="proj_bak" style="width:99.8%;height:100px;"></textarea>
								</td>
							</tr>
							<tr>
								<th valign="top">项目描述：</th>
								<td colspan="3">
									<textarea id="u1" name="proj_description" style="width:99.8%;height:300px;"></textarea>
								</td>
							</tr>
						</table>
						<!-- 
						<p style="border-bottom: 1px solid #e6e6e6;font-size: 18px;font-weight:bold;">资金预算信息</p>
						<div class="moneyDetail">
							总预算资金：<input id="tm" name="proj_const_money" readonly="readonly" value="0" class="easyui-numberbox" type="text" data-options="min:0,precision:1, groupSeparator:','" />&nbsp;元&nbsp;&nbsp;
							<a onClick="moneyDetail();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-bank'">添加明细</a>
							<a onClick="costDetailClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">取消</a>
						</div>
						
						<table id="costDetail" class="tableform" style="margin-top:10px;display: none">
							<tr>
								<th colspan="6" style="text-align: left;">基本支出：</th>
							</tr>
							<tr>
								<th>人员工资：</th>
								<td><input name="proj_budget_amount1" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
								<th>差旅费：</th>
								<td><input name="proj_budget_amount2" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
								<th>设备消耗：</th>
								<td><input name="proj_budget_amount3" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
							</tr>
							<tr>
								<th colspan="6" style="text-align: left;">项目支出：</th>
							</tr>
							<tr>
								<th>项目维护：</th>
								<td><input name="proj_budget_amount4" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
								<th>资源采购：</th>
								<td><input name="proj_budget_amount5" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
								<th>活动支出：</th>
								<td><input name="proj_budget_amount6" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
							</tr>
							<tr>
								<th colspan="6" style="text-align: left;">项目费用：</th>
							</tr>
							<tr>
								<th>差旅费：</th>
								<td><input name="proj_budget_amount7" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
								<th>加班费：</th>
								<td><input name="proj_budget_amount8" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
								<th>奖金：</th>
								<td><input name="proj_budget_amount9" class="easyui-numberbox" data-options="min:0,precision:1, groupSeparator:','" type="text" />&nbsp;元</td>
							</tr>
						</table>
						 -->
						
					</div>
				</form>
			</div>
			
		    <div id="member" class="panel-container" data-options="title: '设置干系人', iconCls: 'icon-standard-layout-header', refreshable: false">
		    	<form id="relation_pro_form">
			    	<div class="form_container">
				    	<table class="tableform">
							<tr>
								<th style="width:150px;">项目创建人：</th>
								<td>
									${USER_SESSION.user.emp_name}
									<input name="proj_creator_name" type="hidden" value="${USER_SESSION.user.emp_name}" />
									<input name="proj_creator_id" type="hidden" value="${USER_SESSION.user.emp_id}" />
								</td>
							</tr>
							<tr>
								<th>所属部长：</th>
								<td colspan="3">
									<ul id="selectList_owner" class="selectList" style="max-width:600px;">
										<li id="${USER_SESSION.user.emp_id}"><span class="name">${USER_SESSION.user.emp_name}</span><span class="remove" onClick="removeMember('${USER_SESSION.user.emp_id}','selectList_owner','proj_owner_id','proj_owner_name')">X</span></li>
									</ul>
									<input name="proj_owner_id" type="hidden" value="${USER_SESSION.user.emp_id}" />
									<input name="proj_owner_name" type="hidden" value="${USER_SESSION.user.emp_name}" />
									<a onClick="addMembers('selectList_owner','proj_owner_id', 'proj_owner_name');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_owner','proj_owner_id', 'proj_owner_name');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>项目审批人：</th>
								<td colspan="3">
									<ul id="selectList_approve" class="selectList" style="max-width:600px;">
										<li id="${USER_SESSION.user.emp_id}"><span class="name">${USER_SESSION.user.emp_name}</span><span class="remove" onClick="alert('不可删除自己')">X</span></li>
									</ul>
									<input name="proj_approve_person_ids" type="hidden" value="${USER_SESSION.user.emp_id}" />
									<input name="proj_approve_person_names" type="hidden" value="${USER_SESSION.user.emp_name}" />
									<a onClick="addMembers('selectList_approve','proj_approve_person_ids', 'proj_approve_person_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_approve','proj_approve_person_ids', 'proj_approve_person_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>可操作人员：</th>
								<td colspan="3">
									<ul id="selectList_oper" class="selectList" style="max-width:600px;"></ul>
									<input name="proj_operation_ids" type="hidden" />
									<input name="proj_operation_names" type="hidden" />
									<a onClick="addMembers('selectList_oper','proj_operation_ids', 'proj_operation_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_oper','proj_operation_ids', 'proj_operation_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>项目负责人：</th>
								<td colspan="3">
									<ul id="selectList_pm" class="selectList" style="max-width:600px;"></ul>
									<input name="proj_manager_ids" type="hidden" />
									<input name="proj_manager_names" type="hidden" />
									<a onClick="addMembers('selectList_pm','proj_manager_ids', 'proj_manager_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_pm','proj_manager_ids', 'proj_manager_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>项目参与人员：</th>
								<td colspan="3">
									<ul id="selectList_viewer" class="selectList" style="max-width:600px;"></ul>
									<input name="proj_viewer_member_ids" type="hidden" />
									<input name="proj_viewer_member_names" type="hidden" />
									<a onClick="addMembers('selectList_viewer','proj_viewer_member_ids', 'proj_viewer_member_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_viewer','proj_viewer_member_ids', 'proj_viewer_member_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<!-- 
							<tr>
								<th>测试工程师：</th>
								<td colspan="3">
									<ul id="selectList_test" class="selectList" style="max-width:600px;"></ul>
									<input name="proj_testing_engine_ids" type="hidden" />
									<input name="proj_testing_engine_names" type="hidden" />
									<a onClick="addMembers('selectList_test','proj_testing_engine_ids', 'proj_testing_engine_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_test','proj_testing_engine_ids', 'proj_testing_engine_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							-->
							<tr>
								<th>SQA：</th>
								<td colspan="3">
									<ul id="selectList_sqa" class="selectList" style="max-width:600px;"></ul>
									<input name="proj_sqa_member_ids" type="hidden" />
									<input name="proj_sqa_member_names" type="hidden" />
									<a onClick="addMembers('selectList_sqa','proj_sqa_member_ids', 'proj_sqa_member_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_sqa','proj_sqa_member_ids', 'proj_sqa_member_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
						</table>
			    	</div>
			    </form>
		    </div>
		    
		    <div id="jdl_param" class="panel-container" data-options="title: '开发人员设置', iconCls: 'icon-standard-layout-header', refreshable: false">
				<div id="developsDetail">
					<div id="developsDetail_toolbars">
		            	<div class="s_box">
							<div class="gr">
								<div class="st">项目角色：</div>
								<div class="si1"><input id="KFRYJS" name="projectRole" style="width:160px;height:25px;"></input></div>
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
				            		<a style="float:left;" onClick="setDevWorkDate();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-settings'">设置</a>
				            		<a style="float:left;" onClick="removeRowCell();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-remove'">清空</a>
				            		<div class="datagrid-btn-separator"></div>
				            		<a onClick="addDevMembers();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="deleteDevMemberRow();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">删除</a>
			            		</div>
		            		</div>
		            	</div>
		            </div>
				</div>
		    </div>
		    
		</div>
	</div>
</div>


