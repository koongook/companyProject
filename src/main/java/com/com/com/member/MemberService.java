package com.com.com.member;

public interface MemberService {
	public void join(MemberVO memberVO);
	public boolean login(String mem_id, int password); // 로그인 메서드 추가
}
