<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title>权限查看</title>
</head>

<body>

   <h2 style="position:relative">Access Permission List
  	 <span class="oper-block-title btn-group">
  	 	<a class="btn btn-mini btn-primary" href="create">
    		<span class="icon-plus-sign icon-white"></span>
    	</a>
    </span>
    </h2>
  <hr/>
  
 <form:form class="well-small well form-inline search-form-style" method="get" action="searchList" modelAttribute="queryForm" style="position:relative">
  <h4 class="search-title margin-top-bottom-small">Filter Access Permission</h4>
  <form:input path="displayname" placeholder="Name" cssClass="input-small"/>
  <button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i>Search</button>
 </form:form>

 <div id="js-ajax-repalce-block" style="position:relative">
 <!-- 如果有参数进来，则预先加载列表。否则列表为空，因为列表默认不显示给用户，用户输入查询条件后才会显示 -->
 	<c:if test="${hasParams}">
 		<%@ include file="searchList.jsp" %>
 	</c:if>
 </div>

<script type="text/javascript">
	$(document).ready(function(){
		$("#queryForm").submit(function(){
			$(this).disable({ 
	        cssClass: 'disabled', 
	        enableOnAjaxComplete: true, 
	        ajaxUrl: "searchList?"+$(this).formSerialize(),
	        ajaxData:{ajax:true},
	        ajaxCallback: function(data){
	            $("#js-ajax-repalce-block").html(data);
	        }
	     });
			
			return false;
		});
		
	});
		
</script>
</body>

