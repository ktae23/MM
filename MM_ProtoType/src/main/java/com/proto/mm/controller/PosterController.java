package com.proto.mm.controller;

import java.io.BufferedInputStream;
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
import org.springframework.web.bind.annotation.GetMapping;

import com.proto.mm.model.Movie;
import com.proto.mm.model.Poster;
import com.proto.mm.service.PosterService;

@Controller
public class PosterController {
	
	@Autowired
	PosterService posterService;
	
	@GetMapping("download")
    public void downLoad(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
			posterService.posterDownload(model, request, response);
			Poster poster = (Poster) model.getAttribute("poster");
			Movie movie = (Movie) model.getAttribute("movie");
			
			File file = new File("/" + poster.getPosterPath());
	        
	        FileInputStream fis = null;
	        BufferedInputStream bis = null;
	        ServletOutputStream sos = null;
	        try {
	        	fis = new FileInputStream(file);
	        	bis = new BufferedInputStream(fis);
	        	sos = response.getOutputStream();
	        	String tmp = movie.getMovieTitle();
	        	String fileName = tmp.replace(" ", "").replace(":", "_") + ".png";
	        	String reFilename = "";
	        	boolean isMSIE = request.getHeader("user-agent").indexOf("MSIE") != -1 || 
	        			request.getHeader("user-agent").indexOf("Trident") != -1;
	        	if(isMSIE) { 
	        	reFilename = URLEncoder.encode(fileName, "utf-8");
	        	reFilename = reFilename.replaceAll("\\+", "%20"); 
	        	}
	        	else {
	        		reFilename = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
	        	} 
	        	
	        	response.setContentType("application/octet-stream;charset=utf-8");
	        	response.addHeader("Content-Disposition", "attachment;filename=\""+reFilename+"\"");
	        	response.setContentLength((int)file.length());
	        	
	        	int read = 0;
	        	while((read = bis.read()) != -1) {
	        		sos.write(read); 
	        		} 
	        	
	    	}catch(IOException e) {
	    		e.printStackTrace(); 
	    	}finally {
	    		try {
	    			sos.close();
	    			bis.close();
	    			}catch (IOException e) {
	    				e.printStackTrace(); }
			}
    }
}


