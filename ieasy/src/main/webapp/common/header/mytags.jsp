<%@ taglib prefix="ict" uri="/ieasy-core-tags"%>
<%@ taglib prefix="icf" uri="/ieasy-core-fun"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>

<%String serverHost = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();%>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>
<%String contextPath = request.getContextPath();%>
<c:set var="serverHost" value="<%=serverHost%>" />
<c:set var="webRoot" value="<%=basePath%>" />
<c:set var="contextPath" value="<%=contextPath%>" />
