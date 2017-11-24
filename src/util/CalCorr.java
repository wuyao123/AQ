package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvWriter;

public class CalCorr {
	
	public Double getCorr(int startId, int endId) throws ClassNotFoundException{
		
		Connection con = null;// 创建一个数据库连接
	    PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet result = null;// 创建一个结果集对象
	    double corr = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
	        System.out.println("开始尝试连接数据库！");
	        String url = "jdbc:oracle:" + "thin:@localhost:1521:XE";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
	        String user = "AQ";// 用户名,系统默认的账户名
	        String password = "wuyao";// 你安装时选设置的密码
	        con = DriverManager.getConnection(url, user, password);// 获取连接
	        System.out.println("连接成功！");
	        String sql = "SELECT corr FROM TBL_CORR WHERE startid = ? and endid = ?";// 预编译语句，“？”代表参数
	        pre = con.prepareStatement(sql);// 实例化预编译语句
	        pre.setString(1,startId+"");// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
	        pre.setString(2,endId+"");
	        result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数      
	        while (result.next()){
	        	corr = Double.parseDouble(result.getString(1));
	        }// 当结果集不为空时
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
	        try{
	            // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
	            // 注意关闭的顺序，最后使用的最先关闭
	            if (result != null)
	                result.close();
	            if (pre != null)
	                pre.close();
	            if (con != null)
	                con.close();
	            System.out.println("数据库连接已关闭！");
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	    }
		return corr;
	}
	
	public Double corr() throws IOException{
		BufferedReader baseCsv = new BufferedReader(new FileReader("F://data//station1.csv"));
		BufferedReader baseCsv1 = new BufferedReader(new FileReader("F://data//CorrOfAllStation.csv"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("F://data//switchCorr.csv"));
		CsvWriter cwriter = new CsvWriter(writer, ',');
		
		String line=baseCsv.readLine();
		
		Printfln print = new Printfln();
		CalDistances calDis = new CalDistances();
		
		List<String[]> stationslist = new ArrayList<String[]>();
		while((line =baseCsv.readLine())!=null){
			String[] curLineArray=line.split(",");
			//stationslist.add(curLineArray[0]);
			stationslist.add(curLineArray);
		}
		//print.print(stationslist);
		
		String line1=baseCsv1.readLine();
		int count = 1;
		writeToExcel(cwriter, "STARTID", "ENDID","CORR","DISTANCE");
		while((line1 =baseCsv1.readLine())!=null){
			String[] curLineArray=line1.split(",");
			for (int i = count; i < curLineArray.length; i++) {
				writeToExcel(cwriter, stationslist.get(count-1)[0], stationslist.get(i)[0], curLineArray[i],
							""+calDis.Distance(stationslist.get(count-1)[4], stationslist.get(count-1)[3], stationslist.get(i)[4], stationslist.get(i)[3]));
			}
			count++;
		}
		cwriter.close();
		baseCsv.close();
		System.out.println("完成");
		return null;
	}
	
	
	private void writeToExcel(CsvWriter cwriter, String string, String string2, String string3, String string4) throws IOException {
		cwriter.write(string);
		cwriter.write(string2);
		cwriter.write(string3);
		cwriter.write(string4);
		cwriter.endRecord();
		cwriter.flush();
	}


	public static void main(String[] args) throws IOException {
		new CalCorr().corr();
	}
}
