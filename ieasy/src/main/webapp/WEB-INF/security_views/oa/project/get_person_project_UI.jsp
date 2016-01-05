<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var $person_project_info_dg ;
	$(function() {
		$person_project_info_dg = $("#person_project_info_dg").datagrid({
			url: $.webapp.root+"/admin/oa/project/get_person_project_info.do",
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200,500,1000], rownumbers: true, showFooter: true,
			remoteSort: true, striped:true, pagination: true, singleSelect: false, queryParams: {"person_id": "${person_id}"},
			frozenColumns: [[
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'person_num', title: '编号', width: 60, sortable: true },
			    { field: 'person_name', title: '姓名', width: 80 },
			    { field: 'proj_num', title: '项目编号', width: 100 },
			    { field: 'proj_name', title: '项目名称', width: 250, tooltip: true },
			    { field: 'dept_name', title: '部门', width: 150 },
			    { field: 'positionName', title: '公司岗位', width: 100, align: 'center'},
			    
			    { field: 'empStatus', title: '员工状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "在职"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "离职"){return "<font color='red'>"+value+"</font>";}
			    	else if(value == "退休"){return "<font color='#B91D47'>"+value+"</font>";}
			    }},
			    
			    { field: 'proj_quot', title: '项目系数', width: 80, align: 'center' },
			    { field: 'proj_role', title: '担任角色', width: 80, align: 'center' },
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
				{ field: 'work_status', title: '状态', width: 80, align: 'center', sortable: true, formatter: function(value, row, index){
			    	if(value==1) return "<font color='#00CC00'>进行中</font>";
			    	else if(value==0) return "<font color='red'>已结束</font>";
			    	else return "-" ;
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
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$person_project_info_dg.datagrid('unselectAll');$person_project_info_dg.datagrid('clearSelections');
			}
		});
	});
	
</script>

<div id="person_project_info_dg"></div>