<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title>标准管理</title>
<script type="text/javascript">
	
	function details(value){
		console.info(value);
		location.href='details';
	}
	
	function edit(value){
		console.info(value);
		location.href='addStandard';
	}
	
	function deleteObject(value){
		 if(confirm("确定要删除数据吗？"))
		 {			 
		 }
	}
</script>
</head>

<body>
<jsp:useBean id="queryForm"  class="com.qingruan.museum.admin.web.evaluation.form.CustomizedStandardQueryForm" scope="request" ></jsp:useBean> 
	<div class="widget-box">
		<div class="widget-title">
			<h5>标准管理</h5>
		</div>
		<div class="widget-content">
			<form:form class="well-small well form-inline search-form-style"
				method="get" modelAttribute="queryForm">
				<form:input path="name" placeholder="Name" cssClass="input-small" style="margin-top: 10px;"  />
				<button type="submit">
					<i class="icon-search icon-white"></i>Search
				</button>			
			</form:form>
			<button class="btn btn-primary" onclick="location.href='${ctx}/evaluation/statistics/customizedStandard/create'">
				<i class="icon-zoom-in icon-white"></i> Add
			</button>

			<table style="background-color: white"
				class="table table-striped table-bordered" id="list-table">
				<thead>
					<tr>
						<th>名称</th>
						<th>区域</th>
						<th>参考标准</th>
						<th>描述</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageObjects.content}" var="customizedStandard">
						<tr>
							<td>${customizedStandard.name}</td>
							<td>${customizedStandard.area.name}</td>
							<td>${customizedStandard.standard.name}</td>
							<td>${customizedStandard.desp}</td>
							<td>
	        					<a class="badge badge-info" href="edit/${customizedStandard.id}">Edit</a>
	        					<a class="badge badge-important"  href="${ctx}/evaluation/statistics/customizedStandard/delete/${customizedStandard.id}">Delete</a>
	        					<a class="badge badge-info" href="${ctx}/evaluation/statistics/customizedStandard/details/${customizedStandard.id}">details</a>
	        				</td>
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
