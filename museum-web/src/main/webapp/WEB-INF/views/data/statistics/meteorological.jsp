<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>气象站</title>

<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts-more.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/modules/data.js"></script>
<script type="text/javascript">
$(function() {
	 $('#windContainer').highcharts({
	    	data: {
		    	table: 'freq',
		    	startRow: 1,
		    	endRow: 17,
		    	endColumn: 7
		    },
		    
		    chart: {
		        polar: true,
		        type: 'column',
		        backgroundColor:''
		    },
		    
		    title: {
		        text: ''
		    },
		    
		    subtitle: {
		    	text: ''
		    },
		    
		    pane: {
		    	size: '85%'
		    },
		    
		    legend: {
		    	reversed: true,
		    	align: 'right',
		    	verticalAlign: 'top',
		    	y: 50,
		    	layout: 'vertical'
		    },
		    
		    xAxis: {
		    	tickmarkPlacement: 'on'
		    },
		        
		    yAxis: {
		        min: 0,
		        endOnTick: false,
		        showLastLabel: true,
		        title: {
		        	text: 'Frequency (%)'
		        },
		        labels: {
		        	formatter: function () {
		        		return this.value + '%';
		        	}
		        }
		    },
		    
		    tooltip: {
		    	valueSuffix: '%',
		    	followPointer: true
		    },
		        
		    plotOptions: {
		        series: {
		        	stacking: 'normal',
		        	shadow: false,
		        	groupPadding: 0,
		        	pointPlacement: 'on'
		        }
		    }
		});
	
	
	$('#container').highcharts({
		chart : {
			type : 'gauge',
			plotBackgroundColor : null,
			plotBackgroundImage : null,
			plotBorderWidth : 0,
			plotShadow : false,
			backgroundColor:''
		},
		title : {
			text : ''
		},
		credits : {
			enabled : false
		},
		pane : {
			startAngle : -150,
			endAngle : 150,
			background : [ {
				backgroundColor : {
					linearGradient : {
						x1 : 0,
						y1 : 0,
						x2 : 0,
						y2 : 1
					},
					stops : [ [ 0, '#FFF' ], [ 1, '#333' ] ]
				},
				borderWidth : 0,
				outerRadius : '109%'
			}, {
				backgroundColor : {
					linearGradient : {
						x1 : 0,
						y1 : 0,
						x2 : 0,
						y2 : 1
					},
					stops : [ [ 0, '#333' ], [ 1, '#FFF' ] ]
				},
				borderWidth : 1,
				outerRadius : '107%'
			}, {
			// default background
			}, {
				backgroundColor : '#DDD',
				borderWidth : 0,
				outerRadius : '105%',
				innerRadius : '103%'
			} ]
		},

		// the value axis
		yAxis : {
			min : 0,
			max : 500,
			minorTickInterval : 'auto',
			minorTickWidth : 1,
			minorTickLength : 0,
			minorTickPosition : 'inside',
			minorTickColor : 'red',

			tickPixelInterval : 50,
			tickWidth : 2,
			tickPosition : 'inside',
			tickLength : 0,
			tickColor : 'yellow',
			labels : {
				step : 2,
				rotation : 'auto'
			},
			title : {
				text : ''
			},
			plotBands : [ {
				from : 0,
				to : 50,
				color : 'green' // 优
			}, {
				from : 50,
				to : 150,
				color : 'blue'//良
			}, {
				from : 150,
				to : 300,
				color : 'yellow'//轻度污染
			}, {
				from : 300,
				to : 400,
				color : 'black'//中度污染
			}, {
				from : 400,
				to : 500,
				color : 'red'//严重污染
			} ]
		},
		tooltip : {
			formatter : function() {
				s = this.y;
				if (this.y < 50) {
					s += '优';
				} else if (this.y < 150) {
					s += '良';
				} else if (this.y < 300) {
					s += '轻度污染';
				} else if (this.y < 400) {
					s += '中度污染';
				} else if (this.y < 500) {
					s += '严重污染';
				}
				return s;
			}
		},
		series : [ {
			data : [ 175 ]
		} ]
	},
	// Add some life
	function(chart) {
		if (!chart.renderer.forExport) {
			setInterval(function () {
			     var point = chart.series[0].points[0],
			         newVal,
			         inc = Math.round((Math.random() - 0.5) * 20);
			     
			     newVal = point.y + inc;
			     if (newVal < 0 || newVal > 200) {
			         newVal = point.y - inc;
			     }
			     
			     point.update(newVal);
			     
			 }, 3000);
		}
	});
});
</script>
</head>
<body>
	<table style="width: 100%; height: 100%; <%-- background: url(${ctx}/static/img/bg.jpg) --%>">
		<tbody>
			<tr>
				<td width="128px" height="100%" align="center">
					<table border="0" height="100%">
						<tbody>
							<tr>
								<td valign="top" align="center" style="padding: 20px 0 0 0">
									<span style="font-size: 20px; font-weight: bold; color: rgb(255, 0, 0);" >甘肃省博物馆</span><br><br>   
									<span title="温度"  style="font-size: 36px; font-weight: bold; color: #003366; padding: 0 0 0 5px">12℃</span>
								</td>
							</tr>
							<tr>
								<td valign="top" align="center">
									<span style="font-size: 25px; color: #0066CC" id="weaSpan">阴 西北风</span><br>
								</td>
							</tr>
							<tr>
								<td valign="bottom" align="center" style="padding: 0 0 10px 0">
									<span title="湿度" style="font-size: 36px; font-weight: bold; color: #003366; padding: 0 0 0 5px;">30%</span>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
				<td>
					<table width="100%" height="100%" border=1>
						<tbody>
							<tr>
								<td style="text-align: center; font-size: 12px">
									<table border=1 cellpadding="5" style="margin: auto;">
										<tr>
											<td>温&nbsp&nbsp&nbsp度：</td>
											<td>30</td>
										</tr>
										<tr>
											<td>湿&nbsp&nbsp&nbsp度：</td>
											<td>30</td>
										</tr>
										<tr>
											<td>光&nbsp&nbsp&nbsp照：</td>
											<td>30</td>
										</tr>
										<tr>
											<td>紫外线：</td>
											<td>10</td>
										</tr>
										<tr>
											<td>风&nbsp&nbsp&nbsp速：</td>
											<td>30</td>
										</tr>
										<tr>
											<td>风&nbsp&nbsp&nbsp向：</td>
											<td>30</td>
										</tr>
										<tr>
											<td>灰&nbsp&nbsp&nbsp尘：</td>
											<td>30</td>
										</tr>
										<tr>
											<td>GPS数据：</td>
											<td>时间、位置</td>
										</tr> 
									</table>
								</td>
								<td width="200px" valign="middle" align="right" style="padding: 0 0 0 0" rowspan="3">
									<div id="windContainer" style="min-width:350px;height:250px"></div>
									<div style="display:none"> <!-- Source: http://or.water.usgs.gov/cgi-bin/grapher/graph_windrose.pl --> <table id="freq" border="0" cellspacing="0" cellpadding="0"> <tr nowrap bgcolor="#CCCCFF"> <th colspan="9" class="hdr">Table of Frequencies (percent)</th> </tr> <tr nowrap bgcolor="#CCCCFF"> <th class="freq">Direction</th> <th class="freq">< 0.5 m/s</th> <th class="freq">0.5-2 m/s</th> <th class="freq">2-4 m/s</th> <th class="freq">4-6 m/s</th> <th class="freq">6-8 m/s</th> <th class="freq">8-10 m/s</th> <th class="freq">> 10 m/s</th> <th class="freq">Total</th> </tr> <tr nowrap> <td class="dir">N</td> <td class="data">1.81</td> <td class="data">1.78</td> <td class="data">0.16</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">3.75</td> </tr> <tr nowrap bgcolor="#DDDDDD"> <td class="dir">NNE</td> <td class="data">0.62</td> <td class="data">1.09</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">1.71</td> </tr> <tr nowrap> <td class="dir">NE</td> <td class="data">0.82</td> <td class="data">0.82</td> <td class="data">0.07</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">1.71</td> </tr> <tr nowrap bgcolor="#DDDDDD"> <td class="dir">ENE</td> <td class="data">0.59</td> <td class="data">1.22</td> <td class="data">0.07</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">1.88</td> </tr> <tr nowrap> <td class="dir">E</td> <td class="data">0.62</td> <td class="data">2.20</td> <td class="data">0.49</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">3.32</td> </tr> <tr nowrap bgcolor="#DDDDDD"> <td class="dir">ESE</td> <td class="data">1.22</td> <td class="data">2.01</td> <td class="data">1.55</td> <td class="data">0.30</td> <td class="data">0.13</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">5.20</td> </tr> <tr nowrap> <td class="dir">SE</td> <td class="data">1.61</td> <td class="data">3.06</td> <td class="data">2.37</td> <td class="data">2.14</td> <td class="data">1.74</td> <td class="data">0.39</td> <td class="data">0.13</td> <td class="data">11.45</td> </tr> <tr nowrap bgcolor="#DDDDDD"> <td class="dir">SSE</td> <td class="data">2.04</td> <td class="data">3.42</td> <td class="data">1.97</td> <td class="data">0.86</td> <td class="data">0.53</td> <td class="data">0.49</td> <td class="data">0.00</td> <td class="data">9.31</td> </tr> <tr nowrap> <td class="dir">S</td> <td class="data">2.66</td> <td class="data">4.74</td> <td class="data">0.43</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">7.83</td> </tr> <tr nowrap bgcolor="#DDDDDD"> <td class="dir">SSW</td> <td class="data">2.96</td> <td class="data">4.14</td> <td class="data">0.26</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">7.37</td> </tr> <tr nowrap> <td class="dir">SW</td> <td class="data">2.53</td> <td class="data">4.01</td> <td class="data">1.22</td> <td class="data">0.49</td> <td class="data">0.13</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">8.39</td> </tr> <tr nowrap bgcolor="#DDDDDD"> <td class="dir">WSW</td> <td class="data">1.97</td> <td class="data">2.66</td> <td class="data">1.97</td> <td class="data">0.79</td> <td class="data">0.30</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">7.70</td> </tr> <tr nowrap> <td class="dir">W</td> <td class="data">1.64</td> <td class="data">1.71</td> <td class="data">0.92</td> <td class="data">1.45</td> <td class="data">0.26</td> <td class="data">0.10</td> <td class="data">0.00</td> <td class="data">6.09</td> </tr> <tr nowrap bgcolor="#DDDDDD"> <td class="dir">WNW</td> <td class="data">1.32</td> <td class="data">2.40</td> <td class="data">0.99</td> <td class="data">1.61</td> <td class="data">0.33</td> <td class="data">0.00</td> <td class="data">0.00</td> <td class="data">6.64</td> </tr> <tr nowrap> <td class="dir">NW</td> <td class="data">1.58</td> <td class="data">4.28</td> <td class="data">1.28</td> <td class="data">0.76</td> <td class="data">0.66</td> <td class="data">0.69</td> <td class="data">0.03</td> <td class="data">9.28</td> </tr> <tr nowrap bgcolor="#DDDDDD"> <td class="dir">NNW</td> <td class="data">1.51</td> <td class="data">5.00</td> <td class="data">1.32</td> <td class="data">0.13</td> <td class="data">0.23</td> <td class="data">0.13</td> <td class="data">0.07</td> <td class="data">8.39</td> </tr> <tr nowrap> <td class="totals">Total</td> <td class="totals">25.53</td> <td class="totals">44.54</td> <td class="totals">15.07</td> <td class="totals">8.52</td> <td class="totals">4.31</td> <td class="totals">1.81</td> <td class="totals">0.23</td> <td class="totals"> </td> </tr> </table> </div>
								</td>
								<td width="200px" valign="middle" align="right"  rowspan="3">
									<div>
										<div id="container" style="min-width: 250px; height: 250px"></div>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>