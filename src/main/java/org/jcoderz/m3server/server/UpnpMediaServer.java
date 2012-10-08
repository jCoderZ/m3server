/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcoderz.m3server.server;

import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.teleal.cling.support.model.PersonWithRole;
import org.teleal.cling.support.model.Protocol;
import org.teleal.cling.support.model.ProtocolInfo;
import org.teleal.cling.support.model.ProtocolInfos;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.SortCriterion;
import org.teleal.cling.support.model.item.MusicTrack;
import org.teleal.common.util.MimeType;

/**
 *
 * @author mrumpf
 */
public class UpnpMediaServer extends AbstractContentDirectoryService {
    private static final Logger logger = Logger.getLogger(UpnpMediaServer.class.getName());

    public static final String MEDIA_SERVER_TYPE = "MediaServer";
    public static final String MEDIA_SERVER_NAME = "m3server";

    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag,
            String filter,
            long firstResult, long maxResults,
            SortCriterion[] orderby) throws ContentDirectoryException {
        try {
            MediaLibrary ml = MediaLibrary.getMediaLibrary();
            logger.info("objectID=" + objectID);
            logger.info("browseFlag=" + browseFlag.toString());
            logger.info("filter=" + filter);
            logger.info("first=" + firstResult + ", max=" + maxResults);
            logger.info("sortCriterion=" + orderby);

            // This is just an example... you have to create the DIDL content dynamically!

            DIDLContent didl = new DIDLContent();

            String album = ("Black Gives Way To Blue");
            String creator = "Alice In Chains"; // Required
            PersonWithRole artist = new PersonWithRole(creator, "Performer");
            MimeType mimeType = new MimeType("audio", "mpeg");

            didl.addItem(new MusicTrack(
                    "101", "3", // 101 is the Item ID, 3 is the parent Container ID
                    "All Secrets Known",
                    creator, album, artist,
                    new Res(mimeType, 123456l, "00:03:25", 8192l, "http://10.0.0.1/files/101.mp3")));

            didl.addItem(new MusicTrack(
                    "102", "3",
                    "Check My Brain",
                    creator, album, artist,
                    new Res(mimeType, 2222222l, "00:04:11", 8192l, "http://10.0.0.1/files/102.mp3")));

            // Create more tracks...

            // Count and total matches is 2
            return new BrowseResult(new DIDLParser().generate(didl), 2, 2);

        } catch (Exception ex) {
            throw new ContentDirectoryException(
                    ContentDirectoryErrorCode.CANNOT_PROCESS,
                    ex.toString());
        }
    }

    @Override
    public BrowseResult search(String containerId,
            String searchCriteria, String filter,
            long firstResult, long maxResults,
            SortCriterion[] orderBy) throws ContentDirectoryException {
            logger.info("searchCriteria=" + searchCriteria);
            logger.info("filter=" + filter);
            logger.info("first=" + firstResult + ", max=" + maxResults);
            logger.info("sortCriterion=" + orderBy);
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
                        new DLNADoc[] {new DLNADoc("DMS-1.50","M-DMS-1.50")}, null), 
                new LocalService[]{contentDirectoryService, connectionManagerService});
        } catch (ValidationException ex) {
            Logger.getLogger(UpnpMediaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return device;
    }
}
