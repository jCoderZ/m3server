$(document).ready(function(){
 
   var search = function(){
        var term = $('#term').val();
        var jqxhr = $.getJSON("http://localhost:8080/m3server-web/rest/search/key/" + term, function(json) {
			jQuery.each(json.mp3, function(i, val) {
				//alert(JSON.stringify(val));
	            //$('#result').html("" + JSON.stringify(json)); 
				$('#resultlist').append(
				    $('<li>').append(
				        $('<a>').attr('href',val['path']).append(
				            $('<span>').append(val['artist'] + " - " + val['album'] + " - " + val['title'] )
				            ))); 
	        });
        }).error(function() {alert("error")});
        return false;
   }

   $('#search').click(search);
});
