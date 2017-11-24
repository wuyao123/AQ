option = {
    legend: {
        data:['女性','男性']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataZoom : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    xAxis : [
        {
            type : 'value',
            scale:true,
            axisLabel : {
                formatter: '{value}'
            }
        }
    ],
    yAxis : [
        {
            type : 'value',
            scale:true,
            axisLabel : {
                formatter: '{value}'
            }
        }
    ],
    series : [
        {
            name:'',
            type:'scatter',
            data: [
            		[1,70],[2,69],[3,68],[19,52],[20,51],[21,50],[22,0],
                   	[23,0],[24,0],[47,0],[48,0],[49,0],[50,21],[51,20],
                   	[52,19],[68,3],[69,2],[70,1]
                  ]
        }
    ]
};
var myChart = echarts.init(document.getElementById('main'));    
myChart.setOption(option);