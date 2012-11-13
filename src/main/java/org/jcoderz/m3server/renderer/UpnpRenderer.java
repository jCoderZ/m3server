package org.jcoderz.m3server.renderer;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.filesystem.AudioFileItem;
import org.jcoderz.m3server.util.DidlUtil;
import org.jcoderz.m3server.util.Logging;
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

    /**
     * Constructor.
     *
     * @param name the name of the renderer
     */
    public UpnpRenderer(UpnpService upnpService, Device device) {
        super(device.getDetails().getFriendlyName(), RendererType.UPNP);
        this.upnpService = upnpService;
        this.device = device;
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
            if (AudioFileItem.class.isAssignableFrom(item.getClass())) {
                AudioFileItem audioFileItem = (AudioFileItem) item;
                // TODO: remove dummy ids
                didlContent.addItem(DidlUtil.createMusicTrack(audioFileItem, "http://localhost:8080/m3server/rest/library/browse" + path, 100, 99));
            } else {
                // TODO
            }

            String didlContentStr = new DIDLParser().generate(didlContent);
            logger.info("didlContentStr=" + didlContentStr);
            URL url = new URL("http://localhost:8080/m3server/rest/library/browse/audio/filesystem/01-gold/A/a-ha/Lifelines/01%20-%20Lifelines.mp3");//item.getUrl();
            ActionCallback setAVTransportURIAction =
                    new SetAVTransportURI(service, url.toString(), didlContentStr) {
                        @Override
                        public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                            // TODO
                            System.err.println(defaultMsg);
                        }
                    };
            upnpService.getControlPoint().execute(setAVTransportURIAction);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "TODO", ex);
        }
    }

    private void upnpPlay(Service service, String url) throws InvalidValueException {
        /*
         Action playAction = service.getAction("Play");

         ActionInvocation playInvocation = new ActionInvocation(playAction);
         playInvocation.setInput("InstanceID", "0");
         playInvocation.setInput("Speed", "1");

         ActionCallback playCallback = new ActionCallback(playInvocation) {
         @Override
         public void success(ActionInvocation invocation) {
         System.err.println("success");

         }

         @Override
         public void failure(ActionInvocation invocation,
         UpnpResponse operation,
         String defaultMsg) {
         System.err.println(defaultMsg);
         }
         };
         */
        //http://msdn.microsoft.com/en-us/library/ms899567.aspx
        ActionCallback playAction =
                new Play(service) {
                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        // Something was wrong
                        System.err.println(defaultMsg);
                    }
                };
        upnpService.getControlPoint().execute(playAction);
    }
}
