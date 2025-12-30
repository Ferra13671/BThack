#version 330 core

#define MAX_RADIUS 50

uniform sampler2D u_Texture;
uniform float weights[MAX_RADIUS + 1];
uniform vec2 offsets;
uniform int radius;
uniform vec2 texelSize;

out vec4 fragColor;

void main() {
    vec2 pos = gl_FragCoord.xy * texelSize;
    vec3 color = texture(u_Texture, pos).rgb * weights[0];

    for (int i = 1; i <= radius; ++i) {
        vec2 off = texelSize * offsets * float(i);
        color += texture(u_Texture, clamp(pos + off, 0., 1.), 0).rgb * weights[i];
        color += texture(u_Texture, clamp(pos - off, 0., 1.), 0).rgb * weights[i];
    }

    fragColor = vec4(color, 1.);
}
