<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>  
<script type="text/javascript">
function getpoint(deviceID,repoAreaId){
	$("#contentDiv").hide();
	$("#contentParent").hide(); 
	$("#model").hide();
	alert("开始在地图取点！！！");
	mydraw(-2,1,[deviceID,repoAreaId]);
}
</script>

<div class="container"  style="width: 60%">
	<div class="modal-body" style="width: 60%; height: 400px; margin: auto;">
		<div class="topSelect" class="widget-content nopadding">
			
			<form:form  method="get" modelAttribute="queryForm" >
			<table id="deviceEdit">
				<tr>
					<td>设备名称:</td>
					<td>
						<c:out value="${device.name}"></c:out>
					</td>
				</tr>
				<tr>
					<td>
					</td>
				</tr>
				<tr>
					<td>设备所属区域:</td>
					<td>
						<form:select path="repoAreaId" id="repoAreaId">
							<form:options items="${repoAreaList}" itemValue="id" itemLabel="name"/>
						</form:select>
					</td>
				</tr>
			</table>
			</form:form>
			<a onclick="getpoint('${device.id}',$('#repoAreaId').val())" class="btn btn-primary btn-small" href="#">取点</a>
		</div>
		</div>
	</div>