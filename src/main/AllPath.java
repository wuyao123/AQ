package main;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;
import util.CalCityId;
import util.CalPath;
import util.GetCriticalPath;

public class AllPath {

	public static void main(String[] args) throws IOException {
		String STATIONID = "22";
		//冬季
		String STATIONNAME = "LF";
		//读取所有监测站信息
		InputStreamReader stationIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//station1.csv"), "GBK");
		BufferedReader stationCsv = new BufferedReader(stationIsr);
		//读取一个周期内的邻接矩阵
		InputStreamReader marticIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//pathMarticCSV//pathNumMartic.csv"), "GBK");
		BufferedReader marticCsv = new BufferedReader(marticIsr);
		File path = new File("//Users//wuyao//graduation_project//newData//pathTXT//path.txt");
		path.createNewFile();//创建新文件
		BufferedWriter pathOut = new BufferedWriter(new FileWriter(path));
		//构建有向图矩阵ATPM
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//ATPM//ATPM"+STATIONNAME+".csv"),"GBK");
		BufferedWriter writer = new BufferedWriter(writerStream);
		CsvWriter cwriter = new CsvWriter(writer, ',');
		
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
				if(curLineArray[i].equals("0.0")){
					continue;
				}
				pathList.add(start + ",,," + stationList.get(i-1) + ",,," + curLineArray[i]);
			}
		}
/********************************************************结束****************************************************************/ 

/*********************************对pathList进行深度优先遍历，筛选出符合条件的路径***********************************************/
		GetCriticalPath DCP = new GetCriticalPath();
		CalCityId city = new CalCityId();
		//存储节点
		List<String> nodes = new ArrayList<String>();
		//对pathList进行深度优先遍历
		List<List<String>> lists = DCP.searchPath(pathList);

		String initMatric = "";

		int count = 0;
		for (List<String> sl : lists) {
			String strs = "";
			//提取城市编号
			String cityId = city.getCityId(sl.get(0));
			if(!cityId.equals(STATIONID)){//设定传播的起点
				continue;
			}else{
				initMatric += '0';
				count++;
				String newPaths = "[\r\n";
				for(String p : sl){
					newPaths += '\"' + p + "\","+"\r\n";

					//向nodes中存储 获取所有子图的节点
					if(!nodes.contains(p)) {
						nodes.add(p);
					}
				}
				newPaths += "],\r\n";
				pathOut.write(newPaths); // \r\n即为换行
			}
		}
		pathOut.flush(); // 把缓存区内容压入文件
		pathOut.close(); // 最后记得关闭文件  
/********************************************************结束****************************************************************/

/*****************************************************构建APTM矩阵************************************************************/
	System.out.println("节点个数："+nodes.size());
	System.out.println("污染物传播子图个数："+count);
	writeToExcelHead(cwriter, nodes); //写入文件头
	Map<String, String> APTM = new HashMap<String, String>(){};
	for (String node1 : nodes){
		List<String> vals = new ArrayList<String>();
		for(String node2 : nodes){
			String key = node1 + ", " + node2;
			for (List<String> sl : lists) {
				String strs = "";
				//提取城市编号
				String cityId = city.getCityId(sl.get(0));
				if(!cityId.equals(STATIONID)){//设定传播的起点
					continue;
				}else{
					if(sl.toString().contains(key)){
						if(APTM.get(key) == null){
							APTM.put(key, "1");
						}else{
							APTM.put(key, APTM.get(key) + "1");
						}
					}else{
						if(APTM.get(key) == null){
							APTM.put(key, "0");
						}else{
							APTM.put(key, APTM.get(key) + "0");
						}
					}
				}
			}
			vals.add(APTM.get(key));
		}
//		System.out.println(vals);
		writeToExcelContent(cwriter, node1, vals, initMatric, count);
	}
/********************************************************结束****************************************************************/

	}

	private static void writeToExcelHead(CsvWriter cwriter, List<String> stationList) throws IOException {
		cwriter.write("");
		for(String head : stationList){
			cwriter.write(head);
		}
		cwriter.endRecord();
		cwriter.flush();
	}
	private static void writeToExcelContent(CsvWriter cwriter, String start, List<String> vals, String initMatric, int count) throws IOException {
		cwriter.write(start);
		for(String val : vals){
			int sum = 0;
			for(char c : val.toCharArray()){
				if (c == '1') sum++;
			}
			if( sum >= count/3.0){
				int newVal = Integer.parseInt(val,2);
				cwriter.write(newVal+"");
//				cwriter.write("'"+val);
			} else{ //小于频繁阈值时 将该路径片段置为0
				cwriter.write("0");
//				cwriter.write("'"+initMatric);
			}
		}
		cwriter.endRecord();
		cwriter.flush();
	}
}
