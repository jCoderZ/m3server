var playlist = 

$(document).ready(function(){
	playlist = new jPlayerPlaylist({
		jPlayer: "#jquery_jplayer_1",
		cssSelectorAncestor: "#jp_container_1"
	}, [
	], {
		swfPath: "jQuery.jPlayer.2.1.0",
		supplied: "oga, mp3",
		size: {
	        width: "320px",
	        height: "320px"
	      }
	});

	$("#jplayer_inspector_1").jPlayerInspector({jPlayer:$("#jquery_jplayer_1")});

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
    var jqxhr = $.getJSON("library/search?term=" + term, function(json) {
		$('#searchresultlist').empty();
		jQuery.each(json.mp3, function(i, val) {
			$('#searchresultlist').append($('<li>').append($('<a/>', {
			    'href': 'library/browse/' + val['path'],
			    'class': 'browselink',
			    'text': val.name,
			    'click': function(event) {
					event.preventDefault();
			    	playlist.add({
			    		  title: val.title,
			    		  artist: val.artist,
			    		  mp3:"library/browse/" + val['path'],
			    		  poster: "library/browse/" + val['path'] + "/cover"
			    		});
			    	return false;
			    }
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
	
    var jqxhr = $.getJSON("library/browse/" + p, function(json) {
		$('#browseresultlist').empty();
		// show ".." only when we are not already at the top
		if (p != null && p != "") {
			$('#browseresultlist').append($('<li>').append($('<a/>', {
			    'href': 'library/browse/' + p + '/../',
			    'class': 'browselink',
			    'text': '..',
			    'click': function(event) {
					event.preventDefault();
					var path = this.href.replace(/^.*\/library\/browse/, '');
					browse(path);
			    	return false;
			    }
			}).append($('<img/>', {
				'src': 'library/browse/' + p + '/../cover'
			}))));
		}
    
		jQuery.each(json, function(i, val) {
			segment = val.name;
			if (p != null && p != "") {
				segment = p + '/' + encodeURIComponent(val.name);
			}
			if (val.name.endsWith(".mp3")) {
				$('#browseresultlist').append($('<li>').append($('<a/>', {
				    'href': 'library/browse/' + segment,
				    'class': 'browselink',
				    'text': val.name,
				    'click': function(event) {
						event.preventDefault();
				    	playlist.add({
				    		  title: val.title,
				    		  artist: val.artist,
				    		  mp3:"library/browse/" + segment,
				    		  poster: "library/browse/" + segment + "/cover"
				    		});
				    	return false;
				    }
				}).append($('<img/>', {
					'src': 'library/browse/' + segment + '/cover'
				}))));
			}
			else {
				$('#browseresultlist').append($('<li>').append($('<a/>', {
				    'href': 'library/browse/' + segment,
				    'class': 'browselink',
				    'text': val.name,
				    'click': function(event) {
						event.preventDefault();
						var path = this.href.replace(/^.*\/library\/browse/, '');
						browse(path);
				    	return false;
				    }
				}).append($('<img/>', {
					'src': 'library/browse/' + segment + '/cover'
				}))));
			}
        });
		$('#browseresultlist').listview("refresh");
    }).error(function() { alert("browse error: '" + path + "'"); });
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

String.prototype.endsWith = function(pattern) {
    var d = this.length - pattern.length;
    return d >= 0 && this.lastIndexOf(pattern) === d;
};
