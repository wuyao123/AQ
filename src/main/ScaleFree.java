package main;

import com.csvreader.CsvWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScaleFree {
	
	public static void main(String[] args) throws IOException {
		//读取一个周期内的邻接矩阵
		InputStreamReader marticIsr = new InputStreamReader(new FileInputStream("//Users/wuyao//graduation_project//data//pathMarticCSV//pathMartic.csv"), "GBK");
		BufferedReader marticCsv = new BufferedReader(marticIsr);

		InputStreamReader allStationCSV = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//oldData//stations.csv"), "GBK");
		BufferedReader allstation = new BufferedReader(allStationCSV);
		String stationLine = allstation.readLine();
		String[] station = stationLine.split(",");

		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//stationInNum.csv"),"GBK");
		BufferedWriter writer = new BufferedWriter(writerStream);
		CsvWriter cwriter = new CsvWriter(writer, ',');

/*********************读取pathMartic文件，统计每个站点的出度、入度********************************************************************/
		List<Integer> OutNum = new ArrayList<Integer>();
		List<Integer> InNum = new ArrayList<Integer>();
		List<Double> InVal = new ArrayList<Double>();
		String martic = marticCsv.readLine();
		String[] stations = martic.split(",");

		String[] num = martic.split(",");
		for (int i = 0; i < num.length-1; i++) {
			InNum.add(0);
			InVal.add(0.0);
		}
		while((martic = marticCsv.readLine()) != null){
			String[] curLineArray = martic.split(",");
			String start = curLineArray[0];
			int count = 0;
			for (int i = 1; i < curLineArray.length; i++) {
				if(!curLineArray[i].equals("1.0")){
					count++;
					InNum.set(i-1, InNum.get(i-1)+1);
					InVal.set(i-1, InVal.get(i-1)+1/Double.parseDouble(curLineArray[i]));
				}
			}
			OutNum.add(count);
		}
		List<Integer> free = new ArrayList<Integer>();
		for (int i = 0; i < InNum.size(); i++) {
			free.add(OutNum.get(i)+InNum.get(i));
//			System.out.println("["+stations[i+1]+","+free.get(i)+"],");
		}
		Collections.sort(InVal);
		Collections.reverse(InVal);
		Collections.sort(free);
//		Collections.reverse(free);
		for (int i = 0; i < station.length; i++) {
			System.out.println("["+station[i]+","+InVal.get(i)+"],");
			writeToExcelContent(cwriter, station[i], InVal.get(i)+"");
		}
/********************************************************结束****************************************************************/ 
		
/********************************************************统计各个度出现的次数**********************************************/ 
		Map<Integer, Double> numMap = new LinkedHashMap<Integer, Double>();
		for (int i = 0; i < free.size(); i++) {
			if(numMap.containsKey(free.get(i))){
				numMap.put(free.get(i), numMap.get(free.get(i))+1.0);
			}else{
				numMap.put(free.get(i), 1.0);
			}
		}
		for(Integer key : numMap.keySet()){
			if(key == 0){
				continue;
			}
//			System.out.println("["+key+","+numMap.get(key)/154+"],");
//			System.out.println(numMap.get(key)/154);   // 计算度概率分布
		}
		double sum = 0.0;
		for(Integer key : numMap.keySet()){
			if(key == 0){
				continue;
			}
			sum += numMap.get(key)/154;
//			System.out.println("["+key+","+sum+"],");  // 计算度累计概率
//			System.out.print(sum+" ");
		}
/********************************************************结束****************************************************************/ 
	}

	private static void writeToExcelContent(CsvWriter cwriter, String start, String num) throws IOException {
		cwriter.write(start);
		cwriter.write(num+"");
		cwriter.endRecord();
		cwriter.flush();
	}
}
