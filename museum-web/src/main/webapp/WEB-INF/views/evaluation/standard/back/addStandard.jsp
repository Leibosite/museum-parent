<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<title>添加标准管理</title>
	<script type="text/javascript">
		var i=0;
		function add(){
			$('#object').show();
		}
		
		function submit(){
			 $('#object').hide(); 
			$('#list-table>tbody').append('<tr id='+i+'><td>'+$('#select-Object').val()+'</td><td>'+$('#idealValue').val()+'</td><td>'+$('#idealMinValue').val()+'</td><td>'+$('#idealMaxValue').val()+'</td><td>'+$('#acceptableValue').val()+'</td><td>'+$('#acceptableMaxValue').val()+'</td><td>'+$('#acceptableMinValue').val()+'</td><td>'+$('#fluctuationCoefficient').val()+'</td><td><button type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary" onclick="removeRow('+i+')"> <i class="icon-remove icon-white"></i> Delete </button><button  type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary" onclick="edit()"><i class="icon-edit icon-white"></i> Edit</button></td></tr>');
			i++;
		}
		
		function removeRow(value){
			 if(confirm("确定要删除数据吗？"))
			 {
				$('#'+value).remove();
			 }
		}
		
		function edit(){
			$('#object').show();
		}
	</script>
</head>
<body>
	<div>
		<span>
			标准名称：<input type="text" id="name" name="name" size="40" value="${Standard.name}" class="required"/>
		</span>
		<span >
			区域：
			<select>
				<option>--Select Options--</option>
				<option>区域</option>
			</select>
		</span>
		<span>
			描述：<textarea rows="1" cols=""></textarea>
		</span>
		<br/>
		<span>
			标准详细定制
			<button  type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary" onclick="add()"><i class="icon-zoom-in icon-white"></i> Add</button>
		</span>
		<br/>
		<table style="background-color: white;margin-top: 10px" class="table table-striped table-bordered" id="list-table">
				<thead>
					<tr>
						<th>对象</th>
						<th>理想值</th>
						<th>理想最小值</th>
						<th>理想最大值</th>
						<th>可接受值</th>
						<th>可接受最大值</th>
						<th>可接受最小值</th>
						<th>波动系数</th> 
						<th>操作</th> 
					</tr>
				</thead>
				<tbody>
					<!-- <tr>
						<td>SO2</td>
						<td>10</td>
						<td>10</td>
						<td>10</td>
						<td>20</td>
						<td>20</td>
						<td>20</td>
						<td>30</td>
						<td>
							<button  style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary"> <i class="icon-remove icon-white"></i> Delete </button>
							<button  style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary"><i class="icon-edit icon-white"></i> Edit</button>
						</td>
					</tr> 
					<tr>
						<td>CO2</td>
						<td>10</td>
						<td>10</td>
						<td>10</td>
						<td>20</td>
						<td>20</td>
						<td>20</td>
						<td>30</td>
						<td>
							<button type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary"> <i class="icon-remove icon-white"></i> Delete </button>
							<button  type="submit" style="margin-left: 4px; width: 75px" class="btn btn-mini btn-primary"><i class="icon-edit icon-white"></i> Edit</button>
						</td>
					</tr>  -->
				</tbody>
			</table>
	</div>
	<div id="object" style="width: 100%; margin-top:30px; display: none">
		<div style="width: 50%;  margin: auto;">
			<table>
				
				<tr>
					<td>对象标签：</td>
					<td>
						<select id="select-Object">
							<option>--Select Object--</option>
							<option>SO2</option>
							<option>CO2</option>
							<option>HO4</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>理想值：</td>
					<td><input type="text" id="idealValue" name="idealValue" size="40"  class="required" required/></td>
				</tr>
				<tr>
					<td>理想最小值：</td>
					<td><input type="text" id="idealMinValue" name="idealMinValue" size="40"  class="required"/></td>
				</tr>
				<tr>
					<td>理想最大值：</td>
					<td><input type="text" id="idealMaxValue" name="idealMaxValue" size="40"  class="required"/></td>
				</tr>
				<tr>
					<td>可接受值：</td>
					<td><input type="text" id="acceptableValue" name="acceptableValue" size="40"  class="required"/></td>
				</tr>
				<tr>
					<td>可接受最大值：</td>
					<td><input type="text" id="acceptableMaxValue" name="acceptableMaxValue" size="40"  class="required"/></td>
				</tr>
				<tr>
					<td>可接受最小值：</td>
					<td><input type="text" id="acceptableMinValue" name="acceptableMinValue" size="40"  class="required"/></td>
				</tr>
				<tr>
					<td>波动系数：</td>
					<td><input type="text" id="fluctuationCoefficient" name="fluctuationCoefficient" size="40"  class="required"/></td>
				</tr>
				<tr>
					<td colspan="2"><button class="btn btn-primary" style="float: right;" type="button" onclick="submit()">提交</button></td>
				</tr>
			</table>
		</div>
	</div>
	<div style="margin-top: 10px"> 
		<button class="btn btn btn-success" style="margin-left: 840px" type="button" onclick="">保存</button>
		<button class="btn btn-inverse" type="button" onclick="location.href='list'">返回</button>
	</div>
</body>
</html>
