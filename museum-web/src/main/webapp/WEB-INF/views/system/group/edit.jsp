<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title>权限组管理</title>
</head>

<body>
    <h2 style="position:relative">
    	<c:if test="${action=='create'}"><fmt:message key="pageTitle.create_access_group" /></c:if>
    	<c:if test="${action=='edit'}"><fmt:message key="pageTitle.edit_access_group" /></c:if>
    </h2>
    <hr/>
  <form:form id="inputForm" modelAttribute="aGroup" action="${ctx}/system/group/save" method="post" class="form-horizontal">
  <c:if test="${not empty aGroup.id}"><form:hidden path="id"/></c:if>
	  <div class="well-small well well-bgwhite">
	    <blockquote>
	      <h3><fmt:message key="top.tips"/></h3>
	      <p><fmt:message key="top.tips_content_group"/></p>
	    </blockquote>
	    <!--h4.margin-top-bottom-small Input attributes-->
	    <ul class="unstyled">
	      <li>
	        <label><fmt:message key="th.group_name"/></label>
	        <form:input path="name" cssClass="required"/>
	      </li>
	      <li class="permissionList">  }
					<div>
						<label for="accessPermissions" class="field">Please select the permissions</label>
						<form:checkboxes path="accessPermissions" items="${allAPers}" itemLabel="displayname" itemValue="id"/>
					</div>
	      </li>
			</ul>
	  </div><!--well-small well well-bgwhite  -->
	  
	  <div class="form-actions">
	    <button type="submit" class="btn btn-large btn-primary"><fmt:message key="btn.submit"/></button>
	    <a class="btn btn-large" href="${ctx}/system/group/list"><fmt:message key="btn.cancel"/></a>
	  </div>
	</form:form>
		<script type="text/javascript">
		$("#inputForm").validate();
	</script>
</body>
