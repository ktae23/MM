//검색

$(document).ready(
		function() {
			var me = {};

			var you = {};

			function formatAMPM(date) {
				var hours = date.getHours();
				var minutes = date.getMinutes();
				var ampm = hours >= 12 ? 'PM' : 'AM';
				hours = hours % 12;
				hours = hours ? hours : 12; // the hour '0' should be '12'
				minutes = minutes < 10 ? '0' + minutes : minutes;
				var strTime = hours + ':' + minutes + ' ' + ampm;
				return strTime;
			}

			//-- No use time. It is a javaScript effect.
			function insertChat(who, text, time) {
				var control = "";
				var date = formatAMPM(new Date());

				if (who == "me") {

					control = '<li style="width:70%">'
							+ '<div class="msj macro" style="margin-left:135px! important; margin-bottom:10px! important; margin-top:10px! important;">'
							+ '<div class="text text-r">' + '<br/>' + '<p>'
							+ text + '</p>' + '<p style="text-align:left;"><small>' + date
							+ '</small></p>' + '<br/>' + '</div>' + '</div>'
							+ '</li>';
				} else {
					control = '<li style="width:70%;">'
							+ '<div class="msj-rta macro">'
							+ '<div class="text text-l">' + '<br/>' + '<p>'
							+ text + '</p>' + '<p><small>' + date
							+ '</small></p>' + '<br/>' + '</div>' + '</div>'
							+ '</li>';
				}
				setTimeout(function() {
					$(".message-box").append(control).scrollTop($(".message-box").prop('scrollHeight'));

				}, time);
				/*$(".message-box").scrollTop($(".message-box").prop('scrollHeight'));
				$(".message-box").scrollTop($(".message-box")[0].scrollHeight);*/
			}
			
			//-- No use time. It is a javaScript effect.
			function insertMovie(movies, posters, time) {
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

			function resetChat() {
				$(".message-box").empty();
			}

			$(".mytext").on("keyup", function(e) {
				if (e.which == 13) {
					var text = $(this).val();
					if (text !== "") {
						insertChat("me", text);
						$(this).val('');
					}

					$.get("chat", {
						chat : text
					}, function(data) {
						var obj=JSON.parse(data);
						var text = obj.chatMsg;
						var movies = obj.movies;
						var posters = obj.posters;
						
						if (text !== "") {
							insertChat("mm", text, 15);
							insertMovie(movies, posters, 15);
						}
					});
				}
			});
			
			$("#btn-chat").click(function(){
				var text = $(".mytext").val();
				if (text !== "") {
					insertChat("me", text);
					$(".mytext").val('');
				}

				$.get("chat", {
					chat : text
				}, function(data) {
					var obj=JSON.parse(data);
					var text = obj.chatMsg;
					var movies = obj.movies;
					var posters = obj.posters;
					
					if (text !== "") {
						insertChat("mm", text, 15);
						insertMovie(movies, posters, 15);
					}
				});
			});

			resetChat();
			insertChat("mm", "안녕하세요. 취향에 맞는 영화를 찾아주는 MM입니다.<br><br>'영화 추천', '취향에 맞는 영화' 등을 입력해서 취향에 맞는 영화를 찾아보세요.<br><br>원하는 영화 결과가 없거나 처음부터 다시 시작하고 싶으신 경우 채팅창에 '다시'를 입력 해주세요.", 0);
		});
