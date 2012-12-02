package org.jcoderz.m3server.util;

/**
 * This class holds all the different thread contexts.
 *
 * @author mrumpf
 */
public class ThreadContext {

    private static final ThreadLocal<ClientContext> THREAD_CONTEXT =
            new ThreadLocal<>();

    private ThreadContext() {
        // do not allow instances
    }

    public static ClientContext getContext() {
        return THREAD_CONTEXT.get();
    }

    public static void setContext(ClientContext ctx) {
        THREAD_CONTEXT.set(ctx);
    }

    public static void clearContext() {
        THREAD_CONTEXT.remove();
    }
}
