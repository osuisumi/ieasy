<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
			title: '开发人员作业日期起止明细', iconCls: 'icon-hamburg-date',
			idField: 'id', fit: true, border: false, rownumbers: true, width:function(){return document.body.clientWidth*0.1;}, height:395,
			remoteSort: false, toolbar: '#jdl_dg_toolbars', striped:true, pagination: false, singleSelect: false, fitColumns: true,
			frozenColumns: [[
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'person_num', title: '人员编号', width: parseInt($(this).width()*0.12) },
			    { field: 'person_name', title: '人员姓名', width: parseInt($(this).width()*0.12) },
			    { field: 'proj_role', title: '担任角色', width: parseInt($(this).width()*0.12)},
			    { field: 'work_startDate', title: '开始日期', width: parseInt($(this).width()*0.12), formatter: function(value, row, index){
			    	return $.date.format(value, "yyyy-MM-dd") ;
			    } },
			    { field: 'work_endDate', title: '结束日期', width: parseInt($(this).width()*0.12), formatter: function(value, row, index){
			    	return $.date.format(value, "yyyy-MM-dd") ;
			    } },
			    { field: 'normalHour', title: '平时加班', width: parseInt($(this).width()*0.12) },
			    { field: 'weekendHour', title: '周六日加班', width: parseInt($(this).width()*0.12) },
			    { field: 'holidaysHour', title: '节假日加班', width: parseInt($(this).width()*0.12) },
			    { field: 'normalHour1', title: '平时加班', width: parseInt($(this).width()*0.12) },
			    { field: 'weekendHour1', title: '周六日加班', width: parseInt($(this).width()*0.12) },
			    { field: 'holidaysHour1', title: '节假日加班', width: parseInt($(this).width()*0.12) },
			    { field: 'work_status', title: '作业状态', width: parseInt($(this).width()*0.12), formatter: function(value, row, index){
			    	if(value==0) return "<font color='#00CC00'>进行中</font>";
			    	else return "<font color='red'>已结束</font>";
			    } },
			    { field: 'created', title: '添加时间', width: parseInt($(this).width()*0.12) },
			    { field: 'createName', title: '添加人姓名', width: parseInt($(this).width()*0.12) },
			    { field: 'modifyDate', title: '修改时间', width: parseInt($(this).width()*0.12) },
			    { field: 'modifyName', title: '修改人姓名', width: parseInt($(this).width()*0.12) }
			]]
		});
		
		$.easyui.loading({ msg: "数据加载中，请稍等...", locale: "#detailCenter" });
		$.post($.webapp.contextPath + "/admin/oa/project/getProjectDetail.do", {"id": projectId}, function(result){
			if(result) {
				$.each(result, function(p, v){
					$("span[name="+p+"]").html(v) ;
				});
				$("#title_proj_name").html(result.proj_name) ;
				$("span[name=proj_start_time]").text($.date.format(result.proj_start_time, "yyyy-MM-dd")) ;
				$("span[name=proj_end_time]").text($.date.format(result.proj_end_time, "yyyy-MM-dd")) ;
				
				$.post($.webapp.contextPath + "/admin/oa/project_dev_worktime/by_Project_Datagrid.do", {"proj_id": projectId}, function(result){
					developsDetail.datagrid("loadData", result) ;
					
					$.easyui.loaded("#detailCenter");
				},"JSON").error(function(){$.easyui.loaded("#detailCenter");});
			}
		},"JSON").error(function(){$.easyui.loaded("#detailCenter");});
	});
	
	function projectDetailEdit() {
		centerPanel.panel("refresh", $.webapp.contextPath + "/admin/oa/project/project_detail_edit.do") ;
	}
	
	function destroy() {
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				alert("del");
			}
		});
	}
	
