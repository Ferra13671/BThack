#version 330 core

in vec2 texPos;

#include<shaderColor>
uniform sampler2D u_Texture;

out vec4 fragColor;

void main() {
    fragColor = texture(u_Texture, texPos) * shaderColor;
}