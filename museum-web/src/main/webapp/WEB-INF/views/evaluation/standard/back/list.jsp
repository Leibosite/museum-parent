<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title>标准管理</title>
<script type="text/javascript">
	
	function details(value){
		console.info(value);
		location.href='details';
	}
	
	function edit(value){
		console.info(value);
		location.href='addStandard';
	}
	
	function deleteObject(value){
		 if(confirm("确定要删除数据吗？"))
		 {
			 
		 }
	}
</script>
</head>

<body>
	<div class="widget-box">
		<div class="widget-title">
			<h5>标准管理</h5>
		</div>
		<div class="widget-content">
		 
				<input type="text" style="margin-top: 10px;" class="required"/>
				<button class="btn btn-primary"><i class="icon-search icon-white"></i>Search</button>
				<button class="btn btn-primary" onclick="location.href='addStandard'"><i class="icon-zoom-in icon-white"></i> Add</button>
			 
			<table style="background-color: white" class="table table-striped table-bordered" id="list-table">
				<thead>
					<tr>
						<th>名称</th>
						<th>区域</th>
						<th>参考标准</th>
						<th>描述</th>
						<th>操作</th> 
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>书画</td>
						<td>马踏飞燕</td>
						<td>英国博物馆</td>
						<td>这是书画描述</td>
						<td>
							<button type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary"  onclick="deleteObject(1)"> <i class="icon-remove icon-white"></i> Delete </button>
							<button  type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary" onclick="edit(1)"><i class="icon-edit icon-white"></i> Edit</button>
							<button type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary" onclick="details(1)"> <i class="icon-search icon-white"></i> Details</button>
						</td>
					</tr>
					<tr>
						<td>书画</td>
						<td>区域</td>
						<td>参考标准</td>
						<td>描述书画</td>
						<td> 
							<button type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary" onclick="deleteObject(2)"> <i class="icon-remove icon-white"></i> Delete </button>
							<button  type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary" onclick="edit(2)"><i class="icon-edit icon-white"></i> Edit</button>
							<button type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary" onclick="details(2)"> <i class="icon-search icon-white"></i> Details</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
