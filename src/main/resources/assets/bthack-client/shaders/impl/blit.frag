#version 330 core

uniform sampler2D u_Texture;

out vec4 fragColor;

void main() {
    fragColor = vec4(texelFetch(u_Texture, ivec2(gl_FragCoord.xy), 0).rgb, 1.);
}