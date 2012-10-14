package org.jcoderz.m3server.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcoderz.m3server.library.AudioFileItem;
import org.jcoderz.m3server.library.FolderItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.MediaLibrary;
import org.jcoderz.m3server.library.Node;
import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.types.DLNADoc;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDN;
import org.teleal.cling.support.connectionmanager.ConnectionManagerService;
import org.teleal.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.teleal.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.teleal.cling.support.contentdirectory.ContentDirectoryException;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.BrowseResult;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.Protocol;
import org.teleal.cling.support.model.ProtocolInfo;
import org.teleal.cling.support.model.ProtocolInfos;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.MusicTrack;
import org.teleal.common.util.MimeType;

/**
 *
 * @author mrumpf
 */
public class UpnpMediaServer extends AbstractContentDirectoryService {

    public static final String MEDIA_SERVER_TYPE = "MediaServer";
    public static final String MEDIA_SERVER_NAME = "m3server";

    private static final Logger logger = Logger.getLogger(UpnpMediaServer.class.getName());
    private static final DIDLObject.Class DIDL_CLASS_OBJECT_CONTAINER = new DIDLObject.Class("object.container");
    private static final Map<Long, Node> idItemMap = new HashMap<>();
    private static long idCounter = 1;
    private static final String ROOT_ID = "0";
    private static final String ROOT_PARENT_ID = "-1";

