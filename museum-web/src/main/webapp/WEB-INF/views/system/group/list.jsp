<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title>MUSEUM-Navi</title>
</head>

<body>
<div id="js-ajax-repalce-block" style="position:relative">
   <h2 style="position:relative"><fmt:message key="pageTitle.access_group_list" />
	   	 <span class="oper-block-title btn-group">
	   	 		<a class="btn btn-mini btn-primary" href="create">
	     			<span class="icon-plus-sign icon-white"></span>
	     		</a>	
	     </span>
	 </h2>
   <hr/>
   
    <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
    	<h4 class="search-title margin-top-bottom-small"><fmt:message key="th.select_access_group"/></h4>
    	<form:input path="name" placeholder="角色名称" cssClass="input-small"/>
    	<button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i><fmt:message key="btn.search" /></button>
 	  </form:form>
    
    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
      	<tr>
	      <th><fmt:message key="th.group_name" /></th>
	      <th><fmt:message key="th.operation" /></th>
	    	</tr>
      </thead>
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="group">
	      <tr>
	        <td>${group.name}</td>
	        <td>
	        		<a class="badge badge-info" href="edit/${group.id}"><fmt:message key="btn.edit" /></a>
	        		<a class="badge badge-important js-hasAssociation-${group.id}" href="javascript:void(0)"><fmt:message key="btn.delete" /></a>
	        	 <script>
		        	$(".js-hasAssociation-${group.id}").click(function(){
		        		if(confirm("确定要删除？")){
			        		$.ajax({
			        			url:"hasAssociation",
			        			data:{groupId:${group.id}},
			        			success:function(data){
			        				if(data == 'ALLOW'){
			        					window.location.href="${ctx}/system/group/delete/${group.id}";
			        				}
			        				else{
			        					alert("Not Allowed!");
			        				}
			        			}
			        		});
		        		}
		        		return false;
		        	});      
		       	 </script>
	        </td>
	      </tr>
        </c:forEach>
      </tbody>
    </table>
    
    <%@ include file="../../component/pagination.jsp" %>
	</div>
</body>

