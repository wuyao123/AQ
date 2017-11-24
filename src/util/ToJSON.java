package util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class ToJSON {

	public void toJson(){
		JSONObject jsonObj = new JSONObject();//创建json格式的数据  
		  
		JSONArray jsonArr = new JSONArray();//json格式的数组  
		  
		JSONObject jsonObjArr = new JSONObject();  
		try {  
			  
	         jsonObjArr.put("item1", "value1");  
	  
	         jsonObjArr.put("item2", "value2");  
	  
	         jsonArr.add(jsonObjArr);//将json格式的数据放到json格式的数组里  
	  
	         jsonObj.put("rows", jsonArr);//再将这个json格式的的数组放到最终的json对象中。  
	  
	         System.out.println(jsonObj.toString());  
	  
	      } catch (JSONException e) {  
	         e.printStackTrace();    
	      }  
	}
	public static void main(String[] args) {
		new ToJSON().toJson();
	}

}
