#version 330 core

in vec4 position;

#include<matrices>

void main() {
    gl_Position = projMat * modelViewMat * position;
}