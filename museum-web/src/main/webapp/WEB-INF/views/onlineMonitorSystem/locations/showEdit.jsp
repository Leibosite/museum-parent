<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>  
<style type="text/css">
body {
	font-family: tahoma, 'microsoft yahei', 微软雅黑;
}
</style>
<script type="text/javascript">
	function saveLocation(){
		$.ajax({
			url:"${ctx}/onlineMonitorSystem/locations/save",
			data:{
				repoAreaId:"${location.repoAreaId}",
				name:"${location.name}",
				type:"${location.type}",
				nwPointx:"${location.nwPointx}",
				nwPointy:"${location.nwPointy}",
				sePointx:"${location.sePointx}",
				sePointy:"${location.sePointy}",
				crPointx:"${location.crPointx}",
				crPointy:"${location.crPointy}",
				radius:"${location.radius}",
				points:'${location.points}'
			},
			success: function(data){
				if(data.toString()=="success"){
					alert("新增区域成功！");
					$("#contentDiv").load("${ctx}/onlineMonitorSystem/locations/list");
				}
				
			},
			error:function(error){
				alert("新增区域失败！");
			}
		});
	}
</script> 
	<div class="container"  style="width: 60%">
		<div class="main-border"  style="width: 60%">
		<table class="table table-hover" border="3" bordercolor="black" style="margin-top: 100px">
			<tr>
				<td>区域名称</td>
				<td><c:out value="${location.name}"></c:out></td>
			</tr>
			<tr>
				<td>区域形状</td>
				<c:if test="${location.type==0}">
				<td>矩形</td>
				</c:if>
				<c:if test="${location.type==1}">
				<td>圆形</td>
				</c:if>
				<c:if test="${location.type==2}">
				<td>多边形</td>
				</c:if>
			</tr>
			<c:choose>
			<c:when test="${location.type==0}">
			<tr>
				<td>左上角x</td>
				<td><c:out value="${location.nwPointx}"></c:out></td>
			</tr>
			<tr>
				<td>左上角y</td>
				<td><c:out value="${location.nwPointy}"></c:out></td>
			</tr>
			<tr>
				<td>右下角x</td>
				<td><c:out value="${location.sePointx}"></c:out></td>
			</tr>
			<tr>
				<td>右下角y</td>
				<td><c:out value="${location.sePointy}"></c:out></td>
			</tr>
			</c:when>
			<c:when test="${location.type==1}">
			<tr>
				<td>圆心x</td>
				<td><c:out value="${location.crPointx}"></c:out></td>
			</tr>
			<tr>
				<td>圆心y</td>
				<td><c:out value="${location.crPointy}"></c:out></td>
			</tr>
			<tr>
				<td>半径</td>
				<td><c:out value="${location.radius}"></c:out></td>
			</tr>
			</c:when>
			<c:when test="${location.type==2}">
				<tr>
					<td>多边形顶点索引</td>
					<td>多边形顶点x</td>
					<td>多边形顶点y</td>
				</tr>
				<c:forEach items="${location.pointList}" var="polygon">
					<tr>
						<td><c:out value="${polygon.pointIndex}"></c:out></td>
						<td><c:out value="${polygon.pointx}"></c:out></td>
						<td><c:out value="${polygon.pointy}"></c:out></td>
					</tr>
				</c:forEach>
			</c:when>
			</c:choose>
		</table>
		</div>
		<div style="margin-right: 200px;">
			<button class="btn btn-primary" style="float: right;" onclick="saveLocation()">保存</button>
		</div>
	</div>

