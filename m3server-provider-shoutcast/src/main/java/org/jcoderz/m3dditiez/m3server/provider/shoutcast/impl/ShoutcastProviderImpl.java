package org.jcoderz.m3dditiez.m3server.provider.shoutcast.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.jboss.weld.environment.osgi.api.annotation.Publish;
import org.jcoderz.m3dditiez.m3server.provider.shoutcast.ShoutcastProvider;
import org.slf4j.Logger;

@Publish
public class ShoutcastProviderImpl implements ShoutcastProvider {

	@Inject
	private Logger log;

	@Override
	public String getName() {
		return "shoutcast";
	}

	@Override
	public List<String> getChildren(String path) {
		log.debug("path=" + path);
		return Collections.EMPTY_LIST;
	}

}
