package org.jcoderz.m3dditiez.m3server.protocol.rest;

import javax.inject.Inject;

import org.jboss.weld.environment.se.WeldContainer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	@Inject
	private static RestletAdaptor adaptor;

	// private static final WeldContainer wc;

	private WeldContainer weld;

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

		// start a Weld container
//		weld = new WeldOSGi(bundleContext).initialize();

//		adaptor = weld.instance().select(RestletAdaptor.class).get();

		// adaptor = wc.instance().select(RestletAdaptor.class).get();
		System.out.println("Starting adaptor=" + adaptor);
//		adaptor.start();
		System.out.println("Starting adaptor=" + adaptor + "...done!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		System.out.println("Stopping adaptor=" + adaptor);
//		adaptor.stop();
		System.out.println("Stopping adaptor=" + adaptor + "...done!");

		// stop the Weld container
//		weld.sh();
	}
}
