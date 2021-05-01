package com.proto.mm.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proto.mm.model.Member;
import com.proto.mm.service.CertificationService;
import com.proto.mm.service.MemberService;

@Controller
public class CertificationController {
	
	@Autowired
	private CertificationService certificationService;
	
	@Autowired
	MemberService memberService;
	
	@GetMapping("sendSMS")
	@ResponseBody
	public String sendSMS(Model model,HttpServletRequest request,
			HttpServletResponse response) {

		String phoneNumber = (String) request.getParameter("phoneNumber");
		System.out.println(phoneNumber);
		String find = (String) request.getParameter("find");
		System.out.println(find);
		if (find.equals("nofind")) {
			Member member = memberService.findId(phoneNumber);
			if(member != null)
				return "이미 존재하는 회원의 핸드폰 번호입니다.";
		}
		
		Random rand = new Random();
		String numStr = "";
		for (int i = 0; i < 4; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			numStr += ran;
		}

		System.out.println("수신자 번호 : " + phoneNumber);
		System.out.println("인증번호 : " + numStr);
		String result = certificationService.certifiedPhoneNumber(phoneNumber, numStr);
		if(result.equals("success"))
			return numStr;
		else
			return "인증번호 발송에 실패했습니다.";
	}
}
