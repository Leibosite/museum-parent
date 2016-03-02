<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="pageTitle.sms_template_list"/></title>
</head>

<body>
<div id="js-ajax-repalce-block" style="position:relative">
   <h2 style="position:relative"><fmt:message key="pageTitle.sms_template_list" />
   		<shiro:hasPermission name="SmsTemplate:create">
	   	 <span class="oper-block-title btn-group">
	   	 	<a class="btn btn-mini btn-primary" id="create">
	     		<span class="icon-plus-sign icon-white"></span>
	     	</a>
	     </span>
	     </shiro:hasPermission>
	     </h2>
    <hr/>
    
     <script type="text/javascript">
	  //get edit form page
	  	$('#create').click(function(){
	  		$("#list-table").simpleRequestForm({
	  			ajaxUrl:"${ctx}/sms/template/create?ajax=true"
	  		});
	  	});
	</script>
    
   <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
    <form:input path="template" placeholder="template content" cssClass="input"/>
    <button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i><fmt:message key="btn.search" /></button>
 	 </form:form>

    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
        <tr>
        	<th><fmt:message key="pojo.SmsTemplate.name"></fmt:message></th>
	      	<th><fmt:message key="th.templateContent" /></th>
	     	<th><fmt:message key="th.operation" /></th>
	    </tr>
      </thead>
      
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="template">
	      <tr>
	      	<td><c:out value="${template.name}" /></td>
	        <td><c:out value="${template.templateContent}" /></td>
	        <td>
	        <shiro:hasPermission name="SmsTemplate:update">
	        <a class="badge badge-info editTemplate"  data-id="${template.id}" ><fmt:message key="btn.edit" /></a>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="SmsTemplate:delete">
	        <a class="badge badge-important delete-item"  href="delete/${template.id}"><fmt:message key="btn.delete" /></a>
	        </shiro:hasPermission>
	        </td>
	      </tr>
        </c:forEach>
      </tbody>
    </table>
    
     <script type="text/javascript">
	  //get edit form page
	  	$('.editTemplate').click(function(){
	  		
	  		$("#list-table").simpleRequestForm({
	  			ajaxUrl:"${ctx}/sms/template/edit?id="+$(this).attr("data-id")+"&ajax=true",
	  		});
	  	});
	</script>

	 <%@ include file="../../component/pagination.jsp" %>
	 </div>
</body>

