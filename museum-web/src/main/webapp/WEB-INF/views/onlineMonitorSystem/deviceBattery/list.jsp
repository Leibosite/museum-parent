<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>  
<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts-3d.js"></script>
<title>设备电量</title> 


<script type="text/javascript">



var chart;  
$(document).ready(function() {  
	
	$.ajax({
		type : "Post",
		dataType : "JSON",
		url : "pie",
		async : false,
		success:function(msg){
			chart = new Highcharts.Chart({  
		        chart: {  
		            renderTo: 'container',  
		            plotBackgroundColor: null,  
		            plotBorderWidth: null,  
		            plotShadow: false,
		            type: 'pie',
		            options3d: {
		                enabled: true,
		                alpha: 45,
		                beta: 0
		            }
		        },  
		        title: {  
		            text: '甘肃省电量设备图'  
		        },  
		        tooltip: {  
		        	pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },  
		        plotOptions: {  
		            pie: {  
		                allowPointSelect: true,  
		                cursor: 'pointer',  
		                depth: 35,
		                dataLabels: {  
		                    enabled: true,  
		                    color: '#000000',  
		                    connectorColor: '#000000',  
		                    formatter: function() {  
		                        return '<b>'+ this.point.name +'</b>: '+ this.y +' %';  
		                    }  
		                }  
		            }  
		        },  
		        series: msg
		    });  
		}
	}); 
});  
      

 
	
	/* var options=$("#container").highcharts().options;
	options.chart.type='pie';
    new Highcharts.Chart(options); */
 

</script>
	 <div id="container" style="min-width:70%;height:70%"></div>
