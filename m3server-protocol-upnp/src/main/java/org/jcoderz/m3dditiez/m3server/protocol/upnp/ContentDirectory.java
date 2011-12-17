package org.jcoderz.m3dditiez.m3server.protocol.upnp;

import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import org.fourthline.cling.model.types.ErrorCode;
import org.fourthline.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryException;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.jcoderz.m3dditiez.m3server.logging.Logging;
import org.seamless.util.io.IO;
import org.slf4j.Logger;

/**
 * 
 * @author Michael Rumpf
 * 
 */
public class ContentDirectory extends AbstractContentDirectoryService {

	// @Inject @OSGiService
	// private MediaServer server;

	@Inject
	private Logger log;

	@Logging
	public BrowseResult browse(String objectID, BrowseFlag browseFlag,
			String filter, long firstResult, long maxResults,
			SortCriterion[] orderby) throws ContentDirectoryException {

		log.debug("################## browse called");
		try {
			if (this != null) {
				List<String> roots = null;
				DIDLContent content = new DIDLContent();
				int i = 1;
				for (String root : roots) {
					System.out.println("roots=" + roots);
					Container c = new Container();
					c.setTitle(root);
					c.setId("" + i);
					c.setParentID("" + 0);
					c.setClazz(new DIDLObject.Class(
							"object.container.storageFolder"));
					content.addContainer(c);
					i++;
				}
				DIDLParser parser = new DIDLParser();
				String result = parser.generate(content);
				System.out.println(result);
				return new BrowseResult(result, 3, 3);
			} else {
				String result = readResource("org/jcoderz/m3dditiez/m3server/browseRootChildren.xml");
				return new BrowseResult(result, 3, 3);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Browse action failed", ex);
			throw new ContentDirectoryException(ErrorCode.ACTION_FAILED,
					ex.toString());
		}
	}

	@Logging
	public BrowseResult search(String containerId, String searchCriteria,
			String filter, long firstResult, long maxResults,
			SortCriterion[] orderBy) throws ContentDirectoryException {
		try {
			log.debug("################## search called");
			return new BrowseResult(
					new DIDLParser().generate(new DIDLContent()), 0, 0);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Search action failed", ex);
			throw new ContentDirectoryException(ErrorCode.ACTION_FAILED,
					ex.toString());
		}
	}

	protected String readResource(String resource) {
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(resource);
			return IO.readLines(is);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {
				//
			}
		}
	}

}
