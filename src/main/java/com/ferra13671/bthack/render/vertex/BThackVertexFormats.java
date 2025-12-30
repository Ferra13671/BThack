package com.ferra13671.bthack.render.vertex;

import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BThackVertexFormats {

    public VertexFormat POSITION_COLOR = VertexFormat.builder()
            .element("Color", BThackVertexElementTypes.RENDER_COLOR, 1)
            .build();

    public VertexFormat POSITION_TEXTURE_COLOR = VertexFormat.builder()
            .element("Texture", VertexElementType.FLOAT, 2)
            .element("Color", BThackVertexElementTypes.RENDER_COLOR, 1)
            .build();

    public VertexFormat ROUNDED = VertexFormat.builder()
            .element("Color", BThackVertexElementTypes.RENDER_COLOR, 1)
            .element("Rect Position", VertexElementType.FLOAT, 2)
            .element("Half Size", VertexElementType.FLOAT, 2)
            .element("Radius", VertexElementType.FLOAT, 1)
            .build();

    public VertexFormat ROUNDED_OUTLINED = VertexFormat.builder()
            .element("Color", BThackVertexElementTypes.RENDER_COLOR, 1)
            .element("Outline Color", BThackVertexElementTypes.RENDER_COLOR, 1)
            .element("Outline Size", VertexElementType.FLOAT, 1)
            .element("Rect Position", VertexElementType.FLOAT, 2)
            .element("Half Size", VertexElementType.FLOAT, 2)
            .element("Radius", VertexElementType.FLOAT, 1)
            .build();

    public VertexFormat ROUNDED_BLUR = VertexFormat.builder()
            .element("Rect Position", VertexElementType.FLOAT, 2)
            .element("Half Size", VertexElementType.FLOAT, 2)
            .element("Radius", VertexElementType.FLOAT, 1)
            .build();

    public VertexFormat ROUNDED_TEXTURE = VertexFormat.builder()
            .element("Texture", VertexElementType.FLOAT, 2)
            .element("Color", BThackVertexElementTypes.RENDER_COLOR, 1)
            .element("Rect Position", VertexElementType.FLOAT, 2)
            .element("Half Size", VertexElementType.FLOAT, 2)
            .element("Radius", VertexElementType.FLOAT, 1)
            .build();
}
