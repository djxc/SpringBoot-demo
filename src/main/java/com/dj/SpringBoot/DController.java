package com.dj.SpringBoot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DController {
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
}
