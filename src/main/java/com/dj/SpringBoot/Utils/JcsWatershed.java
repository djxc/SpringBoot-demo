package com.dj.SpringBoot.Utils;

import java.util.ArrayList;
import java.util.List;

//import java.io.Serializable;
//import org.apache.commons.jcs.JCS;
//import org.apache.commons.jcs.access.CacheAccess;
//import org.apache.commons.jcs.access.exception.CacheException;

import com.dj.SpringBoot.Domain.Water;

public class JcsWatershed {
//	private CacheAccess<Integer, Water> cache = null;
//	 
//	public static void main(String[] args) {
//		JcsWatershed jcswater = new JcsWatershed();
//		jcswater.testCache(22);
////		jcswater.getRainflows(22);
//	}
//    public JcsWatershed() 
//    {
//        try 
//        {
//            cache = JCS.getInstance( "default" );
//        }
//        catch ( CacheException e ) 
//        {
//            System.out.println( String.format( "Problem initializing cache: %s", e.getMessage() ) );
//        }
//    }
//
//    /**
//     * 将数据放入缓存文件中，以map形式,子流域的id为key，water为value
//     * @param city
//     */
//    public void putInCache(List<Water> waters) 
//    {
//    	for (int i = 0; i< waters.size(); i++) {
//    		int id = waters.get(i).id;
//    		try 
//    		{
//    			cache.put(id, waters.get(i));
//    			System.out.println("put watershed id =" + id);
//    		}
//    		catch ( CacheException e ) 
//    		{
//    			System.out.println( String.format( "Problem putting watershed %d in the cache, for key %s",
//    					id, e.getMessage() ) );
//    		}
//    	}
//    }
//    
//    /**
//     * 将water对象存入缓存中，以water的id为key，对象为value
//     * @param water
//     */
//    public void putLIDInCache(List<Water> water) 
//    {
//    	for (int i = 0; i< water.size(); i++) {
//    		int id = water.get(i).id;
//    		try 
//    		{
//    			cache.put(i+100, water.get(i));
//    			System.out.println("put watershed id =" + id+100);
//    		}
//    		catch ( CacheException e ) 
//    		{
//    			System.out.println( String.format( "Problem putting watershed %d in the cache, for key %s",
//    					id, e.getMessage() ) );
//    		}
//    	}
//    }
//    
//    /**
//     * 获取缓存中的数据
//     * @param cityKey
//     * @return
//     */
//    public Water retrieveFromCache(int key) 
//    {
//        return cache.get(key);
//    }
//    /**
//     * 获取缓存中的子流域对象
//     * @param num
//     * @return
//     */
//    public List<Water> testCache(int num) 
//    {
//    	List<Double> dj = new ArrayList<Double>();
//    	for (int j=0; j<10; j++) {
//    		dj.add(j*0.2);
//    	}
//    	List<Water> waters = new ArrayList<Water>();
//    	for (int j=0; j<10; j++) {
//    		Water w = new Water(j, dj);
//    		waters.add(w);
//    	}
//    	putInCache(waters);
//    	List<Water> watersheds = new ArrayList<Water>();
//    	for (int i = 1; i < 20; i++) {	    		
//    		Water retrievedCity1 = retrieveFromCache(i);
//    		if ( retrievedCity1 == null ) 
//            {
//                System.out.println("can not find");
//            }else {            	
//            	watersheds.add(retrievedCity1);
//            	System.out.println(retrievedCity1.getId() + "find it!*********************");
//            }
//    	}	   
//    	return watersheds;	       
//    }
//    
//    /**
//     * 通过时间获取当前时间各个子流域的经流量
//     * @param time
//     * @param size
//     * @return
//     */
//    public List<Water> getRainflows(int size){
//    	List<Water> rainflows = new ArrayList<Water>();
//    	for (int i = 1; i <= size; i++) {
//    		Water w = retrieveFromCache(i);
//    		if ( w == null ) 
//            {
//                System.out.println("can not find id=" + i);
//            }else {            	
//            	rainflows.add(w);
//            }
////    		System.out.println("find it!*********************");
//    	}
//    	return rainflows;
//    }
//    
//    public List<Water> getLIDRainflows(int size){
//    	List<Water> rainflows = new ArrayList<Water>();
//    	for (int i = 1; i <= size; i++) {
//    		Water w = retrieveFromCache(i+100);   
//    		if ( w == null ) 
//            {
//                System.out.println("can not find id=" + i+100);
//            }else {            	
//            	rainflows.add(w);
//            }
//    	}
//    	return rainflows;
//    }
//    
}
