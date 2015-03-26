#version 140 //300

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord_;

out vec2 texCoordData;
out vec2 position_;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * vec4(position, 0.0, 1.0);
	
	texCoordData = texCoord_;		
	position_ = position;
}