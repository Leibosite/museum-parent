<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>  
<style type="text/css">

#locationEdit{
	margin-top: 30px;
}
#locationEdit td{
	margin-top: 10px;
}
#locationEdit td select{
	margin-top: 10px;
}
</style>
<script type="text/javascript">
	function drawLocation(areaId){
		$("#contentDiv").hide();
		$("#contentParent").hide(); 
		$("#model").hide();

		mydraw($("#type").val(),1,[areaId]);
	}
</script>
	<div class="container"  style="width: 60%">
	<div class="modal-body" style="width: 60%; height: 400px; margin: auto;">
		<div class="topSelect" class="widget-content nopadding">
			
			<form:form  method="get" modelAttribute="queryForm" >
			<table id="locationEdit">
				<tr>
					<td>区域名称:</td>
					<td>
						<c:out value="${repoArea.name}"></c:out>
					</td>
				</tr>
				<tr>
					<td>区域形状:</td>
					<td>
						<form:select path="type" id="type">
							<form:option value="0">矩形</form:option>
							<form:option value="1">圆形</form:option>
							<form:option value="2">多边形</form:option>
						</form:select>
					</td>
				</tr>
			</table>
			</form:form>
			<button class="btn btn-primary" style="float: left;" onclick="drawLocation(${repoArea.id})">绘制区域位置</button>
		</div>
		</div>
	</div>