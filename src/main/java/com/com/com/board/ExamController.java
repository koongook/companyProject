package com.com.com.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExamController {
//	@RequestMapping(value="/checkbox", method=RequestMethod.GET)
	@RequestMapping("/checkbox")
	public String checkbox() {
		return "checkbox";
	}

}


