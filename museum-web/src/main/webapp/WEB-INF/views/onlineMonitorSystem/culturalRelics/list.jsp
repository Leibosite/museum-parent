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
  
	function searchCulturalRelics(areaId,info){
		
		$.ajax({
			url:"${ctx}/onlineMonitorSystem/culturalRelics/getpoint",
			data:{
				repoAreaId:areaId
			},
			success:function(data){
				var point = JSON.parse(data);
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
				svg_img.href.baseVal = "${ctx}/static/d3/img/red.png" ;
				svg_img.setAttributeNS(null,"x",point.x);
				svg_img.setAttributeNS(null,"y",point.y);
				svg_img.setAttributeNS(null,"height","35px");
				svg_img.setAttributeNS(null,"width","35px"); 
				tsvg_obj.appendChild(svg_img); 
				
				<!--设备详情隐藏的textarea -->
				$("#culturalRelicsTextarea").css("margin-top",(y+20)+"px");    
				$("#culturalRelicsTextarea").css("margin-left",(x+60)+"px");  
				$("#culturalRelicsTextarea").css("display","block");
				$("#cultural-relic-info").text(info);
			},
			error:function(error){
				alert("文物所属区域还未添加位置信息，请在区域内新增区域，为相应区域添加位置信息");
			}
		});
		
		
	}
</script>
<div id="js-ajax-repalce-block" style="position: relative">
	<div class="container" style="width: 100%">
		<div class="topSelect" class="widget-content nopadding">

			<!-- 这里的queryForm 和 Controller 里面的queryForm通过 form path属性映射CulturalRelicQueryForm里的属性 -->
			<form:form class="well-small well form-inline" method="get"
				modelAttribute="queryForm">
					文物所在区域
					<form:select id="repoAreaId" path="repoAreaId" placeholder=""
					cssClass="input js-select">
					<form:option value="">--全部区域--</form:option>
					<form:options items="${repoAreaList}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<a title="更多选项..."
					href="javascript:$('#more').slideToggle(500);$('.icon-plus-sign').toggleClass('icon-minus-sign');">
					<span class="icon-plus-sign icon-white"></span>
				</a>
				<a class="btn btn-primary" href="#" onclick="load()"> <i
					class="icon-search icon-white"></i> 检索
				</a>
				<div style="display: none; margin-top: 10px;" id="more">
					<form:select path="areaId" data-placeholder="所在区域"
						cssClass="chzn-autoselect-area">
						<form:option value=""></form:option>
						<c:if test="${not empty areaId}">
							<form:option value="${repoArea.id}" label="${repoArea.name}"></form:option>
						</c:if>
					</form:select>
				</div>
			</form:form>

			<script>
					$(function() {
						$(".chzn-autoselect-area").ajaxChosen({
							original_chosen_option : {
								allow_single_deselect : true
							},
							method : 'GET',
							url : '${ctx}/evaluation/statistics/datafetch/area',
							dataType : 'json'
						}, function(data) {
							var terms = {};
							$.each(data, function(i, val) {
								terms[i] = val;
							});
							return terms;
						});
					});
			</script>
			<div class="modal-body" style="width: 90%; height: 500px; margin: auto;">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>文物名称</th>
							<th>文物区域</th>
							<th>查询位置</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${pageObjects.content}" var="relic">
							<tr>
								<td><c:out value="${relic.name}" /></td>
								<td width="500px"><c:out value="${relic.repoArea.name}" />
								</td>
								<td><button class="btn btn-success btn-small" type="button"
										onclick="searchCulturalRelics('${relic.repoArea.id}','${relic.name}')">详情</button></td>
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
	<script>
		function load(){
			$("#contentDiv").load("${ctx}/onlineMonitorSystem/culturalRelics/list",{repoAreaId:$("#repoAreaId").val()});
		}
	</script>
	</div>