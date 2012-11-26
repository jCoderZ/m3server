package org.jcoderz.m3server.protocol.upnp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jcoderz.m3server.library.FileItem;
import org.jcoderz.m3server.library.filesystem.AudioFileItem;
import org.jcoderz.m3server.library.FolderItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.util.Config;
import org.jcoderz.m3server.util.DidlUtil;
import org.jcoderz.m3server.util.DlnaUtil;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3server.util.UrlUtil;
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
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.container.Container;

/**
 * The UPnP MediaServer implementation.
 *
 * @author mrumpf
 */
public class UpnpMediaServer extends AbstractContentDirectoryService {

    public static final String MANUFACTURER_NAME = "jCoderZ.org";
    public static final String MANUFACTURER_URL = "http://www.jcoderz.org";
    public static final String MODEL_DESCRIPTION = "A UPnP/DLNA Media Server";
    public static final String MODEL_NUMBER = "0.0.1";
    public static final String MODEL_URI = "";
    private static final Logger logger = Logging.getLogger(UpnpMediaServer.class);
    private static final Map<Long, UpnpContainer> idUpnpObjectMap = new HashMap<>();
    private static final Map<Long, List<UpnpContainer>> idChildUpnpObjectMap = new HashMap<>();
    private static final Long UPDATE_ID = 1L;
    private static final Long ROOT_ID = 0L;
    private static final Long ROOT_PARENT_ID = -1L;
    private static long idCounter = 0;
    private Configuration config;
    private String staticBaseUrl;

    /**
     * Constructor.
     *
     * @param config the configuration instance
     */
    public UpnpMediaServer(Configuration config) {
        this.config = config;
        staticBaseUrl = config.getString(Config.HTTP_PROTOCOL_KEY) + "://"
                + config.getString(Config.HTTP_HOSTNAME_KEY) + ":"
                + config.getString(Config.HTTP_PORT_KEY)
                + config.getString(Config.HTTP_SERVLET_ROOT_CONTEXT_KEY)
                + config.getString(Config.HTTP_SERVLET_DOWNLOAD_ROOT_CONTEXT_KEY);
        // Populate the maps with the root entry
        createDidlContainer(ROOT_ID, ROOT_PARENT_ID, Library.getRoot());
    }

    public static long getNextId() {
        ++idCounter;
        return idCounter;
    }

