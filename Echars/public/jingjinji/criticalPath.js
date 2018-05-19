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
    for (var i = 0; i < data.length; i++) {
        var dataItem = data[i];
        var fromCoord = geoCoordMap[dataItem[0].name];
        var toCoord = geoCoordMap[dataItem[1].name];
        if (fromCoord && toCoord) {
            res.push({
                fromName: dataItem[0].name,
                toName: dataItem[1].name,
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
allPath.forEach(function(item,i){
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
option = {
    backgroundColor: '#fff',
    title: {
        x:'center',
        textStyle: {
            color: '#fff'
        }
    },
    tooltip: {
        trigger: 'item',
        formatter: function (params) {
            return params.name;
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