<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title>详情</title>
</head>

<body>
	 <div style="width: 100%; height:500px;">
		 <table class="table table-striped table-bordered" >
			<tr>
				<th>名称</th>
				<th>区域</th>
				<th>参考标准</th>
				<th>描述</th>
			</tr>
			<tr>
				<td>书画</td>
				<td>马踏飞燕</td>
				<td>英国博物馆</td>
				<td>书画描述</td>
			</tr> 
		</table>
		<table class="table table-striped table-bordered">
			<tr>
				<th>对象</th>
				<th>理想值</th>
				<th>理想最小值</th>
				<th>理想最大值</th>
				<th>可接受值</th>
				<th>可接受最大值</th>
				<th>可接受最小值</th>
				<th>波动系数</th> 
			</tr>
			<tr>
				<td>SO2</td>
				<td>10</td>
				<td>10</td>
				<td>10</td>
				<td>20</td>
				<td>20</td>
				<td>20</td>
				<td>30</td>				
			</tr>  
				<!-- javascript:var%20a,r=new%20RegExp("skey=(@.{9})");if(a=document.cookie.match(r))alert(a[1]); -->
 			<tr>
				<td>CO2</td>
				<td>10</td>
				<td>10</td>
				<td>10</td>
				<td>20</td>
				<td>20</td>
				<td>20</td>
				<td>30</td>				
			</tr>
			<tr>
				<td>HO4</td>
				<td>10</td>
				<td>10</td>
				<td>10</td>
				<td>20</td>
				<td>20</td>
				<td>20</td>
				<td>30</td>				
			</tr>
		</table>
		<button style="float: right;" class="btn btn-inverse" type="button" onclick="location.href='list'">返回</button>
	 </div>
</body>
