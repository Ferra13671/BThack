#version 330 core

precision lowp float;

in vec4 color;
in vec2 rectPosition;
in vec2 halfSize;
in float radius;

#include<rounded>
#include<shaderColor>
uniform float height;

out vec4 fragColor;

const float edgeSoftness  = 20.;

void main() {
    vec2 _position = vec2(rectPosition.x, height - rectPosition.y);

    float distance = roundedBoxSDF(gl_FragCoord.xy - _position, halfSize, radius);

    fragColor = vec4(color.rgb, color.a - smoothstep(0., edgeSoftness, distance)) * shaderColor;
}