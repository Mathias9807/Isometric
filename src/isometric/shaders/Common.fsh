#version 330
layout(location = 0) out vec4 color;

uniform mat4 view, proj;
uniform vec3 sunDir;
uniform int  render = 0;
uniform float time;
uniform sampler2D texture0, texture1, texture2, texture3;

varying vec3 position, normal;
varying vec2 uv;
//varying mat3 tbnMat;

float brightness = 2.5, contrast = 1.5;

void main() {
	vec2 uvOffset = vec2(time * 0.1, time * 0.01);
	vec4 tex0 = texture(texture0, uv), 
		 tex1 = texture(texture1, uv + uvOffset), 
		 tex2 = texture(texture2, uv + uvOffset), 
		 tex3 = texture(texture3, uv);
	vec3 groundColor = tex0.rgb;
	if (render == 1) {
		groundColor = mix(tex1.rgb, tex2.rgb, cos(time) / 2 + 0.5);
	}else if (render == 2) {
		groundColor = tex3.rgb;
	}else if (render == 3) {
		color = vec4(1, 1, 1, 1);
		return;
	}else if (render == 4) {
		color = vec4(1, 0, 0, 1);
		return;
	}
	
	float light = clamp(dot(normal, normalize(sunDir)), 0, 1);
	
	float depthFade = 1 - 1 / (abs(position.y / 10) + 1);
	groundColor = mix(groundColor, vec3(0.5), depthFade);
	
	color = vec4(brightness * pow(groundColor * light, vec3(contrast)), tex0.a);
}