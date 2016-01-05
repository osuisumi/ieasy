<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript">
	var $dg_person, params={};
	$(function() {
		params["org_id"] = "${dept_id}" ;
		
		$dg_person = $("#dg_person").datagrid({
			url: $.webapp.root+"/admin/oa/jdl/get_person_by_dept_datagrid.do",
			idField: 'id', fit: true, border: false, pageSize: 500, pageList: [30,40,50,100,200,500,1000], rownumbers: true,
			remoteSort: true, toolbar: '#toolbars', striped:true, pagination: true, singleSelect: false,
			sortName: 'num', sortOrder: 'asc', queryParams: params,
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
			    { field: 'empState', title: '状态', width: 60, sortable: true, align: 'center', formatter:function(value,row){
			    	if(value == "在职"){return "<font color='#00CC00'>"+value+"</font>";}
			    	else if(value == "离职"){return "<font color='red'>"+value+"</font>";}
			    	else if(value == "退休"){return "<font color='#B91D47'>"+value+"</font>";}
			    }},
			    { field: 'enterDate', title: '入职日期', align: 'center', width:80, sortable: true },
			    { field: 'dimissionDate', title: '离职日期', align: 'center', width:80, sortable: true }
			]],
			onLoadSuccess: function(node, data) {
				$.fn.datagrid.extensions.onLoadSuccess.apply(this, arguments);
				$dg_person.datagrid('unselectAll');$dg_person.datagrid('clearSelections');$dg_person.datagrid('unselectAll');
			}
		}) ;
	});
	
	function doSearch(value,name){
		params = {} ;
		params["org_id"] = "${dept_id}" ;
		params[name] = value ;
		$dg_person.datagrid("load",params);
	}
	
	//提交表单数据
	var submitNow = function($d, $tg, flag) {
		
		$.easyui.loaded(); if(flag==undefined||flag==false){$d.dialog("close") ;}
	};
	
	//验证表单
	var submitForm = function($d, $tg, flag) {
		if($('#form').form('validate')) {
			$.easyui.loading({ msg: "数据提交中，请稍等..." });
			submitNow($d, $tg, flag) ;
		} 
	};
	
</script>
</head>

<body>

	<div id="dg_person">
		<div id="toolbars">
            <input class="easyui-searchbox" data-options="searcher:doSearch,width: 250, height: 25, menu: '#topSearchboxMenu'" />
			<div id="topSearchboxMenu" style="width: 85px;">
				<div data-options="name:'num', iconCls: 'icon-hamburg-zoom'">编号</div>
				<div data-options="name:'name', iconCls: 'icon-hamburg-zoom'">姓名</div>
				<div data-options="name:'email', iconCls: 'icon-hamburg-zoom'">邮件</div>
			</div>
        </div>
	</div>
