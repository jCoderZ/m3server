$(document).ready(function(){

	new jPlayerPlaylist({
		jPlayer: "#jquery_jplayer_1",
		cssSelectorAncestor: "#jp_container_1"
	}, [
		{
			title:"Cro Magnon Man",
			mp3:"http://www.jplayer.org/audio/mp3/TSP-01-Cro_magnon_man.mp3",
		},
		{
			title:"Your Face",
			mp3:"http://www.jplayer.org/audio/mp3/TSP-05-Your_face.mp3",
		},
		{
			title:"Cyber Sonnet",
			mp3:"http://www.jplayer.org/audio/mp3/TSP-07-Cybersonnet.mp3",
		},
		{
			title:"Tempered Song",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-01-Tempered-song.mp3",
		},
		{
			title:"Hidden",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-02-Hidden.mp3",
		},
		{
			title:"Lentement",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-03-Lentement.mp3",
		},
		{
			title:"Lismore",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-04-Lismore.mp3",
		},
		{
			title:"The Separation",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-05-The-separation.mp3",
		},
		{
			title:"Beside Me",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-06-Beside-me.mp3",
		},
		{
			title:"Bubble",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-07-Bubble.mp3",
		},
		{
			title:"Stirring of a Fool",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-08-Stirring-of-a-fool.mp3",
		},
		{
			title:"Partir",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-09-Partir.mp3",
		},
		{
			title:"Thin Ice",
			mp3:"http://www.jplayer.org/audio/mp3/Miaow-10-Thin-ice.mp3",
		}
	], {
		swfPath: "../js",
		supplied: "mp3",
		wmode: "window"
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

