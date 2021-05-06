package com.proto.mm.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import com.proto.mm.model.Movie;
import com.proto.mm.model.Poster;
import com.proto.mm.repository.MovieRepository;
import com.proto.mm.repository.PosterRepository;

@Service
public class PosterService {
	@Autowired 
	private ServletContext servletContext; 
	
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

	public ModelMap posterDownload(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        
      String movieTitle = request.getParameter("movieTitle");
      Movie movie = movieRepository.findByMovieTitle(movieTitle);
      BigDecimal movieCode = movie.getMovieCode();
      Poster poster = posterRepository.findByMovieCode(movieCode);  
      String dFile = movieCode + ".png";
      model.addAttribute("poster", poster);
      model.addAttribute("dFile", dFile);
      model.addAttribute("movie", movie);
      
      return model;
	}
}
