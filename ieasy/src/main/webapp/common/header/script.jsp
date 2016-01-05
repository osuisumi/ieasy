<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/common/header/mytags.jsp"%>


<%
java.util.Map<String, Cookie> cookieMap = new java.util.HashMap<String, Cookie>();
Cookie[] cookies = request.getCookies();
	if (null != cookies) {
		for (Cookie cookie : cookies) {
		cookieMap.put(cookie.getName(), cookie);
	}
}
String themeName = "bootstrap";
if (cookieMap.containsKey("themeName")) {
	Cookie cookie = (Cookie) cookieMap.get("themeName");
	themeName = cookie.getValue();
}
%> 

<link href="<%=basePath%>/script/jquery-easyui-theme/<%=themeName%>/easyui.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/script/jquery-easyui-theme/icon.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>/script/icons/icon-all.css" rel="stylesheet" type="text/css" />

<script src="<%=basePath%>/script/jquery/jquery-1.11.1.js" type="text/javascript"></script>

<script src="<%=basePath%>/script/jquery-easyui-1.3.6/jquery.easyui.min.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jquery-easyui-1.3.6/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>

<script src="<%=basePath%>/script/tools/jquery.jdirk.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/tools/jquery.ieasy.core.js" type="text/javascript"></script>
<script type="text/javascript">
	$.webapp.host = '<%=serverHost%>' ;
	$.webapp.root = '<%=basePath%>' ;
	$.webapp.account = "${USER_SESSION.user.account}" ;
	$.webapp.user_id = "${USER_SESSION.user.user_id}" ;
	$.webapp.emp_id = "${USER_SESSION.user.emp_id}" ;
	$.webapp.emp_num = "${USER_SESSION.user.emp_num}" ;
	$.webapp.emp_name = "${USER_SESSION.user.emp_name}" ;
	$.webapp.emp_tx = "${USER_SESSION.user.emp_tx}" ;
	$.webapp.emp_email = "${USER_SESSION.user.emp_email}" ;
	$.webapp.org_id = "${USER_SESSION.user.org_id}" ;
	$.webapp.org_name = "${USER_SESSION.user.org_name}" ;
</script>

<link href="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.css" rel="stylesheet" type="text/css" />

<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.progressbar.js"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.linkbutton.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.form.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.validatebox.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.combo.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.combobox.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.menu.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.searchbox.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.panel.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.window.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.dialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.layout.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.tree.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.datagrid.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.treegrid.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.combogrid.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.combotree.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.tabs.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.theme.js" type="text/javascript"></script>

<script src="<%=basePath%>/script/icons/jeasyui.icons.all.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.icons.js" type="text/javascript"></script>

<!-- 引入扩展插件的依赖 -->
<%@ include file="/common/header/sub_script.jsp"%>

<script src="<%=basePath%>/script/jeasyui-extensions/jquery.toolbar.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jquery.comboicons.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jeasyui.extensions.gridselector.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jquery.comboselector.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jquery.euploadify.js"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jquery.my97.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jquery.ueditor.js"></script>
<script src="<%=basePath%>/script/jeasyui-extensions/jquery.portal.js" type="text/javascript"></script>

<!-- 我的Easyui扩展 -->
<link href="<%=basePath%>/common/sys_res/page.css" rel="stylesheet"/>
<script src="<%=basePath%>/script/plugins/my-easyui-extensions/cascadeCheck.js" type="text/javascript"></script>
<script src="<%=basePath%>/script/tools/jquery.ieasy.plugin.js" type="text/javascript"></script>
