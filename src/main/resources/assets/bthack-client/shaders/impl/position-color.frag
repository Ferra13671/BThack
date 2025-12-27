#version 330 core

in vec4 vecColor;

#include<shaderColor>

out vec4 fragColor;

void main() {
    fragColor = vecColor * shaderColor;
}