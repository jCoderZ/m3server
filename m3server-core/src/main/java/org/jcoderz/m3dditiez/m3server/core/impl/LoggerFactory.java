package org.jcoderz.m3dditiez.m3server.core.impl;


import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;

/**
 * NOTE: This is a work-around to make the Logger injection point working.
 * This class can hopefully be removed again when the LoggerFactory class from the base bundle works.
 *
 * @author Michael Rumpf
 *
 */
public class LoggerFactory {

	public @Produces Logger createLogger(InjectionPoint injectionPoint) { 
		String name = injectionPoint.getMember().getDeclaringClass().getName();
		return org.slf4j.LoggerFactory.getLogger(name);
	}

}
