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
import org.teleal.cling.support.model.PersonWithRole;
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
    private static final Map<Long, UpnpContainer> idUpnpObjectMap = new HashMap<>();
    private static long idCounter = 0;
    private static final Long ROOT_ID = 0L;
    private static final Long ROOT_PARENT_ID = -1L;

    public UpnpMediaServer() {
    }

    public static long getNextId() {
        return ++idCounter;
    }

    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag,
            String filter,
            long firstResult, long maxResults,
            SortCriterion[] orderby) throws ContentDirectoryException {
        long id = Long.valueOf(objectID).longValue();
        try {
            MediaLibrary ml = MediaLibrary.getMediaLibrary();
            logger.info("### objectID=" + objectID);
            logger.info("browseFlag=" + browseFlag.toString());
            logger.info("filter=" + filter);
            logger.info("first=" + firstResult + ", max=" + maxResults);
            logger.info("sortCriterion=" + orderby.toString());
            DIDLContent didl = new DIDLContent();

            if (id == ROOT_ID) {
                didl = new DIDLContent();
                if (BrowseFlag.METADATA.equals(browseFlag)) {
                    Container c = createDidlContainer(id, ROOT_PARENT_ID, Library.getRoot());
                    didl.addContainer(c);
                } else if (BrowseFlag.DIRECT_CHILDREN.equals(browseFlag)) {
                    for (Item i : Library.getRoot().getChildren()) {
                        Long nextId = getNextId();
                        Container didlContainer = createDidlContainer(nextId, ROOT_ID, i);
                        idUpnpObjectMap.put(nextId, new UpnpContainer(nextId, didlContainer, i));
                        didl.addContainer(didlContainer);
                    }
                }
            } else if (id > ROOT_ID) {
                UpnpContainer container2Browse = idUpnpObjectMap.get(id);
                if (container2Browse != null) {
                    Item i = container2Browse.getItem();
                    if (i instanceof FolderItem) {
                        FolderItem fi = (FolderItem) i;
                        List<Item> children = fi.getChildren();
                        for (Item c : children) {
                            Long nextId = getNextId();
                            if (FolderItem.class.isAssignableFrom(c.getClass())) {
                                FolderItem f = (FolderItem) c;
                                Container didlContainer = createDidlContainer(nextId, id, f);
                                didl.addContainer(didlContainer);
                                idUpnpObjectMap.put(nextId, new UpnpContainer(nextId, didlContainer, c));
                            } else if (AudioFileItem.class.isAssignableFrom(c.getClass())) {
                                AudioFileItem af = (AudioFileItem) c;
                                org.teleal.cling.support.model.item.Item didlItem = createDidlItem(nextId, id, af);
                                didl.addItem(didlItem);
                                idUpnpObjectMap.put(nextId, new UpnpContainer(nextId, didlItem, c));
                            } else {
                                // TODO: ???
                            }
                        }
                    } else {
                        // TODO: handle browse on FileItem
                        // is it poosible to get a "browse" request on a file? Maybe file details?
                    }
                } else {
                    // throw new Exception("Unknown ID");
                }
            }
            return new BrowseResult(new DIDLParser().generate(didl), 1L, 1L);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "An exception occured during browsing of folder " + id, ex);
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
    private Container createDidlContainer(long nextId, long parentId, Item item) {
        Container c = new Container();
        c.setId("" + nextId);
        c.setParentID("" + parentId);
        c.setChildCount(0 /*item.getChildCount()*/);
        c.setRestricted(true);
        c.setTitle(item.getDisplayName());
        c.setCreator(MEDIA_SERVER_NAME);
        c.setClazz(DIDL_CLASS_OBJECT_CONTAINER);
        return c;
    }

    private org.teleal.cling.support.model.item.Item createDidlItem(long nextId, long parentId, AudioFileItem item) {
        MimeType mimeType = new MimeType("audio", "mpeg");
        PersonWithRole artist = new PersonWithRole(item.getArtist(), "Performer");
        org.teleal.cling.support.model.item.Item result = new MusicTrack(
                "" + nextId, "" + parentId,
                item.getTitle(),
                item.getArtist(),
                item.getAlbum(),
                item.getArtist(),
                new Res(mimeType, item.getSize(), "00:03:25", 8192l, "http://10.0.0.1/files/101.mp3"));
        return result;
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

    public static class UpnpContainer {

        private Long id;
        private DIDLObject object;
        private Item item;

        public UpnpContainer(Long id, DIDLObject object, Item item) {
            this.id = id;
            this.object = object;
            this.item = item;
        }

        public Long getId() {
            return id;
        }

        DIDLObject getContainer() {
            return object;
        }

        Item getItem() {
            return item;
        }
    }
}
