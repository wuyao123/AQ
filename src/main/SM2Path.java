package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SM2Path {
    // 遍历APTM**文件，统计出值重复的边
    public static void main(String[] args) throws IOException {
        //冬季
        String CSVNAME = "LF";
        //读取APTM**文件
        InputStreamReader hebeiIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//ATPM//ATPM"+CSVNAME+".csv"), "GBK");
        BufferedReader ATPMCsv = new BufferedReader(hebeiIsr);

        String ATPMLine = ATPMCsv.readLine();

/*****用numMap存储所有权值****/
        //读取一个周期内的邻接矩阵
        InputStreamReader marticIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//pathMarticCSV//pathNumMartic.csv"), "GBK");
        BufferedReader marticCsv = new BufferedReader(marticIsr);
        Map<String, String> numMap = new HashMap<String, String>();
        String martic = marticCsv.readLine();
        Double valSum = 0.0;
        int valNum = 0;
        String[] stationss = martic.split(",");
        while((martic = marticCsv.readLine()) != null){
            String[] curLineArray = martic.split(",");
            String start = curLineArray[0];
            for (int i = 1; i < curLineArray.length; i++) {
                numMap.put(start+","+stationss[i], curLineArray[i]);
                if(!curLineArray[i].equals("0.0")){
                    valSum += Double.parseDouble(curLineArray[i]);
                    valNum++;
                }
            }
        }
        System.out.println("平均权重："+valSum/valNum);
/*****结束***************/

        String[] stations = ATPMLine.split(",");
        List<String> vals = new ArrayList<String>();
        List<String> vals1 = new ArrayList<String>();
        Map<String,Integer> map = new HashMap<String,Integer>();
        Map<String, List<String>> dateMap = new HashMap<String, List<String>>();

        while((ATPMLine = ATPMCsv.readLine()) != null){
            String[] curLineArray = ATPMLine.split(",");
            String key = curLineArray[0];
            for (int i=1;i<curLineArray.length;i++){
                if(vals.contains(curLineArray[i])){
                    if(!vals1.contains(curLineArray[i])){
                        vals1.add(curLineArray[i]);
                    }
                }else{
                    if(!curLineArray[i].equals("0")){
                        vals.add(curLineArray[i]);
                        System.out.println(Integer.parseInt(curLineArray[i])+"----"+Integer.toBinaryString(Integer.parseInt(curLineArray[i])));
                        String s = Integer.toBinaryString(Integer.parseInt(curLineArray[i]));
                        int sum = 0;
                        for(char c : s.toCharArray()){
                            if (c == '1') sum++;
                        }
                        map.put(s, sum);
                    }
                }
            }
        }
        System.out.println("符合频繁阈值的边的值："+vals);
        System.out.println("呈周期出现的边的值："+vals1);
        InputStreamReader hebeiIsr1 = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//ATPM//ATPM"+CSVNAME+".csv"), "GBK");
        BufferedReader ATPMCsv1 = new BufferedReader(hebeiIsr1);

        Map<String,Double> map1 = new HashMap<String,Double>();
        String ATPMLine1 = ATPMCsv1.readLine();
        while((ATPMLine1 = ATPMCsv1.readLine()) != null){
            String[] curLineArray = ATPMLine1.split(",");
            String key = curLineArray[0];
            for (int i=1;i<curLineArray.length;i++){
                if(vals.contains(curLineArray[i])){
                    System.out.println(curLineArray[i]+":"+key+","+stations[i]+"----"+numMap.get(key+","+stations[i]));
                    String s = Integer.toBinaryString(Integer.parseInt(curLineArray[i]));
                    if(map1.containsKey(s)){
                        map1.put(s, map1.get(s)+1.0);
                    }else{
                        map1.put(s, 1.0);
                    }
                }
            }
        }

        Map<Integer,Double> map2 = new HashMap<Integer,Double>();
        for(String o : map1.keySet()){
            map2.put(Integer.parseInt(o,2), 0.5*map1.get(o)+0.5*map.get(o));
        }
        System.out.println(map2);
    }
}
