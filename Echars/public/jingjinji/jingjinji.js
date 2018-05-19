var convertData = function (data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
        var geoCoord = geoCoordMap[data[i].name];
        if (geoCoord) {
            res.push({
                name: data[i].name,
                value: geoCoord.concat(data[i].value)
            });
        }
    }
    return res;
};
var convertDataLine = function (data) {
    var res = [];
    for (var i = 0; i < data.length-1; i++) {
        var fromCoord = geoCoordMap[data[i]];
        var toCoord = geoCoordMap[data[i+1]];
        if (fromCoord && toCoord) {
            res.push({
                fromName: data[i],
                toName: data[i+1],
                coords: [fromCoord, toCoord]
            });
        }
    }
    return res;
};
var series = [];
var color = ['#a6c84c', '#ffa022', '#46bee9'];
series.push({
    name: 'pm2.5',
    type: 'scatter',
    coordinateSystem: 'geo',
    data: convertData(valueOfPM25),
    symbolSize: function (val) {
        return val[2] / 10;
    },
    label: {
        normal: {
            show: false//显示点上的值
        },
        emphasis: {
            show: true//鼠标滑过时显示值
        }
    },
    itemStyle: {
        emphasis: {
            borderColor: '#fff',
            borderWidth: 1
        }
    }
});
linesHS.forEach(function(item,i){
    series.push({
        name: 'line',
        type: 'lines',
        zlevel: 2,
        symbol: ['none', 'arrow'],
        symbolSize: 10,
        lineStyle: {
            normal: {
                color: '#FF3030',
                width: 1,
                opacity: 0.6,
                curveness: 0.2
            }
        },
        data: convertDataLine(item)
    });
});
// linesSJZ.forEach(function(item,i){
//     series.push({
//         name: 'line',
//         type: 'lines',
//         zlevel: 2,
//         symbol: ['none', 'arrow'],
//         symbolSize: 10,
//         lineStyle: {
//             normal: {
//                 color: '#CD00CD',
//                 width: 1,
//                 opacity: 0.6,
//                 curveness: 0.2
//             }
//         },
//         data: convertDataLine(item)
//     });
// });
// linesCZ.forEach(function(item,i){
//     series.push({
//         name: 'line',
//         type: 'lines',
//         zlevel: 2,
//         symbol: ['none', 'arrow'],
//         symbolSize: 10,
//         lineStyle: {
//             normal: {
//                 color: '#000080',
//                 width: 1,
//                 opacity: 0.6,
//                 curveness: 0.2
//             }
//         },
//         data: convertDataLine(item)
//     });
// });
// linesBD.forEach(function(item,i){
//     series.push({
//         name: 'line',
//         type: 'lines',
//         zlevel: 2,
//         symbol: ['none', 'arrow'],
//         symbolSize: 10,
//         lineStyle: {
//             normal: {
//                 color: '#FFFF00',
//                 width: 1,
//                 opacity: 0.6,
//                 curveness: 0.2
//             }
//         },
//         data: convertDataLine(item)
//     });
// });
// linesLF.forEach(function(item,i){
//     series.push({
//         name: 'line',
//         type: 'lines',
//         zlevel: 2,
//         symbol: ['none', 'arrow'],
//         symbolSize: 10,
//         lineStyle: {
//             normal: {
//                 color: '#FFF',
//                 width: 1,
//                 opacity: 0.6,
//                 curveness: 0.2
//             }
//         },
//         data: convertDataLine(item)
//     });
// });
// linesHS.forEach(function(item,i){
//     series.push({
//         name: 'line',
//         type: 'lines',
//         zlevel: 2,
//         symbol: ['none', 'arrow'],
//         symbolSize: 10,
//         lineStyle: {
//             normal: {
//                 color: '#228B22',
//                 width: 1,
//                 opacity: 0.6,
//                 curveness: 0.2
//             }
//         },
//         data: convertDataLine(item)
//     });
// });
option = {
    backgroundColor: '#fff',
    title: {
        text: '河北省主要城市大气污染物传播网络图',
        x:'center',
        textStyle: {
            color: '#000'
        }
    },
    tooltip: {
        trigger: 'item',
        formatter: function (params) {
            return params.name + ' : ' + params.value[2];
        }
    },
    legend: {
        orient: 'vertical',
        y: 'bottom',
        x:'right',
        data:['pm2.5'],
        textStyle: {
            color: '#fff'
        }
    },
    visualMap: {
        min: 0,
        max: 300,
        calculable: true,
        inRange: {
            color: ['#50a3ba', '#eac736', '#d94e5d']
        },
        textStyle: {
            color: '#fff'
        }
    },
    geo: {
        map: 'jingjinji',
        label: {
            emphasis: {
                show: false
            }
        },
        roam: true,//可缩放
        itemStyle: {
            normal: {
                areaColor: '#fff',
                borderColor: '#111'
            },
            emphasis: {
                areaColor: '#fff'
            }
        }
    },
    series: series
}
var myChart = echarts.init(document.getElementById('main'));    
myChart.setOption(option);