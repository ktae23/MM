//검색 
//$(document).ready(function(){ 
//	$("#searchBtn").click(function(){
//		
//		var movieTitle=$("input[name=movieTitle]").val();
//		if (movieTitle == ""){
//			$.get("movieList",{
//				});
//		}
//		else {
//			$.get("moiveSearch",{
//					movieTitle : movieTitle,
//			});
//		}
//	});
//
//			/*
//			function filter(){
//
//	        var value, name, item, i;
//	
//	        value = document.getElementById("movieList").value.toUpperCase();
//	        item = document.getElementsByClassName("movieTitle");
//	
//	        for(i=0;i<item.length;i++){
//	          name = item[i].getElementsByClassName("searched");
//	          if(name[0].innerHTML.toUpperCase().indexOf(value) > -1){
//	            item[i].style.display = "flex";
//	          }else{
//	            item[i].style.display = "none";
//	          }
//	        }
//      	}
//      }
//      */
//		/*	$.ajax({
//				type: "get",
//				async: true,
//				url: "movieSearch",
//				data: {movieTitle:movieTitle},
//				success: function(data, textStatus){
//					var jsonInfo = JSON.parse(data);
//					displayResult(jsonInfo);
//				},
//				error: function(data, textStatus){
//					alert("에러가 발생했습니다."+data);
//				},
//				complete: function(data, textStatus){
//					alert("작업을 완료했습니다.");
//				}
//			})
//		}
//		
//		function displayResult(jsonInfo){
//			var count = jsonInfo.movieTitle.length;
//			if(count > 0){
//				var html = '';
//				for (var i in jsonInfo.movieTitle) {
//					html += "<a href="javascript:select('"jsonInfo.movieTitle[i]+"')">"
//					+jsonInfo.movieTitle[i]+"</a><br>";
//				}
//				var listView = document.getElementById("suggestList");
//				listView.innerHTML=html;
//				show('suggest');
//			}else{
//				hide('suggest');
//			}
//		}
//		*/
//		
//	
//});

//영화 검색
$(document).ready(function(){
	
	function searchMovie(movies, posters, time) {
		var control = "";
		for(var i=0; i<movies.length; i++){
			control += '<span>'
					+'<div class="col-lg-4 col-md-6 mb-4">'
            		+'<div align="center" class="card h-100">'
            		+'<div class="card-header text-white bg-secondary">'
            		+'<h4 class="movieTitle">'+movies[i].movieTitle+'</h4></div>'
            		+'<div class="card-body">'
            		+'<p> 장르 : ' + movies[i].movieGenre + '</p>'
            		+'<p> 평점 : ' + movies[i].movieRating +'</p>'
            		+'<span>'
                  	+'<img id="poster" alt="'+movies[i].movieTitle+ ' 포스터" src="/poster/'+ posters[i].posterPath + '/'+'"  width="25" height="25%"/>'
                  	+'<div class = "image_overlay image_overlay_blur">'
					+'<div class = "image_movieStory" >'+movies[i].movieStory+'</div>'
					+'<br><br><br>'
		            +'<div> <button class="movieDetailBtn" id="'+i+'" name="movieDetailBtn">자세히 보기</button></div>'
		            +'<br>'
			        +'<div> <button class="cartInsertBtn" id="'+i+'" name="cartInsertBtn">담기</button></div>'
			        +'<br>'
					+'</div>'
					+'</span>'
					+'<br><br>'
					+'<p>'+new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(movies[i].moviePrice)+'</p>'
	                +'<br>'
	                +'</div>'
	                +'</div>'
	                +'</div>'
	                +'</span>';
		}

		setTimeout(function() {
			if(control != ""){
				$(".movie-box").html(control);	
			}

		}, time);

	}

	
	
	$("#searchBtn").click(function(){
		var movieTitle=$("input[name=movieTitle]").val();
		var loc = window.location.href;
		loc = loc.split('/').reverse()[0];
		
		if(movieTitle != "" && movieTitle != null){
			$.get("movieSearch",{
				movieTitle : movieTitle,
				location : loc
			}, function(data) {
				$("input[name=movieTitle]").val('');
				var obj=JSON.parse(data);
				var movies = obj.movies;
				var posters = obj.posters;
			
				if (movies.length != 0){
					if(loc != "home"){
						location.href="home";
					}else
						searchMovie(movies, posters, 15);
				}else{
					Swal.fire({
						  title: "찾으시는 영화가 없습니다.",
						  icon: 'warning',
						  showConfirmButton: false,
						  timer: 2000
					})
				}
			});
		}
	});
	
	$("input[name=movieTitle]").on("keyup", function(e) {
		if (e.which == 13) {
			var movieTitle=$("input[name=movieTitle]").val();
			var loc = window.location.href;
			loc = loc.split('/').reverse()[0];
			
			if(movieTitle != "" && movieTitle != null){
				$.get("movieSearch",{
					movieTitle : movieTitle,
					location : loc
				}, function(data) {
					$("input[name=movieTitle]").val('');
					var obj=JSON.parse(data);
					var movies = obj.movies;
					var posters = obj.posters;
					if (movies.length != 0){
						if(loc != "home"){
							location.href="home";
						}else
							searchMovie(movies, posters, 15);
					}else{
						Swal.fire({
							  title: "찾으시는 영화가 없습니다.",
							  icon: 'warning',
							  showConfirmButton: false,
							  timer: 2000
						})
					}
				
					
				});
			}
			}
		});

});