#version 330 core

in vec4 position;
in vec4 _color;
in vec4 _outlineColor;
in float _outlineSize;
in vec2 _rectPosition;
in vec2 _halfSize;
in float _radius;

#include<matrices>

out vec4 color;
out vec4 outlineColor;
out float outlineSize;
out vec2 rectPosition;
out vec2 halfSize;
out float radius;

void main() {
    gl_Position = projMat * modelViewMat * position;
    color = _color;
    outlineColor = _outlineColor;
    outlineSize = _outlineSize;
    rectPosition = _rectPosition;
    halfSize = _halfSize;
    radius = _radius;
}