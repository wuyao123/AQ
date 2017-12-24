package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import util.CalCityId;
import util.CalPath;
import util.GetCriticalPath;

public class AllPath {

	public static void main(String[] args) throws IOException {
		//读取所有监测站信息
		InputStreamReader stationIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//station1.csv"), "GBK");
		BufferedReader stationCsv = new BufferedReader(stationIsr);
		//读取一个周期内的邻接矩阵
		InputStreamReader marticIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//pathMarticCSV//pathMartic.csv"), "GBK");
		BufferedReader marticCsv = new BufferedReader(marticIsr);
		File path = new File("//Users//wuyao//graduation_project//newData//pathTXT//path.txt");
		path.createNewFile();//创建新文件
		BufferedWriter pathOut = new BufferedWriter(new FileWriter(path)); 
		
/**********************************读取station1文件，将京津冀内所有监测站点存到stationList中**************************************/	
		//将所有站点存储到stationList
		List<String> stationList = new ArrayList<String>();
		String station = stationCsv.readLine();
		while((station = stationCsv.readLine()) != null){
			String[] curLineArray = station.split(",");
			stationList.add(curLineArray[0]+curLineArray[1]);
		}
/********************************************************结束****************************************************************/ 
		
/*********************读取pathMartic文件，将一个周期内所有站点的邻接矩阵转化成路径，写入到allPath文件中******************************/
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

/*********************************对pathList进行深度优先遍历，筛选出符合条件的路径***********************************************/
		GetCriticalPath DCP = new GetCriticalPath();
		CalCityId city = new CalCityId();
		//对pathList进行深度优先遍历
		List<List<String>> lists = DCP.searchPath(pathList);
		int count = 0;
		for (List<String> sl : lists) {
			String strs = "";
			//提取城市编号
			String cityId = city.getCityId(sl.get(0));
			if(!cityId.equals("11")){//设定传播的起点
				continue;
			}else{
				count++;
				String newPaths = "[\r\n";
				for(String p : sl){
					newPaths += '\"' + p + "\","+"\r\n";
				}
				newPaths += "],\r\n";
				pathOut.write(newPaths); // \r\n即为换行 
			}
		}
		System.out.println(count);
		pathOut.flush(); // 把缓存区内容压入文件  
		pathOut.close(); // 最后记得关闭文件  
/********************************************************结束****************************************************************/
	}	
}
