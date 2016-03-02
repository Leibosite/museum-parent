<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>规则配置列表</title>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/scripts.jsp"%>
<%@ include file="../common/styles.jsp"%>
<script type="text/javascript">
	function delFile() {
		if (confirm("真的要删除吗？")) {
			location.href = 'delfile';
		}
	}
</script>
</head>
<body>
	<div class="container">
		<div class="widget-box">
			<div class="widget-title">
				<span class="icon"> <i class="icon-bookmark"></i></span>
				<h5>规则配置</h5>
			</div>
			<div class="widget-content" style="height: 500px;">
				<table style="background-color: white"
					class="table table-striped table-bordered" id="list-table">
					<thead>
						<tr>
							<th>listName</th>
							<th>url</th>
							<th>Operation</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="file">
							<tr>
								<td>${file.name}</td>
								<td>${file.url}</td>
								<td>
									<button type="submit" style="margin-left: 4px; width: 75px"
										onclick="location.href='show?url=${file.name}'"
										class="btn btn-mini btn-primary">
										<i class="icon-edit icon-white"></i> Edit
									</button>
									<button type="submit" disabled="disabled"
										style="margin-left: 4px; width: 75px" onclick="delFile()"
										class="btn btn-mini btn-primary">
										<i class="icon-remove icon-white"></i> Delete
									</button>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>