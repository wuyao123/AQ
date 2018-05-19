package main;

import util.CalCityId;
import util.GetCriticalPath;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeCoverage {
    public static void main(String[] args) throws IOException {
        String STATIONID = "22";
        // 关键路径序列
        String PATH[] = {"22010","1023","1024","1031","19009","1007","1027","1028","1032"};
        //读取所有监测站信息
        InputStreamReader stationIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//station1.csv"), "GBK");
        BufferedReader stationCsv = new BufferedReader(stationIsr);
        //读取一个周期内的邻接矩阵
        InputStreamReader marticIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//pathMarticCSV//pathMartic.csv"), "GBK");
        BufferedReader marticCsv = new BufferedReader(marticIsr);
        //读取各地区污染过程中在关键路径上的传播次数
        InputStreamReader marticPath = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData////pollutionPathNum.csv"), "GBK");
        BufferedReader marticPathCsv = new BufferedReader(marticPath);

        /**********************************读取station1文件","将京津冀内所有监测站点存到stationList中**************************************/
        //将所有站点存储到stationList
        List<String> stationList = new ArrayList<String>();
        String station = stationCsv.readLine();
        while((station = stationCsv.readLine()) != null){
            String[] curLineArray = station.split(",");
            stationList.add(curLineArray[0]+curLineArray[1]);
        }
        /********************************************************结束****************************************************************/

        /*********************读取pathMartic文件","将一个周期内所有站点的邻接矩阵转化成路径","写入到allPath文件中******************************/
        List<String> pathList = new ArrayList<String>();
        String martic = marticCsv.readLine();
        while((martic = marticCsv.readLine()) != null){
            String[] curLineArray = martic.split(",");
            String start = curLineArray[0];
            for (int i = 1; i < curLineArray.length; i++) {
                if(curLineArray[i].equals("1.0")){
                    continue;
                }
                pathList.add(start + ",,," + stationList.get(i-1) + ",,," + curLineArray[i]);
            }
        }
        /********************************************************结束****************************************************************/

        /*********************读取pollutionPathNum************************************************************************************/

        String marticPathNum = marticPathCsv.readLine();
        Map<String,String> pathNum = new HashMap<>();
        while((marticPathNum = marticPathCsv.readLine()) != null){
            String[] curLineArray = marticPathNum.split(",");
            String key = curLineArray[0];
            String val = curLineArray[1];
            pathNum.put(key, val);
        }
        /********************************************************结束****************************************************************/

        /*********************************对pathList进行深度优先遍历","筛选出符合条件的路径***********************************************/
        GetCriticalPath DCP = new GetCriticalPath();
        CalCityId city = new CalCityId();
        //存储节点
        List<String> nodes = new ArrayList<String>();
        //对pathList进行深度优先遍历
        List<List<String>> lists = DCP.searchPath(pathList);
        Map<String,String> calPathNum = new HashMap<String,String>();

        int PathCount = 0;
        Double sum = 0.0;
        for (List<String> sl : lists) {
            String strs = "";
            //提取城市编号
            String cityId = city.getCityId(sl.get(0));
            if(!cityId.equals(STATIONID)){//设定传播的起点
                continue;
            }else{
                PathCount++;
                int len = sl.size();
                int count = 0;

                for(int k=0;k<len-1;k++){
                    if(calPathNum.containsKey(sl.get(k)+","+sl.get(k+1))){
                        continue;
                    }else{
                        calPathNum.put(sl.get(k)+","+sl.get(k+1),"a");
                    }
                }


                for(String p : sl){
                    // 计算子图和关键传播路径的公共节点
                    for(String l : PATH){
                        if(p.contains(l)){
                            count++;
                        }
                    }
                }
                System.out.println(count+"/"+len+"="+count/(len+0.0));
                sum += count/(len+0.0);
            }
        }
        Double pathCoverage = calPathNum.size()/Double.parseDouble(pathNum.get(STATIONID));
        System.out.println(PathCount);
        System.out.println("节点覆盖率为："+sum/PathCount);
        System.out.println("路径覆盖率为："+pathCoverage);
        /********************************************************结束****************************************************************/
    }

}
