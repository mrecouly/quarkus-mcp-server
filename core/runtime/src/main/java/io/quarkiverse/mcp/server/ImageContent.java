package io.quarkiverse.mcp.server;

import java.util.Map;

/**
 * An image content provided to or from an LLM.
 *
 * @param data a base64-encoded string representing the image data (must not be {@code null})
 * @param mimeType the mime type of the image (must not be {@code null})
 * @param _meta the optional metadata (may be {@code null})
 * @param annotations the optional annotations (may be {@code null})
 */
public record ImageContent(String data, String mimeType, Map<MetaKey, Object> _meta, Annotations annotations)
        implements
            Content {

    public ImageContent(String data, String mimeType) {
        this(data, mimeType, null, null);
    }

    public ImageContent {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        if (mimeType == null) {
            throw new IllegalArgumentException("mimeType must not be null");
        }
    }

    @Override
    public Type type() {
        return Type.IMAGE;
    }

    @Override
    public ImageContent asImage() {
        return this;
    }

}
