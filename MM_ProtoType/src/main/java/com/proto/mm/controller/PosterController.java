package com.proto.mm.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.proto.mm.model.Movie;
import com.proto.mm.model.Poster;
import com.proto.mm.service.PosterService;

@Controller
public class PosterController {
	
	@Autowired
	PosterService posterService;
	
	
	@RequestMapping(value="download", 
			method= {RequestMethod.GET})
    public void downLoad(@RequestParam String movieTitle, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		  posterService.posterDownload(model, request, response);
		  Poster poster = (Poster) model.getAttribute("poster");
		  Movie movie = (Movie) model.getAttribute("movie");
		  
		  String dFile = (String) model.getAttribute("dFile");
		  System.out.println("파일 이름 : " + dFile);
		  
		  String upDir = "/"+ File.separator + "MM"+File.separator+"movie_imgs";
		  String path = upDir+File.separator+dFile;
		  
		  File file = new File(path);

		  String userAgent = request.getHeader("User-Agent");
		  boolean ie = userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("rv:11") > -1;
		  String fileName = null;
		  String tmp = movie.getMovieTitle().replace(" ", "").replace(":","_") + ".png";
		  
		  if (ie) {
		   fileName = URLEncoder.encode(tmp, "utf-8");
		  } else {
		   fileName = new String(tmp.getBytes("utf-8"),"iso-8859-1");
		  }
		  
		  response.setContentType("application/octet-stream");
		  response.setHeader("Content-Disposition","attachment;filename=\"" +fileName+"\";");
		  
		  FileInputStream fis=new FileInputStream(file);
		  BufferedInputStream bis=new BufferedInputStream(fis);
		  ServletOutputStream so=response.getOutputStream();
		  BufferedOutputStream bos=new BufferedOutputStream(so);
		  
		  byte[] data=new byte[2048];
		  int input=0;
		  while((input=bis.read(data))!=-1){
		   bos.write(data,0,input);
		   bos.flush();
		  }
		  
		  if(bos!=null) bos.close();
		  if(bis!=null) bis.close();
		  if(so!=null) so.close();
		  if(fis!=null) fis.close();
		 }
}


