#version 330 core

in vec4 position;
in vec2 UV;
in vec4 color;

#include<matrices>

out vec2 texPos;
out vec4 vecColor;

void main() {
    gl_Position = projMat * modelViewMat * position;
    texPos = UV;
    vecColor = color;
}