package com.proto.mm.service;

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
import com.proto.mm.util.SaveImg;

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
        
        String imgUrl = "/" + poster.getPosterPath();
        System.out.println(imgUrl);
        String tmp = movie.getMovieTitle();
  		String fileName = tmp.replace(" ", "").replace(":", "_");
        
		SaveImg saveImg = new SaveImg();
		String path = "C:/Program Files/MovieMentor";

		try {
			int result = saveImg.saveImgFromUrl(imgUrl, path, fileName); // 성공 시 1 리턴, 오류 시 -1 리턴
			if (result == 1) {
				System.out.println("저장된경로 : " + saveImg.getPath());
				System.out.println("저장된파일이름 : " + saveImg.getSavedFileName());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
