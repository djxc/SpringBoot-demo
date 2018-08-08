package com.dj.SpringBoot.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSample {
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
		
		 Statement stmt;
		 try {
			c.setAutoCommit(false);				
			stmt = c.createStatement();
			for (int i=1; i < 22; i++) {			
				String sql = String.format("CREATE TABLE watershed%d (" +
			            "  id integer NOT NULL," + 
						"  rainflow double precision," + 
						"  rainflow_lid double precision," + 
						"  PRIMARY KEY (id));", i);
				System.out.println(sql);
				stmt.executeUpdate(sql);
			}
			c.commit();
			
			stmt.close();
	        c.close();
	        System.out.println("create table successfully");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("create table failed");
		}
	}

}
