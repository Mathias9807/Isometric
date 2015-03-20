#version 330
layout(location = 0) in vec3 vertex_m;
layout(location = 1) in vec2 vertex_uv;
layout(location = 2) in vec3 vertex_n;

uniform mat4 model, view, proj;
uniform vec3 position_m;

varying vec3 position, normal;
varying vec2 uv;
//varying mat3 tbnMat;

void main() {
	mat4 MVP = proj * view;
	normal = vertex_n;
	uv = vec2(vertex_uv.s, 1 - vertex_uv.t);
	gl_Position = MVP * vec4(vertex_m + position_m, 1);
	
	position = position_m;
	
	/*vec3 n = normalize(normal);
	vec3 t;
	if (n.y > 0) t = normalize(cross(n, normalize(vec3(0, 1, -1))));
	if (n.y <= 0) t = normalize(cross(n, normalize(vec3(0, 1, 1))));
	vec3 b = cross(t, n);
	tbnMat = mat3(t, b, n);*/
}