package util;
import java.util.List;
import java.util.Map;

public class ToMap {
	//将读取出的数据转换为map格式
	public String toList(List<String> values){
		String value = "";
		for (int i = 0; i < values.size(); i++) {	
			if(i != values.size()-1){
				value += values.get(i) + ",";
			}else{
				value += values.get(i);
			}
		}
		return value;
	}
	//根据序号获取值
	public String getValue(Map<String, String> map, String key, Integer index){	
		String value = map.get(key);
		String[] values = value.split(",");
		return values[index];
	}
}
