<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>人员查询</title>
<%@ include file="/common/header/meta.jsp"%>
<%@ include file="/common/header/script.jsp"%>
<script type="text/javascript">
	var $dg;
	$(function() {
		//数据字典
		var data = {"dictCode": "JDL_DBMTYPE,JDL_LBMTYPE,YGZT"} ;
		$.post($.webapp.root+"/admin/system/dict/doNotNeedSession_dictAttrMaps.do", data, function(result) {
			$.each(result, function(i, p) {
				$("#"+i).combobox({valueField: 'text', textField: 'text', panelHeight: 'auto', data: p}) ;
			});
		}, 'json');
		
		$dg = $("#dg").datagrid({
			url: $.webapp.root+"/admin/oa/project/view_person_datagrid.do",
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200,500,1000], rownumbers: true,
			remoteSort: true, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'num', sortOrder: 'asc',
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
			columns: [[
			    { field: 'num', title: '编号', width: 80, sortable: true },
			    { field: 'name', title: '姓名', width: 100 },
			    { field: 'sex', title: '性别', width:50, sortable: true, align: 'center'},
			    { field: 'org_name', title: '组织机构', width: 180, tooltip: true }, 
			    { field: 'position_name', title: '岗位', width: 100 },
			    { field: 'operation', title: '操作', width: 150, align: 'center', formatter: function(value, row) {
			    	var ws = row.byProjectWorkStatus ;
			    	var s = $.string.format("<a href='javascript:void(0);' onclick='project_info(\"{0}\")' class='clickLInk ext_project'>项目信息</a>", row.id) ;
			    	s+= $.string.format("<a href='javascript:void(0);' onclick='changeWS(\"{0}\", \"{1}\")' class='clickLInk ext_project'>变更状态</a>", row.id, (ws==""?"0":ws)) ;
			    	return s ;
			    } },
			    { field: 'byProjectWorkStatus', title: '工作状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "1"){return "<font color='#00CC00'>待机</font>";}
			    	else if(value == "0"){return "<font color='red'>在项目中</font>";}
			    	else if(value == "-1"){return "-";}
			    }},
			    { field: 'empState', title: '员工状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "在职"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "离职"){return "<font color='red'>"+value+"</font>";}
			    	else if(value == "退休"){return "<font color='#B91D47'>"+value+"</font>";}
			    }},
			    { field: 'enterDate', title: '入职日期', align: 'center', width:80, sortable: true },
			    { field: 'dimissionDate', title: '离职日期', width: 90, rowspan:2, align: 'center', formatter:function(value, row, index){
					if(undefined != value) {
						return $.date.format(value, "yyyy-MM-dd") ;
					} else {
						return "-" ;
					}
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
			    { field: 'mobile', title: '手机号码', width:100, sortable: true },
			    { field: 'email', title: '邮箱地址', width: 200, formatter: function(value, row) {
			    	return $.string.format("<a href='javascript:alert(\"因功能太多，或该功能优先级别低，暂时忘了实现，请提醒我。thk\");' class='clickLInk ext_email'>{0}</a>", value) ;
			    } },
			    { field: 'created', title: '创建时间', width: 140 },
 			    { field: 'createName', title: '创建者', width: 140 },
 			    { field: 'modifyDate', title: '最后修改时间', width: 140 },
 			    { field: 'modifyName', title: '修改者', width: 140 }
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg.datagrid('unselectAll');$dg.datagrid('clearSelections');$dg.datagrid('unselectAll');
			}
		}) ;
		org_search = $("#org_search").combotree({
			url : $.webapp.root+"/static_res/org.tree.json",
			editable: false, lines:true,
			onChange: function(newValue, oldValue) {
				$dg.datagrid("load",{org_id: newValue});
				
            }
	    });
	});
	
	function doSearch(value,name){
		var o = {} ; o[name] = value ; $dg.datagrid("load",o);
	}
	function searchBox() {
		var search_form = $("#searchForm").form("getData") ;
		$dg.datagrid("load",search_form);
	}
	function searchClear() {
		$("#searchForm").form("clear") ;
		$dg.datagrid("load",{});
	}
	
	function changeWS(id, s) {
		if(s==-1){
			alert("该人员已离职，不可修改工作状态！") ;
			return ;
		}
		$.messager.confirm("您确定要进行该操作？", function (c) { 
			if(c) {
				$.easyui.loading({ msg: "数据处理中，请稍等..."});
				$.post($.webapp.root + "/admin/system/person/doNotNeedAuth_modifyWorkStatus.do", {"id": id, "byProjectWorkStatus": s}, function(result){
					if(result.status) {
						$dg.datagrid("reload");
						$.easyui.loaded();
					}
				},"JSON").error(function(){$.easyui.loaded();});
			}
		});
	}
	
	function project_info(person_id) {
		var $d = $.easyui.showDialog({
			href: $.webapp.root + "/admin/oa/project/get_person_project_UI.do?person_id="+person_id, title: "人员项目明细", iniframe: false, topMost: true, maximizable: true,
			width: (1200 >= parent.$.webapp.getInner().width? parent.$.webapp.getInner().width-80:1200),
			height: (800 > parent.$.webapp.getInner().height? parent.$.webapp.getInner().height-60:800),
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           	]
        });
	}
	
	function export_emp_data() {
		var p = handlerParams(); 
		$("#download").attr("src", $.webapp.root+"/admin/system/person/exportPersonExcelData.do?1=1"+p);
	}
	function handlerParams() {
		var params = "" ;
		var data = $("#searchForm").form("getData") ;
		$.each(data, function(p, v){
			if("" != v) {
				params += "&"+p+"="+encodeURI(v) ;
			}
		});
		return params ;
	}
