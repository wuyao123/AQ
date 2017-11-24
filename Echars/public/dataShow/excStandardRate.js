option = {
    title:{
        text:'京津冀各市污染物超标率',
        textStyle:{
            fontSize:24,
            color:'rgb(23, 20, 20)'
        },
        left:'center'
    },
    color: ['rgb(5, 41, 243)'],
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis : [
        {
            type : 'category',
            data : ['北京', '张家口', '唐山', '天津', '廊坊', '秦皇岛', 
                    '承德市', '保定', '衡水', '沧州', '石家庄'],
            axisTick: {
                alignWithLabel: true
            },
            axisLabel:{ //调整x轴的lable  
                textStyle:{
                    fontSize:18 // 让字体变大
                }
            }
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'超标率',
            type:'bar',
            label: {
                normal: {
                    show: true,
                    position: 'outside',
                    textStyle:{
                        fontSize:12 // 让字体变大
                    }
                }
            },
            barWidth: '60%',
            data:[0.551286441, 0.28977072, 0.670913385, 0.607096911, 
                  0.700730328, 0.611200688, 0.453030708, 0.742635891, 
                  0.826531914, 0.777820177, 0.773584906]
        }
    ]
};
var myChart = echarts.init(document.getElementById('main'));    
myChart.setOption(option);