</script>
<div class="easyui-layout" data-options="fit:true">
		
	<div data-options="region:'north',split:false" style="height:60px;border-top:none;border-left:none;border-right:none;border-bottom: 5px solid #3f9bca;">
		<div id="project_detail_head">
			<p id="title" class="nav">
				<strong>项目管理 &gt;&gt; 项目中心 &gt;&gt; <span id="title_proj_name"></span> &gt;&gt; 项目详细</strong>
			</p>
			<p id="opts">
				<a class="opt iconrRemove" href="javascript:void(0);" onclick="destroy();">销毁项目</a>
				<a class="opt iconrEdit" href="javascript:void(0);" onclick="projectDetailEdit();">修改项目</a>
			</p>
		</div>
	</div>
	
	<div data-options="region:'center',border:false">
		<div id="tabsContainer">
			<div id="basic" class="panel-container" data-options="title: '基本信息', iconCls: 'icon-standard-layout-header', refreshable: false, selected: true">
				<div class="form_container">
					<table class="tableform">
						<tr>
							<th colspan="4" style="text-align:left;">项目详情基本信息</th>
						</tr>
						<tr>
							<th style="width:150px;">项目编号：</th>
							<td style="min-width:300px;"><span name="proj_num"></span></td>
							<th style="width:150px;">项目级别：</th>
							<td style="min-width:300px;"><span name="proj_level"></span>&nbsp;级</td>
						</tr>
						<tr>
							<th>项目名称：</th>
							<td><span name="proj_name"></span></td>
							<th>项目周期：</th>
							<td>
								<span name="proj_start_time"></span> 至 <span name="proj_end_time"></span>
							</td>
						</tr>
						<tr>
							<th>项目创建人：</th> 
							<td><span name="proj_creator_name"></span></td>
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
							<th>项目负责人：</th>
							<td><span name="proj_owner_name"></span></td>
							<th>项目经理：</th>
							<td><span name="proj_manager_name"></span></td>
						</tr>
						<tr>
							<th>案件编号：</th>
							<td><span name="proj_ajbh"></span></td>
							<th>作业范围：</th>
							<td><span name="proj_zyfw"></span></td>
						</tr>
						<tr>
							<th>项目规模：</th>
							<td><span name="proj_xwgm"></span></td>
							<th>顾客返回BUG率目标：</th>
							<td><span name="proj_gkfhbuglmb"></span></td>
						</tr>
						<tr>
							<th>报价总人月：</th>
							<td><span name="proj_bjzry"></span></td>
							<th>预计投入总人月数：</th>
							<td><span name="proj_yjtrzrys"></span></td>
						</tr>
						<tr>
							<th>报价生产性：</th>
							<td><span name="proj_bjscx"></span></td>
							<th>初始粗利润率：</th>
							<td><span name="proj_csclrl"></span></td>
						</tr>
						<tr>
							<th>当前粗利润率：</th>
							<td><span name="proj_dqclyl"></span></td>
							<th>预定生产性：</th>
							<td><span name="proj_ydscx"></span></td>
						</tr>
						<tr>
							<th>合同评审状态：</th>
							<td><span name="proj_htpszt"></span></td>
							<th>财务结算状态：</th>
							<td><span name="proj_cwjszt"></span></td>
						</tr>
						<tr>
							<th>跟进级别：</th>
							<td><span name="proj_gjjb"></span></td>
							<th>财务预定完成结算时间：</th>
							<td><span name="proj_cwydwcjssj"></span></td>
						</tr>
						<tr>
							<th>财务完成结算时间：</th>
							<td><span name="proj_cwwcjssj"></span></td>
							<th>项目结算年份：</th>
							<td><span name="proj_xmjsnf"></span></td>
						</tr>
						<tr>
							<th>合同受注状态：</th>
							<td><span name="proj_htszzt"></span></td>
							<th>结项状态：</th>
							<td><span name="proj_jszt"></span></td>
						</tr>
						
						<tr>
							<th>参与部门：</th>
							<td colspan="3"><span name="proj_partake_dept_names"></span></td>
						</tr>
						<tr>
							<th>项目查看人：</th>
							<td colspan="3"><span name="proj_viewer_member_names"></span></td>
						</tr>
						<tr>
							<th>开发工程师：</th>
							<td colspan="3"><span name="proj_develop_engine_names"></span></td>
						</tr>
						<tr>
							<th>测试工程师：</th>
							<td colspan="3"><span name="proj_testing_engine_names"></span></td>
						</tr>
						<tr>
							<th>质量保障工程师：</th>
							<td colspan="3"><span name="proj_sqa_member_names"></span></td>
						</tr>
						<tr>
							<th>附件：</th>
							<td colspan="3"><span name="attach"></span></td>
						</tr>
						<tr>
							<th valign="top">项目描述：</th>
							<td colspan="3"><span name="proj_description"></span></td>
						</tr>
					</table>
					
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
					
				</div>
			
			</div>
			<div id="developDetailTab" class="panel-container" data-options="title: '开发人员明细', iconCls: 'icon-standard-layout-header', refreshable: false">
				<div id="developsDetail">
				</div>
			</div>
			
			<div id="tasklist" class="panel-container" data-options="title: '任务列表', iconCls: 'icon-standard-layout-header', refreshable: false">
			</div>
			<div id="issue" class="panel-container" data-options="title: '问题追踪', iconCls: 'icon-standard-layout-header', refreshable: false">
			</div>
			<div id="jdl_info" class="panel-container" data-options="title: '稼动率', iconCls: 'icon-standard-layout-header', refreshable: false">
				
			</div>
		</div>
	</div>
	
</div>