</script>
</head>

<body>

	<div id="dg">
		<div id="toolbars">
			<form id="searchForm">
				<div class="s_box">
					<div class="gr">
						<div class="si">
				         	<input class="easyui-searchbox" data-options="searcher:doSearch,width: 150, height: 25, menu: '#topSearchboxMenu'" />
							<div id="topSearchboxMenu" style="width: 85px;">
								<div data-options="name:'num', iconCls: 'icon-hamburg-zoom'">编号</div>
								<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">姓名</div>
								<div data-options="name:'email', iconCls: 'icon-hamburg-zoom'">邮件</div>
							</div>
						</div>
					</div>
					<div class="gr">
						<div class="st">&nbsp;部门&nbsp;</div>
						<div class="si">
							<input id="org_search" name="org_id" style="width:250px;height:25px;" />
						</div>
					</div>
					
					<div class="gr">
						<div class="st">员工状态&nbsp;</div> 
						<div class="si1">
							<input id="YGZT" name="empState" style="width:120px;height:25px;" />
	            		</div>
						<div class="st">到部门状态</div> 
						<div class="si1">
							<input id="JDL_DBMTYPE" name="dbmType" style="width:120px;height:25px;" />
	            		</div>
						<div class="st">离部门状态</div> 
						<div class="si1">
							<input id="JDL_LBMTYPE" name="lbmType" style="width:120px;height:25px;" />
	            		</div>
	            	</div>
				</div>
	            
	            <div id="search_bar">
	            	<div class="s_box">
		            	<div class="gr">
							<div class="st">&nbsp;工作状态&nbsp;</div>
							<div class="si">
								<select name="byProjectWorkStatus" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="height:25px;width:90px;">
									<option value="">请选择</option>
									<option value="1">待机</option>
									<option value="0">在项目中</option>
								</select>
							</div>
						</div>
						<div class="gr">
							<div class="st">入职日期&nbsp;</div> 
							<div class="si1">
								<input id="rz1" name="rz_startDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									maxDate:'#F{$dp.$D(\'rz2\')||\'2020-10-01\'}'
									})"/>
								至
								<input id="rz2" name="rz_endDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'rz1\')}',
									maxDate:'2120-10-01'
									})"/>
		            		</div>
							<div class="st">离职日期</div> 
							<div class="si1">
								<input id="lz1" name="lz_startDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									maxDate:'#F{$dp.$D(\'lz2\')||\'2020-10-01\'}'
									})"/>
								至
								<input id="lz2" name="lz_endDate" class="Wdate" style="height:23px;" type="text" onFocus="WdatePicker({
									isShowClear:true,
									readOnly:true,
									minDate:'#F{$dp.$D(\'lz1\')}',
									maxDate:'2120-10-01'
									})"/>
		            		</div>
		            	</div>
		            	<div class="gr">
							<a onClick="searchBox();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">查询</a>
							<a onClick="searchClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置</a>
				            <a onclick="$dg.datagrid('reload');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'ext_reload'">刷新</a>
						</div>
	            	</div>
	            	<div class="s_box">
	            		<div class="gr">
		            		<a onclick="export_emp_data();" class="easyui-linkbutton" data-options="plain: true, iconCls:'icon-hamburg-down'">导出数据</a>
	            		</div>
	            	</div>
	            	
	            </div>
	        </form>
        </div>
	</div>
	<iframe id="download" style="display:none"></iframe> 
</body>
</html>