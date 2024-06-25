package com.com.com.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

	public void join(MemberVO memberVO);

	public MemberVO getMember(String mem_id);

}
