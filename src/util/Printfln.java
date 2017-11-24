package util;

import java.util.List;

public class Printfln {
	public void print(List<String> list){
		for (int i = 0; i < list.size(); i++) {	
			System.out.println(list.get(i));
		}
	}
}
