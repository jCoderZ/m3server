package org.jcoderz.m3server.renderer;

/**
 * <ul>
 *   <li>MPD</li>
 *   <li>UPnP</li>
 *   <li>AirPlay</li>
 *   <li>...</li>
 * </ul>
 *
 * @author mrumpf
 *
 */
public interface Renderer {
	String getName();
	RendererType getType();
}
