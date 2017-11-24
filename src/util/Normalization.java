package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Normalization {
	
	//计算各个因素的平均值，最大值，最小值
	public List<String> calByExcel() throws IOException{
		InputStreamReader hebeiIsr = new InputStreamReader(new FileInputStream("F://data//hebeiAllInf.csv"), "GBK");
		BufferedReader hebeiCsv = new BufferedReader(hebeiIsr);
		String hebeiLine = hebeiCsv.readLine();
		List<String> data = new ArrayList<String>();
		double count = 0.0;
		double max = 0.0;
		double min = 10000.0;
		int num = 0;
		while((hebeiLine = hebeiCsv.readLine()) != null){
			String[] curLineArray = hebeiLine.split(",");
			Double value = Double.parseDouble(curLineArray[6]);
			count += value;
			++num;
			if(value > max){
				max = value;
			}
			if(value < min){
				min = value;
			}
		}
		data.add("mean is " +count/num);
		data.add("max is " +max);
		data.add("min is " +min);
		return data;
	}
	//计算两点之间距离的平均值，最大值，最小值
	public List<String> calDisNorm() throws IOException{
		InputStreamReader distanceIsr = new InputStreamReader(new FileInputStream("F://data//switchCorr.csv"), "GBK");
		BufferedReader distanceCsv = new BufferedReader(distanceIsr);
		String distanceLine = distanceCsv.readLine();
		List<String> data = new ArrayList<String>();
		double count = 0.0;
		double max = 0.0;
		double min = 10000.0;
		int num = 0;
		while((distanceLine = distanceCsv.readLine()) != null){
			String[] curLineArray = distanceLine.split(",");
			Double value = Double.parseDouble(curLineArray[3]);
			count += value;
			++num;
			if(value > max){
				max = value;
			}
			if(value < min){
				min = value;
			}
		}
		data.add("mean is " +count/num);
		data.add("max is " +max);
		data.add("min is " +min);
		return data;
	}
	
	public Double cal(String val, String name){
		Double value = Double.parseDouble(val);
		Double returnVal = 0.0;
		switch (name) {
		case "PM25":
			returnVal = calNorm(93.9944,1463.0,1.0,value);
			break;
		case "windSpeed":
			returnVal = calNorm(7.4026,20.44,0.0,value);
			break;
		case "temperature":
			returnVal = calNorm(10.3632,41.0,-27.0,value);
			break;
		case "pressure":
			returnVal = calNorm(1000.3455,1042.0,761.0,value);
			break;
		case "humidity":
			returnVal = calNorm(51.4145,100.0,0.0,value);
			break;
		case "distance":
			returnVal = calNorm(198.4306,557.474,0.0124,value);
			break;
		default:
			break;
		}
		return returnVal;
	}
	
	private Double calNorm(double mean, double max, double min, Double value) {
		return (value - min)/(max - min);
	}

	public static void main(String[] args) throws IOException {
		List<String> data = new Normalization().calDisNorm();
		for(String d : data){
			System.out.println(d);
		}
	}

}
