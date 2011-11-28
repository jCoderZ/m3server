package org.jcoderz.m3dditiez.m3server.protocol.rest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static RestletAdaptor adaptor;

//	private static final WeldContainer wc;

	static {
//		Weld w = new Weld();
//		wc = w.initialize();
	}

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
//		adaptor = wc.instance().select(RestletAdaptor.class).get();
//		adaptor.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

//		adaptor.stop();
    }
}
