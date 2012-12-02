package org.jcoderz.m3server.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class stores information about the client. For HTTP requests this is the
 * client address, the client host name and the port from where the request is
 * initiated.
 *
 * @author mrumpf
 */
public class ClientContext {

    private String addr;
    private String host;
    private int port;

    public ClientContext(String addr, String host, int port) {
        this.addr = addr;
        this.host = host;
        this.port = port;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
