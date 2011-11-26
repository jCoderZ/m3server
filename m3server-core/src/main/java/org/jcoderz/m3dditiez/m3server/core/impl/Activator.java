package org.jcoderz.m3dditiez.m3server.core.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

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
		System.out.println("xxx");

//		Weld w = new Weld();
//		wc = w.initialize();
//
//		MediaServerImpl ms = wc.instance().select(MediaServerImpl.class).get();
//		ms.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("xxx");
	}
}
