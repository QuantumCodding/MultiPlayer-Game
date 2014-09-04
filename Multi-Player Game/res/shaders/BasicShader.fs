#version 330

in vec2 texCoordData;
in vec3 colour;
in vec3 position_;

uniform sampler2D sampler;

void main() {
	//vec3 position = mod(position_, 1.0);
	//position = mod(position, 0.5) + (mod(position * 10, 2) * -1);
	vec2 testTextCoord = texCoordData;
	
	gl_FragColor = texture2D(sampler, texCoordData);// * vec4(vec3(texCoordData, 0.0), 1.0);// * vec4(colour, 1.0);
}