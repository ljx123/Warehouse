package tech.heron.hqlexec;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Test;

public class HiveUtils {

	private String user = "root";
	private String password = "123";
//	// Connection
	private static final String URLHIVE_DB_DEFAULT = "jdbc:hive://hadoop1:50000/default";
	private static final String URLHIVE_DB_ODS = "jdbc:hive://hadoop1:50000/ods";
	public ResultSet execute(String SQL , Connection connection) throws SQLException {

		Statement stmt = null;
		ResultSet resultSet = null;
//		try {
			stmt = connection.createStatement();
			resultSet = stmt.executeQuery(SQL);
			
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally{
//			if(stmt != null)
//				stmt.close();
//		}
		return resultSet;

	}
	
	public ResultSet executeForDynamicPartition(String SQL , Connection connection) throws SQLException{
		Statement stmt = null;
		ResultSet resultSet = null;
//		try {
			stmt = connection.createStatement();
			stmt.execute("SET hive.exec.dynamic.partition=true");
			stmt.execute("SET hive.exec.dynamic.partition.mode=nonstrict");			
			resultSet = stmt.executeQuery(SQL);
			
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally{
//			if(stmt != null)
//				stmt.close();
//		}
		return resultSet;
	}
	
	private Connection getHiveConnection(String URLHIVE) {
		Connection connection = null;
			synchronized (HiveUtils.class) {
				if (null == connection) {
					try {
						Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
						connection = DriverManager.getConnection(URLHIVE, user,
								password);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		return connection;
	}
	
	public boolean loadOdsData(String date){
		System.out.print("loadOdsData");
		Connection connection = getHiveConnection(URLHIVE_DB_DEFAULT);
		boolean result = false;
		
		try {
			execute("load data inpath '/user/source/audit/"+date+"/' into table ods.log_detail_base partition(dt='"+date+"')" , connection);
			//execute("alter table ods.log_detail_base drop partition (dt="+date+")" , connection);
			//execute("",connection);
			result = true;
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
			return result;
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	//然后将log_type=’page’的数据取出来，插入dw.fact_page_base表中
	public boolean extractPageLog(String date){
		System.out.print("extractPageLog");
		boolean result = false;
		Connection connection = getHiveConnection(URLHIVE_DB_DEFAULT);
		
		try {
			execute("insert overwrite table dw.fact_page_base partition(dt='"+date+"') select app_name,device_type,ip,app_ver,userkey,log_type,phone,net,city_id,time_stamp,page_id,refer_page_type from ods.log_detail_base where dt='"+date+"' and log_type='page'" , connection);
			//execute("alter table ods.log_detail_base drop partition (dt="+date+")" , connection);
			//execute("",connection);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	//抽取dw.fact_user_aggr_daily表
	public boolean extract_fact_user_aggr_daily(String date){
		System.out.print("extract_fact_user_aggr_daily");
		boolean result = false;
		Connection connection = getHiveConnection(URLHIVE_DB_DEFAULT);
		
		try {
			String sql = "insert overwrite table dw.fact_user_aggr_daily partition(dt , device_type) select app_ver,userkey,count(*) pv,count(distinct page_id) unique_pv,dt,device_type from dw.fact_page_base where dt='"+date+"' group by app_ver,userkey,dt,device_type";
			executeForDynamicPartition(sql, connection);
			//execute("alter table ods.log_detail_base drop partition (dt="+date+")" , connection);
			//execute("",connection);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	//抽取dw.fact_page_aggr_daily表
	public boolean extract_fact_page_aggr_daily(String date){
		boolean result = false;
		Connection connection = getHiveConnection(URLHIVE_DB_DEFAULT);
		
		try {
			String sql = "insert overwrite table dw.fact_page_aggr_daily partition(dt) page_id,reger_page_type,count(*) pv,count(distinct userkey) uv,dt from dw.fact_page_base where dt='"+date+"' group by page_id,dt,reger_page_type";
			executeForDynamicPartition(sql, connection);
			//execute("alter table ods.log_detail_base drop partition (dt="+date+")" , connection);
			//execute("",connection);
			result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
	//抽取dw.dim_user表
		public boolean extract_dim_user(String date){
			boolean result = false;
			Connection connection = getHiveConnection(URLHIVE_DB_DEFAULT);
			
			try {
				String sql = "insert overwrite table dw.fact_user_aggr_daily partition(dt , device_type) select app_ver,userkey,count(*) pv,count(distinct page_id) unique_pv,dt,device_type from dw.fact_page_base where dt='"+date+"' group by app_ver,userkey,dt,device_type";
				executeForDynamicPartition(sql, connection);
				//execute("alter table ods.log_detail_base drop partition (dt="+date+")" , connection);
				//execute("",connection);
				result = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			return result;
		}


}
