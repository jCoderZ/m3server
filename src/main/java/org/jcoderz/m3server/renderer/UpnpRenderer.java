package org.jcoderz.m3server.renderer;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.filesystem.AudioFileItem;
import org.jcoderz.m3server.util.Config;
import org.jcoderz.m3server.util.DidlUtil;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3server.util.UrlUtil;
import org.teleal.cling.UpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.controlpoint.SubscriptionCallback;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.gena.CancelReason;
import org.teleal.cling.model.gena.GENASubscription;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.state.StateVariableValue;
import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.support.avtransport.callback.GetPositionInfo;
import org.teleal.cling.support.avtransport.callback.Pause;
import org.teleal.cling.support.avtransport.callback.Play;
import org.teleal.cling.support.avtransport.callback.SetAVTransportURI;
import org.teleal.cling.support.avtransport.callback.Stop;
import org.teleal.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.teleal.cling.support.avtransport.lastchange.AVTransportVariable;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.lastchange.LastChange;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.PositionInfo;

/**
 * This class represents UPnP/DLNA rendering devices.
 *
 * @author mrumpf
 */
public class UpnpRenderer extends AbstractRenderer {

    private static final Logger logger = Logging.getLogger(UpnpRenderer.class);
    private Device device;
    private UpnpService upnpService;
    private Configuration config;
    private String staticBaseUrl;

    /**
     * Constructor.
     *
     * @param
     */
    public UpnpRenderer(Configuration config, UpnpService upnpService, Device device) {
        super(device.getDetails().getFriendlyName(), RendererType.UPNP);
        this.upnpService = upnpService;
        this.device = device;
        this.config = config;

        staticBaseUrl = config.getString(Config.HTTP_PROTOCOL_KEY) + "://"
                + config.getString(Config.HTTP_HOSTNAME_KEY) + ":"
                + config.getString(Config.HTTP_PORT_KEY)
                + config.getString(Config.HTTP_SERVLET_ROOT_CONTEXT_KEY)
                + config.getString(Config.HTTP_SERVLET_DOWNLOAD_ROOT_CONTEXT_KEY);
        subscribeToAvTransportLastChangeEvent();
    }

    @Override
    public void play(String url) {
        Service service = device.findService(new UDAServiceId("AVTransport"));
        logger.log(Level.INFO, "Playing: " + url);

        upnpSetAvTransportUri(service, url);

        ActionCallback playAction =
                new Play(service) {
            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                // Something was wrong
                System.err.println("### " + defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(playAction);
    }

    public void getPositioninfo() {
        Service service = device.findService(new UDAServiceId("AVTransport"));
        logger.log(Level.INFO, "Getting Position Info");

        upnpGetPositionInfo(service);
    }

    @Override
    public void stop() {
        Service service = device.findService(new UDAServiceId("AVTransport"));
        logger.log(Level.INFO, "Stopping");

        ActionCallback stopAction =
                new Stop(service) {
            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                // Something was wrong
                System.err.println("### " + defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(stopAction);
    }

    @Override
    public void pause() {
        Service service = device.findService(new UDAServiceId("AVTransport"));
        logger.log(Level.INFO, "Pausing");

        ActionCallback pauseAction =
                new Pause(service) {
            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                // Something was wrong
                System.err.println("### " + defaultMsg);
            }
        };
        upnpService.getControlPoint().execute(pauseAction);
    }

    @Override
    public String info() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void upnpSetAvTransportUri(Service service, String path) throws InvalidValueException {
        try {
            DIDLContent didlContent = new DIDLContent();
            Item item = Library.browse(path);
            // TODO: create base url from config
            String url = staticBaseUrl + UrlUtil.encodePath(path);
            if (AudioFileItem.class.isAssignableFrom(item.getClass())) {
                AudioFileItem audioFileItem = (AudioFileItem) item;
                // TODO: remove dummy ids
                didlContent.addItem(DidlUtil.createMusicTrack(audioFileItem, url, 100, 99));
            } else {
                // TODO
            }

            String didlContentStr = new DIDLParser().generate(didlContent);
            logger.info("didlContentStr=" + didlContentStr);
            ActionCallback setAVTransportURIAction =
                    new SetAVTransportURI(service, url, didlContentStr) {
                @Override
                public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                    logger.log(Level.SEVERE, "SetAVITransportURI failed: {0}", defaultMsg);
                }
            };
            upnpService.getControlPoint().execute(setAVTransportURIAction);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "TODO", ex);
        }
    }

    private void upnpPlay(Service service, String url) throws InvalidValueException {
    }

    private void upnpGetPositionInfo(Service service) throws InvalidValueException {
        ActionCallback positionInfoAction =
                new GetPositionInfo(service) {
            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                // Something was wrong
                System.err.println("### " + defaultMsg);
            }

            @Override
            public void received(ActionInvocation invocation, PositionInfo positionInfo) {
                System.err.println("### " + ToStringBuilder.reflectionToString(positionInfo, ToStringStyle.SIMPLE_STYLE));
            }
        };
        upnpService.getControlPoint().execute(positionInfoAction);
    }

