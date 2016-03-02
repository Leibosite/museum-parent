<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>历史告警</title>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/scripts.jsp"%>
<%@ include file="../common/styles.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#datetimepicker1').datetimepicker({
			language : 'pt-BR'
		});
	});
</script>
</head>
<body>
	<div class="container">
		<div class="widget-box">
			<div class="widget-title">
				<span class="icon"> <i class="icon-bookmark"></i></span>
				<h5>历史告警</h5>
			</div>
			<div class="widget-content" style="height: 500px;">

				<p>这是一个P标签~~</p>




				<table style="background-color: white; padding-bottom:"
					class="table table-striped table-bordered" id="list-table">
					<thead>
						<tr>
							<th>Name</th>
							<th>Operation</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${alarms}" var="alarm">
							<tr>
								<td>${alarm.name}</td>
								<td>
									<button type="submit" disabled="disabled"
										style="margin-left: 4px; width: 75px"
										onclick="location.href=''" class="btn btn-mini btn-primary">
										<i class="icon-plus icon-white"></i> Add
									</button>
									<button type="submit" disabled="disabled"
										style="margin-left: 4px; width: 75px" onclick="delFile()"
										class="btn btn-mini btn-primary">
										<i class="icon-remove icon-white"></i> Delete
									</button>
									<button type="submit" disabled="disabled"
										style="margin-left: 4px; width: 75px"
										onclick="location.href=''" class="btn btn-mini btn-primary">
										<i class="icon-edit icon-white"></i> Edit
									</button>
									<button type="submit" disabled="disabled"
										style="margin-left: 4px; width: 75px"
										onclick="location.href=''" class="btn btn-mini btn-primary">
										<i class="icon-search icon-white"></i> Search
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