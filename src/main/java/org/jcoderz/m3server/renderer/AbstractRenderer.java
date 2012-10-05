package org.jcoderz.m3server.renderer;

/**
 * This is the abstract base class of all renderers.
 *
 * @author mrumpf
 */
public abstract class AbstractRenderer implements Renderer {

    private String name;
    private RendererType type;

    private AbstractRenderer() {
        // Do not allow default constructor instantiations
    }

    /**
     * Creates an instance of a renderer.
     *
     * @param name the name of the renderer
     * @param type the type of the renderer
     */
    public AbstractRenderer(String name, RendererType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RendererType getType() {
        return type;
    }
}
