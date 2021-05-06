// 영화 자세히 보기
$(document).ready(function(){
	
	function insertMovie(movie, poster, time) {
		var control = "";
		var prev = '<span id="prev">'
		prev += $(".movie-box").html();
		prev += '</span>';
		control ='<span>'
				+'<div align="center" class="col-lg-4-detail col-md-6-detail mb-4">'
    			+'<div align="center" class="card h-100-detail">'
    			+'<h3 class="movieTitle"><b>'+movie.movieTitle+'</b></h3>'
    			+'<img id="poster" alt="'+movie.movieTitle+ ' 포스터" src="/poster/'+ poster + '/'+'"  width="25" height="25%"/>'
    			+'<br>'
    			+'<p> 평점 : ' + movie.movieRating +'</p>'
    			+'<p> 장르 : ' + movie.movieGenre + '</p>'
    			+'<p> 감독 : ' + movie.movieDirector +'</p>'
    			+'<p> 주연 : ' + movie.movieActor+'</p>'
    			+'<p> 개봉일 : ' + movie.movieRdate+'</p>'
    			+'<p> 상영시간 : ' + movie.movieRtime+'분'+'</p>'
    			+'<p> 가격 : '+new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(movie.moviePrice)+'</p>'
    			+'<p> 줄거리 : ' + movie.movieStory+'</p>'
    			+'<div> <button class="btn btn-secondary my-2 my-sm-0" id="0" name="cartInsertBtn">장바구니 담기</button></div>'
    			+'<br>'
	        	+'<span style="display:none;">'+prev+'</span>'
	        	+'<div><input class="btn btn-secondary my-2 my-sm-0" type="button" value="이전으로 돌아가기" id="prevMovie"> </div>'
	        	+'</div>'
	        	+'</div>'
	        	+'</div>'
	        	+'</div>'
				+'</span>';

		setTimeout(function() {
			if(control != ""){
				$(".movie-box").html(control);
			}

		}, time);

	}
	
	$(document).on("click",'#prevMovie',function(){
		var prev = $("#prev").html();
		$(".movie-box").html(prev);
	});
	
	$(document).on("click",'button[name=movieDetailBtn]',function(){
		var index = $(this).attr("id");
		
		var movieTitle=$('.movieTitle').eq(index).text();
		
		$.get("movieDetail", {
			movieTitle : movieTitle
		}, function(data) {
			var obj=JSON.parse(data);
			var movie = JSON.parse(obj.movie);
			var poster = obj.poster;
			
			insertMovie(movie, poster, 15);
		});
	});
});