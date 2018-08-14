package com.dj.SpringBoot.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dj.SpringBoot.Dao.DataSample;
import com.dj.SpringBoot.Dao.GetWaterInfo;

@RestController
public class DController1 {
	@RequestMapping("/hello")
	public String hello() {
		DataSample ds = new DataSample();
		ds.ConnectPG();
		return "djxc*****Springboot";
	}
	
	@RequestMapping("/dj")
	public Map<String,List<Double>> testajax(int id) {
		System.out.println(id); 
		Map<String, List<Double>> rainflow = GetWaterInfo.getDatabyID(id);
	    return rainflow;
	}
	
	/**
	 * 根据前端传递过来的时间，计算当前事件的经流量，然后修改smdtv_1属性表，
	 * 然后前端加载按rainflow字段分层设色显示
	 * @param time
	 * @return
	 */
	@RequestMapping("/changerainflow")
	public String changeRainflow(int time) {			
		System.out.println(time*2);
		GetWaterInfo.changeRainflow(time*2);
		return "ok";
	}
	
	/**
	 * 根据前端传递过来的时间，重现期，雨峰系数来计算经流量，然后放入每个watershed表中
	 * @param time
	 * @param cxq
	 * @param yfxs
	 * @return
	 */
	@RequestMapping("/calculateRainflow")
	public String calculateRainflow(int time, int cxq, double yfxs) {		
		GetWaterInfo.insertData(time, cxq, yfxs);
		return "ok";
	}
	
	/**
	 * 根据前端传递过来的时间，重现期，雨峰系数来计算布设海绵体之后的经流量，然后放入每个watershed表中
	 * @param time
	 * @param cxq
	 * @param yfxs
	 * @return
	 */
	@RequestMapping("/calculateLIDRainflow")
	public String calculateLIDRainflow(int time, int cxq, double yfxs) {		
		GetWaterInfo.insertLIDData(time, cxq, yfxs);
		return "ok";
	}
	
}
