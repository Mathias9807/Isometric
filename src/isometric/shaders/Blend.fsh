#version 330
layout(location = 0) out vec4 color;

uniform sampler2D texture0;

varying vec2 uv;

void main() {
	vec4 tex0 = texture(texture0, uv);
	tex0.a /= 3;
	color = tex0;
}