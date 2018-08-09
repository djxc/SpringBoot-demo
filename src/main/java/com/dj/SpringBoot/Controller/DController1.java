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
		List<Double> rainflow = GetWaterInfo.getDatabyID(id);
		Map<String,List<Double>> map = new HashMap<String, List<Double>>();
	    if(rainflow.size()>10){
	       map.put("msg", rainflow);
	    }
	    return map;
	}
}
