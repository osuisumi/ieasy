<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>多部门稼动率汇总统计</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var layoutContainer, centerPanel, $dg_jdl, org_tree, chart, year = $.date.format(new Date(), 'yyyy') ;
	
	var options = {
		chart: {
		    plotBackgroundColor: null,
		    plotBorderWidth: null,
		    plotShadow: false,
		    renderTo:'container'
		},
        title: {
            text: year+'部门每月稼动率趋势图'
        },
        exporting : {
			filename : '部门每月稼动率趋势图'
		},
        xAxis: {
        	categories: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            labels : {
				align : 'center',
				style : {
					fontSize : '12px',
					fontWeight: 'bold',
					color: '#000',
					fontFamily : 'Verdana, sans-serif'
				}
			}
        },
        yAxis: {
            min: 0,
            title: {
                text: '百分比 %'
            },
            labels : {
				style : {
					fontWeight: 'bold',
					color: '#000',
					fontFamily : 'Verdana, sans-serif'
				}
			}
        },
        enabled: false,
        tooltip : {
        	headerFormat: '<span style="font-size:12px"><b>{point.key}</b></span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true,
            crosshairs: true
		}
	};
	
	$(function() {
		layoutContainer = $("#layoutContainer");
		centerPanel = layoutContainer.layout("panel", "center") ;
		
		org_tree = $("#org_tree").tree({
			url : $.webapp.root+"/admin/system/org/combo_sync_tree.do",
			lines: true, checkbox: true, cascadeCheck: true, onlyLeafCheck: true,
			onClick: function(node){
					org_tree.tree("check", node.target);
			}
	    });
		
		$("#jdl_report_func").click(function() {
			var dept_ids = [] ;
			var dept = org_tree.tree("getChecked") ;
			if(dept.length > 0) {
				$.each(dept, function(i,p){
					dept_ids.push(p.id) ;
				});
				loadData(dept_ids.join(",")) ;
			} else {
				alertify.warning("请选择一条记录！");
			}
		});
		
		$("button.change").click(function(){
			var type = $(this).attr("value");
			options.tooltip.headerFormat= '<span style="font-size:12px"><b>{point.key}</b></span><table>',
			options.tooltip. pointFormat= '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' + '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>';
            options.tooltip.footerFormat= '</table>';
            options.tooltip.shared= true;
            options.tooltip.useHTML= true;
            options.tooltip.crosshairs= true;
			options.chart.type = type;
			chart = new Highcharts.Chart(options);
		});
	});
	
	function loadData(dept_ids) {
		$.easyui.loading({ msg: "数据提交中，请稍等...", locale: "#reportShow" });
		datagridFun();
		year = $("#searchYear").combobox("getValue") ;
		var params = {} ;
		
		//不计算稼动率的人员ID
		var pids = [] ;
		var noSum_person_ids = $("#noSum li") ;
		if(undefined != noSum_person_ids) {
			$.each(noSum_person_ids, function(i){
				pids.push($(this).attr("id")) ;
			});
			params["exclude_person_ids"] = pids.join(",") ;
		}
		params["searchYear"] = year ;
		params["dept_ids"] = dept_ids ;
		$.post($.webapp.root+"/admin/oa/jdl/get_jdl_dept_report.do", params, function(result) {
			$.easyui.loaded("#reportShow", true);
			$dg_jdl.datagrid("loadData", result.rows) ;
			$dg_jdl.datagrid('clearSelections');
			
			// 图表
			$('#container').show();
			$('.chartType').show();
			options.title.text = '<p><b>部门每月稼动率趋势图,' + year + '</b></p>' ;
			options["series"] = result.report_data ;
			chart = new Highcharts.Chart(options);
		}, 'json').error(function() { $.easyui.loaded("#reportShow", true); });
	}
	
	function open_person_by_dept(dept_id) {
		var $d = $.easyui.showDialog({
			href: $.webapp.root + "/admin/oa/jdl/by_jdl_person_dialog.do?dept_id="+dept_id, title: "人员列表", iniframe: false, topMost: true, maximizable: true,
			width: 840, height: 500,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '选择', iconCls : 'icon-standard-disk', handler : function() { sx_person(); } },
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	]
        });
	}
	
	//选择不计算稼动率人员
	function sx_person() {
		var _ul = $("#noSum") ;
		
		var old_ids = "", pids = [] ;
		var noSum_person_ids = _ul.children("li") ;
		if(undefined != noSum_person_ids) {
			$.each(noSum_person_ids, function(i){
				pids.push($(this).attr("id")) ;
			});
			old_ids = pids.join(",") ;
		}
		
		var rows = parent.$("#dg_person").datagrid('getSelections');
		if (rows.length > 0) {
			for(var i=0; i<rows.length; i++) {
				if(old_ids == "") {
					_ul.append($.string.format("<li id='{0}'><span class='name'>{1}</span><span class='remove' onClick='removeMember(\"{2}\")'>X</span></li>", rows[i].id, rows[i].name, rows[i].id));
				} else {
					if(old_ids.indexOf(rows[i].id) == -1) {
						_ul.append($.string.format("<li id='{0}'><span class='name'>{1}</span><span class='remove' onClick='removeMember(\"{2}\")'>X</span></li>", rows[i].id, rows[i].name, rows[i].id));
					}
				}
			}
		}
	}
	function removeMember(id) {
		$("#noSum").children("#"+id).remove();
	}
	
	function datagridFun() {
		$dg_jdl = $("#dg_jdl").datagrid({
			idField: 'id', fit: false, border: false, rownumbers: true,
			remoteSort: true, striped:true, singleSelect: false,
			sortName: 'org_id', sortOrder: 'asc', queryParams: {"person_id": $.webapp.emp_id},
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
				{ field: 'org_name', title: '所属部门', width: 130, align: 'center', formatter:function(value, row, index){
					return $.string.format("<a href='javascript:void(0);' onClick='open_person_by_dept(\"{0}\")'>{1}</a>", row.org_id, value) ;
				} },
				{ field: 'm1', title: '一月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm2', title: '二月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm3', title: '三月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm4', title: '四月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm5', title: '五月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm6', title: '六月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm7', title: '七月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm8', title: '八月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm9', title: '九月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm10', title: '十月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm11', title: '十一月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'm12', title: '十二月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{field:'annualJdl',title:'年度稼动率',width:80,align:'center', styler: function(value, row, index){if(parseInt(value)<80)return"color:red";}, formatter:function(value,rec){
             	   return "<font class='jdl_ndjdl'>"+value+"</font>" ;
               	}},
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg_jdl.datagrid('unselectAll');$dg_jdl.datagrid('clearSelections');
				
				for(var i=1;i<=12;i++){
			        $($dg_jdl.datagrid('getColumnDom',"m"+i)).css("background","#ddd");
		        }
				for(var i=(new Date().getMonth()+1)+1;i<=12;i++){
			        $($dg_jdl.datagrid('getColumnDom',"m"+i)).css("background","#f0fff0");
		        }
				$($dg_jdl.datagrid('getColumnDom',"m"+(new Date().getMonth()+1))).css("background","#55BF3B");
				
				$($dg_jdl.datagrid('getColumnDom',"annualJdl")).css("background","#fffacd");
			}
		});
	}
	
