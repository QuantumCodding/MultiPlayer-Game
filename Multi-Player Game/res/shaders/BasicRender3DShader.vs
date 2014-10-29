#version 120 //300

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord_;
layout (location = 2) in vec3 colour_;
layout (location = 3) in vec3 normal;

out vec2 texCoordData;
out vec3 colour;
out vec3 position_;
out vec3 normal_;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * vec4(position, 1.0);
	gl_Normal = normal;
	
	texCoordData = texCoord_;	
	colour = colour_;
	
	position_ = position;
	normal_ = normal;
}