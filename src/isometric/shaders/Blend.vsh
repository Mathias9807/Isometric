#version 330
layout(location = 0) in vec3 vertex_m;
layout(location = 1) in vec2 vertex_uv;

varying vec2 uv;

void main() {
	uv = vec2(vertex_uv.s, 1 - vertex_uv.t);
	gl_Position = vec4(vertex_m.xz * 2 + vec2(-1, 1), -1, 1);
}