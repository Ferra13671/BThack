#version 330 core

precision lowp float;

in vec2 texPos;
in vec4 color;
in vec2 rectPosition;
in vec2 halfSize;
in float radius;

#include<rounded>
#include<shaderColor>
uniform sampler2D u_Texture;
uniform float height;

out vec4 fragColor;

const float edgeSoftness  = 2.;

void main() {
    vec2 _position = vec2(rectPosition.x, height - rectPosition.y);

    float distance = roundedBoxSDF(gl_FragCoord.xy - _position, halfSize, radius);

    vec4 col = color * texture(u_Texture, texPos);

    fragColor = vec4(col.rgb, col.a - smoothstep(0., edgeSoftness, distance)) * shaderColor;
}