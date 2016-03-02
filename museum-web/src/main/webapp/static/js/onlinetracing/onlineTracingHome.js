(function($){
//	$("#filter").keydown(function(event){
//		if(event.keyCode == 13){
//			check(event);
//		}
//	});
	var projectName = "pcrf-admin";
	$("#startBtn").click(function(event){
		if($("#filter").val() == ""){
			alert("Please enter filter info.");
			event.preventDefault();
		}else{
			var msg = "";
			$(this).attr("href", "online-tracing/info?filter="+$("#filter").val());
			$.ajax({
				type: "post",
				async: false,
				url: "/"+projectName+"/online/checkFilter",
				dataType: "jsonp",
				data: {
					action: "check",
					filter: $("#filter").val()
				},
				success: function(data){
					if(data.content.filter == "0"){
						alert("filter already have.");
						event.preventDefault();	
					}else if(data.content.filter == "1"){
						alert("filter has full.");
						event.preventDefault();
					}
				},
				error: function(data){
					alert("error !!");
				},
			});
		}
	});
	
	function check(event){
		if($("#filter").val() == ""){
			alert("Please enter filter info.");
			event.preventDefault();
		}else{
			var msg = "";
			$(this).attr("href", "online-tracing/info?filter="+$("#filter").val());
			$.ajax({
				type: "post",
				async: false,
				url: "/"+projectName+"/online/checkFilter",
				dataType: "jsonp",
				data: {
					action: "check",
					filter: $("#filter").val()
				},
				success: function(data){
					if(data.content.filter == "0"){
						alert("filter already have.");
						event.preventDefault();	
					}else if(data.content.filter == "1"){
						alert("filter has full.");
						event.preventDefault();
					}
				},
				error: function(data){
					alert("error !!");
				},
			});
		}
	}
})(jQuery)
