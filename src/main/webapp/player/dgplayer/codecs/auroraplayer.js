UNKNOWN_ART = '/dgplayer/resources/fallback_album_art.png';

function DGAuroraPlayer(player, DGPlayer) {
    this.player = player;
    this.ui = DGPlayer;
    
    DGPlayer.seekTime = 0;
    DGPlayer.duration = 0;
    DGPlayer.bufferProgress = 0;

    var onplay, onpause, onvolume, onformat, onbuffer, onprogress, onduration, onmetadata;
    
    DGPlayer.on('play', onplay = function() {
        player.play();
        DGPlayer.state = 'playing';
    });
    
    DGPlayer.on('pause', onpause = function() {
        player.pause();
        DGPlayer.state = 'paused';
    });
    
    DGPlayer.on('volume', onvolume = function(value) {
        player.volume = value;
    });
    
    player.on('buffer', onbuffer = function(percent) {
        DGPlayer.bufferProgress = percent;
    });
    
    player.on('format', onformat = function(format) {
        if (WebKitAudioDevice.supported && format.sampleRate !== window._sampleRate)
            alert("This song's sample rate does not match your hardware's native sample rate. This may sound strange...");
    });
    
    player.on('progress', onprogress = function(time) {
        DGPlayer.seekTime = time;
    });
    
    player.on('duration', onduration = function(duration) {
        DGPlayer.duration = duration;
    });
    
    function find(data, keys) {
        for (var i = 0; i < keys.length; i++) {
            var val = data[keys[i]];
            if (val) return val;
        }
        
        return 'Unknown ' + keys[0];
    }
    
    player.on('metadata', onmetadata = function(data) {
        DGPlayer.songTitle = find(data, ['Title', 'TITLE', 'Title/Songname/Content description']);
        DGPlayer.songArtist = find(data, ['Artist', 'Album Artist', 'ARTIST', 'Lead artist/Lead performer/Soloist/Performing group']);
        
        var buf = data['Cover Art'];
        if (buf) {
            DGPlayer.coverArt = buf.toBlobURL();
        } else {
            DGPlayer.coverArt = UNKNOWN_ART;
        }
    });
    
    var originalDescription = DGPlayer.fileDescription;
    player.on('error', onerror = function(e) {
        // reset state
        DGPlayer.state = 'paused';
        DGPlayer.duration = 0;
        DGPlayer.bufferProgress = 0;
        DGPlayer.seekTime = 0;
        DGPlayer.coverArt = UNKNOWN_ART;
        DGPlayer.songTitle = 'Unknown Title';
        DGPlayer.songArtist = 'Unknown Artist';
        
        DGPlayer.fileDescription = "Hmm. I don't recognize that format. Try another.";
        setTimeout(function() {
            DGPlayer.fileDescription = originalDescription;
        }, 3000);
    });
    
    player.volume = DGPlayer.volume;
    player.play();
    DGPlayer.state = 'playing';
    
    this.disconnect = function() {
        if (player) player.stop();
        
        DGPlayer.off('play', onplay);
        DGPlayer.off('pause', onpause);
        DGPlayer.off('volume', onvolume);
        player.off('buffer', onbuffer);
        player.off('format', onformat);
        player.off('progress', onprogress);
        player.off('duration', onduration);
        player.off('metadata', onmetadata);
    }
}