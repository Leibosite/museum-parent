(function($){
	var projectName = "pcrf-admin";
	var serverIp = "192.168.1.35";

	//获得地址栏的filter
	var param = window.location.search
	var interval;
	$.ajax({
		type: "post",
		url: "/"+projectName+"/online/startThread",
		data: {
			action: "init",
			filter: param.substring(param.indexOf("=")+1)
		},
		dataType: 'jsonp',
		success: function(data){
			interval = setInterval(function(){
				$.ajax({
					type: "get",
					url: "/"+projectName+"/online/getContent",
					dataType: "jsonp",
					data: {
						action: "add",
						filter: param.substring(param.indexOf("=")+1)
					},
					success: function(data){
						initTree(data.content);
					},
					error: function(data){
						alert("servlet error!");
						clearInterval(interval);
					},

				});
			},3000);
		},
		error: function(){
			alert('servlet error!');		
		}
	});
	$("#stopBtn").click(function(){
		var btn = $(this);
		$.ajax({
			type: "post",
			url: "/"+projectName+"/online/stopPage",
			dataType: "jsonp",
			data: {
				action: "stop",
				filter: param.substring(param.indexOf("=")+1)
			},
			success: function(data){
				btn.button("loading");
				setTimeout(function(){
					btn.text("Stopped");
				}, 1500);
				
				if(interval != null)
					clearInterval(interval);
			},
			error: function(data){
				alert("stop error !!");
				if(interval != null)
					clearInterval(interval);
			},
		});
	});

	$("#saveBtn").click(function(){
		var btn = $(this);
		$.ajax({
			type: "post",
			url: "/"+projectName+"/online/savePackets",
			async: false,
			dataType: "jsonp",
			data: {
				action: "save",
				filter: param.substring(param.indexOf("=")+1)
			},
			success: function(data){
				if(data.content == 1){
					//写死的ip
					window.location.href="http://"+serverIp+":8080/"+projectName+"/pcapFile.pcap";					
				}
			},
			error: function(data){
				alert("save error !!");
				if(interval != null)
					clearInterval(interval);
			},
		});
	});
	//关闭页签的事件监听
	$(window).unload(function(){
		$.ajax({
			type: "post",
			url: "/"+projectName+"/online/closeTab",
			dataType: "jsonp",
			data: {
				action: "stop and close",
				filter: param.substring(param.indexOf("=")+1)
			},
			success: function(data){
			},
			error: function(data){
			},
		});
	});
	//构建树
	function initTree(data){
		var tree = '';
		//循环每一个packet
		for(var i in data){
			var packet = data[i];
			
			tree += '<li class="packet">';
			tree += '<a href="#" role="branch" class="tree-toggle closed" data-toggle="branch"><strong>';
			tree += packet.number+' | '+packet.source+' | '+packet.destination+' | '+packet.protocol+' | <span>'+packet.info+'</span>';
			tree += '</strong></a>';
			tree += '<ul class="branch">';
			
			//tree += packet.protosList.geninfo == null ? '' : getProtoHtml(packet.protosList.geninfo);
			tree += packet.protosList.frame == null ? '' : getProtoHtml(packet.protosList.frame);
			delete packet.protosList.frame;
			tree += packet.protosList.eth == null ? '' : getProtoHtml(packet.protosList.eth);
			delete packet.protosList.eth;
			tree += packet.protosList.ip == null ? '' : getProtoHtml(packet.protosList.ip);
			delete packet.protosList.ip;
			tree += packet.protosList.tcp == null ? '' : getProtoHtml(packet.protosList.tcp);
			delete packet.protosList.tcp;
			delete packet.protosList.geninfo;
			//满足多个diameter的情况
			for(i in packet.protosList){
				tree += packet.protosList[i] == null ? '' : getProtoHtml(packet.protosList[i]);
			}
			
			tree += '</ul>';
			tree += '</li>';
		}
		
		function getProtoHtml(proto){
			var html = '';
			html += '<li class='+proto.name+'>';
			html += '<a href="#" role="branch" class="tree-toggle closed" data-toggle="branch"><strong>'+proto.showname+'</strong></a>';
			html += '<ul class="branch">';
			for(var i in proto.fieldsList){
				html += getFieldHtml(proto.fieldsList[i]);
			}
			html += '</ul>';
			html += '</li>';
			return html;
		}
		
		function getFieldHtml(field){
			var html = '';
			html += '<li>';
			if(field.fieldsList.length > 0){
				html += '<a href="#" role="branch" class="tree-toggle closed" data-toggle="branch">';
				html += field.showname;
				html += '</a>';
				html += '<ul class="branch">';
				for(var i=0; i<field.fieldsList.length; i++){
					html += getFieldHtml(field.fieldsList[i]);
				}
				html += '</ul>';
			}else{
				html += '<a href="#" role="leaf">';
				html += field.showname;
				html += '</a>';
			}
			html += '</li>';
			return html;
		}
		
		$("#tree-ul").append(tree);
		
		$(".packet").on("mouseover",function(){
			$(this).find('span').css('background-color','#faa732');
		});
		$(".packet").on("mouseout",function(){
			$(this).find('span').css('background-color','');
		});
		
	}
})(jQuery)
