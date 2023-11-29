package com.wolfTech.recommendationService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
	@GetMapping("/test")
	public String index() {
		return "/all_crypto_list";
	}
}
