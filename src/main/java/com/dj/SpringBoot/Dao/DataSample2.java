package com.dj.SpringBoot.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dj.SpringBoot.Domain.Water;
import com.dj.SpringBoot.Domain.Watershed;

public class DataSample2 {
	private static Connection c; 
	private static Statement stmt;
	
	/**
	 * 获取数据库链接
	 * @return
	 */
	public static void getConnect() {		
        try {
        	Class.forName("org.postgresql.Driver");
			c = DriverManager
				    .getConnection("jdbc:postgresql://116.196.88.174:5432/Supermap",
				    "postgres", "123dj321");
//			System.out.println("connect database successfully");
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
			rs = stmt.executeQuery( "SELECT * FROM smdtv_4;" );
			while ( rs.next() ) {
		           int id = rs.getInt("smid");
		           double area = rs.getDouble("area");
		           double slope = rs.getDouble("mean_slope");
		           double imper = rs.getDouble("impermeabl");
		           double width = rs.getDouble("feature_wi");
//		           int dj = rs.getInt("dj");
		           System.out.println( "ID = " + id + ";area=" + area + ";imper=" 
		           + imper + ";slope=" + slope + ";width=" + width);
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
			rs = stmt.executeQuery( "SELECT * FROM smdtv_4;" );
			while ( rs.next() ) {
		           int id = rs.getInt("smid");
		           double area = rs.getDouble("area");
		           double slope = rs.getDouble("mean_slope");
		           double imper = rs.getDouble("impermeabl");
		           double width = rs.getDouble("feature_wi");
		           watersheds.add(new Watershed(id, width, area, slope, imper));		        
//		           System.out.println( "ID = " + id + ";area=" + area + ";imper=" 
//		           + imper + ";slope=" + slope + ";width=" + width);
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
	 * 获取海绵体布设后的子流域的信息
	 * @return
	 */
	public static List<Watershed> getLIDWatershedInfo() {
        ResultSet rs = null;        
        List<Watershed> watersheds = new ArrayList<Watershed>();
		try {
		    stmt = c.createStatement();
			rs = stmt.executeQuery( "SELECT * FROM smdtv_4;" );
			while ( rs.next() ) {
		           int id = rs.getInt("smid");
		           double area = rs.getDouble("area");
		           double slope = rs.getDouble("mean_slope");
		           double imper = rs.getDouble("impermeabl");
		           double width = rs.getDouble("feature_wi");
		           double lwd = rs.getDouble("lwd");
		           double xcl = rs.getDouble("xcl");
		           double yssd = rs.getDouble("yssd");
		           double tspz = rs.getDouble("tspz");
		           double xsc = rs.getDouble("xsc");
		           watersheds.add(new Watershed(id, width, area, slope, imper,
		        		   lwd, xcl, yssd, tspz, xsc));		        
//		           System.out.println( "ID = " + id + ";area=" + area + ";imper=" 
//		           + imper + ";slope=" + slope + ";width=" + width);
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
	public static void insertRainflow() {    
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
	public static void alertRainflow(List<Water> waters, String type) {
		try {
			c.setAutoCommit(false);		
		    stmt = c.createStatement();
		    for (int i = 0; i < waters.size(); i++) {
		    	int id = waters.get(i).getId();
		    	List<Double> rainflow = waters.get(i).getRunoff();
		    	for (int j = 0; j < rainflow.size(); j++) {		    		
		    		String sql = String.format("UPDATE watershed%d SET %s=%f"
		    				+ " where id=%d;", id, type, rainflow.get(j), j);
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
	 * 查询某个子流域所有降雨时间段的经流量
	 * @param id
	 * @param num
	 * @return
	 */
	public static  Map<String, List<Double>> selectRainflow(int id, int num) {
        ResultSet rs;
        List<Double> rainflows = new ArrayList<>();
        List<Double> lidrainflows = new ArrayList<>();
        Map<String, List<Double>> map = new HashMap<String, List<Double>>();
		try {
		    stmt = c.createStatement();
		    String sql = String.format("SELECT * FROM watershed%d WHERE id<%d;",id, num);
			rs = stmt.executeQuery(sql);
			while ( rs.next() ) {		        
		           double rainflow = rs.getDouble("rainflow");
		           double lidrainflow = rs.getDouble("rainflow_lid");
		           rainflows.add(rainflow);
		           lidrainflows.add(lidrainflow);
			} 
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
		}   finally {
			map.put("rainflow", rainflows);
			map.put("lidrainflow", lidrainflows);
			closeAll();			
		}     
		return map;
	}
	
	/**
	 * 获取每个子流域某一时刻经流量,返回double类型的list
	 * @param time
	 * @param size
	 * @return
	 */
	public static  List<Water> selectRainflow1(int time, int size, String field) {
		ResultSet rs = null;
		List<Water> rainflows = new ArrayList<Water>();
    	try {
    		stmt = c.createStatement();
    		for (int i = 1; i <= size; i++) {
				String sql = String.format("SELECT * FROM watershed%d WHERE id=%d;",i, time);
	 			rs = stmt.executeQuery(sql);
	 			while ( rs.next() ) {		        
		           double rainflow = rs.getDouble(field);
		           Water w = new Water(i, rainflow);
		           rainflows.add(w);
				} 
    		}
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		}
        return rainflows;
	}
	
	/**
	 * 更新经流量，每分钟更新一次，然后在前端更新显示
	 * @param runoff
	 */
	public static void changeRainflow(List<Water> waters) {
		try {
			c.setAutoCommit(false);		
		    stmt = c.createStatement();
		    for (int i = 0; i < waters.size(); i++) {
		    	double rainflow = waters.get(i).getRainflow();	
		    	int id = waters.get(i).getId();
	    		String sql = String.format("UPDATE smdtv_4 SET rainflow=%f"
	    				+ " where smid=%d;",rainflow, id);
	    		stmt.executeUpdate(sql);		    	
		    }
		    c.commit();
		    System.out.println("update sucessfully!");
		} catch (SQLException e) {
			System.out.println(e.getClass().getName()+": "+e.getMessage());
		}finally {			
			closeAll();
		}
	}
		
}
