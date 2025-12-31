#version 330 core

precision lowp float;

in vec4 color;
in vec4 outlineColor;
in float outlineSize;
in vec2 rectPosition;
in vec2 halfSize;
in float radius;

#include<rounded>
#include<shaderColor>
uniform float height;

out vec4 fragColor;

const float edgeSoftness  = 2.;

//Just kill yourself
vec4 blend(vec4 srcColor, vec4 dstColor) {
    float mixedAlpha = dstColor.a + srcColor.a * (1. - dstColor.a);
    if (mixedAlpha <= 0.)
        return vec4(0.);

    vec3 rgb = (srcColor.rgb * srcColor.a + dstColor.rgb * dstColor.a * (1.0 - srcColor.a)) / mixedAlpha;
    return vec4(rgb, mixedAlpha);
}

void main() {
    vec2 _position = gl_FragCoord.xy - vec2(rectPosition.x, height - rectPosition.y);

    vec2 halfSize2 = halfSize - outlineSize;

    float distanceOut = roundedBoxSDF(_position, halfSize, radius);
    float distance = roundedBoxSDF(_position, halfSize2, radius * (length(halfSize2) / length(halfSize)));

    float step = min(1., smoothstep(0., edgeSoftness, distance));
    vec4 outlineCol = blend(color, outlineColor);
    vec4 outCol = vec4(mix(color.r, outlineCol.r, step), mix(color.g, outlineCol.g, step), mix(color.b, outlineCol.b, step), mix(color.a, outlineCol.a, step));

    fragColor = vec4(outCol.rgb, outCol.a - smoothstep(0., edgeSoftness, distanceOut));

    fragColor *= shaderColor;
}