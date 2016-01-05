<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/common/header/mytags.jsp"%>
<script type="text/javascript">
	/**
		projectId和status来至于open_project_UI.jsp页面
		这里可以获取到，因为open_project_UI.jsp是通过href的方式来打开project_detail_view.jsp的，该页面嵌入到了open_project_UI中
	*/
	var developsDetail ;
	$(function() {
		$("#tabsContainer").tabs({
			fit: true, border: false
		}) ;
		
		developsDetail = $("#developsDetail").datagrid({
			url: $.webapp.root + "/admin/oa/project_dev_worktime/by_project_dev_datagrid.do?proj_id="+projectId,
			title: '开发人员作业日期起止明细', iconCls: 'icon-hamburg-date', showFooter: true,
			idField: 'id', fit: true, border: false, pageSize: 100, pageList: [30,40,50,100,200,500,1000], rownumbers: true,
			remoteSort: true, striped:true, pagination: true, singleSelect: false,
			sortName: 'work_status', sortOrder: 'asc', toolbar: '#dev_toolbars',
			frozenColumns: [[
			    { field: 'id', title: 'ID', width: 80, hidden: true },
			    { field: 'person_num', title: '人员编号', width: 60 },
			    { field: 'person_name', title: '姓名', width: 60 },
			    { field: 'dept_name', title: '部门', width: 80 }
			]],
			columns: [[
			    { field: 'positionName', title: '公司岗位', width: 70, align: 'center'},
			    { field: 'work_status', title: '状态', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
			    	if(value==1) return "<font color='#00CC00'>进行中</font>";
			    	else if(value==0) return "<font color='red'>已结束</font>";
			    	else return "-" ;
			    } },
			    { field: 'proj_role', title: '担任角色', width: 70, align: 'center'},
			    { field: 'work_startDate', title: '开始日期', width: 80, sortable: true, align: 'center', formatter: function(value, row, index){
			    	if(undefined == value) {return "-";} 
			    	else {return "<font class='jdl'>"+$.date.format(value, "yyyy-MM-dd")+"</font>" ;}
			    	
			    } },
			    { field: 'work_endDate', title: '结束日期', width: 80, sortable: true, align: 'center', formatter: function(value, row, index){
			    	if(undefined == value) {return "-";} 
			    	else {return "<font class='jdl'>"+$.date.format(value, "yyyy-MM-dd")+"</font>" ;}
			    } },
			    { field: 'devCyc', title: '天数', width: 80, align: 'center', sortable: true, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'ry', title: '人月', width: 60, sortable: true, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
			    { field: 'ext_devCyc', title: '已消耗天数', width: 80, sortable: true, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
			    { field: 'ext_ry', title: '已消耗人月', width: 80, sortable: true, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
			    { field: 'normalHour', title: '平时加班', width: 80, sortable: true , align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
			    { field: 'weekendHour', title: '周六日加班', width: 80, sortable: true, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
			    { field: 'holidaysHour', title: '节假日加班', width: 80, sortable: true, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
			    { field: 'totalHour', title: '累计加班小时', width: 100, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'jbl', title: '加班率', width: 60, sortable: true, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'positionRecord', title: '岗位变更记录', width: 200, tooltip: true, formatter: function(value, row) {
			    	if(undefined != value && "" != value) {
			    		var str = "" ;
			    		var j = $.parseJSON("["+value+"]") ;
			    		$.each(j, function(i,p){
			    			str += "["+p.name+"#"+p.date+"]" + "==>>" ;
			    		});
			    		return str ;
			    	} else {
			    		return "-" ;
			    	}
			    } },
			    { field: 'empStatus', title: '员工状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "在职"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "离职"){return "<font color='red'>"+value+"</font>";}
			    	else if(value == "退休"){return "<font color='#B91D47'>"+value+"</font>";}
			    }},
				{ field: 'dbmType', title: '到部门类型', width: 100, align: 'center', rowspan:2, formatter:function(value, row, index){
					if(value != undefined) {
						return value ;
					} else {return "-" ;}  
				}},
				{ field: 'dbmDate', title: '到部门日期', width: 90, rowspan:2, align: 'center', formatter:function(value, row, index){
					if(undefined != value) {
						return $.date.format(value, "yyyy-MM-dd") ;
					} else {
						return "-" ;
					}
				}},
				{ field: 'lbmType', title: '离部门类型', width: 110, rowspan:2, align: 'center', formatter:function(value, row, index){
					if(value != undefined) {
						return value ;
					} else {return "-" ;}  
				}},
				{ field: 'lbmDate', title: '离部门日期', width: 90, align: 'center', rowspan:2, formatter:function(value, row, index){
					if(undefined != value) {
						return $.date.format(value, "yyyy-MM-dd") ;
					} else {
						return "-" ;
					}
				}},
			    { field: 'created', title: '添加时间', width: 140 },
			    { field: 'createName', title: '添加人姓名', width: 80 },
			    { field: 'modifyDate', title: '修改时间', width: 140 },
			    { field: 'modifyName', title: '修改人姓名', width: 80 }
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				developsDetail.datagrid('unselectAll');developsDetail.datagrid('clearSelections');
				
				
		        $(developsDetail.datagrid('getColumnDom',"work_startDate")).css("background","#fffacd");
		        $(developsDetail.datagrid('getColumnDom',"devCyc")).css("background","#fffacd");
		        $(developsDetail.datagrid('getColumnDom',"ext_devCyc")).css("background","#fffacd");
		        $(developsDetail.datagrid('getColumnDom',"work_endDate")).css("background","#fffacd");
		        $(developsDetail.datagrid('getColumnDom',"ry")).css("background","#fffacd");
		        $(developsDetail.datagrid('getColumnDom',"ext_ry")).css("background","#fffacd");
		        
		        $(developsDetail.datagrid('getColumnDom',"jbl")).css("background","#fffaf0");
		        $(developsDetail.datagrid('getColumnDom',"normalHour")).css("background","#fffaf0");
		        $(developsDetail.datagrid('getColumnDom',"weekendHour")).css("background","#fffaf0");
		        $(developsDetail.datagrid('getColumnDom',"holidaysHour")).css("background","#fffaf0");
		        $(developsDetail.datagrid('getColumnDom',"totalHour")).css("background","#fffaf0");
			}
		});
		
		$.easyui.loading({ msg: "数据加载中，请稍等...", locale: "#detailCenter" });
		$.post($.webapp.root + "/admin/oa/project/getProjectDetail.do", {"id": projectId}, function(result){
			if(result) {
				document.title = result.proj_name;
				$("#title_proj_name").html(result.proj_name) ;
				$.each(result, function(p, v){
					$("span[name="+p+"]").html(v) ;
				});
				$("span[name=proj_level]").html((undefined==result.proj_level||""==result.proj_level?"-":result.proj_level+"&nbsp;级")) ;
				$("span[name=proj_const_money]").text(parseFloat(result.proj_const_money)+" (元)") ;
				$("span[name=proj_start_time]").text($.date.format(result.proj_start_time, "yyyy-MM-dd")) ;
				$("span[name=proj_end_time]").text($.date.format(result.proj_end_time, "yyyy-MM-dd")) ;
				
				//显示操作按钮，如果是项目创建人，则根据状态进行显示操作按钮，（可以在加入条件，如负责人，或项目经理）
				if($.webapp.emp_id == result.proj_creator_id || 
						result.proj_operation_ids.indexOf($.webapp.emp_id) != -1 || 
						result.proj_viewer_member_ids.indexOf($.webapp.emp_id) != -1) {
					var ps = result.proj_status ;
					//如果状态的值不为空，则按判断进行显示功能按钮
					if(undefined != status && "" != status) {
						//判断是否显示提交审批按钮
						if(result.submitApprove) {
							$("#approveBto").show() ;
							$("#delete").show() ;
						}
						if(ps == 1) {
							$("#approveEdit").show() ;
						}
						//项目状态为：办理中，显示挂起按钮
						if(ps == 2) {
							$("#sleeping").show() ;
							$("#finish").show() ;
							$("#delete").show() ;
						}
						//项目状态为：立项中，审批中，办理中，挂起中状态，显示修改项目按钮
						if(ps == 0 || ps == 1 || ps == 2 || ps == 3) {
							$("#edit").show() ;
							$("#delete").show() ;
						}
						if(ps == 3) {
							$("#activated").show() ;
							$("#delete").show() ;
						}
						if(ps == 4) {
							$("#edit").show() ; 
							$("#changeStatus").show() ;
						}
					}
				} else if($.webapp.emp_num == "ROOT") {
					$("#sleeping").show() ;
					$("#finish").show() ;
					$("#delete").show() ;
					$("#edit").show() ;
					$("#delete").show() ;
				}
				
				//项目审批菜单--提交审批按钮
				if(status == "") {
					if(result.submitApprove) {
						$("#approveBto1").show() ;
						$("#approveBto2").show() ;
					}
				}
				
				$.easyui.loaded("#detailCenter");
			}
		},"JSON").error(function(){$.easyui.loaded("#detailCenter");});
	});
	
	//修改项目
	function projectDetailEdit() {
		centerPanel.panel("refresh", $.webapp.root + "/admin/oa/project/by_Project_edit_page.do") ;
	}
	
	//提交审批
	function submitApprove() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据处理中，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project/by_Project_Approve.do", {"personId": $.webapp.emp_id, "projectId": projectId}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
						window.opener.parentCloseRefered() ;
						window.close() ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	//项目挂起
	function sleep() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据处理中，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project/by_Project_Sleep.do", {"projectId": projectId}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
						window.opener.parentCloseRefered() ;
						window.close() ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	//项目激活
	function activated() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据处理中，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project/by_Project_Activated.do", {"projectId": projectId}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
						window.opener.parentCloseRefered() ;
						window.close() ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	//项目办结
	function finish() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据处理中，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project/by_Project_Finish.do", {"projectId": projectId}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
						window.opener.parentCloseRefered() ;
						window.close() ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	//销毁项目
	function destroy() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "正在销毁项目相关信息，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project/by_Project_delete.do", {"id": projectId}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
						window.opener.parentCloseRefered() ;
						window.close() ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	//将结束状态转换为进行中
	function changeStatus() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "正在将项目状态转换为进行中，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project/by_Project_changeStatus.do", {"projectId": projectId}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
						window.opener.parentCloseRefered() ;
						window.close() ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	//发送邮件
	function byDevSendMail() {
		var ids = [];
		var rows = developsDetail.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				if(rows[i].work_status == 1) {
					ids.push(rows[i].id);
				}
			}
		} else {
			alertify.warning("请选择一条记录！");
			return ;
		}
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "正在发送邮件，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project_dev_worktime/by_DevSendMail.do", {"proj_id": projectId, "changeIds": ids.join(",")}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
					} else {
						$.easyui.loaded();
						alert(result.msg) ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	//审批不通过，邮件通知创建者进行修改
	function noApprove_sendMail() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				var proj_creator_id = $("span[name=proj_creator_id]").text() ;
				$.easyui.loading({ msg: "正在发送邮件，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project/doNotNeedAuth_noApproveSendMail.do", {"id": projectId, "proj_creator_id": proj_creator_id}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
					} else {
						$.easyui.loaded();
						alert(result.msg) ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	//审批不通过，创建者修改数据后，邮件通知审批人查看
	function sendMail_approve() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				var proj_creator_id = $("span[name=proj_creator_id]").text() ;
				$.easyui.loading({ msg: "正在发送邮件，请稍等..."});
				$.post($.webapp.root + "/admin/oa/project/doNotNeedAuth_sendApproveMail.do", {"id": projectId, "proj_creator_id": proj_creator_id}, function(result){
					if(result.status) {
						$.easyui.loaded();
						alert(result.msg) ;
					} else {
						$.easyui.loaded();
						alert(result.msg) ;
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	function export_dev_list() {
		$("#download").attr("src", $.webapp.root+"/admin/oa/project_dev_worktime/by_export_dev_list.do?proj_id="+projectId);
	}
	
</script>
<div class="easyui-layout" data-options="fit:true">
		
	<div data-options="region:'north',split:false" style="height:60px;border-top:none;border-left:none;border-right:none;border-bottom: 5px solid #3f9bca;">
		<div id="project_detail_head">
			<p id="title" class="nav">
				<strong>项目管理 &gt;&gt; 项目中心 &gt;&gt; <span id="title_proj_name"></span> &gt;&gt; 项目详细</strong>
			</p>
			<p id="opts"> 
				<a id="approveBto" class="opt iconIssue" style="display: none;" href="javascript:void(0);" onclick="submitApprove();">提交审批</a>
				
				<a id="approveBto1" class="opt iconrRemove" style="display: none;" href="javascript:void(0);" onclick="noApprove_sendMail();">不通过</a>
				<a id="approveBto2" class="opt iconIssue" style="display: none;" href="javascript:void(0);" onclick="submitApprove();">通过</a>
				
				<a id="approveEdit" class="opt iconIssue" style="display: none;" href="javascript:void(0);" onclick="sendMail_approve();">通知已修改</a>
				
				<c:if test="${icf:hasPermit('/admin/oa/project/by_Project_delete.do')}">
				<a id="delete" class="opt iconrRemove" href="javascript:void(0);" style="display: none;" onclick="destroy();">删除</a>
				</c:if>
				<c:if test="${icf:hasPermit('/admin/oa/project/by_Project_Sleep.do')}">
				<a id="sleeping" class="opt iconrSleep" style="display: none;" href="javascript:void(0);" onclick="sleep();">挂起</a>
				</c:if>
				<c:if test="${icf:hasPermit('/admin/oa/project/by_Project_Activated.do')}">
				<a id="activated" class="opt iconrSleep" style="display: none;" href="javascript:void(0);" onclick="activated();">激活</a>
				</c:if>
				<c:if test="${icf:hasPermit('/admin/oa/project/by_Project_Finish.do')}">
				<a id="finish" class="opt iconrFinish" style="display: none;" href="javascript:void(0);" onclick="finish();">结束</a>
				</c:if>
				<c:if test="${icf:hasPermit('/admin/oa/project/by_Project_edit_page.do')}">
				<a id="edit" class="opt iconrEdit" style="display: none;" href="javascript:void(0);" onclick="projectDetailEdit();">修改</a>
				</c:if>
				<c:if test="${icf:hasPermit('/admin/oa/project/by_Project_changeStatus.do')}">
				<a id="changeStatus" class="opt iconrEdit" style="display: none;" href="javascript:void(0);" onclick="changeStatus();">转为进行状态</a>
				</c:if>
			</p>
		</div>
	</div>
	
	<div data-options="region:'center',border:false">
		<div id="tabsContainer">
			<div id="basic" class="panel-container" data-options="title: '基本信息', iconCls: 'icon-standard-layout-header', refreshable: false, selected: false">
				<div class="form_container">
					<table class="tableform">
						<tr>
							<th colspan="4" style="text-align:left;">项目详情基本信息</th>
						</tr>
						<tr>
							<th style="width:150px;">项目编号：</th>
							<td style="min-width:300px;"><span name="proj_num"></span></td>
							<th style="width:150px;">跟进级别：</th>
							<td style="min-width:300px;"><span name="proj_level"></span></td>
						</tr>
						<tr>
							<th>项目名称：</th>
							<td style="max-width:300px;"><span name="proj_name"></span></td>
							<th>项目周期：</th>
							<td><span name="proj_start_time"></span> 至 <span name="proj_end_time"></span></td>
						</tr>
						<tr>
							<th>项目创建人：</th> 
							<td><span style="display: none;" name="proj_creator_id"></span><span name="proj_creator_name"></span></td>
							<th>所属部门：</th>
							<td><span name="proj_owner_dept_name"></span></td>
						</tr>
						<tr>
							<th>项目审批人：</th>
							<td><span name="proj_approve_person_names"></span></td>
							<th>总预算资金：</th>
							<td><span name="proj_const_money"></span></td>
						</tr>
						<tr>
							<th>所属部长：</th>
							<td><span name="proj_owner_name"></span></td>
							<th>项目负责人：</th>
							<td><span name="proj_manager_names"></span></td>
						</tr>
						<tr>
							<th>ID类型区分：</th>
							<td><span name="distinguish"></span></td>
							<th>合同项目类型：</th>
							<td><span name="proj_type"></span></td>
						</tr>
						<tr>
							<th>合同受注状态：</th>
							<td><span name="proj_shouzhu"></span></td>
							<th>作业范围：</th>
							<td><span name="proj_zyfw"></span></td>
						</tr>
						<tr>
							<th>项目规模：</th>
							<td><span name="proj_gm"></span></td>
							<th>顾客返回BUG率目标：</th>
							<td><span name="proj_buglv"></span></td>
						</tr>
						<tr>
							<th>报价总人月：</th>
							<td><span name="proj_bjzry"></span></td>
							<th>预计投入总人月数：</th>
							<td><span name="proj_yjtrzry"></span></td>
						</tr>
						<tr>
							<th>报价生产性：</th>
							<td><span name="proj_bjscx"></span></td>
							<th>初始粗利润率：</th>
							<td><span name="proj_clrl"></span></td>
						</tr>
						<tr>
							<th>当前粗利润率：</th>
							<td><span name="proj_cclrl"></span></td>
							<th>预定生产性：</th>
							<td><span name="proj_ydscx"></span></td>
						</tr>
						<tr>
							<th>合同评审状态：</th>
							<td><span name="proj_htpjzt"></span></td>
							<th>财务结算状态：</th>
							<td><span name="proj_cwjszt"></span></td>
						</tr>
						<tr>
							<th>项目系数：</th>
							<td><span name="proj_quot"></span></td>
							<th>项目结算年份：</th>
							<td><span name="proj_xmjsnf"></span></td>
						</tr>
						<tr>
							<th>财务完成结算时间：</th>
							<td><span name="proj_cwwcjssj"></span></td>
							<th>结项状态：</th>
							<td><span name="proj_jxzt"></span></td>
						</tr>
						<tr>
							<th>财务预定完成结算时间：</th>
							<td><span name="proj_cwydwcjssj"></span></td>
						</tr>
						
						<tr>
							<th>参与部门：</th>
							<td colspan="3"><span name="proj_partake_dept_names"></span></td>
						</tr>
						<tr>
							<th><!-- 项目查看人 -->项目参与人：</th>
							<td colspan="3"><span name="proj_viewer_member_names"></span></td>
						</tr>
						<!-- 
						<tr>
							<th>开发工程师：</th>
							<td colspan="3"><span name="proj_develop_engine_names"></span></td>
						</tr>
						<tr>
							<th>测试工程师：</th>
							<td colspan="3"><span name="proj_testing_engine_names"></span></td>
						</tr>
						 -->
						<tr>
							<th>SQA：</th>
							<td colspan="3"><span name="proj_sqa_member_names"></span></td>
						</tr>
						<!-- 
						<tr>
							<th>附件：</th>
							<td colspan="3"><span name="attach"></span></td>
						</tr>
						 -->
						<tr>
							<th>项目备注：</th>
							<td colspan="3"><span name="proj_bak"></span></td>
						</tr>
						<tr>
							<th valign="top">项目描述：</th>
							<td colspan="3"><span name="proj_description"></span></td>
						</tr>
					</table>
					
					<!-- 
					<p style="border-bottom: 1px solid #e6e6e6;font-size: 18px;font-weight:bold;">预算资金明细</p>
					
					<table id="costDetail" class="tableform" style="margin-top:10px;">
						<tr>
							<th colspan="6" style="text-align: left;">基本支出：</th>
						</tr>
						<tr>
							<th style="width:200px;">人员工资：</th>
							<td><span name="proj_budget_amount1"></span>&nbsp;元</td>
							<th style="width:200px;">差旅费：</th>
							<td><span name="proj_budget_amount2"></span>&nbsp;元</td>
							<th style="width:200px;">设备消耗：</th>
							<td><span name="proj_budget_amount3"></span>&nbsp;元</td>
						</tr>
						<tr>
							<th colspan="6" style="text-align: left;">项目支出：</th>
						</tr>
						<tr>
							<th>项目维护：</th>
							<td><span name="proj_budget_amount4"></span>&nbsp;元</td>
							<th>资源采购：</th>
							<td><span name="proj_budget_amount5"></span>&nbsp;元</td>
							<th>活动支出：</th>
							<td><span name="proj_budget_amount6"></span>&nbsp;元</td>
						</tr>
						<tr>
							<th colspan="6" style="text-align: left;">项目费用：</th>
						</tr>
						<tr>
							<th>差旅费：</th>
							<td><span name="proj_budget_amount7"></span>&nbsp;元</td>
							<th>加班费：</th>
							<td><span name="proj_budget_amount8"></span>&nbsp;元</td>
							<th>奖金：</th>
							<td><span name="proj_budget_amount9"></span>&nbsp;元</td>
						</tr>
					</table>
					 -->
					
				</div>
			
			</div>
			<div id="developDetailTab" class="panel-container" data-options="title: '开发人员明细', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
				<div id="developsDetail">
					<div id="dev_toolbars">
			            <a onClick="export_dev_list();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-world'">导出开发人员列表</a>
			            <a id="approveBto" class="opt iconIssue" style="display: none;" href="javascript:void(0);" onclick="submitApprove();">提交审批</a>
						<c:if test="${icf:hasPermit('byDevSendMail')}">
			            <a onClick="byDevSendMail();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-email'">发送邮件</a>
						</c:if>
			        </div>
			        <iframe id="download" style="display:none"></iframe> 
				</div>
			</div>
		</div>
	</div>
	
</div>
