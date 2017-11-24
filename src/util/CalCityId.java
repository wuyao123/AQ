package util;

public class CalCityId {
	public String getCityId(String str) {
		String cityId = "";
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if(str.charAt(i) < '0' || str.charAt(i) > '9'){
				count = i;
				break;
			}
		}
		if(count == 4){
			cityId = str.substring(0, 1);
		}else{
			cityId = str.substring(0, 2);
		}
		return cityId;
	}
}
