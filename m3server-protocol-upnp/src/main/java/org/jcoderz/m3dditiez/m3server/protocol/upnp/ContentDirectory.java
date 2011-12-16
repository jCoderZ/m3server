package org.jcoderz.m3dditiez.m3server.protocol.upnp;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.Icon;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.ErrorCode;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.support.avtransport.impl.AVTransportService;
import org.fourthline.cling.support.connectionmanager.ConnectionManagerService;
import org.fourthline.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryException;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.seamless.util.io.IO;

/**
 * NOTE: CDI injection is not working yet as this class is instantiated outside
 * of the CDI container.
 * 
 * @author Michael Rumpf
 * 
 */
public class ContentDirectory extends AbstractContentDirectoryService {

	@Inject @OSGiService
	private MediaServer server;
	
	private static final DeviceType MEDIA_SERVER_DEVICE_TYPE = new UDADeviceType(
			"MediaServer", 3);

	private Logger log = Logger.getLogger(ContentDirectory.class.getName());

	public ContentDirectory() {
	}

	public BrowseResult browse(String objectID, BrowseFlag browseFlag,
			String filter, long firstResult, long maxResults,
			SortCriterion[] orderby) throws ContentDirectoryException {

		System.out.println("################## browse called");
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
					c.setParentID("" +0);
					c.setClazz(new DIDLObject.Class("object.container.storageFolder"));
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
			log.log(Level.SEVERE, "Browse action failed", ex);
			throw new ContentDirectoryException(ErrorCode.ACTION_FAILED,
					ex.toString());
		}
	}

	public BrowseResult search(String containerId, String searchCriteria,
			String filter, long firstResult, long maxResults,
			SortCriterion[] orderBy) throws ContentDirectoryException {
		try {
			return new BrowseResult(
					new DIDLParser().generate(new DIDLContent()), 0, 0);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.log(Level.SEVERE, "Search action failed", ex);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	//@Produces
	//@Logging
	public LocalDevice createDevice() {

		DeviceIdentity identity = new DeviceIdentity(
				UDN.uniqueSystemIdentifier("m3server"));

		DeviceDetails details = new DeviceDetails(
				"m3server",
				new ManufacturerDetails("jCoderz.org"),
				new ModelDetails(
						"MediaServer",
						"An implementation of the UPnP media server specification",
						"v1"));

		AnnotationLocalServiceBinder alsb = new AnnotationLocalServiceBinder();
		LocalService<ConnectionManagerService> connectionManagerService = alsb
				.read(ConnectionManagerService.class);
		connectionManagerService
				.setManager(new DefaultServiceManager<ConnectionManagerService>(
						connectionManagerService,
						ConnectionManagerService.class));

		LocalService<AVTransportService> avTransportService = new AnnotationLocalServiceBinder()
				.read(AVTransportService.class);
		avTransportService
				.setManager(new DefaultServiceManager<AVTransportService>(
						avTransportService, AVTransportService.class));

		LocalService<ContentDirectory> contentDirectory = new AnnotationLocalServiceBinder()
				.read(ContentDirectory.class);
		contentDirectory
				.setManager(new DefaultServiceManager<ContentDirectory>(
						contentDirectory, ContentDirectory.class));

		LocalDevice device = null;
		try {
			Icon icon = new Icon("image/png", 48, 48, 8, 
					ContentDirectory.class.getResource("/images/icon.png"));

			device = new LocalDevice(identity, MEDIA_SERVER_DEVICE_TYPE,
					details, icon, new LocalService[] {
							connectionManagerService, avTransportService,
							contentDirectory });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return device;
	}
}
