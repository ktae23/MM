package com.proto.mm.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.proto.mm.model.Cart;
import com.proto.mm.model.KakaoPayApproval;
import com.proto.mm.model.Member;
import com.proto.mm.model.Movie;
import com.proto.mm.model.Orders;
import com.proto.mm.repository.CartRepository;
import com.proto.mm.repository.MovieRepository;
import com.proto.mm.repository.OrdersRepository;


@Service
public class OrdersService {

	@Autowired
	private OrdersRepository orderRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	CartService cartService;
	
	
	public Model showOrder(Model model,HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("구매 서비스 호출");
		// 모든 영화 목록 조회 후 Movie List에 저장 후 model에 값 저장 후 반환
		String movie_title = request.getParameter("movieTitle");
		Movie movie = movieRepository.findByMovieTitle(movie_title);
		
		model.addAttribute("movie", movie);
		
		return model;
	}
	
	public Model showOrderList(Model model,HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("구매목록 서비스 호출");
		// 모든 영화 목록 조회 후 Movie List에 저장 후 model에 값 저장 후 반환
		HttpSession session=request.getSession();
		Member member = (Member)session.getAttribute("member");
		BigDecimal memCount = member.getMemCount();
		List<Orders> orders = orderRepository.findByMemCount(memCount, Sort.by(Sort.Direction.ASC, "orderCount"));
		if(orders.size()!=0) {
			model.addAttribute("orders", orders);
			
			List<Movie> orderMovies = findMovieByMovieCode(orders);
			model.addAttribute("movies", orderMovies);

			
			/*for(int i=0;i<orders.size();i++) {
				System.out.println(orders.get(i).toString());
				System.out.println(orderMovies.get(i).toString());
			}*/
		}		
		
		return model;
	}
	
	public List<Movie> findMovieByMovieCode (List<Orders> orders){
		
		List<Movie> orderMovies=new ArrayList<Movie>();
		
		for(Orders order : orders) {
			BigDecimal movieCode = order.getMovieCode();
			Movie movie = new Movie();
			movie = movieRepository.findByMovieCode(movieCode);
			orderMovies.add(movie);
		}
		return orderMovies;
	}

	public void orderInsert(Model model,HttpServletRequest request, HttpServletResponse response) {
		System.out.println("구매 진행 서비스 호출");
		HttpSession session = request.getSession(false);
		Member member = (Member) session.getAttribute("member");
		BigDecimal memCount = member.getMemCount();
		
		Movie movie = (Movie) session.getAttribute("movie");
		BigDecimal movieCode = movie.getMovieCode();
		KakaoPayApproval kakaoPayApproval=(KakaoPayApproval) model.getAttribute("info");
		
		String orderMethod = kakaoPayApproval.getPayment_method_type();
		
		BigDecimal orderCount=null;
		Timestamp orderDate = new Timestamp(System.currentTimeMillis());
		Orders order = new Orders(orderCount, orderMethod, orderDate, memCount, movieCode, "0");
		model.addAttribute("order",order);
		orderRepository.save(order);
		Cart cart = cartRepository.findByMovieCodeAndMemCount(movieCode, memCount);
  	  	cartService.cartDelete(cart);
	}

	public Orders orderCheck(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("이미 구매한 영화인지 확인");
		HttpSession session = request.getSession(false);
		Member member = (Member) session.getAttribute("member");
		BigDecimal memCount = member.getMemCount();
		
		String movie_title = request.getParameter("movieTitle");
		Movie movie = movieRepository.findByMovieTitle(movie_title);
		BigDecimal movieCode = movie.getMovieCode();
		
		return orderRepository.findByMemCountAndMovieCode(memCount, movieCode);
	}

	public void updateOrder(Model model, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("다운로드 서비스 호출");
		HttpSession session = request.getSession(false);
		Member member = (Member) session.getAttribute("member");
		BigDecimal memCount = member.getMemCount();
		
		String movie_title = request.getParameter("movieTitle");
		Movie movie = movieRepository.findByMovieTitle(movie_title);
		BigDecimal movieCode = movie.getMovieCode();
		
		Orders order = orderRepository.findByMemCountAndMovieCode(memCount, movieCode);
		order.setOrderDown("1");
		orderRepository.save(order);	
		
		
		
	}

	public void deleteOrder(Model model, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("환불 서비스 호출");
		HttpSession session = request.getSession(false);
		Member member = (Member) session.getAttribute("member");
		BigDecimal memCount = member.getMemCount();
		
		String movie_title = request.getParameter("movieTitle");
		Movie movie = movieRepository.findByMovieTitle(movie_title);
		BigDecimal movieCode = movie.getMovieCode();
		
		Orders order = orderRepository.findByMemCountAndMovieCode(memCount, movieCode);
		orderRepository.delete(order);
	}
}
