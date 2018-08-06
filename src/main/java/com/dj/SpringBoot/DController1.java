package com.dj.SpringBoot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DController1 {
	@RequestMapping("/hello")
	public String hello() {
		return "djxc*****Springboot";
	}
}
