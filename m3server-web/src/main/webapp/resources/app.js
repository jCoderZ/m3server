$(document).ready(function(){
 
   var search = function(){
        var term = $('#term').val();
        $.getJSON("http://localhost:8080/m3server-web/rest/search/key/" + term, function(json) {
            if (json != "Nothing found."){
            }
            else {
               $('#result').html("" + json); 
            }
        });
        return false;
   }

   $('#search').click(search);
});
