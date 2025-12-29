// from https://iquilezles.org/articles/distfunctions
float roundedBoxSDF(vec2 centerPosition, vec2 size, float radius) {
    return length(max(abs(centerPosition) - size + radius, 0.)) - radius;
}