    public UpnpMediaServer() {
    }

    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag,
            String filter,
            long firstResult, long maxResults,
            SortCriterion[] orderby) throws ContentDirectoryException {
        try {
            MediaLibrary ml = MediaLibrary.getMediaLibrary();
            logger.info("### objectID=" + objectID);
            logger.info("browseFlag=" + browseFlag.toString());
            logger.info("filter=" + filter);
            logger.info("first=" + firstResult + ", max=" + maxResults);
            logger.info("sortCriterion=" + orderby.toString());

            DIDLContent didl = new DIDLContent();
            switch (objectID) {
                case ROOT_ID: {
                    if (BrowseFlag.METADATA.equals(browseFlag)) {
                        Container c = createContainer(Library.getRoot());
                        didl.addContainer(c);
                    } else if (BrowseFlag.DIRECT_CHILDREN.equals(browseFlag)) {
                        for (Node n : Library.getRoot().getChildren()) {
                            Container c = createContainer(n);
                            didl.addContainer(c);
                        }
                    }
                    break;
                }
                case "1": {
                    List<Item> items = ml.browse(null);
                    for (Item item : items) {
                        if (item instanceof AudioFileItem) {
                            AudioFileItem fi = (AudioFileItem) item;
                            System.err.println("AudioFileItem=" + fi);
                            didl.addItem(new MusicTrack(
                                    fi.getPath(), "3",
                                    fi.getTitle(),
                                    "???", fi.getAlbum(), fi.getArtist(),
                                    new Res(
                                    new MimeType(fi.getMimetype().getType(),
                                    fi.getMimetype().getSubtype()),
                                    fi.getSize(),
                                    fi.getLengthString(),
                                    fi.getSize(), fi.getUrl())));

                        } else if (item instanceof FolderItem) {
                            FolderItem fi = (FolderItem) item;
                            System.err.println("FolderItem=" + fi);
                            Container c = new Container("10", "1", fi.getName(), MEDIA_SERVER_NAME, DIDL_CLASS_OBJECT_CONTAINER, 0);
                            didl.addContainer(c);
                        } else {
                            // TODO
                        }
                    }
                    break;
                }
                case "2": {
                    break;
                }
                case "3": {
                    break;
                }
                default: {
                    List<Item> items = ml.browse(objectID);
                }
            }
            int count = didl.getItems().size() + didl.getContainers().size();
            return new BrowseResult(new DIDLParser().generate(didl), count, count);
        } catch (Exception ex) {
            throw new ContentDirectoryException(
                    ContentDirectoryErrorCode.CANNOT_PROCESS,
                    ex.toString());
        }
    }

    /**
     * This method returns information on the top-level folder, containing all
     * the different RootFolder instances.
     *
     * <DIDL-Lite xmlns="urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/"
     * xmlns:dc="http://purl.org/dc/elements/1.1/"
     * xmlns:upnp="urn:schemas-upnp-org:metadata-1-0/upnp/"> <container id="0"
     * parentID="-1" restricted="1" childCount="3"> <dc:title>Root</dc:title>
     * <upnp:class>object.container</upnp:class> </container> </DIDL-Lite>
     */
    private Container createContainer(Node item) {
        Container c = new Container();
        if (item.getParent() == null) {
            c.setId(ROOT_ID);
            c.setParentID(ROOT_PARENT_ID);
        } else {
            c.setId("" + idCounter);
            idCounter++;
            idItemMap.put(idCounter, item);
        }

        c.setChildCount(item.getChildCount());
        c.setRestricted(true);
        c.setTitle(item.getItem().getName());
        c.setCreator(MEDIA_SERVER_NAME);
        c.setClazz(DIDL_CLASS_OBJECT_CONTAINER);
        return c;
    }

    @Override
    public BrowseResult search(String containerId,
            String searchCriteria, String filter,
            long firstResult, long maxResults,
            SortCriterion[] orderBy) throws ContentDirectoryException {
        logger.log(Level.INFO, "searchCriteria={0}", searchCriteria);
        logger.log(Level.INFO, "filter={0}", filter);
        logger.log(Level.INFO, "first={0}, max={1}", new Object[]{firstResult, maxResults});
        logger.log(Level.INFO, "sortCriterion={0}", orderBy);
        // You can override this method to implement searching!
        return super.search(containerId, searchCriteria, filter, firstResult, maxResults, orderBy);
    }

    public static LocalDevice createDevice() {
        LocalService<AbstractContentDirectoryService> contentDirectoryService =
                new AnnotationLocalServiceBinder().read(AbstractContentDirectoryService.class);
        contentDirectoryService.setManager(
                new DefaultServiceManager<AbstractContentDirectoryService>(contentDirectoryService, null) {
                    @Override
                    protected AbstractContentDirectoryService createServiceInstance() throws Exception {
                        return new UpnpMediaServer();
                    }
                });
        LocalService<ConnectionManagerService> connectionManagerService =
                new AnnotationLocalServiceBinder().read(ConnectionManagerService.class);
        final ProtocolInfos sourceProtocols =
                new ProtocolInfos(
                new ProtocolInfo(
                Protocol.HTTP_GET,
                ProtocolInfo.WILDCARD,
                "audio/mpeg",
                "DLNA.ORG_PN=MP3;DLNA.ORG_OP=01"));

        connectionManagerService.setManager(
                new DefaultServiceManager<ConnectionManagerService>(connectionManagerService, null) {
                    @Override
                    protected ConnectionManagerService createServiceInstance() throws Exception {
                        return new ConnectionManagerService(sourceProtocols, null);
                    }
                });

        // TODO: Add icon
        LocalDevice device = null;


        try {
            device = new LocalDevice(
                    new DeviceIdentity(UDN.uniqueSystemIdentifier(MEDIA_SERVER_NAME)),
                    new UDADeviceType(MEDIA_SERVER_TYPE),
                    new DeviceDetails(MEDIA_SERVER_NAME,
                    new ManufacturerDetails("jCoderZ.org", "http://www.jcoderz.org"),
                    new ModelDetails(MEDIA_SERVER_NAME, "A UPnP/DLNA Media Server", "0.0.1", ""),
                    new DLNADoc[]{new DLNADoc("DMS-1.50", "M-DMS-1.50")}, null),
                    new LocalService[]{contentDirectoryService, connectionManagerService});
        } catch (ValidationException ex) {
            Logger.getLogger(UpnpMediaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return device;
    }
}
