#version 330
layout(location = 0) in vec3 vertex_m;
layout(location = 1) in vec2 vertex_uv;

uniform vec3 position_m, scale_m;
uniform vec2 uv_m, uv_p;

varying vec2 uv;


void main() {
	uv = vec2(vertex_uv.s, vertex_uv.t) * uv_m + uv_p;
	gl_Position = vec4(vertex_m.xzy * scale_m + position_m, 1);
}