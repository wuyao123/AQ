package main;

import com.csvreader.CsvWriter;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

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
		InputStreamReader marticIsr = new InputStreamReader(new FileInputStream("//Users/wuyao//graduation_project//newData//pathMarticCSV//pathNumMartic.csv"), "GBK");
		BufferedReader marticCsv = new BufferedReader(marticIsr);

		InputStreamReader allStationCSV = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//oldData//stations.csv"), "GBK");
		BufferedReader allStation = new BufferedReader(allStationCSV);
		String stationLine = allStation.readLine();
		String[] station = stationLine.split(",");

		/**输出站点入度*/
		OutputStreamWriter writerStream4 = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//stationInNum.csv"),"GBK");
		BufferedWriter writer4 = new BufferedWriter(writerStream4);
		CsvWriter cwriter4 = new CsvWriter(writer4, ',');
		/**输出站点入度强度*/
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//stationInVal.csv"),"GBK");
		BufferedWriter writer = new BufferedWriter(writerStream);
		CsvWriter cwriter = new CsvWriter(writer, ',');
		/**输出站点度分布情况*/
		OutputStreamWriter writerStream1 = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//监测站度值分布.csv"),"GBK");
		BufferedWriter writer1 = new BufferedWriter(writerStream1);
		CsvWriter cwriter1 = new CsvWriter(writer1, ',');
		/**输出站点度分布概率*/
		OutputStreamWriter writerStream2 = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//监测站度值分布概率.csv"),"GBK");
		BufferedWriter writer2 = new BufferedWriter(writerStream2);
		CsvWriter cwriter2 = new CsvWriter(writer2, ',');
		/**输出站点度分布累计概率*/
		OutputStreamWriter writerStream3 = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//监测站度值分布累计概率.csv"),"GBK");
		BufferedWriter writer3 = new BufferedWriter(writerStream3);
		CsvWriter cwriter3 = new CsvWriter(writer3, ',');

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
				if(!curLineArray[i].equals("0.0")){
					count++;
					InNum.set(i-1, InNum.get(i-1)+1);
					InVal.set(i-1, InVal.get(i-1)+Double.parseDouble(curLineArray[i]));
				}
			}
			OutNum.add(count);
		}

		Collections.sort(InVal);
		Collections.reverse(InVal);
//		Collections.sort(InNum);
//		Collections.reverse(InNum);
		List<Integer> free = new ArrayList<Integer>();
		String sss = "[";
		for (int i = 0; i < InNum.size(); i++) {
			free.add(OutNum.get(i)+InNum.get(i));
//			System.out.println("["+stations[i+1]+","+free.get(i)+"],");
			sss += free.get(i) + " ";
			writeToExcelContent1(cwriter1, stations[i+1], free.get(i)+"");
		}
		sss += "]";
//		System.out.println(sss);

		Collections.sort(free);
//		Collections.reverse(free);
		for (int i = 0; i < station.length; i++) {
//			System.out.println("["+station[i]+","+InVal.get(i)+"],");
			writeToExcelContent(cwriter, station[i], InVal.get(i)+"");
			writeToExcelContent4(cwriter4, station[i], InNum.get(i)+"");
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
		String ss = "[";
		for(Integer key : numMap.keySet()){
			if(key == 0){
				continue;
			}
//			System.out.println("["+key+","+numMap.get(key)/154+"],");
//			System.out.println(numMap.get(key)/154);   // 计算度概率分布
			ss += numMap.get(key)/154 + " ";
			writeToExcelContent2(cwriter2, key+"", numMap.get(key)/154+"");
		}
		ss += "]";
//		System.out.println(ss);

		double sum = 0.0;
		String s = "[";
		for(Integer key : numMap.keySet()){
			if(key == 0){
				continue;
			}
			sum += numMap.get(key)/154;
			s += key + " ";
//			System.out.println("["+key+","+sum+"],");  // 计算度累计概率
//			System.out.print(sum+" ");
			writeToExcelContent3(cwriter3, key+"", sum+"");
		}
		s += "]";
//		System.out.println(s);
/********************************************************结束****************************************************************/ 
	}

	private static void writeToExcelContent(CsvWriter cwriter, String start, String num) throws IOException {
		cwriter.write(start);
		cwriter.write(num+"");
		cwriter.endRecord();
		cwriter.flush();
	}

	private static void writeToExcelContent1(CsvWriter cwriter, String start, String val) throws IOException {
		cwriter.write(start);
		cwriter.write(val+"");
		cwriter.endRecord();
		cwriter.flush();
	}

	private static void writeToExcelContent2(CsvWriter cwriter, String start, String val) throws IOException {
		cwriter.write(start);
		cwriter.write(val+"");
		cwriter.endRecord();
		cwriter.flush();
	}

	private static void writeToExcelContent3(CsvWriter cwriter, String start, String val) throws IOException {
		cwriter.write(start);
		cwriter.write(val+"");
		cwriter.endRecord();
		cwriter.flush();
	}

	private static void writeToExcelContent4(CsvWriter cwriter, String start, String val) throws IOException {
		cwriter.write(start);
		cwriter.write(val+"");
		cwriter.endRecord();
		cwriter.flush();
	}
}
