package org.jcoderz.m3server.protocol;

import java.util.Properties;

/**
 * The common interface of all protocol adapters.
 *
 * @author mrumpf
 */
public abstract class ProtocolAdapter {

    private Properties properties = null;

    public void init(Properties p) {
        properties = p;
    }

    public String getString(String key) {
        return (String) properties.get(key);
    }

    public Integer getInteger(String key) {
        return (Integer) properties.get(key);
    }

    public abstract String getName();

    public abstract void startup();

    public abstract void shutdown();
}
