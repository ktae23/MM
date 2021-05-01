package com.proto.mm.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Entity // 데이터베이스 연동 클래스임을 명시
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
		name="MEMBER_NO_SEQ_GENERATOR",
		sequenceName = "MEMBER_NO_SEQ", // 매핑할 데이터 베이스 시퀀스 이름
		initialValue = 1,
		allocationSize = 1)
public class Member{

	@Id  // PK임을 명시
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator ="MEMBER_NO_SEQ_GENERATOR")
	// GenerationType은 Auto가 기본, 자동으로 해줌. 성능은 시퀀스가 가장 좋지만 많은 건을 처리 하고
	// 자동증가 기본값으로 id 설정 시 IDENTITY가 적절함 
	@Column(name="mem_count")
	private BigDecimal memCount;
	
	@NonNull
	private String id;
	@NonNull
	private String pw;
	@NonNull
	private String name;
	@NonNull
	private String preference;
	@NonNull
	private String phone;

}
