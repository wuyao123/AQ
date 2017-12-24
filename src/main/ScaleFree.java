package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScaleFree {
	
	public static void main(String[] args) throws IOException {
		//读取一个周期内的邻接矩阵
		InputStreamReader marticIsr = new InputStreamReader(new FileInputStream("//Users/wuyao//graduation_project//data//pathMartic.csv"), "GBK");
		BufferedReader marticCsv = new BufferedReader(marticIsr);
		
/*********************读取pathMartic文件，统计每个站点的出度、入度********************************************************************/
		List<Integer> OutNum = new ArrayList<Integer>();
		List<Integer> InNum = new ArrayList<Integer>();
		String martic = marticCsv.readLine();
		String[] num = martic.split(",");
		for (int i = 0; i < num.length-1; i++) {
			InNum.add(0);
		}
		while((martic = marticCsv.readLine()) != null){
			String[] curLineArray = martic.split(",");
			String start = curLineArray[0];
			int count = 0;
			for (int i = 1; i < curLineArray.length; i++) {
				if(!curLineArray[i].equals("1.0")){
					count++;
					InNum.set(i-1, InNum.get(i-1)+1);
				}
			}
			OutNum.add(count);
		}
		List<Integer> free = new ArrayList<Integer>();
		for (int i = 0; i < InNum.size(); i++) {
			free.add(OutNum.get(i)+InNum.get(i));
//			System.out.println("["+(i+1)+","+free.get(i)+"],");
		}
		Collections.sort(free);
//		Collections.reverse(free);
		for (int i = 0; i < InNum.size(); i++) {
			System.out.println("["+(i+1)+","+free.get(i)+"],");
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
//			System.out.print(numMap.get(key)/154+",");
		}
		double sum = 0.0;
		for(Integer key : numMap.keySet()){
			if(key == 0){
				continue;
			}
			sum += numMap.get(key)/154;
//			System.out.println("["+key+","+sum+"],");
			System.out.print(sum+" ");
		}
/********************************************************结束****************************************************************/ 
	}
}
