#version 330

layout(location = 0) in vec2 pos;
layout(location = 1) in vec2 uv;
layout(location = 2) in vec4 col;

out vec2 uvCoord;
out vec4 overrideColor;

uniform mat4 camera;

uniform vec2 cameraPosition = vec2(0.0, 0.0);

void main(){
	//vec2 finalPos = pos - cameraPosition;
	vec2 finalPos = vec2(pos.x - cameraPosition.x, pos.y - cameraPosition.y);
	gl_Position = camera * vec4(finalPos.x, finalPos.y, 0, 1);
	//gl_Position = camera * vec4(pos.x, pos.y, 0, 1);
	uvCoord = uv;
	overrideColor = col;
}