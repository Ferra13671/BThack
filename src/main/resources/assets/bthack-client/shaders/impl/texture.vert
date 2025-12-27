#version 330 core

in vec4 position;
in vec2 UV;

#include<matrices>

out vec2 texPos;

void main() {
    gl_Position = projMat * modelViewMat * position;
    texPos = UV;
}