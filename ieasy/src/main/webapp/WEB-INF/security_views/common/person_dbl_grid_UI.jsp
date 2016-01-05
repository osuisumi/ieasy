<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	window.opts = {
			idField: 'id', fit: true, border: false, pageSize: 30, pageList: [30,40,50,100,200,500,1000], rownumbers: true,
			remoteSort: false, striped:true, pagination: true, singleSelect: false,
			sortName: 'created', sortOrder: 'desc',
			frozenColumns: [[
			    { field: 'ck', checkbox: true },
			    { field: 'id', title: 'ID', width: 80, hidden: true }
			]],
	        columns: [[
			    { field: 'num', title: '员工编号', width: 80 },
			    { field: 'name', title: '员工姓名', width: 100 },
			    { field: 'byProjectWorkStatus', title: '工作状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "1"){return "<font color='#00CC00'>待机</font>";}
			    	else if(value == "0"){return "<font color='red'>在项目中</font>";}
			    	else if(value == "-1"){return "-";}
			    }},
	            { field: 'org_name', title: '组织机构', width: 180, tooltip: true }, 
			    { field: 'position_name', title: '岗位', width: 100 },
			    { field: 'sex', title: '性别', width:50, sortable: true, align: 'center'},
			    { field: 'empState', title: '员工状态', width: 60, align: 'center', formatter:function(value,row){
			    	if(value == "在职"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "离职"){return "<font color='red'>"+value+"</font>";}
			    	else if(value == "退休"){return "<font color='#B91D47'>"+value+"</font>";}
			    }},
			    { field: 'enterDate', title: '入职日期', width: 80 },
			    { field: 'dimissionDate', title: '离职日期', width: 80 },
			    { field: 'created', title: '创建时间', width: 140, sortable: true }
	        ]]
    };
	
	var dg1, dg2, params = {};
	$(function() {
		var person_url = $.webapp.root+"/admin/system/person/combo_datagrid.do" ;
		dg1 = $("#dg1").datagrid($.extend(window.opts, { url: person_url }));
		dg2 = $("#dg2").datagrid($.extend(window.opts, {url: null}));
		
		dg1.datagrid("getPager").pagination({layout:['first','prev','next','last']});
		dg2.datagrid("getPager").pagination({layout:['first','prev','next','last']});
		$("#org_search_db").combotree({
			url : $.webapp.root+"/admin/system/org/combo_sync_tree.do",
			editable: false, lines:true
	    });
	});
	
	function btn1() {
		var rows = dg1.datagrid("getRows"), data = $.array.clone(rows);
        $.each(data, function (i, val) { selectRow(val); });
        dg1.datagrid("unselectAll");
	}
	function btn2() {
		var rows = dg1.datagrid("getSelections"), data = $.array.clone(rows);
        if (!data.length) { $.easyui.messager.show("您未选择任何数据。"); return; }
        $.each(data, function (i, val) { selectRow(val); });
        dg1.datagrid("unselectAll");
	}
	function selectRow(row) {
        if (!row) { return; }
        var tOpts = dg2.datagrid("options"), idField = tOpts.idField;
        if (!row[idField]) {
            var rows = dg1.datagrid("getRows"), index = dg1.datagrid("getRowIndex", row);
            if (index > -1) { row = rows[index]; }
        }
        var isExists = dg2.datagrid("getRowIndex", row[idField] ? row[idField] : row) > -1;
        if (!isExists) { dg2.datagrid("appendRow", row); }
    }
	
	
	function btn3() {
		var rows = dg2.datagrid("getSelections"), data = $.array.clone(rows);
        if (!data.length) { $.easyui.messager.show("您未选择任何数据。"); return; }
        $.each(data, function (i, val) { unselectRow(val); });
        dg2.datagrid("unselectAll");
	}
	function btn4() {
		 var rows = dg2.datagrid("getRows"), data = $.array.clone(rows);
         $.each(data, function (i, val) { unselectRow(val); });
         dg2.datagrid("unselectAll");
	}
	function unselectRow(row) {
        if (!row) { return; }
        var index = dg2.datagrid("getRowIndex", row);
        if (index > -1) { dg2.datagrid("deleteRow", index); }
    }
	
	function doSearch(value,name){
		searchBox() ;
	}
	function searchBox() {
		var search_form = $("#cc").form("getData") ;
		$.each(search_form, function(p,v){
			params[p] = v ;
		}) ;
		dg1.datagrid("load",params); 
	}
	function searchClear() {
		params = {} ;
		$("#cc").form("clear") ;
		searchBox() ;
	}
	
	//返回已选列表数据
	var getDataGrid = function() { 
		return dg2.datagrid("getRows");
	};
	
	
</script>

<div class="easyui-layout" data-options="fit:true">
	<div data-options="region: 'north', split: false, border:false" style="height: 80px;border:0px;">
		<div class="form_container">
			<form id="cc">
				部门&nbsp;<input id="org_search_db" name="org_id" style="width:287px;height:26px;" />
				<input id="topSearchbox" class="easyui-searchbox" data-options="searcher:doSearch,width: 222, height: 26, menu: '#m1'" />
				<div id="m1" style="width: 85px;">
					<div data-options="name:'num', iconCls: 'icon-hamburg-zoom'">编号</div>
					<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">名称</div>
					<div data-options="name:'email', iconCls: 'icon-hamburg-zoom'">邮件地址</div>
				</div>
				<a onClick="searchBox();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom'">查询</a>
	         	<a onClick="searchClear();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-zoom-out'">重置</a>
			</form>
		</div>
	</div>
	<div data-options="region: 'west', title: '待选择项', split: false" style="width: 425px;border-left:0px;border-bottom:0px;">
		<div id="dg1"></div>
    </div>
    <div data-options="region: 'center'" style="border-bottom:0px;">
		<ul class="rl_icon">
			<li><a class="rAll_icon" href="javascript:btn1();">&nbsp;</a></li>
			<li><a class="r_icon" href="javascript:btn2();">&nbsp;</a></li>
			<li><a class="l_icon" href="javascript:btn3();">&nbsp;</a></li>
			<li><a class="lAll_icon" href="javascript:btn4();">&nbsp;</a></li>
		</ul>
    </div>
    <div data-options="region: 'east', title: '已选择项', split: false" style="width: 425px;border-right:0px;border-bottom:0px;">
		<div id="dg2"></div>
    </div>

</div>


