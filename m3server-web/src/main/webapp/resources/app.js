$(document).ready(function(){

    $("#search-page").live('pageinit', function() {

        $("#searchform").submit(function(event) {
            event.stopPropagation();
            event.preventDefault();

	        var term = $('#term').val();
	        if (term) {
	        	search(term);
	        }
            return false;
        });
    });

    $("#browse-page").live('pageinit', function() {
    	// show root
		browse("");
	});
});

function search(term) {
    var jqxhr = $.getJSON("rest/library/search?term=" + term, function(json) {
		$('#searchresultlist').empty();
		jQuery.each(json.mp3, function(i, val) {
			$('#searchresultlist').append($('<li>').append($('<a/>', {
			    'href': 'rest/library/browse/' + val['path']
			}).append($('<h3>', {
			    'text': val['title']
			})).append($('<p>', {
			    'text': val['artist'] + ' - ' + val['album']
			})))).listview("refresh");
        });
    }).error(function() {alert("error: '" + term + "'");});
}

function browse(path) {
	p = stripTrailingSlash(path);
	p = stripLeadingSlash(p);
	
    var jqxhr = $.getJSON("rest/library/browse/" + p, function(json) {
		$('#browseresultlist').empty();
		// show ".." only when we are not already at the top
		if (p != null && p != "") {
			$('#browseresultlist').append($('<li>').append($('<a/>', {
			    'href': 'rest/library/browse/' + p + '/../',
			    'class': 'browselink',
			    'text': '..'
			}))).listview("refresh");
		}
    
		jQuery.each(json, function(i, val) {
			segment = val;
			if (p != null && p != "") {
				segment = p + '/' + val;
			}
			$('#browseresultlist').append($('<li>').append($('<a/>', {
			    'href': 'rest/library/browse/' + segment,
			    'class': 'browselink',
			    'text': val
			}))).listview("refresh");
        });
    }).error(function() {alert("error: '" + term + "'");});

    // install onClick handler on all links with class browselink
	$('.browselink').live('click', function(event) {
		event.preventDefault();

		var path = this.href.replace(/^.*\/rest\/library\/browse/, '');
		
		browse(path);

		return false;
	});
}

function stripTrailingSlash(path) {
	result = path;
	if (path.substr(-1) === '/') {
		result = stripTrailingSlash(path.substring(0, path.length - 1));
	}
	return result;
}

function stripLeadingSlash(path) {
	result = path;
	if (path.substr(0, 1) === '/') {
		result = stripLeadingSlash(path.substring(1, path.length));
	}
	return result;
}

