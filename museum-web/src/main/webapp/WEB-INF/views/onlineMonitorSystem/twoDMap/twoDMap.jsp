<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String path = request.getContextPath(); String basePath = request.getScheme()+"://"+
    request.getServerName()+":"+request.getServerPort()+path+"/"; %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>二维平面图</title>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>
<script type="text/javascript" src="<%=basePath %>/static/d3/js/jquery.json.min.js"></script>
<style>
	body{
		 font-family: tahoma, 'microsoft yahei', 微软雅黑;
	}
	#changeTag span{
		border:solid 1px #FFFFFF;
		background-color:#FFFFFF;
		padding:5px;
		margin-left:15px
	}
	#changeTag span:hover{
		border:solid 1px #0000FF;
		background-color:#33CCFF;
		padding:5px;
		cursor:pointer
	}
	#mapTopIcon{
		background-color: rgb(235, 234, 239);
		border-radius: 5px; 
		height: 30px
	}
	#mapTopIcon i{
		float: right;
		margin-right: 20px;
		margin-top: 8px;
		cursor: pointer;
	}
	 
</style>
<script type="text/javascript">
	//移动到放大缩小地图按钮 事件
	function over(id){
	    if(id=="top"){
	    	//鼠标移动到向上移动按钮时定位背景图像
	        $("#"+id).css("background-position"," -75px -5px");
	    }else if(id=="down"){
	    	//鼠标移动到向下移动按钮时定位背景图像
	        $("#"+id).css("background-position"," -75px -32px");
	    }else if(id=="left"){
	    	//鼠标移动到向左移动按钮时定位背景图像
	        $("#"+id).css("background-position"," -61px -19px");
	    }else if(id=="right"){
	    	//鼠标移动到向右移动按钮时定位背景图像
	        $("#"+id).css("background-position"," -92px -19px");
	    }else if(id=="amplification"){
	    	//鼠标移动到放大按钮时定位背景图像
	        $("#"+id).css("background-position"," -27px -56px");
	    }else if(id=="narrow"){
	    	//鼠标移动到缩小按钮时定位背景图像
	        $("#"+id).css("background-position"," -27px -196px");
	    }
	}
	
	//鼠标离开放大缩小地图按钮事件
	function out(id){
	    if(id=="top"){
	    	//鼠标离开 向上移动按钮时定位背景图像
	        $("#"+id).css("background-position"," -19px -5px");
	    }else if(id=="down"){
	    	//鼠标离开 向下移动按钮时定位背景图像
	        $("#"+id).css("background-position"," -19px -32px");
	    }else if(id=="left"){
	    	//鼠标离开 向左移动按钮时定位背景图像
	        $("#"+id).css("background-position"," -5px -19px");
	    }else if(id=="right"){
	    	//鼠标离开 向右移动按钮时定位背景图像
	        $("#"+id).css("background-position"," -36px -19px");
	    }else if(id=="amplification"){
	    	//鼠标离开 放大按钮时定位背景图像
	        $("#"+id).css("background-position"," -1px -56px");
	    }else if(id=="narrow"){
	    	//鼠标离开 缩小按钮时定位背景图像
	        $("#"+id).css("background-position"," -1px -196px");
	    }
	}
</script>
 
