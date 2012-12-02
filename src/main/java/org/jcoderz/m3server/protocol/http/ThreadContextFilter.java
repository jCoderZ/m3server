package org.jcoderz.m3server.protocol.http;

import org.jcoderz.m3server.util.ClientContext;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3server.util.ThreadContext;

/**
 * This filter stores information on the client in a thread local.
 *
 * @author mrumpf
 */
public class ThreadContextFilter implements Filter {

    private static final Logger logger = Logging.getLogger(ThreadContextFilter.class);

    @Override
    public void init(FilterConfig fc) throws ServletException {
        logger.fine("ThreadContextFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        logger.entering(ThreadContextFilter.class.getName(), "doFilter");

        InetAddress addr = InetAddress.getByName(request.getRemoteAddr());
        ClientContext tc = new ClientContext(request.getRemoteAddr(), addr.getHostName(), request.getRemotePort());
        logger.log(Level.FINER, "ThreadContext: {0}", tc);
        try {
            ThreadContext.setContext(tc);
            fc.doFilter(request, response);
        } finally {
            ThreadContext.clearContext();
            logger.exiting(ThreadContextFilter.class.getName(), "doFilter");
        }
    }

    @Override
    public void destroy() {
        logger.fine("ThreadContextFilter destroyed");
    }
}
