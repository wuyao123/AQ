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
        String CSVNAME = "CZ";
        //读取APTM**文件
        InputStreamReader hebeiIsr = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//ATPM//ATPM"+CSVNAME+".csv"), "GBK");
        BufferedReader ATPMCsv = new BufferedReader(hebeiIsr);

        String ATPMLine = ATPMCsv.readLine();

        String[] stations = ATPMLine.split(",");
        List<String> vals = new ArrayList<String>();
        List<String> vals1 = new ArrayList<String>();
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
                    }
                }
            }
        }
        System.out.println("符合频繁阈值的边的值："+vals);
        System.out.println("呈周期出现的边的值："+vals1);
        InputStreamReader hebeiIsr1 = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//ATPM//ATPM"+CSVNAME+".csv"), "GBK");
        BufferedReader ATPMCsv1 = new BufferedReader(hebeiIsr1);

        String ATPMLine1 = ATPMCsv1.readLine();
        while((ATPMLine1 = ATPMCsv1.readLine()) != null){
            String[] curLineArray = ATPMLine1.split(",");
            String key = curLineArray[0];
            for (int i=1;i<curLineArray.length;i++){
                if(vals.contains(curLineArray[i])){
                    System.out.println(curLineArray[i]+":"+key+","+stations[i]);
                }
            }
        }
    }
}
