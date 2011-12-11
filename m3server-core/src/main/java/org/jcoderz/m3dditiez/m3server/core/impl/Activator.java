package org.jcoderz.m3dditiez.m3server.core.impl;

import javax.inject.Inject;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jcoderz.m3dditiez.m3server.provider.ContentProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

	private static BundleContext context;

	@Inject @OSGiService
	LogService log;

	public BundleContext getContext() {
		return context;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		//log.log(LogService.LOG_INFO, "UPNP Activator>>>");
		System.out.println("Core Activator>>>");
//		Weld w = new Weld();
//		wc = w.initialize();
//
//		MediaServerImpl ms = wc.instance().select(MediaServerImpl.class).get();
//		ms.init();
		ServiceReference[] refs = context.getServiceReferences(ContentProvider.class.getName(), null);
		System.out.println("#refs=" + refs);
		System.out.println("<<<Core Activator");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("core stop");
	}
}
