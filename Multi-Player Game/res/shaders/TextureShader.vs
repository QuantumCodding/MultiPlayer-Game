#version 330

layout (location = 0) in vec3 position;

layout (location = 1) in vec4 texCoords0_0;
layout (location = 2) in vec4 texCoords0_1;
layout (location = 3) in vec4 texCoords0_2;
layout (location = 4) in vec4 texCoords0_3;

layout (location = 5) in vec2 texCoord_;
layout (location = 6) in vec3 colour_;

mat4 texCoords0_;
out mat4 texCoords0;

out vec2 texCoordData;
out vec3 colour;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * vec4(position, 1.0);
		
	texCoords0_ = mat4(texCoords0_0, texCoords0_1, texCoords0_2, texCoords0_3);		
	texCoords0 = texCoords0_;

	texCoordData = texCoord_;	
	colour = colour_;
}