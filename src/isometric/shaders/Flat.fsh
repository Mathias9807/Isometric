#version 330
layout(location = 0) out vec4 color;

uniform sampler2D texture0;
uniform vec3 tint;

varying vec2 uv;

void main() {
	color = texture(texture0, uv) * vec4(tint, 1);
}
