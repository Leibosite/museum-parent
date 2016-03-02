<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>
<script type="text/javascript">
function saveDevicePoint(){
	$.ajax({
		url:"${ctx}/onlineMonitorSystem/devices/save",
		data:{
			deviceId:'${device.id}',
			repoAreaId:'${device.repoArea.id}',
			longitude:'${device.longitude}',
			latitude:'${device.latitude}'
		},
		success:function(data){
			if(data=="success"){
				alert("设备绑定成功！");
				$("#contentDiv").load("${ctx}/onlineMonitorSystem/devices/list");
			}
			
		},
		error:function(error){
			alert("设备绑定失败！");
		}
	});
}

</script>
    <div class="container"  style="width: 60%">
		<div class="main-border"  style="width: 60%">
		<table class="table table-hover" border="3" bordercolor="black" style="margin-top: 100px">
			<tr>
				<td>设备名称</td>
				<td><c:out value="${device.name}"></c:out></td>
			</tr>
			<tr>
				<td>设备类型</td>
				<td><c:out value="${device.deviceType.name}"></c:out></td>
			</tr>
			<tr>
				<td>设备所属区域</td>
				<td><c:out value="${device.repoArea.name}"></c:out></td>
			</tr>
			<tr>
				<td>设备位置经度</td>
				<td><c:out value="${device.longitude}"></c:out></td>
			</tr>
			<tr>
				<td>设备位置纬度</td>
				<td><c:out value="${device.latitude}"></c:out></td>
			</tr>
			
		</table>
		</div>
		<div style="margin-right: 200px;">
			<button class="btn btn-primary" style="float: right;" onclick="saveDevicePoint()">保存</button>
		</div>
	</div>
    