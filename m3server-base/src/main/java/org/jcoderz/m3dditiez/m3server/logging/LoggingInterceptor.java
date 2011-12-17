package org.jcoderz.m3dditiez.m3server.logging;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

/**
 * xxx
 *
 * @author Michael Rumpf
 *
 */
@Interceptor @Logging
public class LoggingInterceptor {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoggingInterceptor.class);

	@AroundInvoke
	public Object log(InvocationContext ctx) throws Exception {
		log.debug(">>> Entering " + ctx.getTarget().getClass().getName() + "." + ctx.getMethod().getName());

		Object result =  ctx.proceed();
		
		log.debug("<<< Exiting " + ctx.getTarget().getClass().getName() + "." + ctx.getMethod().getName());
		return result;
	}
}
