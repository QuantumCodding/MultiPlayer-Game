#version 330

in mat4 texCoords0;

in vec2 texCoordData;
in vec3 colour;

uniform sampler2D sampler;

void main() {
	int x = int(texCoordData.x);
	int y = int(texCoordData.y * 2);

	vec2 texCoord = vec2(
		texCoords0[x][y], 
		texCoords0[x][y + 1]
	);
	
	gl_FragColor = texture2D(sampler, texCoord);// * vec4(colour, 1.0);
}