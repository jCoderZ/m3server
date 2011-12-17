package org.jcoderz.m3dditiez.m3server.logging;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;


/**
 * This logger factory creates a logger in the context of the class to where it gets injected.
 *
 * @author Michael Rumpf
 *
 */
public class LoggerFactory {

	@Produces
	public Logger createLogger(InjectionPoint injectionPoint) {
		String name = injectionPoint.getMember().getDeclaringClass().getName();
		return org.slf4j.LoggerFactory.getLogger(name);
	}
}
