#version 330

layout (location = 0) in vec3 position;
layout (location = 5) in vec2 texCoord_;
layout (location = 6) in vec3 colour_;

out vec2 texCoordData;
out vec3 colour;
out vec3 position_;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * vec4(position, 1.0);
		
	texCoordData = texCoord_;	
	colour = colour_;
	position_ = position;
}