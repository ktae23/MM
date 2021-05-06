
//영화 자세히 보기
$(document).ready(function(){
	$("button[name=movieAllBtn]").click(function(){ 
		var index = $(this).attr("id");
		var movieTitle=$('.movieTitle').eq(index).text();
		$.get('movieAllDetail',
				{			   
					movieTitle:movieTitle
				},
				function(data){
					window.open('movieAllDetail?movieTitle='+movieTitle,'_blank', 'toolbar=yes,scrollbars=yes,resizable=yes,top=50,left=500,width=500,height=500');
				});
		
	});
});