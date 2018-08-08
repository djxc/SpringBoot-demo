package com.dj.SpringBoot.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dj.SpringBoot.Dao.DataSample;

@RestController
public class DController1 {
	@RequestMapping("/hello")
	public String hello() {
		DataSample ds = new DataSample();
		ds.ConnectPG();
		return "djxc*****Springboot";
	}
}
