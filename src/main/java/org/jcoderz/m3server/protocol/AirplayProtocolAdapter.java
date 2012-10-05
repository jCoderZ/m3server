package org.jcoderz.m3server.protocol;

/**
 * TODO: No yet implemented
 * 
 * @author mrumpf
 */
public class AirplayProtocolAdapter extends ProtocolAdapter {

    @Override
    public void startup() {
        // do nothing
    }

    @Override
    public void shutdown() {
        // do nothing
    }
    
    @Override
    public String getName() {
        return AirplayProtocolAdapter.class.getSimpleName();
    }
}
