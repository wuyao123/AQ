package cn.wuyao.dao;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvWriter;

import datasource.Constants;
import datasource.DataSourceUtil;
import util.Printfln;
import util.ToMap;

public class HebeiUtil {

	public List<String> selectHebeiInf() {
		
		Connection conn = null;// 创建一个数据库连接
	    PreparedStatement ps = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet rs = null;// 创建一个结果集对象
	    List<String> returnList = new ArrayList<String>();
	    ToMap toMap = new ToMap();
	    Printfln print = new Printfln();
	    try {
	    	System.out.println("开始连接数据库！");
	    	conn = DataSourceUtil.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
	        System.out.println("连接成功！");
	        String sql = "SELECT * FROM VIEW_HEBEI";// 预编译语句，“？”代表参数
	        ps = conn.prepareStatement(sql);// 实例化预编译语句
	        rs = ps.executeQuery();// 执行查询，注意括号中不需要再加参数
	        
	        OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream("F://data//hebeiAllInf.csv"),"GBK");
	        BufferedWriter writer = new BufferedWriter(writerStream);
	        CsvWriter cwriter = new CsvWriter(writer, ',');
	        writeToExcel(cwriter,"TIME","CITYNAMEC","STATIONNAMEC","STATIONID","LATITUDE","LONGITUDE",
	        					 "PM25","WINDDIRECTION","WINDSPEED","TEMPERATURE","PRESSURE","HUMIDITY");
	        
	        while (rs.next()) {
//	        	writeToExcel(cwriter,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),
//					     rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));
	        	writeToExcel(cwriter,rs.getString(1),rs.getString(3),rs.getString(7),Integer.parseInt(rs.getString(6))+"",
	        					     rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(12),rs.getString(11),
	        					     rs.getString(14),rs.getString(15),rs.getString(16));
//	        	List<String> list = new ArrayList<String>();
//	        	list.add(rs.getString(3));
//	        	list.add(rs.getString(7));
//	        	list.add(rs.getString(6));
//	        	list.add(rs.getString(8));
//	        	list.add(rs.getString(9));
//	        	list.add(rs.getString(12));
//	        	String value = toMap.toList(list);
//	        	returnList.add(value);
	        	//returnList.add("{name:\"" + Integer.parseInt(rs.getString(6)) + rs.getString(7) + "\",value:"+rs.getString(10) + "},");
	        }
		    System.out.println("操作完成");
		    cwriter.close();
	        //print.print(returnList);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
                if (null != rs) {
                    rs.close();
                }
                if (null != ps) {
                    ps.close();
                }
                if (null != conn) {
                    DataSourceUtil.closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
		}
		return returnList;
	}
	
	private void writeToExcel(CsvWriter cwriter, String string, String string2, String string3, String string4,
			String string5, String string6, String string7, String string8,String string9, String string10, 
			String string11, String string12) throws IOException {
		cwriter.write(string);
		cwriter.write(string2);
		cwriter.write(string3);
		cwriter.write(string4);
		cwriter.write(string5);
		cwriter.write(string6);
		cwriter.write(string7);
		cwriter.write(string8);
		cwriter.write(string9);
		cwriter.write(string10);
		cwriter.write(string11);
		cwriter.write(string12);
		cwriter.endRecord();
		cwriter.flush();
	}

	public static void main(String[] args) {
		new HebeiUtil().selectHebeiInf();
	}
}
