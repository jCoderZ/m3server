package org.jcoderz.m3dditiez.m3server.provider.filesystem.impl;

import java.util.Collections;
import java.util.List;

import org.jcoderz.m3dditiez.m3server.provider.filesystem.FilesystemProvider;

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
