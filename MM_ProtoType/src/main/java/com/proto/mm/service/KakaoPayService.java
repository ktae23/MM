package com.proto.mm.service;
 
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.proto.mm.model.KakaoPayApproval;
import com.proto.mm.model.KakaoPayReady;
import com.proto.mm.model.Member;
import com.proto.mm.model.Movie;
import com.proto.mm.model.Orders;
import com.proto.mm.repository.MovieRepository;
import com.proto.mm.repository.OrdersRepository;
 
@Service
public class KakaoPayService {
 
    private static final String HOST = "https://kapi.kakao.com";
    
    private KakaoPayReady kakaoPayReady;
    private KakaoPayApproval kakaoPayApproval;
    
    @Autowired
    private MovieRepository movieRepository;

    
    
    public String kakaoPayReady(Model model, HttpServletRequest request, HttpServletResponse response) {
 
        RestTemplate restTemplate = new RestTemplate();
 
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "e219c9d7583da2ee2718118d75a9d27f");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        
        String movieTitle = request.getParameter("movieTitle");
        System.out.println(movieTitle);
        
       
        Movie movie = movieRepository.findByMovieTitle(movieTitle);
        HttpSession session=request.getSession(false);
        session.setAttribute("movie", movie);
        
        String moviePrice = movie.getMoviePrice().toString();
        BigDecimal movieCode = movie.getMovieCode();
        
        System.out.println(movieCode); 
        
        Member member =  (Member) model.getAttribute("member");
        session.setAttribute("member", member);
        BigDecimal memCount =member.getMemCount();
        String memName = member.getName();
        System.out.println(memCount);
        
        
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", memName);
        params.add("item_name", movieTitle);
        params.add("quantity", "1");
        params.add("total_amount", moviePrice);
        params.add("tax_free_amount", "100");
        params.add("approval_url", "http://localhost:8090/kakaoPaySuccess");
        params.add("cancel_url", "http://localhost:8090/kakaoPayCancel");
        params.add("fail_url", "http://localhost:8090/kakaoPaySuccessFail");
 
         HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
 
        try {
            kakaoPayReady = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReady.class);
            
            return kakaoPayReady.getNext_redirect_pc_url();
 
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "/pay";
        
    }
    

    public KakaoPayApproval kakaoPayInfo(String pg_token, Model model, HttpServletRequest request, HttpServletResponse response) {
 

        
        RestTemplate restTemplate = new RestTemplate();
 
        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "e219c9d7583da2ee2718118d75a9d27f");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        HttpSession session = request.getSession(false);
        Movie movie = (Movie) session.getAttribute("movie");
        System.out.println("==========="+movie.toString());
        
        String moviePrice = movie.getMoviePrice().toString();
        BigDecimal movieCode = movie.getMovieCode();
        
        System.out.println(movieCode); 
        
        Member member =  (Member) session.getAttribute("member");
        BigDecimal memCount =member.getMemCount();
        String memName = member.getName();
        System.out.println(memCount);
        
        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReady.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", memName);
        params.add("pg_token", pg_token);
        params.add("total_amount", moviePrice);
        
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        
        try {
            kakaoPayApproval = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApproval.class);
          
            return kakaoPayApproval;
        
        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }


    
}
 
