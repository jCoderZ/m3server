var path = ""

$(document).ready(function(){

    $("#search-page").live('pageinit', function() {

        $("#searchform").submit(function(event) {
            event.stopPropagation();
            event.preventDefault();

	        var term = $('#term').val();
	        if (term) {
		        var jqxhr = $.getJSON("http://localhost:8080/m3server-web/rest/library/search?term=" + term, function(json) {
					jQuery.each(json.mp3, function(i, val) {
					    // because listview("refresh") takes too much time, we apply the styles directly
						$('#searchresultlist').append($('<li>', {
							'data-corners': 'false', 
							'data-shadow': 'false' ,
							'data-iconshadow': 'true', 
							'data-wrapperels': 'div', 
							'data-icon': 'arrow-r', 
							'data-iconpos': 'right' ,
							'data-theme': 'c', 
							'class': 'ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li'
						}).append($('<div>', {
						    'class': 'ui-btn-inner ui-li',
						    'aria-hidden': 'true'
						}).append($('<div>', {
						    'class': 'ui-btn-text'
						}).append($('<a/>', {
						    'href': val['path'],
						    'text': '' + val['artist'] + ' - ' + val['album'] + ' - ' + val['title']
						}))).append($('<span>', {
						    'class': 'ui-icon ui-icon-arrow-r ui-icon-shadow',
						    'html': '&nbsp;'
						}))));
						//.listview("refresh");
			        });
		        }).error(function() {alert("error: '" + term + "'")});
	        }
            return false;
        });
    });

    $("#browse-page").live('pageinit', function() {

 		        var jqxhr = $.getJSON("http://localhost:8080/m3server-web/rest/library/browse" + path, function(json) {
					jQuery.each(json, function(i, val) {
					    // because listview("refresh") takes too much time, we apply the styles directly
						$('#browseresultlist').append($('<li>', {
							'data-corners': 'false', 
							'data-shadow': 'false' ,
							'data-iconshadow': 'true', 
							'data-wrapperels': 'div', 
							'data-icon': 'arrow-r', 
							'data-iconpos': 'right' ,
							'data-theme': 'c', 
							'class': 'ui-btn ui-btn-up-c ui-btn-icon-right ui-li-has-arrow ui-li'
						}).append($('<div>', {
						    'class': 'ui-btn-inner ui-li',
						    'aria-hidden': 'true'
						}).append($('<div>', {
						    'class': 'ui-btn-text'
						}).append($('<a/>', {
						    'href': 'rest/library/browse/' + val,
						    'text': val
						}))).append($('<span>', {
						    'class': 'ui-icon ui-icon-arrow-r ui-icon-shadow',
						    'html': '&nbsp;'
						}))));
						//.listview("refresh");
			        });
		        }).error(function() {alert("error: '" + term + "'")});
    });
});
