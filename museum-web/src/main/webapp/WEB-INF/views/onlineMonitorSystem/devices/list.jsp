<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<title>设备列表</title>
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
	margin-right: 50px
}
.topSelect ul li span{
	margin-top: 10px;
}
.topSelect ul li select{
	margin-top: 10px;
}
</style>
<script type="text/javascript">
  
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
		svg_img.href.baseVal = "${ctx}/static/d3/img/blue.png" ;
		svg_img.setAttributeNS(null,"x",x);
		svg_img.setAttributeNS(null,"y",y);
		svg_img.setAttributeNS(null,"height","35px");
		svg_img.setAttributeNS(null,"width","35px"); 
		tsvg_obj.appendChild(svg_img); 
		
		
		<!--设备详情隐藏的textarea -->
		$("#devicesTextarea").css("margin-top",(y-170)+"px");    
		$("#devicesTextarea").css("margin-left",(x-80)+"px");  
		$("#devicesTextarea").css("display","block"); 
		$("#device-info").text(info);
	}

</script>
	 

<div id="js-ajax-repalce-block" style="position:relative">
	<div class="container" style="width: 100%">
		<div class="topSelect" class="widget-content nopadding">
			<form:form class="well-small well form-inline" method="get"
				modelAttribute="queryForm">
					设备所属区域
				<form:select path="repoAreaId" id="repoAreaId"
					cssClass="input js-select">
					<form:option value="">--所有区域--</form:option>
					<form:options items="${repoAreaList}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<a title="更多选项..."
					href="javascript:$('#more').slideToggle(500);$('.icon-plus-sign').toggleClass('icon-minus-sign');">
					<span class="icon-plus-sign icon-white"></span>
				</a>
					设备类型
				<form:select path="deviceTypeId" id="deviceTypeId"
					cssClass="input js-select">
					<form:option value="">--所有类型--</form:option>
					<form:options items="${deviceTypeList}" itemValue="id"
						itemLabel="name" />
				</form:select>
					设备版本
				<form:select path="deviceVersionId" id="deviceVersionId"
					cssClass="input js-select">
					<form:option value="">--所有版本--</form:option>
					<form:options items="${deviceVersionList}" itemValue="id"
						itemLabel="name" />
				</form:select>
				<a class="btn btn-primary" href="#" onclick="load()">
						<i class="icon-search icon-white"></i>
						检索
				</a>
				</form:form>

			<div class="modal-body"
				style="width: 90%; height: 500px; margin: auto;">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>设备名称</th>
							<th>设备类型</th>
							<th>设备版本</th>
							<th>设备状态</th>
							<th>设备管理</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${pageObjects.content}" var="device">
					      <tr>
					        <td>
					        <c:out value="${device.name}"/>
					        </td>
					         <td>
					        <c:out value="${device.deviceType.name}"/>
					        </td>
					         <td>
					        <c:out value="${device.deviceVersion.name}"/>
					        </td>
					        <td>
					        <c:if test="${device.status==0}">正常</c:if>
    						<c:if test="${device.status==1}">异常</c:if>
					        </td>
					       <td>
					       <a onclick="details('${device.longitude}','${device.latitude}','${device.name}')" class="btn btn-success btn-small" href="#">详情</a>
					       <a onclick="updateDevice('${device.id}')" class="btn btn-warning btn-small" href="#">修改</a>
					       <a onclick="deleteDevice('${device.id}')" class="btn btn-danger btn-small" href="#">删除</a>
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
		<button class="btn btn-primary" style="margin-top: 20px; margin-left: 65px;" onclick="bundleDevice()">新增设备</button>
	</div>
</div>
<script type="text/javascript">
		function load(){
			$("#contentDiv").load("${ctx}/onlineMonitorSystem/devices/list",
					{repoAreaId:$("#repoAreaId").val(),
				   deviceTypeId:$("#deviceTypeId").val(),
				   deviceVersionId:$("#deviceVersionId").val()
					});
		}
		
		function bundleDevice(){
			$("#contentDiv").load("${ctx}/onlineMonitorSystem/devices/edit");
		}
		
		function updateDevice(deviceID){
			$("#contentDiv").load("${ctx}/onlineMonitorSystem/devices/update",{
				deviceId:deviceID
			});
		}
		
		function deleteDevice(deviceID){
			if(confirm("你确认要删除此设备么？")){
				$.ajax({
					url:"${ctx}/onlineMonitorSystem/devices/delete",
					data:{
						deviceId:deviceID
					},
					success:function(data){
						if(data=='success'){
							$("#contentDiv").load("${ctx}/onlineMonitorSystem/devices/list");
						}
					},
					error:function(error){
						alert("删除设备失败");
					}
				});
			}
		}
</script>
