package com.com.com.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

	@Autowired
	private MemberServiceImpl service;

	@GetMapping("/member")
	public String member() {
		return "/mem/login";
	}

	@GetMapping("/join_form")
	public String join_form() {
		return "/mem/join";
	}

	@PostMapping("join")
	public String join(MemberVO memberVO) {
		service.join(memberVO);
		return "/mem/success";
	}

	@PostMapping("/login")
	public String login(@RequestParam("mem_id") String mem_id, @RequestParam("password") int password) {
		if (service.login(mem_id, password)) {
			return "/mem/success"; // 로그인 성공 시 success.jsp로 이동
		} else {
			return "/mem/login"; // 로그인 실패 시 다시 로그인 페이지로 이동
		}
	}
}
