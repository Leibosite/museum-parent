<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="pageTitle.evaluation_reference_standard"/></title> 

</head>
<body>

 <div id="js-ajax-repalce-block" style="position:relative">
   <h2 style="position:relative"><fmt:message key="pageTitle.evaluation_reference_customize_standard_list" />
   		<shiro:hasPermission name="ReferenceStandard:create">
	   	 <span class="oper-block-title btn-group">
	   	 	<a class="btn btn-mini btn-primary" href="create">
	     		<span class="icon-plus-sign icon-white"></span></a>
	     </span>
	     </shiro:hasPermission>
	     </h2>
    <hr/>
     <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
       <form:input path="name" placeholder="Name" cssClass="input"/>
      <button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i><fmt:message key="btn.search" /></button>
    </form:form>
    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
      	<tr>
	       <th><fmt:message key="th.name"/></th>
	       <th><fmt:message key="th.area_name"/></th>
	       <th><fmt:message key="th.customize_standard"/></th>
	       <th><fmt:message key="th.customize_standard_desp"/></th>       
	       <th><fmt:message key="th.operation" /></th>
	    </tr>
      </thead>
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="customizedStandard">
	      <tr>
	        <td><c:out value="${customizedStandard.name}"/></td>
	        <td><c:out value="${customizedStandard.area.name}"></c:out>
	        <td><c:out value="${customizedStandard.standard.name}"></c:out>
	        <td><c:out value="${customizedStandard.desp}"></c:out></td>
	        <td>
	        	<shiro:hasPermission name="ReferenceStandard:update">
	            <a class="badge badge-info" href="edit/${customizedStandard.id}"><fmt:message key="btn.edit" /></a>
	            </shiro:hasPermission>
	            <shiro:hasPermission name="ReferenceStandard:delete">
		        <a class="badge badge-important delete-item" href="delete/${customizedStandard.id}"><fmt:message key="btn.delete" /></a>
		        </shiro:hasPermission>
		        <shiro:hasPermission name="ReferenceStandard:view">
		        <a class="badge badge-info" href="${ctx}/evaluation/customize/show/${customizedStandard.id}"><fmt:message key="btn.viewStandard" /></a>
		        </shiro:hasPermission>
	        </td>
	      </tr>
        </c:forEach>
      </tbody>
    </table>
    <%@ include file="../../component/pagination.jsp" %>
</div>
</body>