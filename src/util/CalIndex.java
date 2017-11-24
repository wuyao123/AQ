package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CalIndex {

	public static void main(String[] args) throws IOException {
		InputStreamReader lineCsv = new InputStreamReader(new FileInputStream("F://data//allLines.csv"), "GBK");
		BufferedReader br = new BufferedReader(lineCsv);
		String stationLine = br.readLine();
		int count = 0;
		while((stationLine = br.readLine()) != null){
			count++;
		}
		System.out.println(count);
	}
}
