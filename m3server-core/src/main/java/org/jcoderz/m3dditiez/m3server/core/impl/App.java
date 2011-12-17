package org.jcoderz.m3dditiez.m3server.core.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.weld.environment.osgi.api.events.BundleContainerEvents;
import org.jboss.weld.environment.osgi.api.events.Invalid;
import org.jboss.weld.environment.osgi.api.events.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class App {

	private static final Logger log = LoggerFactory.getLogger(App.class);

    public void onStartup(@Observes BundleContainerEvents.BundleContainerInitialized event) {
        log.info("CDI Container for bundle "
                + event.getBundleContext().getBundle() + " started");
    }

    public void onShutdown(@Observes BundleContainerEvents.BundleContainerShutdown event) {
    	log.info("CDI Container for bundle "
                + event.getBundleContext().getBundle() + " stopped");
    }

    public void validListen(@Observes Valid valid) {
    	log.info("CDI Container for bundle "
                + "listen started");
    }

    public void invalidListen(@Observes Invalid invalid) {
    	log.info("CDI Container for bundle "
                + "listen stopped");
    }
}
