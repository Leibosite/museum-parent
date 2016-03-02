<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="pageTitle.pma_policygroup_listPolicy"/></title>

</head>
<body>
<div id="js-ajax-repalce-block" style="position:relative">

   <h2 style="position:relative"><c:out value="${name}"/> <fmt:message key="pageTitle.monitor_history_alarm"/>
        <!-- 
	   	 <span class="oper-block-title btn-group">
	   	 	<a class="btn btn-mini btn-primary" href="${ctx}/pma/policy/addPolicy?groupId=${id}&page=${page}&id=">
	     		<span class="icon-plus-
	     		sign icon-white"></span></a>
	     </span>
	      -->
	     </h2>
    <hr/>
	     <!--搜索框开始  start   by tommy at 2014-03-25 -->
    <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
      
       <form:input path="name" placeholder="alarm name" cssClass="input"/>
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
     
     <!--搜索框结束 -->
     
    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
      	<tr>
	      
	      <th><fmt:message key="th.alarmName" /></th>
	      <th><fmt:message key="th.alarmDesp" /></th>
	      <th><fmt:message key="th.alarmPriority" /></th>
	      <th><fmt:message key="th.operation" /></th>
	    </tr>
      </thead>
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="alarm">
	      <tr>
	        <td><c:out value="${alarm.name}"/></td>
	           <td width="500px"><c:out value="${alarm.desp}"/></td>
	            <td><c:out value="${alarm.priority}"/></td>
	        <td width="165px">
	        <!--  
	        <a class="badge badge-info" href="${ctx}/pma/policy/addPolicy?groupId=${id}&id=${policy.id}&page=${page}"><fmt:message key="btn.edit" /></a>
	        -->
	        <a class="badge badge-important delete-item" href="${ctx}/monitor/alarm/history/delete/${alarm.id}"><fmt:message key="btn.delete" /></a>
	        <c:if test="${alarm.status == true}">
	        <a class="badge badge-warning" href="${ctx}/monitor/alarm/history/disable/${alarm.id}"><fmt:message key="btn.disable" /></a>
	        </c:if>
	        <c:if test="${alarm.status == false || empty alarm.status}">
	        <a class="badge badge-success" href="${ctx}/monitor/alarm/history/restore/${alarm.id}"><fmt:message key="btn.enable" /></a>
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
    <!--  
    <a class="btn btn-large" href="${ctx}/pma/policygroup/list?urlType=return"><fmt:message key="btn.return_to_list" /></a>
    -->
</div>
</body>