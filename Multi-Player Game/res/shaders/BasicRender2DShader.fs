#version 120 //300

in vec2 texCoordData;
in vec3 position_;

uniform sampler2D sampler;

void main() {
	gl_FragColor = texture2D(sampler, texCoordData);
}