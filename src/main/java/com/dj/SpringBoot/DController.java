package com.dj.SpringBoot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DController {
	@RequestMapping("/hello")
	public String hello() {
		return "djxc*****Springboot";
	}
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
}
