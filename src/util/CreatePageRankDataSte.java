package util;

import java.io.*;

public class CreatePageRankDataSte {
    public static void main(String[] args) throws IOException {
        //读取所有监测站信息
        InputStreamReader allCostCSV = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//pathMarticCSV//pathNumMartic.csv"), "GBK");
        BufferedReader allCost = new BufferedReader(allCostCSV);

        File pageRank = new File("//Users//wuyao//graduation_project//newData//pageRank//pageRank.txt");
        pageRank.createNewFile();//创建新文件
        BufferedWriter pageRanks = new BufferedWriter(new FileWriter(pageRank));

        String costLine = allCost.readLine();
        String[] stations = costLine.split(",");
        while((costLine = allCost.readLine()) != null){
            String[] costs = costLine.split(",");
            int len = costs.length;
            String startStation = costs[0];
            String line = startStation;
            for(int i=1;i<len;i++){
                if(!costs[i].equals("0.0")){
                    line = line + " "+stations[i]+","+Double.parseDouble(costs[i]);
                }
            }
            line += "\r";
            System.out.println(line);
            pageRanks.write(line); // \r\n即为换行
        }

    }

}
