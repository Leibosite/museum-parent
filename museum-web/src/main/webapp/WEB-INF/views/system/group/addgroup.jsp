<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>添加权限</title>
</head>
<body>
	<form:form id="inputForm" modelAttribute="aGroup" action="${ctx}/system/group/save/${aGroup.id}" method="post">
		<input type="hidden" name="id" value="${aGroup.id}"/>
		<fieldset class="prepend-top">
			
			<legend>添加权限组</legend>
			
			<div id="messageBox" class="error" style="display:none">输入有误，请先更正。</div>
	
			<div>
				<label for="group_name" class="field">权限组名:</label>
				<input type="text" id="group_name" name="group_name" size="40" value="${aGroup.group_name}" class="required"/>
			</div>
			<div>
				<label for="accessPermissions" class="field">权限列表:</label>
				<form:checkboxes path="accessPermissions" items="${allAPers}" itemLabel="displayname" itemValue="id"/>
			</div>	
		</fieldset>
		<div>
			<input id="submit" class="button" type="submit" value="提交"/>&nbsp;	
			<input id="cancel" class="button" type="button" value="返回" onclick="history.back()"/>
		</div>
	</form:form>
</body>
</html>
