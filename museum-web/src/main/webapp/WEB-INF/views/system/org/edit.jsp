<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
	<title>PCRF-Navi</title>
</head>
<body>
    <h2 style="position:relative">
    	<c:if test="${action=='create'}">Create Organization</c:if>
    	<c:if test="${action=='edit'}">Edit Organization</c:if>
    </h2>
    <hr/>
    
	<form:form id="inputForm" modelAttribute="orgInfo" action="${ctx}/system/org/save" method="post" class="form-horizontal">
	  <c:if test="${not empty orgInfo.id}"><form:hidden path="id"/></c:if>
	  <div class="well-small well well-bgwhite">
	    <blockquote>
	      <h3>Tips:</h3>
	      <p>This function is used for organization information</p>
	    </blockquote>
	    <!--h4.margin-top-bottom-small Input attributes-->
	    <ul class="unstyled">
	      <li>
	        <label>Name</label>
	        <form:input path="name" cssClass="required"/>
	      </li>
	      <li>
	        <label>Status</label>
	        <form:select path="status" cssClass="js-select">
	        	<form:options items="${statusMap}"/>
	        </form:select>
	      </li>
	      <li>
	        <label>Domain</label>
	        <form:input path="domain"/>
	      </li>
	      <li>
	        <label>MCC</label>
	        <form:input path="mcc"/>
	      </li>
	      <li>
	        <label>MNC</label>
	        <form:input path="mnc"/>
	      </li>
	    </ul>
	  </div>
	  <div class="form-actions">
	    <button type="submit" class="btn btn-large btn-primary">Submit</button>
	    <a class="btn btn-large" href="${ctx}/system/org/list">Cancel</a>
	  </div>
	</form:form>
		<script type="text/javascript">
		$("#inputForm").validate();
	</script>
</body>