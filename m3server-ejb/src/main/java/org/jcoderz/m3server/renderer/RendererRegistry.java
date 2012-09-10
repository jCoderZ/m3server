package org.jcoderz.m3server.renderer;

import java.util.ArrayList;
import java.util.List;

public class RendererRegistry {

	/**
	 * Discover all renderers on the network which can be used for playing
	 * media.
	 * 
	 * @return a list of renderers
	 */
	public List<Renderer> discoverRenderer() {
		return null;
	}

	/**
	 * Return a list of all known renderers which have been discovered before
	 * but which might not be accessible at the moment.
	 * 
	 * @return a list of renderers
	 */
	public List<Renderer> listRenderer() {
		List<Renderer> result = new ArrayList<Renderer>();
		result.add(new MpdRenderer("Kitchen", "localhost", 1234));
		result.add(new UpnpRenderer("Sony KDL-52W5500", "localhost", 1234));
		result.add(new AirplayRenderer("Kitchen", "localhost", 1234));
		result.add(new MpdRenderer("Kitchen", "localhost", 1234));
		result.add(new MpdRenderer("Kitchen", "localhost", 1234));
		return result;
	}
/*
	public static void main(String[] args) throws Exception {
		// UPnP discovery is asynchronous, we need a callback
		RegistryListener listener = new RegistryListener() {

			public void remoteDeviceDiscoveryStarted(Registry registry,
					RemoteDevice device) {
				System.out.println("Discovery started: "
						+ device.getDisplayString());
			}

			public void remoteDeviceDiscoveryFailed(Registry registry,
					RemoteDevice device, Exception ex) {
				System.out.println("Discovery failed: "
						+ device.getDisplayString() + " => " + ex);
			}

			public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
				System.out.println("Remote device available: "
						+ device.getDisplayString());
			}

			public void remoteDeviceUpdated(Registry registry,
					RemoteDevice device) {
				System.out.println("Remote device updated: "
						+ device.getDisplayString());
			}

			public void remoteDeviceRemoved(Registry registry,
					RemoteDevice device) {
				System.out.println("Remote device removed: "
						+ device.getDisplayString());
			}

			public void localDeviceAdded(Registry registry, LocalDevice device) {
				System.out.println("Local device added: "
						+ device.getDisplayString());
			}

			public void localDeviceRemoved(Registry registry, LocalDevice device) {
				System.out.println("Local device removed: "
						+ device.getDisplayString());
			}

			public void beforeShutdown(Registry registry) {
				System.out
						.println("Before shutdown, the registry has devices: "
								+ registry.getDevices().size());
			}

			public void afterShutdown() {
				System.out.println("Shutdown of registry complete!");

			}
		};
		final UpnpService upnpService = new UpnpServiceImpl(listener);

		// Broadcast a search message for all devices
		upnpService.getControlPoint().search();

		System.out.println("Getting devices...");
		Collection<Device> devs = upnpService.getRegistry().getDevices();
		System.out.println("Printing devices...");
		for (Device dev : devs) {
			System.out.println(dev.getDisplayString());
		}
		Thread.sleep(1000000);
		// upnpService.shutdown();
		System.out.println("...done!");
	}
	*/
}
