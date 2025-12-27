#version 330 core

in vec4 position;
in vec4 color;

#include<matrices>

out vec4 vecColor;

void main() {
    gl_Position = projMat * modelViewMat * position;
    vecColor = color;
}