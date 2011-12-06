package org.jcoderz.m3dditiez.m3server.provider.shoutcast;

import org.jcoderz.m3dditiez.m3server.provider.ContentProvider;
import org.jcoderz.m3dditiez.m3server.provider.shoutcast.impl.ShoutcastProviderImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static ServiceRegistration registration;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("shoutcast start");
		Activator.context = bundleContext;
		
		ShoutcastProvider fp = new ShoutcastProviderImpl();
		Activator.registration = context.registerService(ContentProvider.class.getName(), fp, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("shoutcast stop");
		Activator.context = null;
		
		Activator.registration.unregister();
	}

}
