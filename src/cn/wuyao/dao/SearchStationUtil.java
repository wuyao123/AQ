package cn.wuyao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasource.Constants;
import datasource.DataSourceUtil;
import util.Printfln;

public class SearchStationUtil {
	
	public void selectStation() {
		Connection conn = null;// 创建一个数据库连接
	    PreparedStatement ps = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet rs = null;// 创建一个结果集对象
	    List<String> returnList = new ArrayList<String>();
	    Printfln print = new Printfln();
	    try {
	    	conn = DataSourceUtil.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
	        String sql = "SELECT * FROM TBL_STATION";// 预编译语句，“？”代表参数
	        ps = conn.prepareStatement(sql);// 实例化预编译语句
	        rs = ps.executeQuery();// 执行查询，注意括号中不需要再加参数
	        while (rs.next()) {
//	        	returnList.add("\"" + Integer.parseInt(rs.getString(1)) + rs.getString(2) + "\":[" + rs.getString(5) + "," + rs.getString(4) + "],");
//	        	returnList.add("{name:\"" + Integer.parseInt(rs.getString(1)) + rs.getString(2) + "\",value:80},");
				System.out.println(rs.getString(2));
	        }
	        print.print(returnList);
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
	}
	
	
	public static void main(String[] args) {
		new SearchStationUtil().selectStation();
	}

}
