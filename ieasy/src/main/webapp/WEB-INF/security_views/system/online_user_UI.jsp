<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>在线用户统计</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg, chart ;
	$(function() {
		chart() ;
		
		$dg = $("#dg").datagrid({
			idField: 'id', fit: true, border: false, remoteSort: false, striped:true,rownumbers: true,
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'sessionId', title: 'sessionId', width: 100, hidden: true },
			    { field: 'userId', title: 'userId', width: 100, hidden: true },
			    { field: 'empId', title: '人员编号', width: 100, hidden: true }, 
			    { field: 'status', title: '用户状态', width: 100, hidden: true },
			]],
			columns: [[
			    { field: 'userAccount', title: '登陆账号', width: 100 },
			    { field: 'empName', title: '人员姓名', width: 100 },
			    { field: 'orgName', title: '所属部门', width: 140 },
			    { field: 'ip', title: 'IP地址', width: 140 },
			    { field: 'loginTime', title: '登录时间', width: 140 }
			]]
		}) ;
		loadOnlineData() ;
		
		$.post($.webapp.root+"/admin/system/log/doNotNeedAuth_loginTimeChartLL.do", null, function(result) {
			setChart(result) ;
		}, 'json');
		
	});
	
	//人数和登陆用户数据
	function loadOnlineData() {
		$.post($.webapp.root+"/admin/system/online/doNotNeedAuth_getOnlineUser.do", null, function(result) {
			$("#curent_login_count").html(result.curent_login_count);
			$("#max_online_count").html(result.max_online_count);
			$("#total_history_count").html(result.total_history_count);
			$("#datetime").html(result.datetime);
			$("#serverTime").html(result.serverTime);
			$dg.datagrid("loadData", result.users) ;
		}, 'json');
	}
	
	//强制用户退出
	function forceLogout() {
		var sids = [] ;
		var rows = $dg.datagrid('getChecked');
		if (rows.length > 0) {
			for ( var i = 0; i < rows.length; i++) {
				sids.push(rows[i].sessionId);
			}
		}
		$.post($.webapp.root+"/admin/system/online/doNotNeedAuth_forceLogout.do", {"sessionIds": sids.join(",")}, function(result) {
			loadOnlineData() ;
		}, 'json');
	}
	
	function setChart(data) {
		chart.series[0].remove(false); 
		chart.addSeries({
			data: data, 
			color: Highcharts.getOptions().colors[0],
			dataLabels: {
				enabled : true,
				rotation : -90,
				color : '#FFFFFF',
				align : 'right',
				x : 4,
				y : 10,
				style : {
					fontSize : '13px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		}, false);
		chart.redraw();
	}
	
	function chart() {
		chart = $('#container').highcharts({
	        chart: {
	        	type : 'column',
	        },
	        title: {
	            text: '用户登录时间段'
	        },
	        exporting : {
				filename : '用户登录时间分布'
			},
			xAxis : {
				categories : [ '00-02', '02-04', '04-06', '06-08', '08-10', '10-12', '12-14', '14-16', '16-18', '18-20', '20-22', '22-24' ],
				labels : {
					rotation : -45,
					align : 'right',
					style : {
						fontSize : '13px',
						fontFamily : 'Verdana, sans-serif'
					}
				}
			},
	        yAxis: {
	        	min : 0,
	            title: {
	                text: '时间段用户登录数'
	            }
	        },
	        legend : {
				enabled : false
			},
			tooltip : {
				formatter : function() {
					return '<b>' + this.x + '点</b><br/>' + '此时间段用户登录数量为: ' + this.y + '个用户';
				}
			},
	        series : [ {
	        	data: [],
	        	dataLabels : {
					enabled : true,
					rotation : -90,
					color : '#FFFFFF',
					align : 'right',
					x : 4,
					y : 10,
					style : {
						fontSize : '13px',
						fontFamily : 'Verdana, sans-serif'
					}
				}
	        } ]
	    }).highcharts();
	}
	
</script>
</head> 

<body>
	<div class="easyui-layout" data-options="fit: true">
		<div data-options="region: 'center', border: false" style="overflow: hidden;">
			<div id="portal" class="easyui-portal" data-options="fit: true, border: false">
				<div style="width: 33%;">
					<div data-options="title: '统计',
					 height: 200, collapsible: false, closable: false, iconCls: 'icon-hamburg-current-work'">
						<div style="padding:5px;line-height: 30px;">
							<table>
								<tr>
									<td align="right"></td>
									<td></td>
								</tr>
								<tr>
									<td align="right">目前登录用户：</td>
									<td id="curent_login_count"></td>
								</tr>
								<tr>
									<td align="right">最大同时在线人数：</td>
									<td><span id="max_online_count"></span>&nbsp;&nbsp;&nbsp;&nbsp;发生在<span id="datetime"></span></td>
								</tr>
								<tr>
									<td align="right">历史累计访客人数：</td>
									<td id="total_history_count"></td>
								</tr>
								<tr>
									<td align="right">服务器启动时间：</td>
									<td id="serverTime"></td>
								</tr>
							</table>
						</div>
					</div>
					<div data-options="title: '图表', height: 370, collapsible: false, closable: false, iconCls: 'icon-standard-chart-bar'">
						<div id="container" style="width: 100%; height: 100%; margin: 0 auto"></div>
					</div>
				</div>
				<div style="width: 34%;">
					<div data-options="title: '在线用户', height: 580, collapsible: false, closable: false, iconCls: 'icon-hamburg-customers', tools: [
					{iconCls: 'icon-hamburg-sign-out', handler: function () { forceLogout(); }},
					{iconCls: 'icon-hamburg-refresh', handler: function () { loadOnlineData() }}
					]">
						<div id="dg"></div>
					</div>
				</div>
			</div>
		</div>
		
	</div>

	
	

</body>
</html>
