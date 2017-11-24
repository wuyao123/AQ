package util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CalCost {

	public Double cal(String startId, String endId, String hum, String tem, Map<String,List<String>> map) throws IOException{
		
		Normalization nor = new Normalization();
		Double humidity = nor.cal(hum, "humidity");
		Double temperature = nor.cal(tem, "temperature");
		Double corr = 0.0;
		Double distance = 0.0;
		
		if(map.containsKey(startId+","+endId)){
			corr = Double.parseDouble(map.get(startId+","+endId).get(0));
			distance = nor.cal(map.get(startId+","+endId).get(1), "distance");
		}else if(map.containsKey(endId+","+startId)){
			corr = Double.parseDouble(map.get(endId+","+startId).get(0));
			distance = nor.cal(map.get(endId+","+startId).get(1), "distance");
		}else{
			corr = 1.0;
			distance = 0.0;
		}
		return distance*humidity*temperature/corr;
	}

}
