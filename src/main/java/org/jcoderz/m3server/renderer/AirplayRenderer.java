package org.jcoderz.m3server.renderer;

import java.net.URL;

/**
 * This class represents Airplay rendering devices.
 *
 * @author mrumpf
 */
public class AirplayRenderer extends AbstractRenderer {

    /**
     * Constructor.
     *
     * @param name the name of the renderer
     */
    public AirplayRenderer(String name) {
        super(name, RendererType.AIRPLAY);
    }

    @Override
    public void play(URL url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
