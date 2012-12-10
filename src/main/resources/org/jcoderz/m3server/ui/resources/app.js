var playlist;
$(document).ready(function() {
    /*
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

     $("#jplayer_inspector_1").jPlayerInspector({jPlayer: $("#jquery_jplayer_1")});
     */
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
        renderer();
    });
});

function renderer() {
    var jqxhr = $.getJSON("/m3server/rest/renderer/list", function(json) {
        $('#select-renderer').empty();
        jQuery.each(json, function(i, val) {
            $('#select-renderer').append($('<option/>').append(val.name));
        });
        $('#select-renderer').selectmenu("refresh", true);
    }).error(function() {
        alert("error: '" + term + "'");
    });
}

function search(term) {
    var jqxhr = $.getJSON("rest/library/search?term=" + term, function(json) {
        $('#searchresultlist').empty();
        jQuery.each(json.mp3, function(i, val) {
            $('#searchresultlist').append($('<li>').append($('<a/>', {
                'href': 'rest/library/browse/' + val['path'],
                'class': 'browselink',
                'text': val.name,
                'click': function(event) {
                    event.preventDefault();
                    /*
                     playlist.add({
                     title: val.title,
                     artist: val.artist,
                     mp3: "rest/library/browse/" + val['path'],
                     poster: "rest/library/browse/" + val['path'] + "/cover"
                     });
                     */
                    return false;
                }
            }).append($('<h3>', {
                'text': val['title']
            })).append($('<p>', {
                'text': val['artist'] + ' - ' + val['album']
            }))));
        });
        $('#searchresultlist').listview("refresh");
    }).error(function() {
        alert("error: '" + term + "'");
    });
}

function browse(path) {
    p = stripTrailingSlash(path);
    p = stripLeadingSlash(p);

    var jqxhr = $.getJSON("/m3server/rest/library/browse/" + p, function(json) {
        $('#browseresultlist').empty();
        // show ".." only when we are not already at the top
        if (p != null && p != "") {
            $('#browseresultlist').append($('<li>').append($('<a/>', {
                'href': '/m3server/rest/library/browse/' + p + '/../',
                'class': 'browselink',
                'text': '..',
                'click': function(event) {
                    event.preventDefault();
                    var path = this.href.replace(/^.*\/rest\/library\/browse/, '');
                    browse(path);
                    return false;
                }
            })));
        }

        jQuery.each(json.childrenNames, function(i, val) {
            segment = val;
            if (p != null && p != "") {
                segment = p + '/' + encodeURIComponent(val);
            }
            if (val.endsWith(".mp3")) {
                $('#browseresultlist').append($('<li>').append($('<a/>', {
                    'href': '/m3server/rest/library/browse/' + segment,
                    'class': 'browselink',
                    'text': val,
                    'click': function(event) {
                        event.preventDefault();
                        var path = this.href.replace(/^.*\/rest\/library\/browse/, '');
                        var renderer = $('#select-renderer').val();
                        play(renderer, path);
                        return false;
                    }
                })));
            }
            else {
                $('#browseresultlist').append($('<li>').append($('<a/>', {
                    'href': '/m3server/rest/library/browse/' + segment,
                    'class': 'browselink',
                    'text': val,
                    'click': function(event) {
                        event.preventDefault();
                        var path = this.href.replace(/^.*\/rest\/library\/browse/, '');
                        browse(path);
                        return false;
                    }
                })));
            }
        });
        $('#browseresultlist').listview("refresh");
    }).error(function() {
        alert("browse error: '" + path + "'");
    });
}

function play(renderer, path) {
    var jqxhr = $.post("/m3server/rest/renderer/" + renderer + "/playpath" + path, function() {
// TODO: Highlight line
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

String.prototype.endsWith = function(pattern) {
    var d = this.length - pattern.length;
    return d >= 0 && this.lastIndexOf(pattern) === d;
};
