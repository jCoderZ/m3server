package org.jcoderz.m3dditiez.m3server.core.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jboss.weld.environment.osgi.api.events.BundleContainerEvents;
import org.jboss.weld.environment.osgi.api.events.Invalid;
import org.jboss.weld.environment.osgi.api.events.Valid;
import org.osgi.service.log.LogService;

@ApplicationScoped
public class App {

	@Inject @OSGiService
	private LogService log;

    public void onStartup(@Observes BundleContainerEvents.BundleContainerInitialized event) {
        log.log(LogService.LOG_INFO, "CDI Container for bundle "
                + event.getBundleContext().getBundle() + " started");
    }

    public void onShutdown(@Observes BundleContainerEvents.BundleContainerShutdown event) {
    	log.log(LogService.LOG_INFO, "CDI Container for bundle "
                + event.getBundleContext().getBundle() + " stopped");
    }

    public void validListen(@Observes Valid valid) {
    	log.log(LogService.LOG_INFO, "CDI Container for bundle "
                + "listen started");
    }

    public void invalidListen(@Observes Invalid invalid) {
    	log.log(LogService.LOG_INFO, "CDI Container for bundle "
                + "listen stopped");
    }
}