    private void subscribeToAvTransportLastChangeEvent() {
        Service service = device.findService(new UDAServiceId("AVTransport"));
        SubscriptionCallback callback = new SubscriptionCallback(service, 600) {
            @Override
            public void established(GENASubscription sub) {
                logger.log(Level.FINE, "Subscription established: {0}", sub.getSubscriptionId());
            }

            @Override
            protected void failed(GENASubscription subscription,
                    UpnpResponse responseStatus,
                    Exception exception,
                    String defaultMsg) {
                logger.log(Level.WARNING, "Subscription failed: {0}", defaultMsg);
            }

            @Override
            public void ended(GENASubscription sub,
                    CancelReason reason,
                    UpnpResponse response) {
                assert reason == null;
                logger.log(Level.FINE, "Subscription ended: {0}", sub.getSubscriptionId());
            }

            @Override
            public void eventReceived(GENASubscription sub) {
                logger.log(Level.FINE, "Subscription events received: {0} (seqId: {1})", new Object[] {sub.getCurrentValues().keySet(), sub.getCurrentSequence().getValue()});

                Map<String, StateVariableValue> values = sub.getCurrentValues();
                StateVariableValue lastChange = values.get("LastChange");
                if (lastChange != null) {
                    logger.log(Level.FINE, "LastChange is: {0}", ToStringBuilder.reflectionToString(lastChange, ToStringStyle.SIMPLE_STYLE));
                    String lcStr = (String) lastChange.getValue();
                    logger.log(Level.FINE, "LastChange str {0}", lcStr);
                    AVTransportLastChangeParser lcParser = new AVTransportLastChangeParser();
                    try {
                        LastChange lc = new LastChange(lcParser, lcStr);
                        
                        /*
                        <Event xmlns="urn:schemas-upnp-org:metadata-1-0/AVT/">   
                                <InstanceID val="0">     
                                        <TransportState val="PLAYING"/>     
                                        <TransportStatus val="OK"/>     
                                        <CurrentPlayMode val="NORMAL"/>     
                                        <TransportPlaySpeed val="1"/>     
                                        <NumberOfTracks val="1"/>     
                                        <NextAVTransportURI val=""/>     
                                        <NextAVTransportURIMetaData val=""/>   
                                        <CurrentTrack val="1"/>     
                                        <CurrentTrackDuration val="0:03:27.733"/>     
                                        <CurrentMediaDuration val="0:03:27.733"/>     
                                        <CurrentTrackMetaData val="##########################################"/>     
                                            <DIDL-Lite xmlns="urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:upnp="urn:schemas-upnp-org:metadata-1-0/upnp/">
                                                    <item id="100" parentID="99" restricted="false">
                                                            <dc:title>Highway to Hell</dc:title>
                                                            <dc:creator>AC/DC</dc:creator>
                                                            <upnp:class>object.item.audioItem.musicTrack</upnp:class>
                                                            <upnp:album>Highway to Hell</upnp:album>
                                                            <upnp:artist role="">AC/DC</upnp:artist>
                                                            <upnp:originalTrackNumber>2</upnp:originalTrackNumber>
                                                            <upnp:genre>(79)Hard Rock</upnp:genre>
                                                            <res bitrate="24576" duration="3:28" protocolInfo="http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=01500000000000000000000000000000" size="5044510">http://192.168.0.17:8080/m3server/download/audio/filesystem/01-gold/A/AC-DC/Highway%20to%20Hell/01%20-%20Highway%20to%20Hell.mp3</res>
                                                    </item>
                                            </DIDL-Lite>"/>
                                        <PlaybackStorageMedium val="NETWORK"/>     
                                        <PossiblePlaybackStorageMedia val="NETWORK"/>     
                                        <RecordStorageMedium val="NOT_IMPLEMENTED"/>     
                                        <PossibleRecordStorageMedia val="NOT_IMPLEMENTED"/>     
                                        <RecordMediumWriteStatus val="NOT_IMPLEMENTED"/>     
                                        <CurrentRecordQualityMode val="NOT_IMPLEMENTED"/>     
                                        <PossibleRecordQualityModes val="NOT_IMPLEMENTED"/>     
                                </InstanceID>
                        </Event>
                        */
                        logger.log(Level.FINE, "LastChange parsed {0}", ToStringBuilder.reflectionToString(lc.getEventedValue(0, AVTransportVariable.CurrentMediaDuration.class), ToStringStyle.SIMPLE_STYLE));
                    } catch (Exception ex) {
                        Logger.getLogger(UpnpRenderer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // TODO:
                }
            }

            @Override
            public void eventsMissed(GENASubscription sub, int numberOfMissedEvents) {
                logger.log(Level.FINE, "Missed subscription events: {0}", numberOfMissedEvents);
            }
        };

        upnpService.getControlPoint().execute(callback);
    }
}
