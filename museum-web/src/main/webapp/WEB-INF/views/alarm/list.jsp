<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>实时告警监控</title>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/scripts.jsp"%>
<%@ include file="../common/styles.jsp"%>
<style type="text/css">
ul, li {
	margin: 0;
	padding: 0;
	list-style: none;
}

#content {
	border: 1px solid #ddd;
	height:490px;
	width: 100%;
	overflow: hidden;
}

.ulCss li {
	height: 20px;
	line-height: 30px;
	font-size: 20px; 
	text-align: center;
}
</style>
<script type="text/javascript">
	var i = 0;
	$(function() {
		window.setInterval(function(){
			i++;
			$("#content").animate({
				scrollTop : (i * 30) + "px"
			}, 'slow');
		}, 2000);
	});
	
	
	function downExcel(){
		var cc = [];
		$("#messages").find('li').each(function(dom) {
			 cc.push({cc:$(this).text(),sendDate:$(this).attr('lang')})
		})
		location.href="${ctx}/doExcel/down?content="+JSON.stringify(cc);
	}
</script>
</head>
<body>
<body class="container" style="background-color: #E5E5E5">

	<div class="widget-box">
		<div class="widget-title">
			<span class="icon"> <i class="icon-bookmark"></i>
			</span>
			<h5>告警监控</h5>
			<div id="excel" class="buttons">
				<a class="btn btn-mini" onclick="downExcel()"><i class="icon-download"></i>导出EXcel</a>
			</div>
		</div>
		<div class="widget-content" style="height: 500px;">
			<div id="content" style=" margin: auto;">
				<ul class="ulCss" id="messages">
				</ul>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		//ws://10.0.1.67:8989/websocket                          ws://localhost:8080/museum-web/websocket
		var webSocket = new WebSocket('ws://127.0.0.1:8989/websocket');

		webSocket.onerror = function(event) {
			onError(event)
		};

		webSocket.onopen = function(event) {
			onOpen(event)
		};

		webSocket.onmessage = function(event) {
			onMessage(event)
		};

		function onMessage(event) {
			i++;
			document.getElementById('messages').innerHTML += '<li lang='+'2012-2-2'+'>接收的'
					+ event.data + "</li>";
		}

		
		/* (a>b)?true:false */
		function onOpen(event) {
			/* document.getElementById('messages').innerHTML = '连接建立'; */
		}

		function onError(event) {
			/* alert(event.data); */
		}

		function start() {
			webSocket.send('hello');
			return false;
		}
	</script>
</body>

</html>