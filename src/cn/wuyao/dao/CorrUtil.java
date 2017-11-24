package cn.wuyao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import datasource.Constants;
import datasource.DataSourceUtil;

public class CorrUtil {
public Map<String,Double> selectCorr() {
		
		Connection conn = null;// 创建一个数据库连接
	    PreparedStatement ps = null;// 创建预编译语句对象，一般都是用这个而不用Statement
	    ResultSet rs = null;// 创建一个结果集对象
	    Map<String,Double> returnMap = new HashMap<String,Double>();
	    try {
	    	conn = DataSourceUtil.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
	        String sql = "SELECT * FROM TBL_CORR";// 预编译语句，“？”代表参数
	        ps = conn.prepareStatement(sql);// 实例化预编译语句
	        rs = ps.executeQuery();// 执行查询，注意括号中不需要再加参数
	        while (rs.next()) {
	        	String key = rs.getString(2) + rs.getString(3);
	        	Double value = Double.parseDouble(rs.getString(4));
	        	returnMap.put(key, value);
	        }
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
		return returnMap;
	}

}
