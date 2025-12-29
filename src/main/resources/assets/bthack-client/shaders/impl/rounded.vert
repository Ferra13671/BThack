#version 330 core

in vec4 position;
in vec4 _vertexColor;
in vec2 _rectPosition;
in vec2 _halfSize;
in float _radius;

#include<matrices>

out vec4 vertexColor;
out vec2 rectPosition;
out vec2 halfSize;
out float radius;

void main() {
    gl_Position = projMat * modelViewMat * position;
    vertexColor = _vertexColor;
    rectPosition = _rectPosition;
    halfSize = _halfSize;
    radius = _radius;
}