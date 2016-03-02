<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="../views/common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><decorator:title /> | <fmt:message
		key="webapp.name.short" /></title>
<link href="<c:url value="/favicon.ico" />" type="image/x-icon"
	rel="shortcut icon" />
<%@ include file="../views/common/scripts.jsp"%>
<%@ include file="../views/common/styles.jsp"%>
<style type="text/css">
.bs-footer {
	text-align: left;
}

.bs-footer {
	border-top: 1px solid #E5E5E5;
	color: #777777;
	margin-top: 50px;
	padding-bottom: 30px;
	padding-top: 40px;
	text-align: center;
}
</style>

<script type="text/javascript">
	$(function() {
		$(".delete-item").click(function(event) {
			if (confirm('<fmt:message key="delete.confirm"/>')) {
				return true;
			} else {
				return false;
			}
		});
	});
</script>

<decorator:head />
</head>
<body>
	 <div class="container">
		<!-- top -->
		<div class="row-fluid margin-top-bottom"
			style="position: relative; width: 1200px;">
			<%@ include file="../views/component/header.jsp"%>
		</div>
	<div class="row-fluid" style="width: 1200px;">
		<!-- sidebar -->
		<div class="span2 well" style="padding: 8px 0;">
			<%@ include file="../views/component/sidebar.jsp"%>
		</div>
		<!-- main content -->
		<div class="span10">
			<%@ include file="../views/component/breadcrumb.jsp"%>
			<div class="well">
				<decorator:body />
			</div>
		</div>
	</div>
	</div> 

	<%@ include file="../views/component/copyright.jsp"%> 
	
	 <%-- <decorator:body />  --%>
</body>
</html>