</script>
</head>

<body>
<div id="layoutContainer" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west',split:false" style="width:250px;border-top:none;">
		<div class="easyui-layout" data-options="fit:true"> 
			<div data-options="region:'center',border:false" class="project_state_left">
				<ul id="org_tree"></ul>
			</div>
		</div>
	</div>
	
	<div id="detailCenter" data-options="region:'center',border:false">
		<div id="reportShow" class="easyui-layout" data-options="fit:true">
			
			<div data-options="region:'center',border:false">
			
				<div style="width:100%;border-top:none;border-left:none;border-right:none;border-bottom: 5px solid #3f9bca;display: table;">
					<div id="project_detail_head" style="float:left;">
						<p id="opts" style="float:left;margin-left:20px;">
							<a id="jdl_report_func" class="opt iconIssue" href="javascript:void(0);">生成部门汇总及图表</a>
						</p>
					</div>
					<div class="sy">
						<select id="searchYear" name="searchYear" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="width:80px;height:40px;">
							<option value="2011">2011</option>
							<option value="2012">2012</option>
							<option value="2013">2013</option>
							<option value="2014" selected="selected">2014</option>
							<option value="2015">2015</option>
							<option value="2016">2016</option>
							<option value="2017">2017</option>
							<option value="2018">2018</option>
						</select>
					</div>
					<div style="clear: both;">
						<ul id="noSum" class="selectList" style="max-width:1000px;"></ul>
					</div>
				</div>
				
				
				
				<div id="dg_jdl"></div>
				
				<div id="container" style="width:1007px;height:400px;display: none"></div>
				<div class="chartType">
					<button class="change easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-chart-curve'" value="line">曲线图</button>
					<button class="change easyui-linkbutton" data-options="plain: true, iconCls: 'ext_column2'" value="column">柱状图</button>
					<button class="change easyui-linkbutton" data-options="plain: true, iconCls: 'icon-hamburg-statistics'" value="bar">条形图</button>
				</div>
			</div>
			
		</div>
	</div>
</div>

</body>
</html>