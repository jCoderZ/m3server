package org.jcoderz.m3dditiez.m3server.provider.shoutcast.impl;

import java.util.Collections;
import java.util.List;

import org.jcoderz.m3dditiez.m3server.provider.shoutcast.ShoutcastProvider;

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
