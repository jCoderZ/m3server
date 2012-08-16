var path = "";

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
    var jqxhr = $.getJSON("http://localhost:8080/m3server-web/rest/library/search?term=" + term, function(json) {
		$('#searchresultlist').empty();
		jQuery.each(json.mp3, function(i, val) {
			/*
		    // TODO: because listview("refresh") takes too much time, we apply the styles directly 
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
			    'href': 'rest/library/browse/' + val['path'],
			    'text': '' + val['artist'] + ' - ' + val['album'] + ' - ' + val['title']
			}))).append($('<span>', {
			    'class': 'ui-icon ui-icon-arrow-r ui-icon-shadow',
			    'html': '&nbsp;'
			}))));
			*/
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
    var jqxhr = $.getJSON("http://localhost:8080/m3server-web/rest/library/browse" + path, function(json) {
		$('#browseresultlist').empty();
		jQuery.each(json, function(i, val) {
			/*
		    // TODO: because listview("refresh") takes too much time, we apply the styles directly
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
			    'href': 'rest/library/browse/' + path + '/' + val,
			    'class': 'browselink',
			    'text': val
			}))).append($('<span>', {
			    'class': 'ui-icon ui-icon-arrow-r ui-icon-shadow',
			    'html': '&nbsp;'
			}))));
			*/
			$('#browseresultlist').append($('<li>').append($('<a/>', {
			    'href': 'rest/library/browse/' + path + '/' + val,
			    'class': 'browselink',
			    'text': val
			}))).listview("refresh");
        });
    }).error(function() {alert("error: '" + term + "'");});

	$('.browselink').live('click', function(event) {
		event.preventDefault();

		var path = this.href.replace(/^.*\/rest\/library\/browse/, '');
		
		browse(path);

		return false;
	});
}
