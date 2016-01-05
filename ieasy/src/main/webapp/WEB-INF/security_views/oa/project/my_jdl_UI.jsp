<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>我的稼动率</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg, params={};
	$(function() {
		$('#icheck input').iCheck({
			checkboxClass: 'icheckbox_minimal-red'
	   	});
		$('#icheck input').on('ifUnchecked', function(event){
			$("#exclude_sum_product").val("false") ;
		});
		$('#icheck input').on('ifChecked', function(event){
			$("#exclude_sum_product").val("true") ;
		});
		
		params["person_id"] = $.webapp.emp_id ;
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/oa/jdl/by_my_jdl.do", 
			idField: 'person_id', fit: true, border: false, pageSize: 30, rownumbers: true, showFooter: true,
			remoteSort: true, toolbar: '#toolbars', striped:true, singleSelect: false,
			sortName: 'num', sortOrder: 'asc', queryParams: params,
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', hidden: true },
			    { field: 'person_num', title: '编号', width: 60, tooltip: true }, 
			    { field: 'person_name', title: '名称', width: 70 }
			   
			]],
			columns: [[
				{ field: 'org_name', title: '所属部门', width: 80, rowspan:2 },
				{ field: 'total_work_days', title: '总工作天数', width:100, sortable: true, align: 'center', rowspan:2, formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'total_quot_work_days', title: '乘系数后总天数', width:100, sortable: true, align: 'center', rowspan:2, formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
                {title:'每月稼动率',colspan:12},
                {field:'annualJdl',title:'年度稼动率',width:100,align:'center', styler: function(value, row, index){if(parseInt(value)<60)return"color:red";}, rowspan:2, formatter:function(value,rec){
             	   return "<font class='jdl_ndjdl'>"+value+"</font>" ;
               	}},
                {title:'每月标准天数(排除周六日)',colspan:12},
                {field:'totalBzts',title:'总标准工作天数',width:100,align:'center', rowspan:2, formatter:function(value,rec){
             	   return "<font class='jdl_ndjdl'>"+value+"</font>" ;
               	}},
               	{ field: 'emp_status', title: '员工状态', width: 70, sortable: true, align: 'center', formatter:function(value, row, index){
			    	if(value == "在职"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "离职"){return "<font color='red'>"+value+"</font>";}
			    	else if(value == "退休"){return "<font color='#B91D47'>"+value+"</font>";}
			    }, rowspan:2},
				{ field: 'emp_enter_date', title: '入职日期', align: 'center', width: 80, rowspan:2 },
				{ field: 'emp_dimission_date', title: '离职日期', align: 'center', width: 80, rowspan:2 },
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
				}}
           ],[ 
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
				
				{ field: 'bzts1', title: '一月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts2', title: '二月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts3', title: '三月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts4', title: '四月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts5', title: '五月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts6', title: '六月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts7', title: '七月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts8', title: '八月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts9', title: '九月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts10', title: '十月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts11', title: '十一月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} },
				{ field: 'bzts12', title: '十二月', width: 60, align: 'center', formatter:function(value, row, index){
					return "<font class='jdl'>"+value+"</font>" ;
				} }
            ]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg.datagrid('unselectAll');$dg.datagrid('clearSelections');$dg.datagrid('unselectAll');
				
				for(var i=1;i<=12;i++){
			        $($dg.datagrid('getColumnDom',"m"+i)).css("background","#F0FBEB");
		        }
				for(var i=(new Date().getMonth()+1)+1;i<=12;i++){
			        $($dg.datagrid('getColumnDom',"m"+i)).css("background","#f8f8ff").css("color","#000");
		        }
				for(var i=1;i<=12;i++){
			        $($dg.datagrid('getColumnDom',"bzts"+i)).css("background","#f0fff0");
		        }
		        $($dg.datagrid('getColumnDom',"annualJdl")).css("background","#fffacd");
		        $($dg.datagrid('getColumnDom',"total_work_days")).css("background","#fffaf0");
		        $($dg.datagrid('getColumnDom',"total_quot_work_days")).css("background","#fffaf0");
		        $($dg.datagrid('getColumnDom',"totalBzts")).css("background","#ffffe0");
			}
		}) ;
		org_search = $("#org_search").combotree({
			url : $.webapp.root+"/admin/system/org/combo_sync_tree.do?pid="+$.webapp.org_id,
			editable: false, lines:true
	    });
	});
	
	function doSearch(value,name){
		searchBox() ;
	}
	function searchBox() {
		var search_form = $("#searchForm").form("getData") ;
		$.each(search_form, function(p,v){
			params[p] = v ;
		}) ;
		
		//排除不计算的人员
		var person_ids = [] ;
		var isSumJdl =$("#exclude_sum_product").val();
		if("true" == isSumJdl) {
			var rows = $dg.datagrid('getSelections');
			if (rows.length > 0) {
				for(var i=0; i<rows.length; i++) {
					person_ids.push(rows[i].person_id) ;
				}
				params["exclude_person_ids"] = person_ids.join(",") ;
			}
		}
		
		$dg.datagrid("load",params); 
	}
	function searchClear() {
		$(".sx").hide() ;
		$("#searchForm").form("clear") ;
		$("#searchForm").form('load', {"searchYear": $.date.format(new Date(), 'yyyy')});
		params = {} ;
		params["org_id"] = null ;
		params["person_id"] = $.webapp.emp_id ;
		searchBox() ;
	}
	
	function my_dept_jdl() {
		$(".sx").show() ;
		params = {} ; 
		org_search.combotree("reload", $.webapp.root+"/admin/system/org/combo_sync_tree.do?pid="+$.webapp.org_id) ;
		org_search.combotree("setValue", $.webapp.org_id) ;
		searchBox();
	}
</script>
</head>

<body>

	<div id="dg">
		<div id="toolbars">
			<form id="searchForm">
				<div class="s_box">
					<c:if test="${icf:hasPermit('my_dept_jdl')}">
	           		<div class="gr">
	            		<a onclick="my_dept_jdl();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-bremen-sitemap'">我的部门</a>
	           		</div>
	           		<div class="gr sx" style="display: none;">
	            		<div class="si">
		            		<input class="easyui-searchbox" data-options="searcher:doSearch,width: 228, height: 25, menu: '#topSearchboxMenu'" />
							<div id="topSearchboxMenu" style="width: 85px;">
								<div data-options="name:'person_num', iconCls: 'icon-hamburg-zoom'">编号</div>
								<div data-options="name:'person_name', iconCls: 'icon-hamburg-zoom'">姓名</div>
							</div>
	            		</div>
	           		</div>
	           		<div class="gr sx" style="display: none;">
	           			<div class="st">部门&nbsp;</div>
	           			<div class="si">
	            			<input id="org_search" name="org_id" style="width:287px;height:25px;" />
	            		</div>
	           		</div>
	           		<div class="gr sx" style="display: none;">
	           			<div class="st">
	            			<span id="icheck">
								<input id="exclude_sum_product" type="checkbox" name="exclude_sum_product" value="false">
								<label for="exclude_sum_product">不计算稼动率</label>
							</span>
	            		</div>
	           		</div>
	           		</c:if>
	           		<div class="gr">
						<div class="st">&nbsp;年份&nbsp;</div>
						<div class="si">
							<select id="searchYear" name="searchYear" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="height:25px;">
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
					</div>
	           		<div class="gr">
						<a onClick="searchBox();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">查询</a>
					    <a onClick="searchClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置</a>
			            <a onclick="$dg.datagrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
					</div>
	           	</div>
           	</form>
        </div>
	</div>
</body>
</html>