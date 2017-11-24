package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;

public class PathToMatrix {

	public static void main(String[] args) throws IOException {
		InputStreamReader stationIsr = new InputStreamReader(new FileInputStream("F://data//station1.csv"), "GBK");
		BufferedReader stationCsv = new BufferedReader(stationIsr);
		InputStreamReader pathIsr = new InputStreamReader(new FileInputStream("F://data//allLines.csv"), "GBK");
		BufferedReader pathCsv = new BufferedReader(pathIsr);
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("F://data//pathMartic.csv"),"GBK");
	    BufferedWriter writer = new BufferedWriter(writerStream);
	    CsvWriter cwriter = new CsvWriter(writer, ',');
	    
		//将所有站点存储到stationList
		List<String> stationList = new ArrayList<String>();
		String station = stationCsv.readLine();
		while((station = stationCsv.readLine()) != null){
			String[] curLineArray = station.split(",");
			stationList.add(curLineArray[0]+curLineArray[1]);
		}
	    writeToExcelHead(cwriter,stationList);
		//初始化矩阵matrix
		Map<String, Map<String, Integer>> matrix = new LinkedHashMap<String, Map<String, Integer>>();
		for (int i = 0; i < stationList.size(); i++) {
			Map<String, Integer> map = new LinkedHashMap<String, Integer>();
			for (int j = 0; j < stationList.size(); j++) {
				map.put(stationList.get(j), 0);
			}
			matrix.put(stationList.get(i), map);
		}
		
		//将路径映射到matrix
		String path = pathCsv.readLine();
		while((path = pathCsv.readLine()) != null){
			String[] curLineArray = path.split(",\"");
			String[] paths = curLineArray[1].split(",");
			//查看矩阵中是否该路径		
			for (int i = 0; i < paths.length-1; i++) {
				//查看矩阵中是否该路径
				if(matrix.get(paths[i]).get(paths[i+1]) == 1){
					continue;
				}else{
					matrix.get(paths[i]).put(paths[i+1], 1);
				}
			}
		}
		
		//将matrix输出到excle
		for(String start : matrix.keySet()){
			writeToExcelContent(cwriter,start,matrix.get(start));
		}
		
	}

	private static void writeToExcelHead(CsvWriter cwriter, List<String> stationList) throws IOException {
		cwriter.write("");
		for(String head : stationList){
			cwriter.write(head);
		}
		cwriter.endRecord();
		cwriter.flush();
	}
	
	private static void writeToExcelContent(CsvWriter cwriter, String start, Map<String, Integer> map) throws IOException {
		cwriter.write(start);
		for(String content : map.keySet()){
			cwriter.write(map.get(content)+"");
		}
		cwriter.endRecord();
		cwriter.flush();
	}
}
