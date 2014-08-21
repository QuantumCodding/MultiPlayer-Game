#version 330

in vec2 texCoordData;
in vec3 colour;
in vec3 position_;

uniform sampler2D sampler;

void main() {
	gl_FragColor = texture2D(sampler, texCoordData);// * vec4(colour, 1.0);
}