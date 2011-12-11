package org.jcoderz.m3dditiez.m3server.provider.filesystem.impl;

import java.util.Collections;
import java.util.List;

import org.jboss.weld.environment.osgi.api.annotation.Publish;
import org.jcoderz.m3dditiez.m3server.provider.filesystem.FilesystemProvider;

@Publish
public class FilesystemProviderImpl implements FilesystemProvider {

	@Override
	public String getName() {
		return "filesystem";
	}

	@Override
	public List<String> getChildren(String path) {
		return Collections.EMPTY_LIST;
	}

}
