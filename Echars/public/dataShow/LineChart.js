option = {
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['邮件营销','联盟广告','直接访问','搜索引擎']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1500]
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'',
            type:'line',
            symbol:'emptyCircle',
            itemStyle: {
                normal: {
                    lineStyle: {            // 系列级个性化折线样式，横向渐变描边
                        width: 2,
                        color: 'red',
                        shadowColor : 'rgba(0,0,0,0.5)',
                        shadowBlur: 10,
                        shadowOffsetX: 8,
                        shadowOffsetY: 8
                    }
                },
                emphasis : {
                    label : {show: true}
                }
            },
            data:[0.492, 0.538, 0.45, 0.547, 0.497, 0.597, 0.356, 0.262, 0.226, 0.349, 0.202, -0.256, -0.247]
        }
    ]
};
var myChart = echarts.init(document.getElementById('main'));    
myChart.setOption(option);