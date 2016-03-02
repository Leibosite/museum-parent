<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>比较</title> 
<script type="text/javascript" src="${staticFile}/js/highstock/highstock.js"></script>
<script type="text/javascript" src="${staticFile}/js/highstock/exporting.js"></script>
<script>
	$(function() {
	var seriesOptions = [],
		yAxisOptions = [],
		seriesCounter = 0,
		names = ['highstock-data'],
		colors = Highcharts.getOptions().colors;
	
	var path = "${staticFile}/js/highstock/json/";
	$.each(names, function(i, name) {  
		$.getJSON(path+ name.toLowerCase() +'.json',function(result) {
			var data = result.data;
			seriesOptions[i] = {
				name: name,
				data: data
			};
 
			seriesCounter++;

			if (seriesCounter == names.length) {
				createChart();
			}
		});
	});



	// create the chart when all data is loaded
	function createChart() {

		$('#container').highcharts('StockChart', {
		    chart: {
		    },

		    rangeSelector: {
		        selected: 4
		    },

		    yAxis: {
		    	labels: {
		    		formatter: function() {
		    			return (this.value > 0 ? '+' : '') + this.value + '%';
		    		}
		    	},
		    	plotLines: [{
		    		value: 0,
		    		width: 2,
		    		color: 'silver'
		    	}]
		    },
		    credits:{
		    	enabled:false
		    },
		    plotOptions: {
		    	series: {
		    		compare: 'percent'
		    	}
		    },
		    
		    tooltip: {
		    	pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
		    	valueDecimals: 2
		    },
		    
		    series: seriesOptions
		});
	}

});				
</script>
</head>
<body>
	<div id="container" style="height: 400px; min-width: 310px"></div>
</body>
</html>