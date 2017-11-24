package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CalPathNum {
	public static void main(String[] args) throws IOException {
		InputStreamReader pathIsr = new InputStreamReader(new FileInputStream("F://data//linesCSV//allLinesTJ.csv"), "GBK");
		BufferedReader pathCsv = new BufferedReader(pathIsr);
		
		File pathFile = new File("F://data//pathTXT//pathNumTJ.txt");
		pathFile.createNewFile();//创建新文件
		BufferedWriter out = new BufferedWriter(new FileWriter(pathFile)); 
	    
		Map<String,Integer> map = new HashMap<String,Integer>();
		String path = pathCsv.readLine();
		int num = 0;
		while((path = pathCsv.readLine()) != null){
			String[] curLineArray = path.split(",\"");
			String[] paths = curLineArray[1].split(",");
			++num;
			for (int i = 0; i < paths.length-1; i++) {
				String key = paths[i] + "," + paths[i+1];
				if(map.containsKey(key)){
					map.put(key, map.get(key) + 1);
				}else{
					map.put(key, 0);
				}
			}
		}
		
		ArrayList<Entry<String, Integer>> arrayList = new ArrayList<Entry<String, Integer>>(map.entrySet());  
		//排序  
		Collections.sort(arrayList, new Comparator<Entry<String, Integer>>() {
		    public int compare(Entry<String, Integer> map1, Entry<String, Integer> map2) {
		        return (map2.getValue() - map1.getValue());  
		    }  
		});  
		//输出  
		for (Entry<String, Integer> entry : arrayList) { 
			Double rate = Double.parseDouble(entry.getValue()+"")/num;
			System.out.println(entry.getKey() + ":" + Double.parseDouble(entry.getValue()+"")/num);
			if(rate < 0.01){
				break;
			}
			String[] keys = entry.getKey().split(",");
			String node = "[{name:\'" + keys[0] + "\'},{name:\'" + keys[1] +"\'}],\r\n";
			out.write(node); // \r\n即为换行 
		}  
		out.flush(); // 把缓存区内容压入文件  
        out.close(); // 最后记得关闭文件  
	}
}
