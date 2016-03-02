<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title>详情</title>
</head>

<body>
	 <div style="width: 100%; height:500px;">
	<table class="table table-bordered"
			style="text-align: center; width: 100%; margin: auto;" border="1"
			modelAttribute="customizedStandard" id="customizedStandard">
			<thead>
			     <tr>
			     	<th>名称</th>
			     	<th>区域</th>
			     	<th>参考标准</th>
			     	<th>描述</th>
			     </tr>
			</thead>
			<tbody>
				<tr style="text-align: center;">
				    <td><c:out value="${customizedStandard.name}" /></td>
				    <td><c:out value="${customizedStandard.area.name}" /></td>
				    <td><c:out value="${customizedStandard.standard.name}" /></td>
				    <td><c:out value="${customizedStandard.desp}" /></td>					
				</tr>				
			</tbody>
		</table>
		<table class="table table-striped table-bordered">
			<tr>
				<th>对象</th>
				<th>理想值</th>
				<th>理想最小值</th>
				<th>理想最大值</th>
				<th>可接受最小值</th>
				<th>可接受最大值</th>
				<th>波动系数</th> 
			</tr>
			<c:forEach  items="${standards}" var="standard">  
			<tr>
				<td><c:out value="${standard.monitorObjectType.value}" /></td>
				<td><c:out value="${standard.idealValue}" /></td>
				<td><c:out value="${standard.idealMin}" /></td>
				<td><c:out value="${standard.idealMax}" /></td>
				<td><c:out value="${standard.acceptMin}" /></td>
				<td><c:out value="${standard.acceptMax}" /></td>
				<td><c:out value="${standard.fluctuatuionCoefficient}" /></td>
							
			</tr>
			</c:forEach>
		</table>
		<button style="float: right;" class="btn btn-inverse" type="button" onclick="location.href='list'">返回</button>
	 </div> 
</body>
