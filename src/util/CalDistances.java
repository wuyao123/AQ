package util;

public class CalDistances {
	
	public static double Distance(String lo1, String la1, String lo2, String la2) {  
		Double long1 = Double.parseDouble(lo1);
		Double lat1 = Double.parseDouble(la1);
		Double long2 = Double.parseDouble(lo2);
		Double lat2 = Double.parseDouble(la2);
	    double a, b, R;  
	    R = 6378137; // 地球半径  
	    lat1 = lat1 * Math.PI / 180.0;  
	    lat2 = lat2 * Math.PI / 180.0;  
	    a = lat1 - lat2;  
	    b = (long1 - long2) * Math.PI / 180.0;  
	    double d;  
	    double sa2, sb2;  
	    sa2 = Math.sin(a / 2.0);  
	    sb2 = Math.sin(b / 2.0);  
	    d = 2 * R  * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));  
	    return d/1000.0;  
	}
	
	public static double Angle(String lo1, String la1, String lo2, String la2, String windVector){
		Double dis = Distance(lo1,la1,lo2,la2);
		Double dis1 = Distance(lo2,la1,lo2,la2);
		Double stationVector = Math.asin(dis1/dis)*180/Math.PI;
		return Math.abs(stationVector - Double.parseDouble(windVector));
	}
	public static void main(String[] args) {
		System.out.println(new CalDistances().Distance("116.173553", "40.090679", "116.20531", "40.00395"));
	}
}