</head>
<body>
	 
	<!--比例尺工具栏-->
	<div style="margin: 80px 70px 0px 0px; z-index: 0; position: absolute; right: 0px; top: 0px; width: 20px; height: 0px;">
	    <div style="position: absolute; width: 56px; height: 56px; left: 11px; top: 0px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: 0px 0px;"></div>
	    <div style="position: absolute; width: 8px; height: 118px; left: 35px; top: 88px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: -36px -80px;"></div>
		
		<!--放大缩小按钮-->
	    <div id="amplification" style="position: absolute; width: 26px; height: 26px; cursor: pointer; left: 26px; top: 64px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: -1px -56px;"
	         title="放大" onmouseover="over(this.id)" onmouseout="out(this.id)" onclick="clickMove(this.id)"></div>
	    <div id="narrow" style="position: absolute; width: 26px; height: 26px; cursor: pointer; left: 26px; top: 205px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: -1px -196px;"
	         title="缩小" onmouseover="over(this.id)" onmouseout="out(this.id)" onclick="clickMove(this.id)"></div>
	
	    <!--平移按钮-->
	    <div id="top" style="position: absolute; width: 20px; height: 18px; cursor: pointer; left: 30px; top: 5px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: -19px -5px;"
	         title="向上平移" onmouseover="over(this.id)" onmouseout="out(this.id)" onclick="clickMove(this.id)"></div>
	    <div id="down" style="position: absolute; width: 20px; height: 18px; cursor: pointer; left: 30px; top: 32px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: -19px -32px;"
	         title="向下平移" onmouseover="over(this.id)" onmouseout="out(this.id)"onclick="clickMove(this.id)"></div>
	    <div id="left" style="position: absolute; width: 17px; height: 18px; cursor: pointer; left: 16px; top: 19px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: -5px -19px;"
	         title="向左平移" onmouseover="over(this.id)" onmouseout="out(this.id)"onclick="clickMove(this.id)"></div>
	    <div id="right" style="position: absolute; width: 17px; height: 18px; cursor: pointer; left: 47px; top: 19px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: -36px -19px;"
	         title="向右平移" onmouseover="over(this.id)" onmouseout="out(this.id)"onclick="clickMove(this.id)"></div>
	    <div style="position: absolute; cursor: pointer;" title=""></div>
	
	    <!--中间上下移动按钮-->
	    <div id="moveButton" style="position: absolute; width: 24px; height: 14px; cursor: pointer; left: 27px; top: 194px; background-image: url(http://s.map.qq.com/themes/default/img/nav.png?v=v4.0.11); background-position: -53px -56px;"></div>
	</div>

	<!-- 左下角比例尺条 -->
	<div onpositionupdate="return;"  style="width: 110px;height: 35px; position: absolute; left: 0px; bottom: 20px;">
	     <div style="position: absolute;">
	         <div style="width: 100px; position: absolute;">
	             <div style="position: absolute; border-width: 1px 1px 0px; border-style: solid; border-color: rgb(255, 255, 255); width: 2px; height: 9px; font-size: 0px; line-height: 0; top: 7px; left: 0px; z-index: 0; background: rgb(0, 0, 0);"></div>
	             <!-- 比例尺默认是10米 -->
	             <div id="distance" style="position: relative; top: 0px; font-style: normal; font-variant: normal; font-weight: normal; font-stretch: normal; font-size: 11px; line-height: normal; font-family: arial, simsun; color: rgb(0, 0, 0); text-align: left; left: 5px; width: 99px;text-align:center; ">10米</div>
	             
	             <div style="position: absolute; height: 7px; width: 84px; bottom: 0px; z-index: 0;">
	                 <!-- 比例尺右线 -->
	                 <div id="rightLine" style="position: absolute; border-width: 1px 1px 0px; border-style: solid; border-color: rgb(255, 255, 255); width: 2px; height: 6px; font-size: 0px; line-height: 0; top: 0px; left: 100px; z-index: 2; background: rgb(0, 0, 0);"></div>
	                 <!-- 比例尺下线 -->
	                 <div id="downLine" style="position: absolute; border-width: 1px 1px 1px 0px; border-style: solid; border-color: rgb(255, 255, 255); width: 100px; height: 2px; font-size: 0px; line-height: 0; z-index: 1; left: 3px; top: 6px; background: rgb(0, 0, 0);"></div>
	             </div>
	         </div> 
	     </div>
	 </div>

	
	<!--设备详情隐藏的textarea -->
	<div id="devicesTextarea" style="width:250px; height:200px; border:1px solid; display:none; position:absolute;">
		<div style="background-color:#CCCCCC; border-bottom:1px solid; padding:5px">设备详情<span style="margin-left:170px; cursor: pointer;" onclick="$('#devicesTextarea').hide()">×</span></div>
		<!--添加标记-->
		<div style="width:250px; height:169px;  display:block; background-color:#FFFFFF">
				<span id="pointId" class="id" style="display: none"></span>
				<textarea id="device-info" rows="8" cols="30" style="width: 237px;border-radius: 0px"></textarea>
		</div>  
	</div> 
	
	<!--文物详情隐藏的textarea -->
	<div id="culturalRelicsTextarea" style="width:250px; height:200px; border:1px solid; display:none; position:absolute;">
		<div style="background-color:#CCCCCC; border-bottom:1px solid; padding:5px">文物详情<span style="margin-left:170px;cursor: pointer;" onclick="$('#culturalRelicsTextarea').hide()">×</span></div>
		<!--添加标记-->
		<div style="width:250px; height:169px;  display:block; background-color:#FFFFFF">
				<span id="pointId" class="id" style="display: none"></span>
				<textarea id="cultural-relic-info" rows="8" cols="30" style="width: 237px;border-radius: 0px"></textarea>
		</div>  
	</div>
	
	<!--告警日志隐藏的textarea -->
	<div id="warnsTextarea" style="width:250px; height:200px; border:1px solid; display:none; position:absolute;">
		<div style="background-color:#CCCCCC; border-bottom:1px solid; padding:5px">告警日志<span style="margin-left:170px; cursor: pointer;" onclick="$('#warnsTextarea').hide()">×</span></div>
		<!--添加标记-->
		<div style="width:250px; height:169px;  display:block; background-color:#FFFFFF">
				<span id="pointId" class="id" style="display: none"></span>
				<textarea id="warn-info" rows="8" cols="30" style="width: 237px;border-radius: 0px"></textarea>
		</div>  
	</div> 
	
	<!--隐藏的标识提示框-->
	<div id="addPoint" style="width:200px; height:200px; border:2px solid; display:none; position:absolute;">
		<div id="tag" style="background-color:#CCCCCC; border-bottom:1px solid; padding:5px">添加标记</div>
		<!--添加标记-->
		<div id="addTag" style="width:200px; height:169px;  display:block; background-color:#FFFFFF">
			<div id="leftDiv" style="width:85%; height:100%;  float:left; font-size:12px">
				设备ID:<span id="device_id"></span><br />
				区域ID:<span id="repo_area_id"></span><br />
				点经度:<span id="x"></span><br />
				点纬度:<span id="y"></span><br />
				<!-- 隐藏Id -->
				<span id="pointId" class="id" style="display: none"></span>
				<input style=" margin-left:55px ;" type="button" onClick="saveIcon()" value="保存">
				<input type="button" onClick="displayDiv($('#pointId').html())" value="取消">
			</div>
			<!-- 大头针图标 -->
			<div id="imgIcon" style="width:15%; height:100%; float:left; font-size:12px;">
				<img src="${ctx}/static/d3/img/blue.png" width="20px" height="20px" style="margin-top:20px">
				<br/>
				<a href="#" onClick="changeStyle($('#pointId').html())">更换</a>
			</div>
		</div> 
		
		<!--更换标记样式-->
		<div id="changeTag" style="width:250px; height:169px; background-color:#FFFFFF; display:none">
			<br/>
			<!-- 蓝色大头针 -->
			<span id="blue" onClick="Icon(this.id,$('#pointId').html())" style="margin-left:10px"><img src="${ctx}/static/d3/img/blue.png" width="20px" height="20px" ></span>
			<!-- 红色大头针 -->
			<span id="red"  onClick="Icon(this.id,$('#pointId').html())"><img src="${ctx}/static/d3/img/red.png" width="20px" height="20px" ></span>
			<!-- 黄色大头针 -->
			<span id="yellow" onClick="Icon(this.id,$('#pointId').html())"><img src="${ctx}/static/d3/img/yellow.png" width="20px" height="20px" ></span>
			<!-- 绿s色大头针 -->
			<span id="green"  onClick="Icon(this.id,$('#pointId').html())"><img src="${ctx}/static/d3/img/green.png" width="20px" height="20px" ></span> 
			<br/>
			<input style="margin-top:40px; margin-left:190px" type="button" value="返回" onClick="returnaddTag()"/>
		</div>
	</div> 
 

	<!--最上面地图工具栏-->
	<div id="mapTopIcon">
		<!-- 画点 -->
		<i class="icon-map-marker" title="点" onclick="mydraw(-2,0)"></i>
		<!-- 画线 -->
		<i class="icon-resize-horizontal" title="线" onclick="mydraw(-1,0)"></i>
		<!-- 画圆 -->
		<i class="icon-ok-circle" title="圆" onclick="mydraw(1,0)"></i>
		<!-- 画矩形 -->
		<i class="icon-stop" title="矩形" onclick="mydraw(0,0)"></i>
		<!-- 画多边形 -->
		<i class="icon-bookmark" title="多边形" onclick="mydraw(2,0)"></i>
		<!-- 删除，清空地图上所有图形 -->
		<i class="icon-trash"title="删除" onclick="clearAll()"></i>
	</div>
	
	<br/><br/>
	<script src="${ctx}/static/d3/js/d3.js"></script>
	<script type="text/javascript">
	 var pointCountId=0; //大头针ID
     var rectData="";//矩形数据
	 var circleData="";//圆数据
	 var polygonData={};//多边形数据 
	 var pointData = [];//保存添加备注名字
	 var pathId=0;//区域Id
	function mydraw(type,category,params){
		 //点 -2 线 -1 矩形 0 圆形 1 多边形 2
		//画线
		if(type==-1){
			//清除鼠标双击事件
			svg.on("dblclick",function(){});
			//清除鼠标按下时事件
			svg.on("mousedown",function(){});
			//清除鼠标移动时事件
			svg.on("mousemove",function(){});
			//清除鼠标释放时事件
			svg.on("mouseup",function(){}); 
			var one=[]; 
				var clickTimes = 0;
				//第一次鼠标点击的时候开始画
				svg.on("click",function(){
				   var e =  window.event;
				   //第一次点击时的x轴坐标
				   var x = e.clientX-svg.property("offsetLeft")-360;
				   //第一次点击时的y轴坐标
				   var y = e.clientY-svg.property("offsetTop")-60;
				   //吧第一个点 保存到数组				   
				   one.push(x,y);
				   clickTimes++;
				    //鼠标移动的时候（线跟着鼠标跑）
					svg.on("mousemove",function(){
						var ev = arguments.callee.caller.arguments[0] || window.event; 
						//第二次点击时的x轴坐标
						var x1 = ev.clientX-svg.property("offsetLeft")-360;
						//第二次点击时的y轴坐标
						var y1 = ev.clientY-svg.property("offsetTop")-60;
						//清除所有的线
						svg.selectAll("line").remove();
						//使用D3画线
						var line = svg.append("g").append("line")
						   			  .attr("x1",x)
						   			  .attr("y1",y)
						    		  .attr("x2",x1)
						   			  .attr("y2",y1)
									  .attr("stroke","red"); //设置线的颜色
									  //第二次单击的时候画成一条线
									  if(clickTimes==2){
											//清除鼠标移动时间
										  	svg.on("mousemove",null);
											//使用D3画线，确定最后的直线
											var line = svg.append("g").append("line").attr("x1",one[0]).attr("y1",one[1]) .attr("x2",one[2]).attr("y2",one[3]).attr("stroke","red"); 
											//清空   数组 ，clickTimes
											one=[];
											clickTimes = 0;
									}
						});  
				   	  
				});  
		}else if(type==1){
			//圆
			//清除鼠标单击事件
			svg.on("click",function(){});
			//清除鼠标移动事件
			svg.on("mousemove",function(){});
			//存放按下时 获取到的点（x,y）
			var cir=[];
			//圆的半径
			var r;
			//鼠标按下时 确定圆心
			svg.on("mousedown",function(){
					var e = arguments.callee.caller.arguments[0] || window.event;
					//鼠标按下时的x轴坐标
					var x = e.clientX-svg.property("offsetLeft")-350;
					//鼠标按下时的y轴坐标
				    var y = e.clientY-svg.property("offsetTop")-70; 
					//存放到数组
					cir.push(x,y);
					//鼠标移动的时候（手绘圆的半径）
					svg.on("mousemove",function(){
						var ev = arguments.callee.caller.arguments[0] || window.event; 
						//鼠标移动时的x轴坐标
						var x1 = ev.clientX-svg.property("offsetLeft")-350;
						//鼠标移动时的y轴坐标
						var y1 = ev.clientY-svg.property("offsetTop")-70; 
						//计算出圆的半径
						r =Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y));  
						//清除鼠标移动时生成的圆
						svg.selectAll("circle").remove();
						//鼠标移动时生成圆
						var circle = svg.append("g").attr("transform","translate(0,0)").append("circle")
						   .attr("cx",x)
						   .attr("cy",y)
						   .attr("r",r)
						   .attr("fill","rgba(255, 50, 30, 0.5)");
					});
					 //鼠标释放的时候确定圆的半径
					 svg.on("mouseup",function(){ 
						//清除鼠标移动事件
						svg.on("mousemove",null);
						//清除鼠标释放事件
						svg.on("mouseup",null); 
						//category后台传过来类别 （点 -2       线 -1      矩形 0      圆形 1     多边形 2）
						if(category==1){
							if(confirm("是否保存此圆形？")){
								$("#contentDiv").load("${ctx}/onlineMonitorSystem/locations/showEdit",{
									repoAreaId:params[0],
									type:$("#type").val(),
									crPointx:cir[0],
									crPointy:cir[1],
									radius:r
								}); 		
							}
							 //清除地图上所有的图形
							 clearAll();
							 
							 //弹出model层
							 $("#contentDiv").show();
							 $("#contentParent").show(); 
							 $("#model").show();
						}
					});
					
				}); 
		}else if(type==2){
			//多边形
			//清除鼠标按下时事件
			svg.on("mousedown",function(){});
			//清除鼠标移动时事件
			svg.on("mousemove",function(){});
			//清除鼠标释放时事件
			svg.on("mouseup",function(){}); 
			//存放第一次单击时的xy轴的点
			var location=[]; 
			//记录点击次数
			var polygonClickTimes = 0; 
			//鼠标单击确定一点
			svg.on("click",function(){
			   var e =  window.event;
			   //第一次点击的x轴
			   var x = e.clientX-svg.property("offsetLeft")-355;
			   //第一次点击的y轴
			   var y = e.clientY-svg.property("offsetTop")-60;
			   //往后台传的数据
			   var point = {pointIndex:polygonClickTimes,pointx:x,pointy:y};
			   //存放到数组
			   location.push(point);  
			   polygonClickTimes++;
			   //鼠标移动事件
			   svg.on("mousemove",function(){ 
			   		if(polygonClickTimes==1){
						var ev = arguments.callee.caller.arguments[0] || window.event; 
						//第二次点击时的x轴
						var x1 = ev.clientX-svg.property("offsetLeft")-355;
						//第二次点击时的y轴
						var y1 = ev.clientY-svg.property("offsetTop")-60; 
						//清除鼠标移动时画的所有多边形
						svg.selectAll("polygon").remove();
						//画多边形
						var drawPolygon = ""+location[0].pointx+","+location[0].pointy+" "+x1+","+y1+"";
						var polygon = svg.append("g").append("polygon").attr("points", drawPolygon).attr("stroke-width",2).attr("stroke","blue").attr("fill","yellow");  
					}else{
						var ev = arguments.callee.caller.arguments[0] || window.event; 
						//点击时x轴
						var x2 = ev.clientX-svg.property("offsetLeft")-355;
						//点击时y轴
						var y2 = ev.clientY-svg.property("offsetTop")-60; 
						//清除鼠标移动时画的所有多边形
						svg.selectAll("polygon").remove();
						var drawPolygon="";
						//循环location数组里的数据
						for(var i=0; i<location.length;i++){ 
						 	drawPolygon = drawPolygon  +  location[i].pointx+"," + location[i].pointy+" " 	 
				  		}
						//画多边形
				  		drawPolygon = drawPolygon.substring(0,drawPolygon.length-1)+" "+x2+","+y2; 
						console.info("drawPolygon:"+drawPolygon);
						var polygon = svg.append("g").append("polygon").attr("points", drawPolygon).attr("stroke-width",2).attr("stroke","blue").attr("fill","yellow");  
					} 
			   }); 
			   //鼠标双击的时候完成多边形
			   svg.on("dblclick",function(){  
				    //点击次数归零
			   		polygonClickTimes=0;
				    //清除移动事件
			   		svg.on("mousemove",null);
					var drawPolygon = "";
					  //循环location数组里的数据
					  for(var i=0; i<location.length-1;i++){ 
							 drawPolygon = drawPolygon  +  location[i].pointx+"," + location[i].pointy+" "; 	 
					  }
					  //画多边形
					  drawPolygon = drawPolygon.substring(0,drawPolygon.length-1); 
					  var polygon = svg.append("g").append("polygon").attr("points", drawPolygon).attr("stroke-width",2).attr("stroke","blue").attr("fill","yellow");  
					  location.pop();
					  //category后台传过来的值
					  if(category==1){
						  if(confirm("是否保存此多边形？")){
							  $("#contentDiv").load("${ctx}/onlineMonitorSystem/locations/showEdit",{
									repoAreaId:params[0],
									type:$("#type").val(),
									points:JSON.stringify(location)
							  });
						  }
						  //清除地图上所有图形
						  clearAll();
						  //显示model
						  $("#contentDiv").show();
						  $("#contentParent").show(); 
						  $("#model").show();
					  }
					  //清空数组
					  location=[];
			   });  
			}); 
		}else if(type==0){
			//矩形
			//清除鼠标单击时事件
			svg.on("click",function(){});
			//清除鼠标移动时事件
			svg.on("mousemove",function(){}); 
			//清除鼠标双击时事件
			svg.on("dblclick",function(){}); 
			//存放第一次单击时 x，y 的点
			var recPoint1=[];  
			//存放第二次单击时 x，y 的点
			var recPoint3=[];
			//单击次数
			var count=0; 
			//点击时所有的值
			var drawRect;
			//鼠标移动时的x轴
			var x1;
			//鼠标移动时的y轴
			var y1;
			 //单击事件确定一点
			 svg.on("click",function(){  
				   var e =  window.event;
				   //第一次点击时x轴值
				   var x = e.clientX-svg.property("offsetLeft")-360;
				   //第一次点击时y轴值
				   var y = e.clientY-svg.property("offsetTop")-60; 
				   //存放到数组
				   recPoint1.push(x,y);
				   count++;
				   //鼠标移动
				   svg.on("mousemove",function(){
						var ev = arguments.callee.caller.arguments[0] || window.event; 
						//鼠标移动时的x轴值
						x1 = ev.clientX-svg.property("offsetLeft")-360;
						//鼠标移动时的y轴值
						y1 = ev.clientY-svg.property("offsetTop")-60;
					 	//存放到数组
						recPoint3.push(x1,y1); 
						//点击时所有的值
						drawRect=""+recPoint1[0]+","+recPoint1[1]+" "+recPoint3[0]+","+recPoint1[1]+" "+recPoint3[0]+","+recPoint3[1]+" "+recPoint1[0]+","+recPoint3[1]+"";  
						//清空数组
						recPoint3.length=0;  
						//清除所有矩形
						svg.selectAll("polygon").remove(); 
						//使用D3画矩形
						var polygon = svg.append("g").append("polygon")
					   					 .attr("points", drawRect)
					   					 .attr("stroke-width",2)
					   				     .attr("stroke","blue")
					   					 .attr("fill","yellow"); 
									    
						});  
				   		//第二次单击确定对角点 形成矩形
						if(count==2){ 
							//清除点击次数
							count=0; 
							//清除移动事件
							svg.on("mousemove",null);
							//使用D3画矩形
							var polygon = svg.append("g").append("polygon") .attr("points", drawRect) .attr("stroke-width",2).attr("stroke","blue").attr("fill","yellow");
							
							if(category==1){
								 if(confirm("是否保存此矩形？")){
										$("#contentDiv").load("${ctx}/onlineMonitorSystem/locations/showEdit",{
											repoAreaId:params[0],
											type:$("#type").val(),
											nwPointx:recPoint1[0],
											nwPointy:recPoint1[1],
											sePointx:recPoint1[2],
											sePointy:recPoint1[3]
										});
								}
								 //清除地图上所有的图形
								 clearAll();
								 //显示model
								 $("#contentDiv").show();
								 $("#contentParent").show(); 
								 $("#model").show();
								 //清空数组
								 recPoint1=[];
								 recPoint3=[]; 
							}
						} 
			 });
		}else if(type==-2){
			//画点
			//清除双击事件
			svg.on("dblclick",function(){});
			//清除鼠标按下事件
			svg.on("mousedown",function(){});
			//清除鼠标移动事件
			svg.on("mousemove",function(){});
			//清除释放事件
			svg.on("mouseup",function(){}); 
			var pointId=0;
			//鼠标双击画点
			svg.on("dblclick",function(){
				var e =  window.event;
				//点的x轴坐标
				var x = e.clientX-svg.property("offsetLeft")-375;
				//点的y轴坐标
				var y = e.clientY-svg.property("offsetTop")-85;
				//存放到数组
				pointData.push(x,y);
				
				//创建一个image对象 
				var tsvg_obj = document.getElementById("svg_obj"); 
				var xmlns = "http://www.w3.org/2000/svg"; 
				var svg_img = document.createElementNS(xmlns,"image");
				//设置大头针图片路径
				svg_img.href.baseVal = "<%=basePath %>/static/d3/img/blue.png";
				//位置
				svg_img.setAttributeNS(null,"x",x);
				svg_img.setAttributeNS(null,"y",y);
				//图片大小
				svg_img.setAttributeNS(null,"height","35px");
				svg_img.setAttributeNS(null,"width","35px"); 
				//设置Id属性
				svg_img.setAttributeNS(null,"id",pointId);  
				//单击大头针的事件
				svg_img.setAttributeNS(null,"onclick","clickIcon("+x+","+y+",id)"); 
				//追加到tsvg_obj对象里
				tsvg_obj.appendChild(svg_img);  
				
				//设置点跟着地图放大缩小
				pointCountId++;
				//给大头针添加Id属性
			 	svg.append("g").attr("id","gImage"+pointCountId); 
				//设置隐藏Div的内容（隐藏的标识提示框）
			 	$("#gImage"+pointCountId+"").html(svg_img) ;
			 	$("#leftDiv #device_id").html(params[0]);
			 	$("#leftDiv #repo_area_id").html(params[1])
				$("#leftDiv #x").html(x);
				$("#leftDiv #y").html(y); 
				$("#leftDiv .id").html(pointId); 
				//设置隐藏的div位置
				$("#addPoint").css("margin-top",(y+20)+"px");    
				$("#addPoint").css("margin-left",(x+60)+"px");  
				//显示div
				$("#addPoint").css("display","block"); 
				pointId++;
			}); 
		}
	}
	//单击大头针事件
	function clickIcon(x,y,id){ 
		//alert(x+","+y+","+id);
		$("#leftDiv .id").html(id); 
		$("#addPoint").css("margin-top",(y+20)+"px");    
		$("#addPoint").css("margin-left",(x+60)+"px");  
		$("#addPoint").css("display","block");	
	}
	 
	
	//Icon标记
	function changeStyle(value){
		$("#tag").html("更换标记样式");  
		//显示改变标记
		$("#changeTag").css("display","block"); 
		//隐藏添加标记
		$("#addTag").css("display","none"); 
	}
	
	//返回
	function returnaddTag(){
		$("#tag").html("添加标记");
		$("#changeTag").css("display","none"); 
		$("#addTag").css("display","block"); 
	}
	//更换大头针事件
	function Icon(obj,id){
		//如果在标识框中选中的是蓝色,那么地图上的大头针也是蓝色
		if(obj=="blue"){
			$("#imgIcon img").attr("src","<%=basePath %>/static/d3/img/blue.png");
			$("#"+id+"").attr("href","<%=basePath %>/static/d3/img/blue.png");  
		}
		//如果在标识框中选中的是红色,那么地图上的大头针也是红色
		else if(obj=="red"){
			$("#imgIcon img").attr("src","<%=basePath %>/static/d3/img/red.png")
			$("#"+id+"").attr("href","<%=basePath %>/static/d3/img/red.png"); 
		}
		//如果在标识框中选中的是黄色,那么地图上的大头针也是黄色
		else if(obj=="yellow"){
			$("#imgIcon img").attr("src","<%=basePath %>/static/d3/img/yellow.png")
			$("#"+id+"").attr("href","<%=basePath %>/static/d3/img/yellow.png"); 
		}
		//如果在标识框中选中的是绿色,那么地图上的大头针也是绿色
		else{
			$("#imgIcon img").attr("src","<%=basePath %>/static/d3/img/green.png")
			$("#"+id+"").attr("href","<%=basePath %>/static/d3/img/green.png"); 
		}
		//修改隐藏标识框的标题
		$("#tag").html("添加标记");
		//显示改变标记
		$("#changeTag").css("display","none"); 
		//隐藏添加标记
		$("#addTag").css("display","block");  
	}
	
	//隐藏div事件
	function displayDiv(id){
		/* alert(id); */ 
		/* $("#svg_obj").removeChild(svg_img); */
		if(id==id){
			$("#"+id+"").css("display","none");
		}
		$("#addPoint").css("display","none"); 
	}
	
	//保存大头针
	function saveIcon(){
		d3.select("svg").append("g").append("text")
						.text($("#tagName").val())
						.attr("dx",pointData[0])
						.attr("fill","red")
						.attr("dy",pointData[1]);
		$("#addPoint").css("display","none");
		
		if(confirm("是否保存此点？")){
			$("#contentDiv").load("${ctx}/onlineMonitorSystem/devices/showEdit",{
				deviceId:$("#device_id").html(),
				repoAreaId:$("#repo_area_id").html(),
				   longitude:$("#x").html(),
					latitude:$("#y").html()
			});
			//清除地图上所有的图形
			clearAll();
		   //显示model
		   $("#contentDiv").show();
		   $("#contentParent").show(); 
		   $("#model").show();
		 }else{
			 //清除地图上所有的图形
			 clearAll();
		 }
		//清空数组
		pointData=[];
	}
	 
	 
	 
	//清除所有覆盖物
 	function clearAll(){   
 		$("#map").load("map"); 
	}
	//设置svg大小
    var width  = 1350;
    var height = 700;
	//加载地图
    var svg = d3.select("#map").append("svg")
            .attr("width", width)
            .attr("height", height)
			.attr("position","absolute")
            .attr("cursor", "pointer")
            .attr("id", "svg_obj")
			.attr("xmlns", "http://www.w3.org/2000/svg")
            .attr("transform", "translate(-1561,64)");

 	// 容器
    var g = svg.append('g').attr("transform","translate(0,0)");
	
 	// 设定投影函数
    var projection = d3.geo.mercator() 
            .center([116.309,39.981])  
            .scale(100000000)
            .translate([-50, 1200]);
    
    //放大比例尺
    /* var zoom = d3.behavior.zoom().scaleExtent([1, 10]).on("zoom", zoomed);
	var circles_group = svg.append("g") .call(zoom); */
	
    //设定 path 函数
	var path = d3.geo.path().projection(projection);
	//设定颜色
    var color = d3.scale.category20()
    //加载json文件
    d3.json("${ctx}/static/d3/assert/map.json", function(error, root) {
    	
			if (error)
				return console.error(error); 
			/* circles_group.selectAll("path") */  //放大比例尺
				 g.selectAll("path")
				    //加载数据
					.data( root.features )
					.enter()
					.append("path") 
					//线的颜色
					.attr("stroke","#000")
					//区域添加Id
					.attr("id",function(){
						pathId++;
						return "path"+pathId;
					})
					//stroke-width设定线的宽度
					.attr("stroke-width",1)
					.attr("fill", function(d,i){
						 //设定区域默认颜色
						return color(1);
					})
					.attr("d", path )
					//鼠标移动到某个区域变色
					.on("mouseover",function(d,i){ 
						d3.select(this) .attr("fill","yellow");
					})
					//鼠标离开某个区域颜色
					.on("mouseout",function(d,i){
						d3.select(this) .attr("fill",color(1));
					})
			 
			//在地图上面添加字	
			 svg.selectAll("text")
			   .data(root.features )
			   .enter()
			   .append("g")
			   .append("text")
			   .text(function(d){
				  return d.properties.name;   //获取josn文件里name
				})
				.attr("dx",function(d){  //设置字体X轴位置
				  return path.centroid(d)[0]
				 })
				 .attr("fill","white")//字体颜色
				 .attr("dy",function(d){   //设置字体Y轴位置
				 return path.centroid(d)[1]
			   });
			     
			   
    });
    //放大缩小初始值
    var scale = 1;
    //xy轴的放大平移默认值
    var gx=0;//70
	var gy=0;//200
	//点击放大缩小次数
	var maxMinCount=0
	function clickMove(id){ 
		//放大地图
        if(id=="amplification"){
        	maxMinCount++;
        	//比例尺放大一倍
            if(maxMinCount==1){
            	//比例尺显示多远代表的多少米
                $("#distance").html("8米");
            	//右边框（竖线）的位置
                $("#rightLine").css("left","80px")
                //底边（底线）的位置
                $("#downLine").css("width","80px");
                //显示米数的位置
                $("#distance").css("width","75px");
            //比例尺放大二倍
            }else if(maxMinCount==2){
                $("#distance").html("5米");
                $("#rightLine").css("left","70px")
                $("#downLine").css("width","70px");
                $("#distance").css("width","69px");
            //比例尺放大三倍
            }else if(maxMinCount==3){
                $("#distance").html("2米");
                $("#rightLine").css("left","60px")
                $("#downLine").css("width","60px");
                $("#distance").css("width","58px");
            //比例尺放大四倍
            }else if(maxMinCount==4){
                $("#distance").html("1米");
                $("#rightLine").css("left","50px")
                $("#downLine").css("width","50px"); 
                $("#distance").css("text-align","center");
                $("#distance").css("width","47px");
            //控制最大 放大四倍
            }else if(maxMinCount>=5){
                maxMinCount=4;
            }
        	//控制放大 最大值
            if($("#moveButton").css("top").substring(0,$("#moveButton").css("top").length-2)=="88"){
				scale=1.4; 
            }else{ 
            	 //每次放大0.1
            	 scale += 0.1;		
            	 //放大地图
            	 d3.selectAll("g").attr("transform","translate("+gx+","+gy+"),scale(" + scale + ")");
            	 //设置放大缩小中间上下移动按钮的位置
            	 $("#moveButton").css("top",$("#moveButton").css("top").substring(0,$("#moveButton").css("top").length-2)-26.5);
            }
        //缩小地图
        }else if(id=="narrow"){
        	maxMinCount--;
            if(maxMinCount==3){
                $("#distance").html("2米");//比例尺
                $("#rightLine").css("left","60px")
                $("#downLine").css("width","60px");
                $("#distance").css("width","58px");
            }else if(maxMinCount==2){
                $("#distance").html("5米");
                $("#rightLine").css("left","70px")
                $("#downLine").css("width","70px");
                $("#distance").css("width","70px");
            }else if(maxMinCount==1){
                $("#distance").html("8米");
                $("#rightLine").css("left","80px")
                $("#downLine").css("width","80px");
                $("#distance").css("width","75px");
            }else if(maxMinCount==0){
                $("#distance").html("10米");
                $("#rightLine").css("left","100px")
                $("#downLine").css("width","100px");
                $("#distance").css("width","99px");
            //控制地图最小缩放尺度
            }else if(maxMinCount<0){
                maxMinCount=0;
            }
            //控制缩小 最小值
            if($("#moveButton").css("top").substring(0,$("#moveButton").css("top").length-2)=="194"){
                scale=0.9; 
            }else{
            	//每次缩小0.1
				scale -= 0.1;
				//缩小地图
				d3.selectAll("g").attr("transform","translate("+gx+","+gy+"),scale(" + scale + ")");
                //设置放大缩小中间上下移动按钮的位置
				$("#moveButton").css("top",Number($("#moveButton").css("top").substring(0,$("#moveButton").css("top").length-2))+Number(26.5));
            }
       	//向上平移地图
        }else if(id=="top"){
        	gy-=10;
        	if(gy>=-120){
        		d3.selectAll("g").attr("transform","translate("+gx+","+gy+"),scale(" + scale + ")");
        	} else{
        		gy=-120;
        	}
        //向下平移地图
        }else if(id=="down"){
        	gy+=10;
        	if(gy<=370){
        		d3.selectAll("g").attr("transform","translate("+gx+","+gy+"),scale(" + scale + ")");
        	} else{
        		gy=370;
        	} 
        //向左平移地图
        }else if(id=="left"){
        	gx-=10;
        	if(gx>=-510){
        		d3.selectAll("g").attr("transform","translate("+gx+","+gy+"),scale(" + scale + ")");
        	}else{
        		gx=-510;
        	}
        //向右平移地图
        }else if(id=="right"){
        	gx+=10;
        	if(gx<=400){
        		d3.selectAll("g").attr("transform","translate("+gx+","+gy+"),scale(" + scale + ")");
        	}else{
        		gx=400;
        	}
        	
        }
    }
      ////放大比例尺
	  /* function zoomed() {
			circles_group.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
	  } */
</script>
