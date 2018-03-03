package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;

import util.CalCityId;
import util.CalCost;
import util.CalDistances;
import util.CalPath;
import util.DateUtil;
import util.SwitchWindVector;

public class DateCollection {
	
	public void dateList() throws IOException{
		
		//程序开始时间
		Date stratDate = new Date();

		String STATIONNAME = "Winter";
		String CITYID = "";

		//读取所有监测站信息
		InputStreamReader stationIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//station1.csv"), "GBK");
		BufferedReader stationCsv = new BufferedReader(stationIsr);
	    //读取hebeiAllInf文件，并以时间为key,存储在dateMap中，为之后计算做准备
		InputStreamReader hebeiIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//hebeiAllInf.csv"), "GBK");
		BufferedReader hebeiCsv = new BufferedReader(hebeiIsr);
		//将switchCorr中的数据存储到corrAndDisMap中，方便后边进行查找
		InputStreamReader corrAndDisIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//switchCorr.csv"), "GBK");
		BufferedReader corrAndDisCsv = new BufferedReader(corrAndDisIsr);
		//将路径数据写入到allLines***文件中
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//linesCSV//allLines"+STATIONNAME+".csv"),"GBK");
	    BufferedWriter writer = new BufferedWriter(writerStream);
	    CsvWriter cwriter = new CsvWriter(writer, ',');
	    writeToExcel(cwriter,"TIME","LINE");
	    //将整个周期的路径映射到邻接矩阵中
	    OutputStreamWriter writerStream1 = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//pathMarticCSV//pathMartic"+STATIONNAME+".csv"),"GBK");
	    BufferedWriter writer1 = new BufferedWriter(writerStream1);
	    CsvWriter cwriter1 = new CsvWriter(writer1, ',');
	    //将整个周期的路径出现次数映射到邻接矩阵中
	    OutputStreamWriter writerStream2 = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//pathMarticCSV//pathNumMartic"+STATIONNAME+".csv"),"GBK");
	    BufferedWriter writer2 = new BufferedWriter(writerStream2);
	    CsvWriter cwriter2 = new CsvWriter(writer2, ',');
/***************************************初始化存储边权值的矩阵、记录次数的矩阵***************************************************/	    
		//将所有站点存储到stationList
		List<String> staList = new ArrayList<String>();
		String station = stationCsv.readLine();
		while((station = stationCsv.readLine()) != null){
			String[] curLineArray = station.split(",");
			staList.add(curLineArray[0]+curLineArray[1]);
		}
	    writeToExcelHead(cwriter1,staList);
	    writeToExcelHead2(cwriter2,staList);
		/*
		 * 初始化矩阵matrixNum：用于存储边出现的次数
		 * 初始化矩阵matrixVal：用于存储边的权值和
		 * 初始化矩阵matrix：用于存储边的平均权值
		 * 矩阵中的值初始化均为0
		 */
		Map<String, Map<String, Double>> matrixNum = new LinkedHashMap<String, Map<String, Double>>();
		Map<String, Map<String, Double>> matrixVal = new LinkedHashMap<String, Map<String, Double>>();
		Map<String, Map<String, Double>> matrix = new LinkedHashMap<String, Map<String, Double>>();
		for (int i = 0; i < staList.size(); i++) {
			Map<String, Double> map = new LinkedHashMap<String, Double>();
			Map<String, Double> mapNum = new LinkedHashMap<String, Double>();
			Map<String, Double> mapVal = new LinkedHashMap<String, Double>();
			for (int j = 0; j < staList.size(); j++) {
				map.put(staList.get(j), 0.0);
				mapNum.put(staList.get(j), 0.0);
				mapVal.put(staList.get(j), 0.0);
			}
			matrixNum.put(staList.get(i), mapNum);
			matrixVal.put(staList.get(i), mapVal);
			matrix.put(staList.get(i), map);
		}
/****************************************************结束***********************************************************/
		
/****************************将京津冀区域内的所有监测站的信息按时刻（小时）进行统计***************************************/
		/*
		 * key：curLineArray[0]，时刻（某一天的某一小时）
		 * value：监测站上所有的信息（气象，污染物，地理位置）
		 */
		String hebeiLine = hebeiCsv.readLine();
		Map<String, List<String>> dateMap = new HashMap<String, List<String>>();
		while((hebeiLine = hebeiCsv.readLine()) != null){
			String[] curLineArray = hebeiLine.split(",");
			String key = curLineArray[0];
			String value = curLineArray[1] + "," + curLineArray[2] + "," +
					   curLineArray[3] + "," + curLineArray[4] + "," +
					   curLineArray[5] + "," + curLineArray[6] + "," +
					   curLineArray[7] + "," + curLineArray[8] + "," +
					   curLineArray[9] + "," + curLineArray[10] + "," +
					   curLineArray[11];
			if(dateMap.containsKey(key)){
				dateMap.get(key).add(value);
			}else{
				List<String> dateList = new ArrayList<String>();
				dateList.add(value);
				dateMap.put(key, dateList);
			}
			
		}
		/*
		 * 存储每个监测站之间的相关性以及距离
		 * curLineArray[2]：相关性
		 * curLineArray[3]：距离
		 */
		String corrAndDis = corrAndDisCsv.readLine();
		Map<String,List<String>> corrAndDisMap = new HashMap<String,List<String>>();
		while((corrAndDis = corrAndDisCsv.readLine()) != null){
			String[] curLineArray = corrAndDis.split(",");
			List<String> list = new ArrayList<String>();
			String key = curLineArray[0] + "," + curLineArray[1];
			list.add(curLineArray[2]);
			list.add(curLineArray[3]);
			corrAndDisMap.put(key, list);
		}
/**************************************************************结束**********************************************************/
		
/***********************************************对一个周期内的每个时刻（小时）进行遍历*******************************************************/		
		CalDistances calDis = new CalDistances();
		CalCost calCost = new CalCost();
		SwitchWindVector switchWindVector = new SwitchWindVector();
		for(String key : dateMap.keySet()){
			List<String> dateList = dateMap.get(key);
			List<String> stationList = new ArrayList<String>();
			for (int i = 0; i < dateList.size(); i++) {
				String[] valis = dateList.get(i).split(",");
				for (int j = 0; j < dateList.size(); j++) {
					String[] valjs = dateList.get(j).split(",");
					//计算两点之间的距离
					Double D = calDis.Distance(valis[4], valis[3], valjs[4], valjs[3]);
					//将风向图中的标识符转换为角度
					String angle = switchWindVector.SWV(valis[6]);
					// Double.parseDouble(valjs[3]) > Double.parseDouble(valis[3]) && Double.parseDouble(valjs[4]) < Double.parseDouble(valis[4])
					if(D > 10 && D <= 40 && !angle.equals("NULL") && Double.parseDouble(valjs[3]) < Double.parseDouble(valis[3])){
						//计算两个监测站的角度
						Double stationWithVector = switchWindVector.Angle(valis[4], valis[3], valjs[4], valjs[3], angle);
						if(stationWithVector > 0 && stationWithVector < 80){
							//计算两点之间的传播代价
							Double cost = calCost.cal(valis[2], valjs[2], valis[10], valis[8], corrAndDisMap);
							if(cost < 0.01 ){
								String sTos = valis[2] + "," + valis[0] + "," + valis[1] + "," + valjs[2] + "," + 
										  valjs[0] + "," + valjs[1] + "," + stationWithVector;
								stationList.add(sTos);
								matrixVal.get(valis[2]+valis[1]).put(valjs[2]+valjs[1], matrixVal.get(valis[2]+valis[1]).get(valjs[2]+valjs[1]) + cost);
								matrixNum.get(valis[2]+valis[1]).put(valjs[2]+valjs[1], matrixNum.get(valis[2]+valis[1]).get(valjs[2]+valjs[1]) + 1);
							}
						}
					}
				}
			}
			
			CalPath calPath = new CalPath();
			CalCityId city = new CalCityId();
			//对stationList进行深度优先遍历
			List<List<String>> lists = calPath.searchPath(stationList);
			for (List<String> sl : lists) {
				String strs = "";
				//提取城市编号
				String cityId = city.getCityId(sl.get(0));
				if(sl.size() <= 4){//设定传播的起点
					continue;
				}else{
					for (String str : sl) {
						strs += str + ",";
					}
				}
				//写入文件
				writeToExcel(cwriter, key, strs);
			}
		}	
/***********************************************结束*******************************************************/	
		
/*************************计算一个周期内每条边的权值，并输出到文件*********************************************/		
		/*
		 * key1：起点
		 * key2:终点
		 * avgVal：一个周期内边的权值
		 */
	    for(String key1 : matrixNum.keySet()){
	    	for(String key2 : matrixNum.get(key1).keySet()){
	    		if(matrixNum.get(key1).get(key2) == 0.0){
	    			matrix.get(key1).put(key2, 1.0);
	    			continue;
	    		}
	    		Double avgVal = matrixVal.get(key1).get(key2)/matrixNum.get(key1).get(key2);
	    		matrix.get(key1).put(key2, avgVal);
	    	}
	    }
	    
		//将matrix输出到excle
		for(String start : matrix.keySet()){
			writeToExcelContent(cwriter1,start,matrix.get(start));
		}
		//将matrixNum输出到excle
		for(String start : matrixNum.keySet()){
			writeToExcelContent2(cwriter2,start,matrixNum.get(start));
		}
/***********************************************结束*******************************************************/			
		
		//程序结束时间
		Date endDate = new Date();
		//计算运行时间
		DateUtil getDate = new DateUtil();
		System.out.println(getDate.getDate(stratDate));
		System.out.println(getDate.getDate(endDate));
	}
	
	private static void writeToExcelHead(CsvWriter cwriter, List<String> stationList) throws IOException {
		cwriter.write("");
		for(String head : stationList){
			cwriter.write(head);
		}
		cwriter.endRecord();
		cwriter.flush();
	}
	private static void writeToExcelHead2(CsvWriter cwriter, List<String> stationList) throws IOException {
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
	private static void writeToExcelContent2(CsvWriter cwriter, String start, Map<String, Double> map) throws IOException {
		cwriter.write(start);
		for(String content : map.keySet()){
			cwriter.write(map.get(content)+"");
		}
		cwriter.endRecord();
		cwriter.flush();
	}
	private void writeToExcel(CsvWriter cwriter, String string, String string2) throws IOException {
		cwriter.write(string);
		cwriter.write(string2);
		cwriter.endRecord();
		cwriter.flush();
	}
	
	public static void main(String[] args) throws IOException {
		new DateCollection().dateList();
	}

}
