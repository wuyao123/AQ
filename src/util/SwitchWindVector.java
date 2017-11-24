package util;

public class SwitchWindVector {
	public String SWV(String vector){
		String wind_dir = "";
		switch (vector) {
		case "1":
			wind_dir = "0";
			break;
		case "2":
			wind_dir = "180";
			break;
		case "3":
			wind_dir = "90";
			break;
		case "4":
			wind_dir = "270";
			break;
		case "13":
			wind_dir = "135";
			break;
		case "14":
			wind_dir = "225";
			break;
		case "23":
			wind_dir = "45";
			break;
		case "24":
			wind_dir = "315";
			break;
		default://将vector为0 9 NULL的统一记作NULL
			wind_dir = "NULL";
			break;
		}
		return wind_dir;
	}
	
	public Double Angle(String lo1, String la1, String lo2, String la2,String ang){	
		Double long1 = Double.parseDouble(lo1);
		Double lat1 = Double.parseDouble(la1);
		Double long2 = Double.parseDouble(lo2);
		Double lat2 = Double.parseDouble(la2);
		//转换为弧度
		Double angle = Double.parseDouble(ang)*Math.PI/180.0;
		Double dis = Math.sqrt(Math.pow(long1-long2, 2) + Math.pow(lat1-lat2, 2));
		return Math.acos(((long2-long1)*Math.cos(angle)+(lat2-lat1)*Math.sin(angle))/dis)*180/Math.PI;
	}
}
