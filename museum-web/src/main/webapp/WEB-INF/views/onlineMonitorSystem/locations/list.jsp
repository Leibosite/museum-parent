<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>   
<style type="text/css">
body {
	font-family: tahoma, 'microsoft yahei', 微软雅黑;
}

.topSelect ul {
	list-style: none;
}

.topSelect ul li {
	float: left;
	margin-right: 50px
}

.topSelect ul li span {
	margin-top: 10px;
}

.topSelect ul li select {
	margin-top: 10px;
}
</style>
<script type="text/javascript">
	function addLocation() {
		$("#titleName").html("编辑区域");
		$("#contentDiv").load("${ctx}/onlineMonitorSystem/locations/edit");
	}
	
	function deleteLocation(locationId){
		$.ajax({
			url:"${ctx}/onlineMonitorSystem/locations/delete",
			data:{
				repoAreaId:locationId
			},
			success:function(data){
				if(data=="success"){
					alert("删除区域成功！");
					$("#contentDiv").load("${ctx}/onlineMonitorSystem/locations/list");
				}else if(data=="has_relic_or_device"){
					alert("在删除此区域之前，请解除此区域的所有设备和文物的绑定关系")
				}
			},
			error:function(error){
				alert("删除区域失败！");
			}
		});
	}
	
	function updateLocation(locationId){
		$("#contentDiv").load("${ctx}/onlineMonitorSystem/locations/update",{
			repoAreaId:locationId
		});
	}
	
	function showMapPath(areaId){
		$("#contentParent").fadeOut(300, function() {
			$("#model").fadeOut(300);
		});
		$("#devices").css("backgroundColor", "");
		$("#locations").css("backgroundColor", "");
		$("#culturalRelics").css("backgroundColor", "");
		$("#warns").css("backgroundColor", "");
		
		$.ajax({
			url:"${ctx}/onlineMonitorSystem/locations/drawLocation",
			data:{
				repoAreaId:areaId
			},
			success:function(data){
				var map = JSON.parse(data);
				if(map.type==0){
					svg.selectAll("polygon").remove(); 
					svg.append("g").append("polygon")
	   				   .attr("points", map.rect)
	   				   .attr("stroke-width",2)
	   				   .attr("stroke","blue")
	   				   .attr("fill","yellow");
				}else if(map.type==1){
					svg.append("g").append("circle")
					   .attr("cx",map.cx)
					   .attr("cy",map.cy)
					   .attr("r", map.r)
					   .attr("fill","rgba(255, 50, 30, 0.5)");
					
				}else if(map.type==2){
					svg.append("g")
					   .append("polygon")
					   .attr("points", map.polygon)
					   .attr("stroke-width",2)
					   .attr("stroke","blue")
					   .attr("fill","yellow");
				}
			},
			error:function(error){
				alert(error);
			}
		});
	}
</script>
<div id="js-ajax-repalce-block" style="position:relative">
<div class="container" style="width: 100%">
	<div class="topSelect" class="widget-content nopadding">
		<div class="modal-body" style="width: 90%; height: 400px; margin: auto;">
			<form:form class="well-small well form-inline" method="get" modelAttribute="queryForm" >
				所属楼层
				<form:select id="name" path="name" placeholder="" cssClass="input js-select">
			        <form:option value="">--所有楼层--</form:option>
			        <form:option value="0">一楼</form:option>
			        <form:option value="1">二楼</form:option>
			        <form:option value="2">三楼</form:option>
			    </form:select>
			      
				<a class="btn btn-primary" href="#" onclick="load()">
						<i class="icon-search icon-white"></i>
						检索
				</a>
			</form:form>
			
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>区域名称</th>
						<th>是否绑定文物</th>
						<th>是否绑定设备</th>
						<th>区域管理</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageObjects.content}" var="location">
					<tr>
						<td>
						<c:out value="${location.name}"></c:out>
						</td>
						<td>
						<c:if test="${location.bundleCulturalRelic==0 || location.bundleCulturalRelic==null }">
							<c:out value="未绑定"></c:out>
						</c:if>
						<c:if test="${location.bundleCulturalRelic==1}">
							<c:out value="已绑定"></c:out>
						</c:if>
						</td>
						<td>
						<c:if test="${location.bundleDevice==0 || location.bundleDevice==null}">
							<c:out value="未绑定"></c:out>
						</c:if>
						<c:if test="${location.bundleDevice==1}">
							<c:out value="已绑定"></c:out>
						</c:if>
						</td>
						<td>
						<a class="btn btn-success btn-small"  onclick="showMapPath(${location.id})"  href="#">详情</a>
						<a class="btn btn-warning btn-small"  onclick="updateLocation(${location.id})" href="#">修改</a>
						<a class="btn btn-danger  btn-small"  onclick="deleteLocation(${location.id})" href="#">删除</a></td>
					</tr>
					</c:forEach>
				</tbody>
			</table> 
		</div>
	</div>
	<div style="float: right; margin-right: 210px; margin-top: 20px">
		<%@ include file="../../component/pagination.jsp"%>
	</div>
	<button class="btn btn-primary" style="margin-top: 20px; margin-left: 65px;" onclick="addLocation()">新增区域</button>
</div>
</div>
