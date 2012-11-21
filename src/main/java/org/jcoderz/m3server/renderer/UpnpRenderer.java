package org.jcoderz.m3server.renderer;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.filesystem.AudioFileItem;
import org.jcoderz.m3server.util.Config;
import org.jcoderz.m3server.util.DidlUtil;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3server.util.UrlUtil;
import org.teleal.cling.UpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.support.avtransport.callback.Play;
import org.teleal.cling.support.avtransport.callback.SetAVTransportURI;
import org.teleal.cling.support.contentdirectory.DIDLParser;
import org.teleal.cling.support.model.DIDLContent;

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
    }

    @Override
    public void play(String url) {
        Service service = device.findService(new UDAServiceId("AVTransport"));
        logger.log(Level.INFO, "Playing: " + url);

        upnpSetAvTransportUri(service, url);
        upnpPlay(service, url);
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pause() {
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
}
