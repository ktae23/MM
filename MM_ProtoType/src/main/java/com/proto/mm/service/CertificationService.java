package com.proto.mm.service;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class CertificationService {
	 public String certifiedPhoneNumber(String phoneNumber, String cerNum) {

	        //String api_key = "NCSKKGZPRUMS1TUL";
	        //String api_secret = "GS0UBMIXE5GXZFLM75IUDDRBLJ8PINDZ";
		 	//위에꺼로 해두고 아래 발신전화번호 바꿔서 실행하시면 진짜로 문자 메세지 전송됩니다..! 2번 실험해봐서 남은 횟수는 13회...
		 	
		 	//String api_key = "본인의 API KEY";
	        //String api_secret = "본인의 API SECRET";					//이렇게 해서 실행하면 발송실패로 나옵니다!
		 	
	        String api_key = "NCSVJBMVYB7TOOQV";					//이거는 테스트 계정이라 발송성공으로 나오는데 실제로 보내지지는 않습니다! 
	        String api_secret = "DYGDIZQPLVPHNEXMXQAH2YJFXZOL36NG";	//콘솔에 나오는 인증번호로 입력해서 테스트하시면됩니다.
		 	
	        Message coolsms = new Message(api_key, api_secret);

	        // 4 params(to, from, type, text) are mandatory. must be filled
	        HashMap<String, String> params = new HashMap<String, String>();
	        params.put("to", phoneNumber);    // 수신전화번호
	        params.put("from", "01033334444");    // 발신전화번호. 진짜 테스트하고싶으시면 제 번호로 바꿔서 보내셔야합니다! 제 번호 모르시면 카톡에서 물어봐주세요ㅎㅎ
	        params.put("type", "SMS");
	        params.put("text", "Movie Mentor 휴대폰인증 메시지 : 인증번호는" + "["+cerNum+"]" + "입니다.");
	        params.put("app_version", "MM app 1.1"); // application name and version

	        try {
	            JSONObject obj = (JSONObject) coolsms.send(params);
	            System.out.println(obj.toString());
	            return "success";
	        } catch (CoolsmsException e) {
	            System.out.println(e.getMessage());
	            System.out.println(e.getCode());
	            return "fail";
	        }

	    }
}
