<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/common/header/mytags.jsp"%>
<script type="text/javascript">
	/**
		projectId和status来至于open_project_UI.jsp页面
		这里可以获取到，因为open_project_UI.jsp是通过href的方式来打开project_detail_view.jsp的，该页面嵌入到了open_project_UI中
	*/
	var developsDetail ;
	
	$(function() {
		$.easyui.loading({ msg: "数据加载中，请稍等...", locale: "#detailCenter" });
		$("#tabsContainer").tabs({
			fit: true, border: false
		}) ;
		
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_square-blue'
	   	});
		
		//数据字典
		var data = {"dictCode": "KFRYJS,XM_LXQF,XM_PSZT,XM_SZZT,XM_JXZT,XM_JSZT,XM_GJJB"} ;
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
		
		u1 = new UE.ui.Editor({ toolbars : $.webapp.ue_simple() });
		u1.render('u1');
		u1.addListener('ready', function(e) {
			$.post($.webapp.root + "/admin/oa/project/getProjectDetail.do", {"id": projectId}, function(result){
				if(result) {
					$("#title_proj_name").html(result.proj_name) ;
					$("input[name=proj_level][value="+result.proj_level+"]").iCheck('check') ;
					
					
					$("#basic_pro_form, #relation_pro_form").form('load', result);
					
					$("input[name=proj_start_time]").val($.date.format(result.proj_start_time, "yyyy-MM-dd")) ;
					$("input[name=proj_end_time]").val($.date.format(result.proj_end_time, "yyyy-MM-dd")) ;
					partake_dept.combotree("setValues", (undefined !=result.proj_partake_dept_ids?result.proj_partake_dept_ids.split(","):""));
					u1.setContent((undefined==result.proj_description?"":result.proj_description));
					
					 
					initMembersShow(result.proj_approve_person_ids, result.proj_approve_person_names, "selectList_approve", "proj_approve_person_ids", "proj_approve_person_names") ;
					initMembersShow(result.proj_owner_id, result.proj_owner_name, "selectList_owner", "proj_owner_id", "proj_owner_name") ;
					initMembersShow(result.proj_manager_ids, result.proj_manager_names, "selectList_pm", "proj_manager_ids", "proj_manager_names") ;
					initMembersShow(result.proj_operation_ids, result.proj_operation_names, "selectList_oper", "proj_operation_ids", "proj_operation_names") ;
					initMembersShow(result.proj_viewer_member_ids, result.proj_viewer_member_names, "selectList_viewer", "proj_viewer_member_ids", "proj_viewer_member_names") ;
					initMembersShow(result.proj_testing_engine_ids, result.proj_testing_engine_names, "selectList_test", "proj_testing_engine_ids", "proj_testing_engine_names") ;
					initMembersShow(result.proj_sqa_member_ids, result.proj_sqa_member_names, "selectList_sqa", "proj_sqa_member_ids", "proj_sqa_member_names") ;
					
					loadMembersWorkTime();
					$.easyui.loaded("#detailCenter");
				}
			},"JSON").error(function(){$.easyui.loaded("#detailCenter");});
		});
	});
	function loadMembersWorkTime() {
		developsDetail = $("#developsDetail").datagrid({
			url: $.webapp.root + "/admin/oa/project_dev_worktime/by_project_dev_datagrid.do?proj_id="+projectId,
			title: '开发人员作业起止日期及加班设置', iconCls: 'icon-hamburg-date',
			idField: 'person_id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200,500,1000], rownumbers: true,
			remoteSort: true, toolbar: '#developsDetail_toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'work_status', sortOrder: 'asc',
			frozenColumns: [[
 				{ field: 'ck', checkbox: true },
      		    { field: 'id', title: 'ID', width: 250, hidden: true },
      		    { field: 'proj_id', title: 'person_id', width: 250, hidden: true },
      		    { field: 'person_id', title: 'person_id', width: 250, hidden: true }
      		]],
     		columns: [[
     		    { field: 'person_num', title: '人员编号', width: 80 },
     		    { field: 'person_name', title: '人员姓名', width: 80 },
     		    { field: 'proj_role', title: '担任角色', width: 120},
     		    { field: 'work_startDate', title: '开始日期', width: 80, formatter: function(value, row, index){
     		    	return (undefined != value&&""!=value?$.date.format(value, "yyyy-MM-dd"):"") ;
     		    } },
     		    { field: 'work_endDate', title: '结束日期', width: 80, formatter: function(value, row, index){
     		    	return (undefined != value&&""!=value?$.date.format(value, "yyyy-MM-dd"):"") ;
     		    } },
     		    { field: 'normalHour', title: '平时加班', width: 80 },
     		    { field: 'weekendHour', title: '周六日加班', width: 80 },
     		    { field: 'holidaysHour', title: '节假日加班', width: 80 },
     		    { field: 'work_status', title: '作业状态', width: 80, align: 'center', formatter: function(value, row, index){
     		    	if(value==1) return "<font color='#00CC00'>进行中</font>";
     		    	else if(value==0) return "<font color='red'>已结束</font>";
     		    } },
     		   	{ field: 'oper', title: '操作', width: 200, formatter: function(value, row) {
     		   		if(undefined != row.work_status) {
     		   			var s = "" ;
     		   			if(row.work_status == 1) {
	     		   			s+= $.string.format("<a href='javascript:exitProject(\"{0}\", \"{1}\", \"{2}\", \"{3}\", \"{4}\");' class='clickLInk ext_email'>退出项目</a>", row.id, row.person_id, row.proj_id, row.work_startDate, row.work_endDate) ;
     		   			} else {
	     		   			s+= $.string.format("<a href='javascript:by_modify_sed(\"{0}\", \"{1}\", \"{2}\");' class='clickLInk ext_email'>修改时间</a>", row.id, row.work_startDate, row.work_endDate) ;
     		   			}
     		   			s+= $.string.format("<a href='javascript:by_delete_dev_person(\"{0}\", \"{1}\", \"{2}\", \"{3}\");' class='clickLInk ext_email'>删除数据</a>", row.id, row.person_id, row.proj_id, (row.work_status==0?"0":row.work_status)) ;
				    	return s ;
     		   		} else{
     		   			return "" ;
     		   		}
			    } },
     		    { field: 'created', title: '添加时间', width: 140 },
     		    { field: 'createName', title: '添加人姓名', width: 80 },
     		    { field: 'modifyDate', title: '修改时间', width: 140 },
     		    { field: 'modifyName', title: '修改人姓名', width: 80 }
     		]]
		});
	}
	
	//人员退出项目
	function exitProject(id, person_num, proj_id, startDate, endDate) {
		$.easyui.loading({ msg: "数据修改中，请稍等..." });
		$.post($.webapp.root + "/admin/oa/project_dev_worktime/by_exit_project.do", {id: id, person_num: person_num, proj_id: proj_id, work_startDate: $.date.format(startDate, "yyyy-MM-dd"), work_endDate: $.date.format(endDate, "yyyy-MM-dd")}, function(result) {
			if (result.status) {
				alertify.success(result.msg);
				$.easyui.loaded();
				developsDetail.datagrid("reload") ;
				developsDetail.datagrid('unselectAll');developsDetail.datagrid('clearSelections');
			} else {
				alertify.warning(result.msg);
				$.easyui.loaded();
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	}
	
	//修改开发人员时间
	function by_modify_sed(id, startDate, endDate) {
		$.easyui.loading({ msg: "数据修改中，请稍等..." });
		$.post($.webapp.root + "/admin/oa/project_dev_worktime/by_modify_sed.do", {id: id, work_startDate: $.date.format(startDate, "yyyy-MM-dd"), work_endDate: $.date.format(endDate, "yyyy-MM-dd")}, function(result) {
			if (result.status) {
				alertify.success(result.msg);
				$.easyui.loaded();
				developsDetail.datagrid("reload") ;
				developsDetail.datagrid('unselectAll');developsDetail.datagrid('clearSelections');
			} else {
				alertify.warning(result.msg);
				$.easyui.loaded();
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	}
	
	//删除开发人员
	function by_delete_dev_person(id, person_num, proj_id, work_status) {
		$.easyui.loading({ msg: "数据删除中，请稍等..." });
		$.post($.webapp.root + "/admin/oa/project_dev_worktime/by_delete_dev_person.do", {id: id, person_num: person_num, proj_id: proj_id, work_status: work_status}, function(result) {
			if (result.status) {
				alertify.success(result.msg);
				$.easyui.loaded();
				developsDetail.datagrid("reload") ;
				developsDetail.datagrid('unselectAll');developsDetail.datagrid('clearSelections');
			} else {
				alertify.warning(result.msg);
				$.easyui.loaded();
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	}
	
	
	function initMembersShow(idsData, namesData, showId, hideInPutIds, hideInputNames) {
		if(undefined != idsData && idsData.length > 0) {
			var ids = idsData.split(",") ;
			var names = namesData.split(",") ;
			var _ul = $("#"+showId) ;
			$.each(ids, function(i, id){
				_ul.append($.string.format("<li id='{0}'><span class='name'>{1}</span><span class='remove' onClick='removeMember(\"{2}\",\"{3}\",\"{4}\",\"{5}\")'>X</span></li>", id, names[i], id, showId, hideInPutIds, hideInputNames));
			});
		}
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
        //如果该人员存在，则判断该人员是否已退出项目，如果是退出项目则可以重复添加人员
        if(isExists) {
        	var index = developsDetail.datagrid("getRowIndex", row["id"]) ;
        	var select = developsDetail.datagrid("selectRow", index) ;
        	var node = developsDetail.datagrid("getSelected") ;
        	developsDetail.datagrid('unselectAll');developsDetail.datagrid('clearSelections');
        	//如果添加相同的人员，则判断该人员是否已退出项目，如果已退出，则可以继续添加人员
        	if(node.work_status == 0) {
        		isExists = false ;
        	}
        }
        if (!isExists) { 
        	var nr = {
				person_id: row.id,
				person_num: row.num,
				person_name: row.name,
				oper: $.string.format("<a href='javascript:alert(\"因功能太多，或该功能优先级别低，暂时忘了实现，请提醒我。thk\");' class='clickLInk ext_email'>{0}</a>", "删除")
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
		$.each(data, function (i, val) { setWorkDateRowCell(val, projectRole, startDate, endDate); });
		developsDetail.datagrid('unselectAll');developsDetail.datagrid('clearSelections');
	}
	//更新列数据
	function setWorkDateRowCell(row, projectRole, startDate, endDate){
		developsDetail.datagrid("updateRow",{
			index: developsDetail.datagrid("getRowIndex", row),
			row: {
				proj_role: projectRole,
				work_startDate: startDate,
				work_endDate: endDate
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
					proj_role: '',
					work_startDate: '',
					work_endDate: ''
				}
			});
		});
	}
	//设置开发人员加班时间
	function setOtDate() {
		var normalHour = $("#normalHour").val() ;
		var weekendHour = $("#weekendHour").val() ;
		var holidaysHour = $("#holidaysHour").val() ;
		if(normalHour == "" || weekendHour == "" || holidaysHour == "") {$.easyui.messager.show("加班时间不能为空！"); return;}
		
		var rows = developsDetail.datagrid("getSelections"), data = $.array.clone(rows) ;
		if(rows.length == 0){$.easyui.messager.show("您未选择任何数据。"); return;}
		$.each(data, function (i, val) { setOtRowCell(val, normalHour, weekendHour, holidaysHour); });
		developsDetail.datagrid('unselectAll');developsDetail.datagrid('clearSelections');
	}
	//更新列数据（加班时间）
	function setOtRowCell(row, normalHour, weekendHour, holidaysHour){
		developsDetail.datagrid("updateRow",{
			index: developsDetail.datagrid("getRowIndex", row),
			row: {
				normalHour: normalHour,
				weekendHour: weekendHour,
				holidaysHour: holidaysHour
			}
		});
	}
	//删除列内容
	function removeOtRowCell(){
		var rows = developsDetail.datagrid("getSelections"), data = $.array.clone(rows) ;
		if(rows.length == 0){$.easyui.messager.show("您未选择任何数据。"); return;}
		$.each(data, function (i, val) {
			developsDetail.datagrid("updateRow",{
				index: developsDetail.datagrid("getRowIndex", val),
				row: {
					normalHour: 0,
					weekendHour: 0,
					holidaysHour: 0
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
			if(undefined == val.work_status) {
		        if (index > -1) { developsDetail.datagrid("deleteRow", index); }
			}
		});
	}
	
	//获取开发人员作业日期
	function getRowsCellField() {
		var flag = 0;
		var jdl_develops_work = [] ;
		var rows = developsDetail.datagrid("getRows") ;
		if(rows.length == 0) {return flag;/*没有添加开发人员*/}
		
		$.each(rows, function (i, row) {
			if(row.work_startDate == undefined || "" == row.work_startDate || row.work_endDate == undefined || "" == row.work_endtDate) {
				flag = -1 ;	//开发人员为设置起始日期
				return false ;
			}
			//作业周期ID，人员ID，人员编号，人员姓名，项目角色，作业开始时间，作业结束时间，平时加班，周末加班，节假日加班，作业状态
			jdl_develops_work.push(row.id+","+row.person_id+","+row.person_num+","+row.person_name+","+
					row.proj_role+","+row.work_startDate+","+row.work_endDate+","+
					(undefined == row.normalHour?0:row.normalHour)+","+
					(undefined == row.weekendHour?0:row.weekendHour)+","+
					(undefined == row.holidaysHour?0:row.holidaysHour)+","+
					(undefined == row.work_status?1:row.work_status)) ;  
		});
		if(flag == 0) {
			return jdl_develops_work ;
		} else {
			return flag ;
		}
	}
	
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
	
	
	//提交表单数据
	var save = function() {
		if($('#basic_pro_form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			
			var projectOwner = $("#selectList_owner li") ;
			if(projectOwner.length == 0 || projectOwner.length > 1) {
				$.easyui.loaded();$.easyui.loaded(); $.messager.alert("警告", "项目负责人必须和只能设置一人！", "warning");
				return ;
			}			
			var projectApprove = $("#selectList_approve li") ;
			if(projectApprove.length == 0) {
				$.easyui.loaded();$.easyui.loaded(); $.messager.alert("警告", "项目审批人不能为空！", "warning");
				return ;
			}	
			
			var devMembersWorkDate = "";
			var devRows = getRowsCellField() ;
			if(devRows != 0) {
				if(devRows == -1) {
					$.easyui.loaded();$.easyui.loaded(); $.messager.alert("警告", "开发人员的作业周期未设置！", "warning");
					return ;
				} else {
					devMembersWorkDate = devRows.join("|") ;
				}
			}
			
			var basicform = $.webapp.serializeObject("#basic_pro_form") ;
			if(undefined == basicform.proj_start_time || "" == basicform.proj_start_time || undefined == basicform.proj_end_time || "" == basicform.proj_end_time) {
				$.easyui.loaded(); $.messager.alert("警告", "项目周期不能为空！", "warning");
				return ;
			}
			
			var relationform = $.webapp.serializeObject("#relation_pro_form") ;
			
			//组装数据
			var o = {} ;
			o["proj_status"] = status ;
			o["proj_owner_dept_name"] = owner_dept.combotree("getText") ;
			o["proj_partake_dept_names"] = partake_dept.combotree("getText") ;
			addAttr(o, basicform) ;
			addAttr(o, relationform) ;
			
			//开发人员的作业日期
			o["develops_worktime"] = devMembersWorkDate ;
			
			$.post($.webapp.root + "/admin/oa/project/by_UpdateProject.do", o, function(result) {
				if (result.status) {
					$.easyui.loaded();
					alertify.success(result.msg);
					back();
				} else {
					$.easyui.loaded();$.messager.alert("错误", result.msg, "error");
				}
			}, 'json').error(function() { $.easyui.loaded();$.messager.alert("错误", result.msg, "error"); });
		}
	};
	//添加数据
	function addAttr(datas, obj){
		$.each(obj, function(p, v){
			datas[p] = v ;
		}) ;
	}
	
	function back() {
		centerPanel.panel("refresh", $.webapp.root + "/admin/oa/project/project_detail_view.do") ;
	}
</script>
<div class="easyui-layout" data-options="fit:true">
		
	<div data-options="region:'north',split:false" style="height:60px;border-top:none;border-left:none;border-right:none;border-bottom: 5px solid #3f9bca;">
		<div id="project_detail_head">
			<p id="title" class="nav">
				<strong>项目管理 &gt;&gt; 项目中心 &gt;&gt; <span id="title_proj_name"></span> &gt;&gt; 项目详细</strong>
			</p>
			<p id="opts">
				<a class="opt iconrSave" href="javascript:void(0);" onclick="save()">保存</a>
				<a class="opt iconrBack" href="javascript:void(0);" onclick="back()">返回</a>
			</p>
		</div>
	</div>
	
	<div data-options="region:'center',border:false">
		<div id="tabsContainer">
			<c:if test="${icf:hasPermit('enable_basic')}">
			<div id="basic" class="panel-container" data-options="title: '立项及预算', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
				<form id="basic_pro_form">
					<input type="hidden" name="id">
					<input type="hidden" name="proj_status">
					<input type="hidden" name="createName">
					<div class="form_container">
						<table class="tableform">
							<tr>
								<th style="width:150px;">项目编号：</th> 
								<td><input name="proj_num" class="easyui-validatebox i300" type="text" /></td>
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
								<td><input name="proj_quot" class="easyui-validatebox i300" style="width:309px;" type="text" /></td>
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
			</c:if>
			
			<c:if test="${!icf:hasPermit('enable_gxr')}">
			<script>
			$("#member").hide();
			</script>
			</c:if>
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
								<th>项目审批人：</th>
								<td colspan="3">
									<ul id="selectList_approve" class="selectList" style="max-width:600px;"></ul>
									<input name="proj_approve_person_ids" type="hidden" />
									<input name="proj_approve_person_names" type="hidden" />
									<a onClick="addMembers('selectList_approve','proj_approve_person_ids', 'proj_approve_person_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_approve','proj_approve_person_ids', 'proj_approve_person_names');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
								</td>
							</tr>
							<tr>
								<th>所属部长：</th>
								<td colspan="3">
									<ul id="selectList_owner" class="selectList" style="max-width:600px;"></ul>
									<input name="proj_owner_id" type="hidden" />
									<input name="proj_owner_name" type="hidden" />
									<a onClick="addMembers('selectList_owner','proj_owner_id', 'proj_owner_name');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_add'">添加</a>
									<a onClick="removeAllMembers('selectList_owner','proj_owner_id', 'proj_owner_name');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-redo2'">清空</a>
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
			
		    
		    <c:if test="${!icf:hasPermit('enable_kfry')}">
		    <script>
			$("#jdl_param").hide();
			</script>
			 </c:if>
		    <div id="jdl_param" class="panel-container" data-options="title: '开发人员设置', iconCls: 'icon-standard-layout-header', refreshable: false">
				<div id="developsDetail">
					<div id="developsDetail_toolbars">
		            	<div class="s_box">
							<div class="gr">
								<div class="st">项目角色：</div>
								<div class="si1"><input id="KFRYJS" name="projectRole" class="easyui-combobox" style="width:160px;height:25px;"></input></div>
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
									<a onClick="deleteDevMemberRow();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_remove'">删除未保存行</a>
			            		</div>
		            		</div>
		            	</div>
		            	<div class="s_box">
							<div class="gr">
								平时加班：<input id="normalHour" name="normalHour" value="0" class="easyui-numberbox" data-options="min:0,precision:1" type="text" style="width:103px" />
								周末加班：<input id="weekendHour" name="weekendHour" value="0" class="easyui-numberbox" data-options="min:0,precision:1" type="text" style="width:103px" />
								节假日加班：<input id="holidaysHour" name="holidaysHour" value="0" class="easyui-numberbox" data-options="min:0,precision:1" type="text" style="width:103px" />&nbsp;
			            	</div>
		            		<div class="gr">
			            		<div class="st">
				            		<a style="float:left;" onClick="setOtDate();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-settings'">设置</a>
				            		<a style="float:left;" onClick="removeOtRowCell();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-metro-remove'">清空</a>
			            		</div>
		            		</div>
		            	</div>
		            </div>
				</div>
		    </div>
		   
		    
		</div>
	</div>
	
</div>
