<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="pageTitle.access_user"></fmt:message></title>
</head>

<body>
    <h2 style="position:relative">
    	<c:if test="${action=='create'}"><fmt:message key="pageTitle.create_access_user" /></c:if>
    	<c:if test="${action=='edit'}"><fmt:message key="pageTitle.edit_access_user" /></c:if>
    </h2>
    <hr/>
  <form:form id="inputForm" modelAttribute="user" action="${ctx}/system/user/save" method="post" class="form-horizontal">
  <c:if test="${not empty user.id}"><form:hidden path="id"/></c:if>
	  <div class="well-small well well-bgwhite">
	    <blockquote>
	      <h3><fmt:message key="top.tips" /></h3>
	      <p><fmt:message key="top.tips_content_user" /></p>
	    </blockquote>
	    <!--h4.margin-top-bottom-small Input attributes-->
	    <ul class="unstyled">
	      <li>
	        <label><fmt:message key="th.login_name" /></label>
	        <form:input path="loginName" cssClass="required"/>
	      </li>
		    <li>
	        <label><fmt:message key="th.user_name" /></label>
	        <form:input path="name"/>
	      </li>
			  <li>
	        <label><fmt:message key="th.password"/></label>
	        <form:input path="password" cssClass="required"/>
	      </li>
	      <li>
	        <label><fmt:message key="th.email"/></label>
	        <form:input path="email" cssClass="email"/>
	      </li>
	      <li class="permissionList">
					<div>
						<label for="accessGroups" class="field"><fmt:message key="th.select_access_user"/></label>
						<form:checkboxes path="accessGroups" items="${allGroups}" itemLabel="name" itemValue="id"/>
					</div>
	      </li>
			</ul>
	  </div><!--well-small well well-bgwhite  -->
	  
	  <div class="form-actions">
	    <button type="submit" class="btn btn-large btn-primary"><fmt:message key="btn.submit"/></button>
	    <a class="btn btn-large" href="${ctx}/system/user/list"><fmt:message key="btn.cancel"/></a>
	  </div>
	</form:form>
	
	<script type="text/javascript">
		$("#inputForm").validate();
	</script>
</body>
