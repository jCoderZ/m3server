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
			}))));
        });
		$('#searchresultlist').listview("refresh");
    }).error(function() { alert("error: '" + term + "'"); });
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
			    'text': '..',
			    'click': function(event) {
					event.preventDefault();
					var path = this.href.replace(/^.*\/rest\/library\/browse/, '');
					browse(path);
			    	return false;
			    }
			}).append($('<img/>', {
				'src': 'rest/library/browse/' + p + '/../cover'
			}))));
		}
    
		jQuery.each(json, function(i, val) {
			segment = val.name;
			if (p != null && p != "") {
				segment = p + '/' + encodeURIComponent(val.name);
			}
			$('#browseresultlist').append($('<li>').append($('<a/>', {
			    'href': 'rest/library/browse/' + segment,
			    'class': 'browselink',
			    'text': val.name,
			    'click': function(event) {
					event.preventDefault();
					var path = this.href.replace(/^.*\/rest\/library\/browse/, '');
					browse(path);
			    	return false;
			    }
			}).append($('<img/>', {
				'src': 'rest/library/browse/' + segment + '/cover'
			}))));
        });
		$('#browseresultlist').listview("refresh");
    }).error(function() { alert("error: '" + path + "'"); });
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

