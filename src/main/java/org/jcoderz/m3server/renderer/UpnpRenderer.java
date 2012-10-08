package org.jcoderz.m3server.renderer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.teleal.cling.UpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.cling.model.types.UDAServiceId;

public class UpnpRenderer extends AbstractRenderer {

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

    private void upnpSetAvTransportUri(Service service, String url) throws InvalidValueException {
        Action setAVTransportURIAction = service.getAction("SetAVTransportURI");

        ActionInvocation setAVTransportURIInvocation = new ActionInvocation(setAVTransportURIAction);
        setAVTransportURIInvocation.setInput("InstanceID", "0");
        setAVTransportURIInvocation.setInput("CurrentURI", url);
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(UpnpRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\">");
        sb.append("<item id=\"" + u.getPath() + "\" parentID=\"" + u.getPath() + "\" restricted=\"1\">");
        sb.append("<upnp:class>object.item.audioItem.musicTrack</upnp:class>");
        sb.append("<dc:title>Blind Willie</dc:title>");
        sb.append("<dc:creator>Rob Towns</dc:creator>");
        sb.append("<upnp:artist>Rob Towns</upnp:artist>");
        sb.append("<upnp:albumArtURI></upnp:albumArtURI>");
        sb.append("<upnp:album>media</upnp:album><dc:date>2001-01-01</dc:date>");
        sb.append("<res protocolInfo=\"http-get:*:audio/mpeg:DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0\" size=\"2703360\" duration=\"0:02:15.000\">" + url + "</res></item>");
        sb.append("</DIDL-Lite>");
        System.err.println(sb.toString());
        setAVTransportURIInvocation.setInput("CurrentURIMetaData", sb.toString());

        ActionCallback setAVTransportURICallback = new ActionCallback(setAVTransportURIInvocation) {
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

        upnpService.getControlPoint().execute(setAVTransportURICallback);
    }

    private void upnpPlay(Service service, String url) throws InvalidValueException {
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

        upnpService.getControlPoint().execute(playCallback);
    }
}
