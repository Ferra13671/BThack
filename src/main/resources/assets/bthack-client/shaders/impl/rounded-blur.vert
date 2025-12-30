#version 330 core

in vec4 position;
in vec2 _rectPosition;
in vec2 _halfSize;
in float _radius;

#include<matrices>

out vec2 rectPosition;
out vec2 halfSize;
out float radius;

void main() {
    gl_Position = projMat * modelViewMat * position;
    rectPosition = _rectPosition;
    halfSize = _halfSize;
    radius = _radius;
}