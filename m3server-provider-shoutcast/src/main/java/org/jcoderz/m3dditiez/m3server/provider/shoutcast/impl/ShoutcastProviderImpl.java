package org.jcoderz.m3dditiez.m3server.provider.shoutcast.impl;

import java.util.Collections;
import java.util.List;

import org.jboss.weld.environment.osgi.api.annotation.Publish;
import org.jcoderz.m3dditiez.m3server.provider.shoutcast.ShoutcastProvider;

@Publish
public class ShoutcastProviderImpl implements ShoutcastProvider {

	@Override
	public String getName() {
		return "shoutcast";
	}

	@Override
	public List<String> getChildren(String path) {
		return Collections.EMPTY_LIST;
	}

}
