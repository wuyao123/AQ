package util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.wuyao.dao.CorrUtil;
import cn.wuyao.dao.HebeiUtil;

public class Main {
	/**
	 * 一个非常标准的连接Oracle数据库的示例代码
	 */
	public void todo()
	{
		Printfln printOut = new Printfln();
		CalDistances calDis = new CalDistances();
        SwitchWindVector switchWindVector = new SwitchWindVector();
        HebeiUtil hebeiUtil = new HebeiUtil();
        CorrUtil corrUtil = new CorrUtil();
        
        //查询河北省信息
	    List<String> returnList = hebeiUtil.selectHebeiInf();
	    printOut.print(returnList);
	    //站点相关性
	    Map<String,Double> returnMap = corrUtil.selectCorr();
	    List<String> stationList = new ArrayList<String>();
	    for (int i = 0; i < returnList.size(); i++) {
	    	String[] valis = returnList.get(i).split(",");
//	        double maxVal = 100000;
//	        int maxJ = 0;
			for (int j = 0; j < returnList.size(); j++) {
				String[] valjs = returnList.get(j).split(",");
				//计算两点之间的距离
				Double D = calDis.Distance(valis[4], valis[3], valjs[4], valjs[3]);
				String angle = switchWindVector.SWV(valis[5]);
				if(D > 20 && D <= 50 && !angle.equals("NULL")){
					
					//计算两个监测站的角度
					Double stationWithVector = switchWindVector.Angle(valis[4], valis[3], valjs[4], valjs[3], angle);
						
					//查询相关两个站点之间的相关性
				    Double stationCorr = returnMap.get(Integer.parseInt(valis[2])+""+Integer.parseInt(valjs[2]));
				    if(stationCorr == null){
				    	stationCorr = returnMap.get(Integer.parseInt(valjs[2])+""+Integer.parseInt(valis[2]));
				    }
					if(stationWithVector > 0 && stationWithVector < 80 && stationCorr > 0.5){
						String sTos = Integer.parseInt(valis[2]) + "," + valis[0] + "," + valis[1] + "," + Integer.parseInt(valjs[2])  + "," + valjs[0] + "," + valjs[1] + "," + stationWithVector + "," + stationCorr;
						stationList.add(sTos);
					}
				}
			}
		}
	    CalPath calPath = new CalPath();
	    //printOut.print(stationList);
	    List<List<String>> lists = calPath.searchPath(stationList);
	    System.out.println(stationList.size());
//		for (List<String> sl : lists) {
//			if(sl.size() < 4){
//				continue;
//			}else{
//				for (String str : sl) {
//					System.out.print(str+" ");
//				}
//				System.out.println();
//			}
//		}
	}
	public static void main(String[] args){  
        new Main().todo();
    }  
}
