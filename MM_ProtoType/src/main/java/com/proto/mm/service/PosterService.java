package com.proto.mm.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.proto.mm.model.Movie;
import com.proto.mm.model.Poster;
import com.proto.mm.repository.MovieRepository;
import com.proto.mm.repository.PosterRepository;

@Service
public class PosterService {
	
	@Autowired
	PosterRepository posterRepository;

	@Autowired
	private MovieRepository movieRepository;

	public Model showDetailPoster(Model model, String movieTitle) {

		 Movie movie = movieRepository.findByMovieTitle(movieTitle); 
		 BigDecimal movieCode = movie.getMovieCode(); 
		 Poster poster = posterRepository.findByMovieCode(movieCode); 
		 String filePath = poster.getPosterPath();//DB에서 조회한 파일경로 System.out.println(filePath);
		 model.addAttribute("filePath", filePath);

		return model;
	}
	
	public Model showPosterResult(Model model) {
		List<Movie> movies = (List<Movie>) model.getAttribute("movies");
		List<Poster> posters = new ArrayList<Poster>();
		Poster poster = new Poster();
		for(Movie movie : movies) {
			BigDecimal movieCode = movie.getMovieCode();
			poster = posterRepository.findByMovieCode(movieCode);
			posters.add(poster);
		}
		model.addAttribute("posters", posters);

		
		return model;
	}

	public void posterDownload(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String movieTitle = request.getParameter("movieTitle");
        System.out.println(movieTitle);
        Movie movie = movieRepository.findByMovieTitle(movieTitle);
        BigDecimal movieCode = movie.getMovieCode();
        System.out.println(movieCode);
        Poster poster =	posterRepository.findByMovieCode(movieCode);
        
        File file = new File("http://52.200.16.8:8090/poster/" + poster.getPosterPath());
        
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ServletOutputStream sos = null;
        try {
        	fis = new FileInputStream(file);
        	bis = new BufferedInputStream(fis);
        	sos = response.getOutputStream();
        	String tmp =movieTitle;
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

        
		/*
		 * String imgUrl = "http://52.200.16.8:8090/poster/" + poster.getPosterPath();
		 * 
		 * String tmp = movie.getMovieTitle(); String fileName = tmp.replace(" ",
		 * "").replace(":", "_");
		 * 
		 * String home = File.separator+"Users"+ File.separator +
		 * System.getProperty("user.name") + File.separator; String path =
		 * (home+"Downloads" + File.separator); System.out.println(path); SaveImg
		 * saveImg = new SaveImg();
		 * 
		 * try { int result = saveImg.saveImgFromUrl(imgUrl, path, fileName); // 성공 시 1
		 * 리턴, 오류 시 -1 리턴 if (result == 1) { System.out.println("저장된경로 : " +
		 * saveImg.getPath()); System.out.println("저장된파일이름 : " +
		 * saveImg.getSavedFileName()); } } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

	}
}
