#version 330 core

precision lowp float;

in vec4 vertexColor;
in vec2 rectPosition;
in vec2 halfSize;
in float radius;

#include<rounded>
#include<shaderColor>
uniform vec2 resolution;

out vec4 fragColor;

const float edgeSoftness  = 2.;

void main() {
    vec2 _position = vec2(rectPosition.x, resolution.y - rectPosition.y);

    float distance = roundedBoxSDF(vec2(gl_FragCoord.x - halfSize.x, gl_FragCoord.y + halfSize.y) - _position, halfSize, radius);

    fragColor = vec4(vertexColor.rgb, vertexColor.a - smoothstep(0., edgeSoftness, distance)) * shaderColor;
}