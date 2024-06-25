package com.com.com.member;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{
	@Inject
	private MemberMapper mapper;
	
	public void join(MemberVO memberVO) {
		mapper.join(memberVO);
		
	}		 // 로그인 메서드 구현
	    public boolean login(String mem_id, int password) {
	        // 데이터베이스에서 해당 mem_id의 회원 정보를 가져옵니다.
	        MemberVO member = mapper.getMember(mem_id);
	        if (member != null && member.getPassword() == password) {
	            return true; // 비밀번호가 일치하면 true 반환
	        } else {
	            return false; // 아니면 false 반환
	        }
	    }
	}

