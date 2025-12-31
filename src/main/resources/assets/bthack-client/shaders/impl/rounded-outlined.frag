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

void main() {
    vec2 _position = gl_FragCoord.xy - vec2(rectPosition.x, height - rectPosition.y);

    vec2 halfSize2 = halfSize - outlineSize;

    float distanceOut = roundedBoxSDF(_position, halfSize, radius);
    float distance = roundedBoxSDF(_position, halfSize2, radius * (length(halfSize2) / length(halfSize)));

    float step = min(1., smoothstep(0., edgeSoftness, distance));
    vec4 outCol = vec4(mix(color.r, outlineColor.r, step), mix(color.g, outlineColor.g, step), mix(color.b, outlineColor.b, step), mix(color.a, outlineColor.a, step));

    fragColor = vec4(outCol.rgb, outCol.a - smoothstep(0., edgeSoftness, distanceOut));

    fragColor *= shaderColor;
}