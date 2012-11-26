package org.jcoderz.m3server.renderer;

import java.util.logging.Logger;
import org.jcoderz.m3server.util.Logging;

/**
 * This class represents MPD rendering devices.
 * <ul>
 *   <li><a href="http://mpd.wikia.com/wiki/Protocol_Reference">Protocol Reference</a></li>
 *   <li><a href="http://mpd.wikia.com/wiki/Built-in_HTTP_streaming_part_2">Built-in HTTP streaming part 2</a></li>
 * </ul>
 *
 * @author mrumpf
 *
 */
public class MpdRenderer extends AbstractRenderer {

     private static final Logger logger = Logging.getLogger(MpdRenderer.class);
   /**
     * Constructor.
     *
     * @param name the name of the renderer
     */
    public MpdRenderer(String name) {
        super(name, RendererType.MPD);
    }

    @Override
    public void play(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String info() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void play() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Position position() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
