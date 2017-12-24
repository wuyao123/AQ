package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.csvreader.CsvWriter;

import util.CalCityId;
import util.GetCriticalPath;

public class PathToArray {

	public static void main(String[] args) throws IOException {
		//读取所有监测站信息
		InputStreamReader stationIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//station1.csv"), "GBK");
		BufferedReader stationCsv = new BufferedReader(stationIsr);
		//读取一个周期内的邻接矩阵
		InputStreamReader marticIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//pathMarticCSV//pathMartic.csv"), "GBK");
		BufferedReader marticCsv = new BufferedReader(marticIsr);
		//读取以一个城市为起点的所有路径
		InputStreamReader pathIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//linesCSV//allLinesHS.csv"), "GBK");
		BufferedReader pathCsv = new BufferedReader(pathIsr);
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//pathMarticCSV//pathMarticHS.csv"),"GBK");
	    BufferedWriter writer = new BufferedWriter(writerStream);
	    CsvWriter cwriter = new CsvWriter(writer, ',');
		File pathFile = new File("//Users//wuyao//graduation_project//newData//linesTXT//pathDataHS.txt");
		pathFile.createNewFile();//创建新文件
		BufferedWriter out = new BufferedWriter(new FileWriter(pathFile)); 
		File pathNum = new File("//Users//wuyao//graduation_project//newData//pathTXT//pathNumHS.txt");
		pathNum.createNewFile();//创建新文件
		BufferedWriter pathNumOut = new BufferedWriter(new FileWriter(pathNum)); 
		File allPath = new File("//Users//wuyao//graduation_project//newData//pathTXT//allPath.txt");
		allPath.createNewFile();//创建新文件
		BufferedWriter allPathOut = new BufferedWriter(new FileWriter(allPath)); 
		
/**********************************读取station1文件，将京津冀内所有监测站点存到stationList中**************************************/	
		//将所有站点存储到stationList
		List<String> stationList = new ArrayList<String>();
		String station = stationCsv.readLine();
		while((station = stationCsv.readLine()) != null){
			String[] curLineArray = station.split(",");
			stationList.add(curLineArray[0]+curLineArray[1]);
		}
	    writeToExcelHead(cwriter,stationList);
/********************************************************结束****************************************************************/  
	
/*********************************************初始化matrix用来存储邻接矩阵*****************************************************/	
		//初始化矩阵matrix
		Map<String, Map<String, Double>> matrix = new LinkedHashMap<String, Map<String, Double>>();
		for (int i = 0; i < stationList.size(); i++) {
			Map<String, Double> map = new LinkedHashMap<String, Double>();
			for (int j = 0; j < stationList.size(); j++) {
				map.put(stationList.get(j), 0.0);
			}
			matrix.put(stationList.get(i), map);
		}
/********************************************************结束****************************************************************/  
		
/*********************读取pathMartic文件，将一个周期内所有站点的邻接矩阵转化成路径，写入到allPath文件中******************************/
		String martic = marticCsv.readLine();
		while((martic = marticCsv.readLine()) != null){
			String[] curLineArray = martic.split(",");
			String start = curLineArray[0];
			for (int i = 1; i < curLineArray.length; i++) {
				if(curLineArray[i].equals("1.0")){
					continue;
				}
				String node = "[{name:\'" + start + "\'},{name:\'" +stationList.get(i-1) +"\'}],\r\n";
				allPathOut.write(node);
			}
		}
/********************************************************结束****************************************************************/ 
		
/*********************************读取allLines***文件，以指定城市为起点，统计边出现的次数*****************************************/ 
		Map<String,Integer> map = new HashMap<String,Integer>();
		String path = pathCsv.readLine();
		int count = 0;
		while((path = pathCsv.readLine()) != null){
			String[] curLineArray = path.split(",\"");
			String[] paths = curLineArray[1].split(",");
			if(paths.length < 8){
				continue;
			}
			count++;
			//生成路径数据集
			String newPaths = "[\r\n";
			for(String p : paths){
				newPaths += '\"' + p + "\","+"\r\n";
			}
			newPaths += "],\r\n";
			out.write(newPaths); // \r\n即为换行 
			
			//统计路径中相邻点之间路劲出现的次数
			for (int i = 0; i < paths.length-1; i++) {
				String key = paths[i] + "," + paths[i+1];
				if(map.containsKey(key)){
					map.put(key, map.get(key) + 1);
				}else{
					map.put(key, 0);
				}
			}
		} 
/********************************************************结束****************************************************************/ 
		
/*********************************对边出现的次数进行排序，当出现的次数大于0.02时，将该边映射到matrix*******************************/
		ArrayList<Entry<String, Integer>> arrayList = new ArrayList<Entry<String, Integer>>(map.entrySet());  
		//排序  
		Collections.sort(arrayList, new Comparator<Entry<String, Integer>>() {
		    public int compare(Entry<String, Integer> map1, Entry<String, Integer> map2) {
		        return (map2.getValue() - map1.getValue());  
		    }  
		});  
		//输出  
		List<String> pathList = new ArrayList<String>();
		for (Entry<String, Integer> entry : arrayList) { 
			Double rate = Double.parseDouble(entry.getValue()+"")/count;
			if(rate <= 0.02){
				break;
			}
			String[] keys = entry.getKey().split(",");
			pathList.add(keys[0] + ",,," + keys[1] + ",,," + rate);
			//设置输出到text的数据格式
			String node = "[{name:\'" + keys[0] + "\'},{name:\'" + keys[1] +"\'}],\r\n";
			pathNumOut.write(node); // \r\n即为换行 
			//将路径映射到矩阵matrix中
			matrix.get(keys[0]).put(keys[1], rate);
		} 
		//将matrix输出到excle
		for(String start : matrix.keySet()){
			writeToExcelContent(cwriter,start,matrix.get(start));
		}
/********************************************************结束****************************************************************/
		
/*********************************对pathList进行深度优先遍历，筛选出符合条件的路径***********************************************/
		GetCriticalPath DCP = new GetCriticalPath();
		CalCityId city = new CalCityId();
		//对pathList进行深度优先遍历
		List<List<String>> lists = DCP.searchPath(pathList);
		for (List<String> sl : lists) {
			String strs = "";
			//提取城市编号
			String cityId = city.getCityId(sl.get(0));
			if(sl.size() < 4 || !cityId.equals("11")){//设定传播的起点
				continue;
			}else{
				for (String str : sl) {
					System.out.print(str+" ");
					strs += str + ",";
				}
				System.out.println();
			}
		}
/********************************************************结束****************************************************************/
		
		System.out.println(count);
		pathNumOut.flush(); // 把缓存区内容压入文件  
		pathNumOut.close(); // 最后记得关闭文件  
		out.flush(); 
        out.close(); 
        allPathOut.flush();
        allPathOut.close();
	}

	private static void writeToExcelHead(CsvWriter cwriter, List<String> stationList) throws IOException {
		cwriter.write("");
		for(String head : stationList){
			cwriter.write(head);
		}
		cwriter.endRecord();
		cwriter.flush();
	}
	
	private static void writeToExcelContent(CsvWriter cwriter, String start, Map<String, Double> map) throws IOException {
		cwriter.write(start);
		for(String content : map.keySet()){
			cwriter.write(map.get(content)+"");
		}
		cwriter.endRecord();
		cwriter.flush();
	}

}
