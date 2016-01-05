<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
    <title>项目分布</title>
	<%@ include file="/common/header/meta.jsp"%>
	<%@ include file="/common/header/script.jsp"%>
	<link rel="stylesheet" href="<%=basePath%>/css/report.css" />
    <script type="text/javascript">
    var options = {
   		chart: {
	           plotBackgroundColor: null,
	           plotBorderWidth: null,
	           plotShadow: false,
           },
           title: {
               style: { color: '#000', font: 'bold 16px "Trebuchet MS", Verdana, sans-serif' }
           },
           exporting : {
   			filename : '华智项目状态统计'
   		},
   		colors: ["#55BF3B", "#058DC7", "#8BBC21", "#ED561B", "#910000"], 
           tooltip: {
           	headerFormat: '<span style="font-size:12px"><b>{point.key}</b></span><br>',
       	    pointFormat: '<font style="font-size:12px;"><b>{series.name}</b></font>: <b>{point.percentage:.1f}%</b>'
           },
           plotOptions: {
               pie: {
                   allowPointSelect: true,
                   cursor: 'pointer',
                   dataLabels: {
                       enabled: true,
                       useHTML: true,
                       color: '#000000',
                       connectorColor: '#000000',
                       format: '<font style="font-size:12px;"><b>{point.name}</b></font>: {point.percentage:.1f} %',
                       distance: 22
                   },
                   size:180
               },
           },
           series: [{
               type: 'pie',
               name: '率',
               data: []
           }]
   	};
	$(function() {
		$.post($.webapp.root+"/admin/oa/project/get_project_id_type_count_report.do", function(result) {
			$(".report_text div#TA").css("color","#55BF3B").children("span:eq(1)").html(result[0][1]) ;
			$(".report_text div#HTXM").css("color","#058DC7").children("span:eq(1)").html(result[1][1]) ;
			$(".report_text div#ZZYF").css("color","#8BBC21").children("span:eq(1)").html(result[2][1]) ;
			$(".report_text div#GSSY").css("color","#ED561B").children("span:eq(1)").html(result[3][1]) ;
			options.series[0].data = result ;
			options.title.text = 'ID类型区分统计' ;
			options.chart.renderTo = container1 ;
			chart = new Highcharts.Chart(options);
		}, 'json').error(function() {alert("ID类型区分统计，发生错误！");});
		
		$.post($.webapp.root+"/admin/oa/project/get_project_status_count_report.do", function(result) {
			$(".report_text div#status0").css("color","#55BF3B").children("span:eq(1)").html(result[0][1]) ;
			$(".report_text div#status1").css("color","#058DC7").children("span:eq(1)").html(result[1][1]) ;
			$(".report_text div#status2").css("color","#8BBC21").children("span:eq(1)").html(result[2][1]) ;
			$(".report_text div#status3").css("color","#ED561B").children("span:eq(1)").html(result[3][1]) ;
			$(".report_text div#status4").css("color","#910000").children("span:eq(1)").html(result[4][1]) ;
			options.series[0].data = result ;
			options.title.text = '项目状态统计' ;
			options.chart.renderTo = container2 ;
			chart = new Highcharts.Chart(options);
		}, 'json').error(function() {alert("项目状态统计，发生错误！");});
		
		$.post($.webapp.root+"/admin/oa/project/get_project_shouzhu_count_report.do", function(result) {
			$(".report_text div#WSZ").css("color","#55BF3B").children("span:eq(1)").html(result[0][1]) ;
			$(".report_text div#YSZ").css("color","#058DC7").children("span:eq(1)").html(result[1][1]) ;
			options.series[0].data = result ;
			options.title.text = '受注状态统计' ;
			options.chart.renderTo = container3 ;
			chart = new Highcharts.Chart(options);
		}, 'json').error(function() {alert("受注状态统计，发生错误！");});
		
		$.post($.webapp.root+"/admin/oa/project/get_project_cwjszt_count_report.do", function(result) {
			$(".report_text div#WJS").css("color","#55BF3B").children("span:eq(1)").html(result[0][1]) ;
			$(".report_text div#YJS").css("color","#058DC7").children("span:eq(1)").html(result[1][1]) ;
			options.series[0].data = result ;
			options.title.text = '财务结算状态统计' ;
			options.chart.renderTo = "container4" ;
			chart = new Highcharts.Chart(options);
		}, 'json').error(function() {alert("财务结算状态统计，发生错误！");});
	});
    </script>
</head>

<body>
	
	<div id="report">
		<div class="report_box">
			<div class="report_container1">
				<div id="container1" style="width:99%;"></div>
				<div class="report_text">
					<div id="TA"><span>提案（个）：</span><span>0</span></div>
					<div id="HTXM"><span>合同项目（个）：</span><span>0</span></div>
					<div id="ZZYF"><span>自主研发（个）：</span><span>0</span></div>
					<div id="GSSY"><span>公司使用（个）：</span><span>0</span></div>
				</div>
			</div>
			<div class="report_container2">
				<div id="container2" style="width:99%;"></div>
				<div class="report_text">
					<div id="status0"><span>立项中（个）：</span><span>0</span></div>
					<div id="status1"><span>审批中（个）：</span><span>0</span></div>
					<div id="status2"><span>进行中（个）：</span><span>0</span></div>
					<div id="status3"><span>挂起中（个）：</span><span>0</span></div>
					<div id="status4"><span>已结束（个）：</span><span>0</span></div>
				</div>
			</div>
			<div class="report_container3">
				<div id="container3" style="width:99%;"></div>
				<div class="report_text">
					<div id="YSZ"><span>已受注（个）：</span><span>0</span></div>
					<div id="WSZ"><span>未受注（个）：</span><span>0</span></div>
				</div>
			</div>
			<div class="report_container4">
				<div id="container4" style="width:99%;"></div>
				<div class="report_text">
					<div id="YJS" class="center"><span>已结算（个）：</span><span>0</span></div>
					<div id="WJS" class="center"><span>未结算（个）：</span><span>0</span></div>
				</div>
			</div>
		</div>
	</div>
    
</body>
</html>



