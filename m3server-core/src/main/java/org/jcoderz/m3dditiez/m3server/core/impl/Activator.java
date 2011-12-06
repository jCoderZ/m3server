package org.jcoderz.m3dditiez.m3server.core.impl;

import org.jcoderz.m3dditiez.m3server.provider.ContentProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	//private WeldContainer wc;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("core start");

//		Weld w = new Weld();
//		wc = w.initialize();
//
//		MediaServerImpl ms = wc.instance().select(MediaServerImpl.class).get();
//		ms.init();
		ServiceReference[] refs = context.getServiceReferences(ContentProvider.class.getName(), null);
		System.out.println("#refs=" + refs.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("core stop");
	}
}
