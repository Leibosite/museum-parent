<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="pageTitle.pma_policygroup_listPolicy"/></title>

</head>
<body>
<div id="js-ajax-repalce-block" style="position:relative">

   <h2 style="position:relative"><c:out value="${name}"/> <fmt:message key="pageTitle.pma_policies_of_policygroup"/>
	   	 <span class="oper-block-title btn-group">
	   	 	<a class="btn btn-mini btn-primary" href="${ctx}/pma/policy/addPolicy?groupId=${id}&page=${page}&id=">
	     		<span class="icon-plus-sign icon-white"></span></a>
	     </span>
	     </h2>
    <hr/>
	     <!--search banner  start  -->
    <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
      
       <form:input path="policyName" placeholder="policy name" cssClass="input"/>
        <form:select path="status" placeholder="Status" cssClass="input js-select">
        <form:option value="">--Please Select--</form:option>
        <form:option value="0">Disable</form:option>
        <form:option value="1">Enable</form:option>
      </form:select>
       <form:input path="startTime" placeholder="开始时间" cssClass="time_picker"/>
       <form:input path="endTime" placeholder="结束时间" cssClass="time_picker"/>
      
      <button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i>
      <fmt:message key="btn.search" /></button>
    </form:form>
     
     <!--search banner   end -->
    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
      	<tr>
	      
	      <th><fmt:message key="th.policyName" /></th>
	       <th><fmt:message key="th.desp" /></th>
	     <th><fmt:message key="th.salience" /></th>
	      <th><fmt:message key="th.operation" /></th>
	    </tr>
      </thead>
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="policy">
	      <tr>
	        <td><c:out value="${policy.policyName}"/></td>
	           <td width="500px"><c:out value="${policy.desp}"/></td>
	            <td><c:out value="${policy.salience}"/></td>
	        <td width="165px"><a class="badge badge-info" href="${ctx}/pma/policy/addPolicy?groupId=${id}&id=${policy.id}&page=${page}"><fmt:message key="btn.edit" /></a>
	        <a class="badge badge-important delete-item" href="${ctx}/pma/policy/deletePolicy?groupId=${id}&id=${policy.id}&page=${page}"><fmt:message key="btn.delete" /></a>
	        <c:if test="${policy.status == true}">
	        <a class="badge badge-warning" href="${ctx}/pma/policy/disable?groupId=${id}&id=${policy.id}&page=${page}"><fmt:message key="btn.disable" /></a>
	        </c:if>
	        <c:if test="${policy.status == false || empty policy.status}">
	        <a class="badge badge-success" href="${ctx}/pma/policy/restore?groupId=${id}&id=${policy.id}&page=${page}"><fmt:message key="btn.enable" /></a>
	        </c:if>
      		 </td>
	      </tr>
        </c:forEach>
      </tbody>
    </table>
    
    <script type="text/javascript">
		$(function() {
			$('.time_picker').datetimepicker({
	    		language:'zh-CN',
	    		startView:1,
	    		format:'yyyy-mm-dd hh:ii',
	    		minuteStep:10,
	    		maxView:4
	    	});
		});
	</script>
    <%@ include file="../../component/pagination.jsp" %>
    <a class="btn btn-large" href="${ctx}/pma/policygroup/list?urlType=return"><fmt:message key="btn.return_to_list" /></a>
</div>
</body>