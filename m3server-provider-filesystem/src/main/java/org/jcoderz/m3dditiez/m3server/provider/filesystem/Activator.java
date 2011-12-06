package org.jcoderz.m3dditiez.m3server.provider.filesystem;

import java.util.Dictionary;
import java.util.Hashtable;

import org.jcoderz.m3dditiez.m3server.provider.ContentProvider;
import org.jcoderz.m3dditiez.m3server.provider.filesystem.impl.FilesystemProviderImpl;
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
		System.out.println("filesystem start");
		Activator.context = bundleContext;

		FilesystemProvider fp = new FilesystemProviderImpl();
		Dictionary<String, String> properties = new Hashtable<String, String>();
		properties.put("name", fp.getName());
		Activator.registration = context.registerService(ContentProvider.class.getName(), fp, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("filesystem stop");
		Activator.context = null;
		
		Activator.registration.unregister();
	}

}
