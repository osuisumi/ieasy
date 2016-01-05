<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>${title}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="easy">
	<meta name="description" content="easy">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
	<link rel="shortcut icon" href="${domain}/images/sys_img/favicon.ico">
	<style>
		body,h1,h2,h3,ul,li{margin:0px;padding:0px;}body{width:1000px;margin:0 auto;font-size:14px;}
		.tableform{border-collapse:collapse;width:100%;}.tableform th{background:#F2F7FE;padding:3px;border:1px solid #B8CCE2;padding-left:5px;color:#333;height:24px;text-align:left;}
		.tableform td{padding:2px;border:1px solid #B8CCE2;padding-left:5px;color:#333;height:24px;}
		ul{list-style:none;}#top{height:80px;border-bottom:6px solid #3F9BCA;position:relative;}
		#top h2{line-height:80px;font-size:26px;text-align:center;}#top .pd{position:absolute;right:0px;bottom:5px;display:inline-block;}
		#center{}#center .list{height:25px;line-height:25px;float:left;margin:2px 12px;}#center .list span.l{display:inline-block;float:left;width:60px;} 
		#center .list span.r{display:inline-block;float:right;}#center .list_nd{height:25px;line-height:25px;float:left;margin:2px 12px;}
		#footer{margin-top:10px;border-top:3px solid #3F9BCA;height:50px;line-height:20px;}#footer ul{margin-top:5px;float:right;}#footer li{text-align:right;}
		#footer li a{text-decoration:none;color:red;}
	</style>
</head>

<body>

	<div id="top">
		<h2>${title}</h2>
		<span class="pd">打印日期：${.now?string("yyyy/MM/dd")}</span>
	</div>
	<div id="center">
		<table class="tableform">
			<tr>
				<th style="width:150px;">项目编号：</th>
				<td style="min-width:300px;">${project.proj_num!}</td>
				<th style="width:150px;">跟进级别：</th>
				<td style="min-width:300px;">
					<#if project.proj_level??>
						${project.proj_level}&nbsp;级
					</#if>
				</td>
			</tr>
			<tr>
				<th>项目名称：</th>
				<td style="max-width:300px;">${project.proj_name!}</td>
				<th>项目周期：</th>
				<td>${project.proj_start_time?string("yyyy-MM-dd")}~<font color="red">${project.proj_end_time?string("yyyy-MM-dd")}</font></td>
			</tr>
			<tr>
				<th>ID类型区分：</th>
				<td>${project.distinguish!}</td>
				<th>合同项目类型：</th>
				<td>${project.proj_type!}</td>
			</tr>
			<tr>
				<th>项目创建人：</th> 
				<td>${project.proj_creator_name!}</td>
				<th>所属部门：</th>
				<td>${project.proj_owner_dept_name!}</td>
			</tr>
			<tr>
				<th>项目负责人：</th>
				<td>${project.proj_owner_name!}</td>
				<th>项目PM：</th>
				<td>${project.proj_manager_names!}</td>
			</tr>
			<tr>
				<th>项目审批人：</th>
				<td>${project.proj_approve_person_names!}</td>
				<th>项目状态：</th>
				<td>
					<#if project.proj_status == 2>
						<font color='green'>进行中</font>
					<#elseif project.proj_status == 3>
						<font color='orange'>已挂起</font>
					<#elseif project.proj_status == 4>
						<font color='red'>已结束</font>
					<#else>
						<font color='red'>未知</font>
					</#if>
				</td>
			</tr>
			<tr>
				<th>项目系数：</th>
				<td colspan="3">${project.proj_quot!}</td>
			</tr>
			<tr>
				<th>参与部门：</th>
				<td colspan="3">${project.proj_partake_dept_names!}</td>
			</tr>
			<tr>
				<th>质量保障工程师：</th>
				<td colspan="3">${project.proj_sqa_member_names!}</td>
			</tr>
			
			<tr>
				<th colspan="4">开发工程师</th>
			</tr>
			<tr>
				<td colspan="4">
					<#if project.proj_status == 4>
						<#list devList as d>
							<div class="list" style="color:red;">
								<span class="l">${d.person_name}</span><span class="r">[${d.work_startDate?string("yyyy-MM-dd")}~${d.work_endDate?string("yyyy-MM-dd")}]</span>
							</div>
						</#list>
					<#else>
						<#list devList as d>
							<div class="list">
								<span class="l">${d.person_name}</span><span class="r">[${d.work_startDate?string("yyyy-MM-dd")}~${d.work_endDate?string("yyyy-MM-dd")}]</span>
							</div>
						</#list>
					</#if>
				</td>
			</tr>
			<#if project.proj_status == 4>
			<tr>
				<td colspan="4"><b style="color:red;font-size:12px;">项目作业时间结束,开发人员将自动退出项目，及如果开发人员的结束日期大于项目结束日期，将重置开发人员的结束日期为项目的结束日期。</b></td>
			</tr>
			</#if>
		</table>
	</div>
	<div id="footer">
		<ul>
   			<li>广州华智科技有限公司&copy;项目管理部</li>
   			<li>如无法正常显示，请点击<a href="${link}">&nbsp;这里&nbsp;</a>仅华智内部使用</li>
   		</ul>
	</div>
    
</body>
</html>



