package com.proto.mm.repository;


import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proto.mm.model.Member;

public interface MemberRepository extends JpaRepository<Member, BigDecimal>{
	
	// id와 pw가 일치하는 DB 값 조회
	public Member findByIdAndPw(String id, String pw);	
	
	// id가 일치 하는 DB 값 조회
	public Member findById(String id);
	
	// phone과 일치 하는 DB 값 조회
	public Member findByPhone(String phone);
	
	// phone과 id 일치 하는 DB 값 조회
	public Member findByIdAndPhone(String id, String phone);
	
	
}
