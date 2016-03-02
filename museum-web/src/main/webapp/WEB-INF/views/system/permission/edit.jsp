<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="pageTitle.rights.management"></fmt:message></title>
</head>

<body>
    <h2 style="position:relative">
    	<c:if test="${action=='create'}"><fmt:message key="pageTitle.create.permission"></fmt:message></c:if>
    	<c:if test="${action=='edit'}"><fmt:message key="pageTitle.edit.permission"></fmt:message></c:if>
    </h2>
    <hr/>
    
  <form:form id="inputForm" modelAttribute="aPer" action="${ctx}/system/permission/save" method="post" class="form-horizontal">
  <c:if test="${not empty aPer.id}"><form:hidden path="id"/></c:if>
	  <div class="well-small well well-bgwhite">
	    <blockquote>
	      <h3><fmt:message key="pageTitle.tips"></fmt:message>:</h3>
	      <p><fmt:message key="pageTitle.filter.access.permission"></fmt:message></p>
	    </blockquote>
	    <!--h4.margin-top-bottom-small Input attributes-->
	    <ul class="unstyled">
	      <li>
	        <label><fmt:message key="lable.permission.name"></fmt:message></label>
	        <form:input path="displayname" cssClass="required"/>
	      </li>
		    <li>
	        <label><fmt:message key="lable.permission.value"></fmt:message></label>
	        <form:input path="value" cssClass="required"/>
	      </li>
	      <li>
	        <label><fmt:message key="lable.description"></fmt:message></label>
	        <form:input path="desp"/>
	      </li>
	      <li>
	        <label><fmt:message key="lable.link"></fmt:message></label>
	        <form:input path="link" cssClass="required"/>
	      </li>
			  <li>
	        <label><fmt:message key="lable.priority"></fmt:message></label>
	        <form:input path="priority"/>
	      </li> 
	      
	      <li>
			  	<label><fmt:message key="lable.choose.parent.permission"></fmt:message></label>
			    <form:select path="superPermission.id" data-placeholder="Please choose" cssClass="chzn-autoselect">
			    	<form:option value=""></form:option>
			    	<c:if test="${not empty aPer.superPermission.id }">
			    		<form:option value="${aPer.superPermission.id}">${aPer.superPermission.displayname}</form:option>
			    	</c:if>
			    	<c:if test="${empty aPer.superPermission.id }">
			    		<form:option value=""></form:option>
			    	</c:if>
		        </form:select>
		        
		        <script>
			        $(".chzn-autoselect").ajaxChosen({
			        	original_chosen_option:{allow_single_deselect: true},
			        	  method: 'GET',
			        	  url: '${ctx}/system/permission/fetchPermissionList',
			        	  dataType: 'json'
			        	}, function (data) {
			        	  var terms = {};
			        	  $.each(data, function (i, val) {
			        	    terms[i] = val;
			        	  });
		        	  return terms;
		        	});
		        </script>
			  </li>
			</ul>
	  </div><!--well-small well well-bgwhite  -->
	  
	  <div class="form-actions">
	    <button type="submit" class="btn btn-large btn-primary"><fmt:message key="btn.submit"></fmt:message></button>
	    <a class="btn btn-large" href="${ctx}/system/permission/list"><fmt:message key="btn.cancel"></fmt:message></a>
	  </div>
	</form:form>
	<script type="text/javascript">
		$("#inputForm").validate();
	</script>
</body>