    // TODO: Temporary hack, make synchronized for the idUpnpObjectMap
    @Override
    public synchronized BrowseResult browse(String objectID, BrowseFlag browseFlag,
            String filter,
            long firstResult, long maxResults,
            SortCriterion[] orderby) throws ContentDirectoryException {
        logger.entering(UpnpMediaServer.class.getSimpleName(), "browse", new Object[]{objectID, browseFlag, filter, firstResult, maxResults, Arrays.asList(orderby)});

        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "idUpnpObjectMap: {0}", idUpnpObjectMap.keySet());
            logger.log(Level.FINEST, "idChildUpnpObjectMap: {0}", idChildUpnpObjectMap.keySet());
        }

        BrowseResult result = null;
        long id = Long.valueOf(objectID).longValue();
        int maxCount = 0;
        try {
            DIDLContent didlContent = new DIDLContent();
            UpnpContainer upnpContainer = idUpnpObjectMap.get(id);
            if (upnpContainer != null) {
                if (logger.isLoggable(Level.FINER)) {
                    logger.log(Level.FINER, "''{0}'' existing item with id ''{1}'': {2}", new Object[]{browseFlag, id, upnpContainer.getItem()});
                }
                if (BrowseFlag.METADATA.equals(browseFlag)) {
                    browseMetadata(upnpContainer, didlContent);
                } else if (BrowseFlag.DIRECT_CHILDREN.equals(browseFlag)) {
                    maxCount = browseDirectChildren(upnpContainer, id, maxCount, firstResult, didlContent, maxResults);
                } else {
                    // TODO: throw unknown BrowseFlag
                }
            } else {
                // TODO: throw unknown id
            }

            String resultXml = new DIDLParser().generate(didlContent);
            long count = didlContent.getContainers().size() + didlContent.getItems().size();
            result = new BrowseResult(resultXml, count, maxCount, UPDATE_ID);

            logger.exiting(UpnpMediaServer.class
                    .getSimpleName(), "browse", "DIDL response xml (" + count + "): " + resultXml);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "An exception occured during browsing of folder " + id, ex);
            throw new ContentDirectoryException(
                    ContentDirectoryErrorCode.CANNOT_PROCESS,
                    ex.toString());
        }

        return result;
    }

    private UpnpContainer createDidlContainer(long parentId, Item item) {
        Long nextId = getNextId();
        return createDidlContainer(nextId, parentId, item);
    }

    private UpnpContainer createDidlContainer(long nextId, long parentId, Item item) {
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Creating DIDL container id ''{0}'' (parent: {1}): {2}", new Object[]{nextId, parentId, item});
        }
        Container c = DidlUtil.createContainer(nextId, parentId, item);

        UpnpContainer uc = new UpnpContainer(nextId, c, item);
        idUpnpObjectMap.put(nextId, uc);
        if (parentId >= ROOT_ID) {
            List<UpnpContainer> l = idChildUpnpObjectMap.get(parentId);
            if (l == null) {
                l = new ArrayList<>();
                idChildUpnpObjectMap.put(parentId, l);
                idChildUpnpObjectMap.put(nextId, new ArrayList<UpnpContainer>());
            }
            l.add(uc);
        }

        return uc;
    }

    private UpnpContainer createDidlItem(long parentId, Item item) throws UnsupportedEncodingException {
        Long nextId = getNextId();
        return createDidlItem(nextId, parentId, item);
    }

    private UpnpContainer createDidlItem(long nextId, long parentId, Item item) throws UnsupportedEncodingException {
        String url = staticBaseUrl + UrlUtil.encodePath(item.getFullPath());

        DIDLObject didlObject = DidlUtil.createMusicTrack((AudioFileItem) item, url, nextId, parentId);

        UpnpContainer uc = new UpnpContainer(nextId, didlObject, item);
        idUpnpObjectMap.put(nextId, uc);
        List<UpnpContainer> l = idChildUpnpObjectMap.get(parentId);
        if (l == null) {
            l = new ArrayList<>();
            idChildUpnpObjectMap.put(parentId, l);
        }
        l.add(uc);

        return uc;
    }

    @Override
    public BrowseResult search(String containerId,
            String searchCriteria, String filter,
            long firstResult, long maxResults,
            SortCriterion[] orderBy) throws ContentDirectoryException {
        logger.entering(UpnpMediaServer.class.getSimpleName(), "search", new Object[]{searchCriteria, filter, firstResult, maxResults, orderBy});

        // You can override this method to implement searching!
        BrowseResult result = super.search(containerId, searchCriteria, filter, firstResult, maxResults, orderBy);

        logger.entering(UpnpMediaServer.class.getSimpleName(), "search", new Object[]{searchCriteria, filter, firstResult, maxResults, orderBy});
        return result;
    }

    public static LocalDevice createDevice(final Configuration config) {
        LocalService<AbstractContentDirectoryService> contentDirectoryService =
                new AnnotationLocalServiceBinder().read(AbstractContentDirectoryService.class);
        contentDirectoryService.setManager(
                new DefaultServiceManager<AbstractContentDirectoryService>(contentDirectoryService, null) {
                    @Override
                    protected AbstractContentDirectoryService createServiceInstance() throws Exception {
                        return new UpnpMediaServer(config);
                    }
                });
        LocalService<ConnectionManagerService> connectionManagerService =
                new AnnotationLocalServiceBinder().read(ConnectionManagerService.class);
        final ProtocolInfos sourceProtocols =
                new ProtocolInfos(
                new ProtocolInfo(
                Protocol.HTTP_GET,
                ProtocolInfo.WILDCARD,
                DidlUtil.MIMETYPE_AUDIO_MPEG,
                DlnaUtil.DLNA_ORG_PN + ";" + DlnaUtil.DLNA_ORG_OP + ";" + DlnaUtil.DLNA_ORG_CI));

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
            String name = config.getString(Config.UPNP_SERVER_NAME_KEY);
            device = new LocalDevice(
                    new DeviceIdentity(UDN.uniqueSystemIdentifier(name)),
                    new UDADeviceType(config.getString(Config.UPNP_SERVER_TYPE_NAME_KEY)),
                    new DeviceDetails(name,
                    new ManufacturerDetails(MANUFACTURER_NAME, MANUFACTURER_URL),
                    new ModelDetails(name, MODEL_DESCRIPTION, MODEL_NUMBER, MODEL_URI),
                    new DLNADoc[]{new DLNADoc(DlnaUtil.DLNA_DEVICE_CLASS, DlnaUtil.DLNA_DEVICE_VERSION)}, null),
                    new LocalService[]{contentDirectoryService, connectionManagerService});
        } catch (ValidationException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return device;
    }

    public int browseDirectChildren(UpnpContainer upnpContainer, long id, int maxCount, long firstResult, DIDLContent didlContent, long maxResults) throws UnsupportedEncodingException, RuntimeException {
        Item item = upnpContainer.getItem();
        if (FolderItem.class.isAssignableFrom(item.getClass())) {
            // check whether we already read the children
            List<UpnpContainer> ucChildren = idChildUpnpObjectMap.get(id);
            logger.log(Level.FINEST, "Children of id ''{0}'': {1}", new Object[]{id, ucChildren});
            if (ucChildren == null || ucChildren.isEmpty()) {
                FolderItem folderItem = (FolderItem) item;
                List<Item> children = folderItem.getChildren();
                for (Item childItem : children) {
                    if (FolderItem.class.isAssignableFrom(childItem.getClass())) {
                        createDidlContainer(id, childItem);
                    } else if (FileItem.class.isAssignableFrom(childItem.getClass())) {
                        createDidlItem(id, childItem);
                    } else {
                        // TODO: ???
                        throw new RuntimeException("TODO");
                    }
                }
                ucChildren = idChildUpnpObjectMap.get(id);
            }

            int pos = 0;
            maxCount = ucChildren.size();
            for (UpnpContainer ucChild : ucChildren) {
                pos++;
                if (pos <= firstResult) {
                    continue;
                }
                if (FolderItem.class.isAssignableFrom(ucChild.getItem().getClass())) {
                    logger.log(Level.FINER, "Returning existing DIDL container with id ''{0}'': {1}", new Object[]{ucChild.getId(), ucChild.getItem().getName()});
                    didlContent.addContainer((Container) ucChild.getDidlObject());
                } else if (FileItem.class.isAssignableFrom(ucChild.getItem().getClass())) {
                    logger.log(Level.FINER, "Returning existing DIDL item with id ''{0}'': {1}", new Object[]{ucChild.getId(), ucChild.getItem().getName()});
                    didlContent.addItem((org.teleal.cling.support.model.item.Item) ucChild.getDidlObject());
                } else {
                    // TODO: ???
                }
                if (didlContent.getItems().size() + didlContent.getContainers().size() >= maxResults) {
                    break;
                }
            }
        }
        return maxCount;
    }

    public void browseMetadata(UpnpContainer upnpContainer, DIDLContent didlContent) {
        DIDLObject obj = upnpContainer.getDidlObject();
        if (Container.class.isAssignableFrom(obj.getClass())) {
            // TODO: if upnpContainer.getObject() instanceof DIDLContainer...
            didlContent.addContainer((Container) upnpContainer.getDidlObject());
        } else {
            didlContent.addItem((org.teleal.cling.support.model.item.Item) upnpContainer.getDidlObject());
        }
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

        DIDLObject getDidlObject() {
            return object;
        }

        Item getItem() {
            return item;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
