package main;

import com.csvreader.CsvWriter;
import java.io.*;
import java.util.*;

public class PageRank {
    public static String filepath="//Users//wuyao//graduation_project//newData//pageRank//pageRank";
    public static void main(String[] args) throws Exception {

        //读取所有监测站信息
        InputStreamReader allCostCSV = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//newData//pathMarticCSV//pathMartic.csv"), "GBK");
        BufferedReader allCost = new BufferedReader(allCostCSV);
        String costLine = allCost.readLine();
        String[] stations = costLine.split(",");

        InputStreamReader allStationCSV = new InputStreamReader(new FileInputStream("//Users//wuyao//graduation_project//oldData//stations.csv"), "GBK");
        BufferedReader allstation = new BufferedReader(allStationCSV);
        String stationLine = allstation.readLine();
        String[] station = stationLine.split(",");

        OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("//Users//wuyao//graduation_project//newData//PageRank排名序号.csv"),"GBK");
        BufferedWriter writer = new BufferedWriter(writerStream);
        CsvWriter cwriter = new CsvWriter(writer, ',');

        String[] linesarr;
        Hashtable<String, Integer> docIDandNum = new Hashtable<String, Integer>();
        Map<String,Integer> deMap=new LinkedHashMap<String,Integer>();
        Map<String,Integer> indexMap=new LinkedHashMap<String,Integer>();
        int total = 0;
        int totals = station.length-2;
        int father, son;
        int outdegree = 0;
        double threshold = 0.00001;   //确定迭代是否结束的参数
        // 读取文件，得到docid，计算链接总数total，outdegree在迭代的时候计算
        File linkfile = new File(filepath+".txt");
        BufferedReader linkinput = new BufferedReader(new FileReader(linkfile));
        String line = linkinput.readLine();
        while (line != null) {
            ++total;
            linesarr = line.split(" ");
            if (linesarr.length > 0) {
                // outdegree = linesarr.length - 1;
                // for(int j = 1; j <= linesarr.length - 1; ++j) {
                // if(linesarr[j].equals(linesarr[0]))
                // outdegree--;
                // }
                if (linesarr[0] != null) {
                    docIDandNum.put(linesarr[0], total);
                    // System.out.println("链接" + linesarr[0] + "的出度为" + outdegree);
                }
            }
            linesarr = null;
            line = linkinput.readLine();
        }
        linkinput.close();
        System.out.println("链接总数为：" + total);
        if (total > 0) {
            // pageRank[]存放PR值
            float[] pageRank = new float[total + 1];
            float[] tRank = new float[total + 1];
            // 链入页面的计算总和
            float[] prTmp = new float[total + 1];
            // 设置pageRank[]初始值为1.0f
            for (int i = 1; i <= total; ++i) {
                pageRank[i] = 1.0f;
                prTmp[i] = 0.0f;
            }
            // 当前页面的PR值
            //float fatherRank = 1f;
            // 阻尼系数d或称为alpha
            float alpha = 0.85f;
            // 进行1000次迭代
            for (int iterator = 0; iterator < 1000; iterator++) {
                long startTime = System.currentTimeMillis();
                double change = 0;
                int count = 0;
                //Map<String,Float> weighMap=new LinkedHashMap<String,Float>();
                linkinput = new BufferedReader(new FileReader(linkfile));
                line = linkinput.readLine();
                // 读出docid和outdegree和sons
                while (line != null) {
                    Map<String,Float> weighMap=new LinkedHashMap<String,Float>();
                    float weigh = 0;
                    linesarr = line.split(" ");
                    if (linesarr.length > 0) {
                        outdegree = linesarr.length - 1;
                        for (int j = 1; j <= linesarr.length - 1; ++j) {
                            String[] split=linesarr[j].split(",");
                            // 指向自身的链接无效，不计算在内
                            if (split[0].equals(linesarr[0])){
                                outdegree--;
                            }else{
                                weighMap.put(split[0], Float.parseFloat(split[1]));
                                weigh+=Float.parseFloat(split[1]);
                            }
                        }
                        deMap.put(linesarr[0], outdegree);
                    }
                    if (outdegree > 0) {
                        father = (int) docIDandNum.get(linesarr[0]);
                        // 对应公式中的pr(Ti)/c(Ti),Ti为指向father的页面
                        //fatherRank = pageRank[father] / weigh;
                        for (int k = 1; k <= linesarr.length - 1; ++k) {
                            String[] plit = linesarr[k].split(",");
                            if (plit[0].equals(linesarr[0])) {
                                continue;
                            }
                            son = docIDandNum.get(plit[0]);
                            if (total >= son && son >= 0) {
                                prTmp[son] += Arith.div(Arith.mul(pageRank[father],weighMap.get(plit[0])),weigh);
//                                System.out.println(son+" "+weigh+" "+weighMap.get(plit[0])+" "+prTmp[son]);
                            }
                        }
                    }
                    linesarr = null;
                    weighMap = null;
                    line = linkinput.readLine();
                }
                // 准备下次迭代的初始值
                for (int i = 1; i <= total; ++i)     {
                    // PR公式1
                    // prTmp[i] = 0.15f + alpha * prTmp[i];
                    // PR公式2
                    prTmp[i] = 0.15f/total + alpha * prTmp[i];
                    change += Math.abs(pageRank[i] - prTmp[i]);
                    // 每次迭代后的真正pr值
                    pageRank[i] = prTmp[i];
                    prTmp[i] = 0.0f;
                    if(change <	threshold){
                        count++;
                    }
                }
                // 打印出每次迭代值，此操作耗费时间和内存
                // for (int i = 1; i <= total; ++i)
                // System.out.print(pageRank[i] + " \t ");
                // System.out.println(" ");
                linkinput.close();
                long endTime = System.currentTimeMillis();
                System.out.println("第" + iterator + "次迭代耗时" + (endTime - startTime) + "ms");
                if(count == total){
                    break;
                }
            }
            for (int i = 1; i <= total; ++i) {
                tRank[i] = pageRank[i] * (deMap.get(stations[i]));
            }
            //最终PR值输出至文件
            BufferedWriter newlink = new BufferedWriter(new FileWriter(new File(filepath+"tRank无排名序号.txt")));
            BufferedWriter newlink2 = new BufferedWriter(new FileWriter(new File(filepath+"tRank排名序号.txt")));
            BufferedWriter newlink1 = new BufferedWriter(new FileWriter(new File(filepath+"tRank结果.txt")));
            for (int i = 1; i <= total; ++i) {
                newlink1.write(i+" "+String.valueOf(tRank[i]));
                indexMap.put(String.valueOf(tRank[i]), i);
                newlink1.newLine();
            }
            List list=new ArrayList();

            for (int i = 1; i <= total; ++i) {
                // System.out.println(docIDandNum.toString());
                list.add(tRank[i]);
            }
            Collections.sort(list);
            Collections.reverse(list);
            for (int i = 0; i<totals; ++i){
                newlink.write(list.get(i).toString());
                writeToExcelContent(cwriter, list.get(i).toString(), station[i]);
                newlink2.write(i+1+" "+list.get(i).toString()+" "+station[i]);
                newlink.newLine();
                newlink2.newLine();
            }
            newlink.flush();
            newlink.close();
            newlink2.flush();
            newlink2.close();
            newlink1.flush();
            newlink1.close();
            deMap =null;
            indexMap = null;
            tRank = null;
            pageRank = null;
            prTmp = null;
        }
    }
    private static void writeToExcelContent(CsvWriter cwriter, String start, String num) throws IOException {
        cwriter.write(start);
        cwriter.write(num);
        cwriter.endRecord();
        cwriter.flush();
    }
}
