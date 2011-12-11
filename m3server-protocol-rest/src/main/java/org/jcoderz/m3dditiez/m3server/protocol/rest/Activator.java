package org.jcoderz.m3dditiez.m3server.protocol.rest;

import javax.inject.Inject;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

	private static BundleContext context;

	@Inject @OSGiService
	private LogService log;

	@Inject @OSGiService
	private RestletAdaptor adaptor;

	public BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		log.log(LogService.LOG_INFO, "Starting RestletAdaptor...");
		adaptor.start();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		log.log(LogService.LOG_INFO, "Stopping RestletAdaptor...");
		adaptor.stop();
	}
}
