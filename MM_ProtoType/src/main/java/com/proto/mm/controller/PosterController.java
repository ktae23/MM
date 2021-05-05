package com.proto.mm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proto.mm.model.Movie;
import com.proto.mm.model.Poster;
import com.proto.mm.service.PosterService;

@Controller
public class PosterController {
	
	@Autowired
	PosterService posterService;
	
	@RequestMapping("download")
    public void downLoad(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		posterService.posterDownload(model, request, response);
		
		Poster poster = (Poster) model.getAttribute("poster");
		Movie movie = (Movie) model.getAttribute("movie");
		
		 String saveDir = "/" + poster.getPosterPath();
	        System.out.println(saveDir);
	        String tmp = movie.getMovieTitle();
	  		String fileName = tmp.replace(" ", "").replace(":", "_");
	  		
	  		File file = new File(saveDir);
	  		 
	  	    FileInputStream fileInputStream = null;
	  	    ServletOutputStream servletOutputStream = null;
	  	 
	  	    try{
	  	        String downName = null;
	  	        String browser = request.getHeader("User-Agent");
	  	        //파일 인코딩
	  	        if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//브라우저 확인 파일명 encode  
	  	            
	  	            downName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
	  	            
	  	        }else{
	  	            
	  	            downName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
	  	            
	  	        }
	  	        
	  	        response.setHeader("Content-Disposition","attachment;filename=\"" + downName+"\"");             
	  	        response.setContentType("application/octer-stream");
	  	        response.setHeader("Content-Transfer-Encoding", "binary;");
	  	 
	  	        fileInputStream = new FileInputStream(file);
	  	        servletOutputStream = response.getOutputStream();
	  	 
	  	        byte b [] = new byte[1024];
	  	        int data = 0;
	  	 
	  	        while((data=(fileInputStream.read(b, 0, b.length))) != -1){
	  	            
	  	            servletOutputStream.write(b, 0, data);
	  	            
	  	        }
	  	 
	  	        servletOutputStream.flush();//출력
	  	        
	  	    }catch (Exception e) {
	  	        e.printStackTrace();
	  	    }finally{
	  	        if(servletOutputStream!=null){
	  	            try{
	  	                servletOutputStream.close();
	  	            }catch (IOException e){
	  	                e.printStackTrace();
	  	            }
	  	        }
	  	        if(fileInputStream!=null){
	  	            try{
	  	                fileInputStream.close();
	  	            }catch (IOException e){
	  	                e.printStackTrace();
	  	            }
	  	        }

	  	    }
	}
}

