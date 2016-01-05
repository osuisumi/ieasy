<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script>
	var chart ;
	var options = {
		chart: {
		    plotBackgroundColor: null,
		    plotBorderWidth: null,
		    plotShadow: false,
		    type: 'column',
		    renderTo:'container'
		},
        title: {},
        exporting : {
			filename : '每月稼动率'
		}, 
        xAxis: {
            categories: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
            labels : {
				align : 'center',
				style : {
					fontSize : '12px',
					fontWeight: 'bold',
					color: '#000',
					fontFamily : 'Verdana, sans-serif'
				}
			}
        },
        yAxis:[{
         	title:{
         		text:"",
         		rotation:0,
         		
         	},
         	min:0,
            labels : {
				style : {
					fontWeight: 'bold',
					color: '#000',
					fontFamily : 'Verdana, sans-serif'
				}
			}
        }],
        enabled: false,
        tooltip : {
        	headerFormat: '<span style="font-size:12px"><b>{point.key}</b></span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} %</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true,
            crosshairs: true
		}
	};
	
	
	var drawChart = function(d, year) {
		var dataLabels = {
			enabled: true,
			color: '#000000',
			style: {
                fontSize: '13px',
                fontFamily: 'Verdana, sans-serif',
                textShadow: '0 0 1px black'
            },
            /* enabled: true,
            rotation: -90,
            color: '#FFFFFF',
            align: 'right',
            x: 4,
            y: 10,
            style: {
                fontSize: '13px',
                fontFamily: 'Verdana, sans-serif',
                textShadow: '0 0 3px black'
            }, */
            formatter: function() {
                return this.y +'%';
            }
        };
		options.title.text = year + '<p><b>每月稼动率</b></p>' ;
		options["series"] = [{name: '稼动率', data: d, dataLabels: dataLabels}] ;
		chart = new Highcharts.Chart(options);
	};
	
</script>
<div id="container" style="width:100%;height:600px;"></div>
