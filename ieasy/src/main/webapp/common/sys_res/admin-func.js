(function ($) {
	$.util.namespace("func");
	var myinfo_form = "#myinfo_form", modify_pwd = "#modify_pwd" ;

	/**
	 * 个人信息编辑
	 */
	window.func.myinfo = function() {
		var form_url = $.webapp.root+"/admin/system/myinfo/my_info_form_UI.do" ;
		var $d = $.easyui.showDialog({
			href: form_url, title: "个人信息", iniframe: false, topMost: true, maximizable: true,
			width: (982 >= parent.$.webapp.getInner().width? parent.$.webapp.getInner().width-80:982),
			height: (690 > parent.$.webapp.getInner().height? parent.$.webapp.getInner().height-60:690),
			enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
			buttons : [ 
	           { text : '保存', iconCls : 'icon-standard-disk', handler : function() { $.easyui.parent.submitForm($d, false) ; } },
	           { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } } 
           ]
		});
	};
	
	/**
	 * 修改密码
	 */
	window.func.modifyPwd = function() {
		var form_url = $.webapp.root+"/admin/system/myinfo/my_pwd_form_UI.do" ;
		var $d = $.easyui.showDialog({
			href: form_url, title: "修改密码", iniframe: false, topMost: true, maximizable: true,
			width: 400,
			height: 190,
            enableApplyButton: false, enableCloseButton: false,  enableSaveButton: false,
            buttons : [ 
              { text : '关闭', iconCls : 'ext_cancel', handler : function() { $d.dialog('destroy'); } },
              { text : ' 修改', iconCls : 'ext_save', handler : function() { $.easyui.parent.submitForm($d, false) ; } }
           	]
        });
	};
	
	window.func.homepage_proj_info = function() {
		$.post($.webapp.root+"/admin/system/notice/view_datagrid.do", {
			type: "项目管理注意事项", rows: 11, sort: "created", order: "desc",
			approve: 1
		}, function(result) {
			if (result) {
				var list_box = $("#proj_info");
				list_box.html("");
				$.each(result.rows, function(p,v){
					var a = $.string.format("<a href='javascript:void(0);' id='{2}'>{0}</a><span>{1}</span>", v.title, $.date.format(v.created, "yyyy-MM-dd"), v.id);
					list_box.append(a);
				});
				
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	};
	
	window.func.homepage_sqa_info = function() {
		$.post($.webapp.root+"/admin/system/notice/view_datagrid.do", {
			type: "质量管理注意事项", rows: 11, sort: "created", order: "desc",
			approve: 1
		}, function(result) {
			if (result) {
				var list_box = $("#sqa_info");
				list_box.html("");
				$.each(result.rows, function(p,v){
					var a = $.string.format("<a href='javascript:void(0);' id='{2}'>{0}</a><span>{1}</span>", v.title, $.date.format(v.created, "yyyy-MM-dd"), v.id);
					list_box.append(a);
				});
			}
		}, 'json').error(function() { $.easyui.loaded(); });
	};
	
})(jQuery);

$(function(){
	window.func.homepage_proj_info();
	window.func.homepage_sqa_info();
	
	$("span#proj_info_more").click(function(){
		window.mainpage.addModuleTab({
			title: "项目管理注意事项",
			iniframe: true,
			href: $.webapp.root + "/admin/system/notice/info_main_type_UI.do?type=proj_info"
		});
	});
	$("span#sqa_info_more").click(function(){
		window.mainpage.addModuleTab({
			title: "质量管理注意事项",
			iniframe: true,
			href: $.webapp.root + "/admin/system/notice/info_main_type_UI.do?type=sqa_info"
		});
	});
	$("#sqa_info").on("click", "a", function(){
		$.webapp.open($.webapp.root + "/admin/system/notice/info_open_UI.do?id="+$(this).attr("id")) ;
	});
	$("#proj_info").on("click", "a", function(){
		$.webapp.open($.webapp.root + "/admin/system/notice/info_open_UI.do?id="+$(this).attr("id")) ;
	});
	$("#proj_info_icon").click(function(){
		window.func.homepage_proj_info();
	});
	$("#sqa_info_icon").click(function(){
		window.func.homepage_sqa_info();
	});
});

