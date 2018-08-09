package com.dj.SpringBoot.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dj.SpringBoot.Domain.Watershed;

public class DataSample {
	private static Connection c; 
	private static Statement stmt;

	public static void main(String[] args) {
		ConnectPG();
	}
	
	/**
	 * 链接数据库
	 */
	public static void ConnectPG() {
		 Connection c = null;
         try {
        	Class.forName("org.postgresql.Driver");
			c = DriverManager
			    .getConnection("jdbc:postgresql://121.248.96.97:5432/testdb",
			    "postgres", "123321");
//			selectData(c);	       
//			alertData(c);
//			createTable(c);
			System.out.println("Opened database successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Opened database failed");
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}        
	}
	
	/**
	 * 获取数据库链接
	 * @return
	 */
	public static void getConnect() {		
        try {
        	Class.forName("org.postgresql.Driver");
			c = DriverManager
			    .getConnection("jdbc:postgresql://121.248.96.97:5432/testdb",
			    "postgres", "123321");
			System.out.println("connect database successfully");
		} catch (Exception e) {
			System.out.println("connect database failed");
		} 
	}
	
	/**
	 * 执行完数据库相关操作时候要关闭Statement，以及数据库链接
	 */
	public static void closeAll() {
		try {
			if (stmt!=null) {				
				stmt.close();
			}
			if (c!=null) {
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改数据，需要提供链接对象
	 * @param c
	 */
	private static void alertData(Connection c) {
		 Statement stmt;
		 try {
			c.setAutoCommit(false);
				
			stmt = c.createStatement();
			String sql = "UPDATE smdtv_1 set dj = 10 where smid=10;";
			stmt.executeUpdate(sql);
			c.commit();
			
			stmt.close();
	        c.close();
	        System.out.println("alert table successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("alert table failed");
		}
	}
	
	/**
	 * 查询数据，需要提供链接对象
	 * @param c
	 */
	private static void selectData(Connection c) {
        ResultSet rs;
        Statement stmt;
		try {
		    stmt = c.createStatement();
			rs = stmt.executeQuery( "SELECT * FROM smdtv_1;" );
			while ( rs.next() ) {
		           int id = rs.getInt("smid");
		           double area = rs.getDouble("area");
		           double slope = rs.getDouble("mean_slope");
		           double imper = rs.getDouble("impermeabl");
		           double width = rs.getDouble("feature_wi");
		           int dj = rs.getInt("dj");
		           System.out.println( "ID = " + id + ";area=" + area + ";imper=" 
		           + imper + ";slope=" + slope + ";width=" + width + ";dj=" + dj);
		          }
		     rs.close();
		     stmt.close();
	         c.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
		}        
	}

	/**
	 * 创建表，每个子流域对应一个表，
	 * 包含时间（id），当前时间的经流量（rainflow），布设海绵体之后的经流量（rainflow_lid） 
	 * @param c
	 */
	private static void createTable(Connection c) {
		 try {
			c.setAutoCommit(false);				
			stmt = c.createStatement();
			for (int i=1; i <= 22; i++) {			
				String sql = String.format("CREATE TABLE watershed%d (" +
			            "  id integer NOT NULL," + 
						"  rainflow double precision," + 
						"  rainflow_lid double precision," + 
						"  PRIMARY KEY (id));", i);
				System.out.println(sql);
				stmt.executeUpdate(sql);
			}
			c.commit();			
			closeAll();
	        System.out.println("create table successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("create table failed");
		}
	}

	/**
	 * 获取每个流域的信息，返回Watershed类型的list
	 * @param c
	 */
	public static List<Watershed> getWatershedInfo() {
        ResultSet rs = null;        
        List<Watershed> watersheds = new ArrayList<Watershed>();
		try {
		    stmt = c.createStatement();
			rs = stmt.executeQuery( "SELECT * FROM smdtv_1;" );
			while ( rs.next() ) {
		           int id = rs.getInt("smid");
		           double area = rs.getDouble("area");
		           double slope = rs.getDouble("mean_slope");
		           double imper = rs.getDouble("impermeabl");
		           double width = rs.getDouble("feature_wi");
		           watersheds.add(new Watershed(id, width, area, slope, imper));
		           int dj = rs.getInt("dj");
//		           System.out.println( "ID = " + id + ";area=" + area + ";imper=" 
//		           + imper + ";slope=" + slope + ";width=" + width + ";dj=" + dj);
		          }
		     rs.close();
		} catch (SQLException e) {
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			return null;
		}finally {
			if (rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return watersheds;
	}
	
	/**
	 * 初始化表创建320行，以后只更新表就可以
	 * @param mean_rainflow
	 */
	public static void insertRainflow(List<List<Double>> mean_rainflow) {    
		try {
		    stmt = c.createStatement();
		    for (int i = 0; i < 22; i++) {		    
		    	for (int j = 0; j < 320; j++) {		    		
		    		String sql = String.format("INSERT INTO watershed%d (id)"
		    				+ " VALUES (%d);",i+1, j);
		    		stmt.executeUpdate(sql);
		    	}
		    }
		} catch (SQLException e) {
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}finally {			
			closeAll();
		}
	}

	/**
	 * 填充经流量表数据
	 * @param mean_rainflow
	 */
	public static void alertRainflow(List<List<Double>> mean_rainflow) {
		try {
			c.setAutoCommit(false);		
		    stmt = c.createStatement();
		    for (int i = 0; i < mean_rainflow.size(); i++) {
		    	List<Double> rainflow = mean_rainflow.get(i);
		    	for (int j = 0; j < rainflow.size(); j++) {		    		
		    		String sql = String.format("UPDATE watershed%d SET rainflow=%f"
		    				+ " where id=%d;",i+1,rainflow.get(j), j);
		    		stmt.executeUpdate(sql);
		    	}
		    	c.commit();
		    }
		} catch (SQLException e) {
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}finally {			
			closeAll();
			System.out.println("update sucessfully!");
		}
	}
	
	/**
	 * 填充设置海绵体后经流量表数据
	 * @param mean_rainflow
	 */
	public void alertLIDRainflow(List<List<Double>> mean_rainflow) {
		try {
			c.setAutoCommit(false);		
		    stmt = c.createStatement();
		    for (int i = 0; i < mean_rainflow.size(); i++) {
		    	List<Double> rainflow = mean_rainflow.get(i);
		    	for (int j = 0; j < rainflow.size(); j++) {		    		
		    		String sql = String.format("UPDATE watershed%d SET rainflow_lid=%f"
		    				+ " where id=%d;",i+1,rainflow.get(j), j);
		    		stmt.executeUpdate(sql);
		    	}
		    }
		} catch (SQLException e) {
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}finally {			
			closeAll();
		}
	}
	
	
	public static List<Double> selectRainflow(int id, int num) {
        ResultSet rs;
        List<Double> rainflows = new ArrayList<>();
		try {
		    stmt = c.createStatement();
		    String sql = String.format("SELECT * FROM watershed%d WHERE id<%d;",id, num);
			rs = stmt.executeQuery(sql);
			while ( rs.next() ) {		        
		           double rainflow = rs.getDouble("rainflow");
		           rainflows.add(rainflow);
			} 
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
		}   finally {
			closeAll();			
		}     
		return rainflows;
	}
	
}
