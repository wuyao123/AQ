/**
 * 计算京津冀各城市污染物超标率
 **/
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;

public class ExcStandardRate {

	public static void main(String[] args) throws IOException {
		InputStreamReader hebeiIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//data//hebei.csv"), "GBK");
		BufferedReader hebeiCsv = new BufferedReader(hebeiIsr);
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//ExcStandardRate.csv"),"GBK");
	    BufferedWriter writer = new BufferedWriter(writerStream);
	    CsvWriter cwriter = new CsvWriter(writer, ',');
	    writeToExcel(cwriter,"CITY","AVERAGECON","EXCSTANDARDRATE");
	    
		String hebeiLine = hebeiCsv.readLine();
		Map<String, List<Integer>> dateMap = new HashMap<String, List<Integer>>();
		while((hebeiLine = hebeiCsv.readLine()) != null){
			String[] curLineArray = hebeiLine.split(",");
			String key = curLineArray[1];
			if(curLineArray[6].equals("NULL")){
				continue;
			}
			int value = Integer.parseInt(curLineArray[6]);
			if(dateMap.containsKey(key)){
				/*List<Integer>里第一数代表一个城市污染物浓度总和
				 *List<Integer>里第二数代表一个城市有污染数据的天数
				 *List<Integer>里第二数代表一个城市有污染物浓度超标的天数
				 */
				dateMap.get(key).set(0, dateMap.get(key).get(0) + value);
				dateMap.get(key).set(1, dateMap.get(key).get(1) + 1);
				if(value >= 50){
					dateMap.get(key).set(2, dateMap.get(key).get(2) + 1);
				}
			}else{
				List<Integer> PM25ValAndCount = new ArrayList<Integer>();
				PM25ValAndCount.add(0);
				PM25ValAndCount.add(0);
				PM25ValAndCount.add(0);
				dateMap.put(key, PM25ValAndCount);
			}
		}
		for(String key : dateMap.keySet()){
			Double value = Double.parseDouble(dateMap.get(key).get(0) +"");
			int num = dateMap.get(key).get(1);
			Double overNum = Double.parseDouble(dateMap.get(key).get(2) + "");
			writeToExcel(cwriter,key,value/num+"",overNum/num+"");
		}
	}

	private static void writeToExcel(CsvWriter cwriter, String string, String string2, String string3) throws IOException {
		cwriter.write(string);
		cwriter.write(string2);
		cwriter.write(string3);
		cwriter.endRecord();
		cwriter.flush();
	}

}
