$(document).ready(function(){
 
   var search = function(){
        var term = $('#term').val();
        var jqxhr = $.getJSON("http://localhost:8080/m3server-web/rest/search/key/" + term, function(json) {
               $('#result').html("" + JSON.stringify(json)); 
        }).error(function() {alert("error")});
        return false;
   }

   $('#search').click(search);
});
