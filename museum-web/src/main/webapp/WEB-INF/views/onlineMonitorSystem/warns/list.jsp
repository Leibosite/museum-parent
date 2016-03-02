<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.qingruan.museum.nosql.entity.Alarm"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>
<style type="text/css">
body{
	font-family: tahoma, 'microsoft yahei', 微软雅黑; 
}
.topSelect ul {
	list-style: none;
}

.topSelect ul li {
	float: left;
	margin-right: 30px
}

.topSelect ul li select {
	width: 120px
}
.topSelect ul li span{
	margin-top: 10px;
}
.topSelect ul li select{
	margin-top: 10px;
}
</style>
<script type="text/javascript">
	$(function() {
		$('#startTime').datetimepicker();
	});
	
	$(function() {
		$('#endTime').datetimepicker();
	});
	
	function getDateTime(millisecond){
		var time = new Date(millisecond);
		return time;
	}
	
	
	function details(x,y,info){
		
		if(x=='' || y==''){
			alert("提示：此设备还没有取点，请点击取点，在地图上取点！");
			return;
		}
		
		$("#contentParent").fadeOut(300, function() {
			$("#model").fadeOut(300);
		});
		$("#devices").css("backgroundColor", "");
		$("#locations").css("backgroundColor", "");
		$("#culturalRelics").css("backgroundColor", "");
		$("#warns").css("backgroundColor", "");
		
		//在地图上面添加点
		var tsvg_obj = document.getElementById("svg_obj"); 
		var xmlns = "http://www.w3.org/2000/svg";  
		var svg_img = document.createElementNS(xmlns,"image");
		svg_img.href.baseVal = "${ctx}/static/d3/img/green.png" ;
		svg_img.setAttributeNS(null,"x",x);
		svg_img.setAttributeNS(null,"y",y);
		svg_img.setAttributeNS(null,"height","35px");
		svg_img.setAttributeNS(null,"width","35px"); 
		tsvg_obj.appendChild(svg_img); 
		
		<!--设备详情隐藏的textarea -->
		$("#warnsTextarea").css("margin-top",(y-170)+"px");    
		$("#warnsTextarea").css("margin-left",(x-80)+"px");  
		$("#warnsTextarea").css("display","block");
		$("#warn-info").text(info);
	}
	
</script>
<div id="js-ajax-repalce-block" style="position:relative">
	<div class="container" style="width: 100%">
		<div class="topSelect" class="widget-content nopadding">
			
			<form:form class="well-small well form-inline" method="get"
				modelAttribute="queryForm">
					文物所在区域
					<form:select id="repoAreaId" path="repoAreaId" placeholder=""
					cssClass="input js-select">
					<form:option value="">--所有区域--</form:option>
					<form:options items="${repoAreaList}" itemValue="id"
						itemLabel="name" />
				</form:select>
					 报警类型
				    <form:select id="warnType" path="warnType" placeholder=""
					cssClass="input js-select">
					<form:option value="">--所有类型--</form:option>
					<form:options items="${warnCategoryList}" itemValue="id"
						itemLabel="category.value" />
				</form:select>
					  开始时间
				<form:input path="startTime" data-format="dd/MM/yyyy hh:mm:ss" id="startTime"></form:input>
					  结束时间
				<form:input path="endTime" data-format="dd/MM/yyyy hh:mm:ss" id="endTime"></form:input>
				<a class="btn btn-primary" href="#" onclick="load()">
						<i class="icon-search icon-white"></i>
						检索
				</a>
			</form:form>

			<div style="width: 90%; margin: auto;">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>报警类型</th>
							<th>报警设备</th>
							<th>报警区域</th>
							<th>报警时间</th>
							<th>处理状态</th>
							<th>详情</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${pageObjects.content}" var="warn">
						<tr>
							<td><c:out value="${warn.warnCategory.category.value}"></c:out></td>
							<td><c:out value="${warn.name}"></c:out></td>
							<td><c:out value="${warn.device.repoArea.name}"></c:out></td>
							<td><c:out value="${warn.dateStamp}"></c:out></td>
							<td><c:if test="${warn.dealStatus == 0 || warn.dealStatus == null}">
								<c:out value="未处理"></c:out>
								</c:if>
								<c:if test="${warn.dealStatus == 1 }">
								<c:out value="已处理"></c:out>
								</c:if>
							</td>
							<td>
							<button class="btn btn-success btn-small" type="button" onclick="details('${warn.device.longitude}','${warn.device.latitude}','${warn.name}')">详情</button>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div style="float: right; margin-right: 210px; margin-top: 20px">
			<%@ include file="../../component/pagination.jsp"%>
		</div>
	</div>
	<script type="text/javascript">
		function load(){
			$("#contentDiv").load("${ctx}/onlineMonitorSystem/warns/list",
					{startTime:$("#startTime").val(),
					 endTime:$("#endTime").val(),
					 repoAreaId:$("#repoAreaId").val(),
					 warnType:$("#warnType").val(),
					 warnCategoryId:$("#warnCategoryId").val()
					});
		}
	</script>
</div>