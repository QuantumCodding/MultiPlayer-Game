package com.GameName.Cube.Render;
//
//import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
//import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
//
//import java.nio.FloatBuffer;
//import java.util.List;
//
//import com.GameName.Cube.Cube;
//import com.GameName.Main.GameName;
//import com.GameName.Render.Types.Render3D;
//import com.GameName.Util.BufferUtil;
//
public class TestCubeRender {// extends Render3D {
//	private static final boolean[] VISABLE_FACES = new boolean[] {true, true, true, true, true, true};
//	private ICubeRender cubeRender;
//	private Cube cube;
//	
//	@SuppressWarnings("deprecation")
//	public TestCubeRender(ICubeRender cubeRender, Cube cube) {
//		super();
//		
//		this.cubeRender = cubeRender;
//		this.cube = cube;		
//		
//		name = "Test Cube Render";
//		
//		setTexture(Cube.getTextureSheet());
//	}
//	
//	public void updateVBOs() {
//		List<Float> vertcies = cubeRender.getVertices(0, 0, 0, VISABLE_FACES);
//		List<Float> texCoords = cubeRender.getTextureCoords(cube.getId(), 0, VISABLE_FACES);
//		List<Float> colors = cubeRender.getColors(cube.getId(), 0, VISABLE_FACES);
//		List<Float> normals = cubeRender.getNormals(cube.getId(), 0, VISABLE_FACES);
//		
//		FloatBuffer vertexBuffer = BufferUtil.createFillipedFloatBuffer(vertcies);
//		FloatBuffer texCoordBuffer = BufferUtil.createFillipedFloatBuffer(texCoords);
//		FloatBuffer colorBuffer = BufferUtil.createFillipedFloatBuffer(colors);
//		FloatBuffer normalBuffer = BufferUtil.createFillipedFloatBuffer(normals);
//				
//		GameName.getGLContext()
//			.addBufferBind(vertexBuffer, GL_ARRAY_BUFFER, vertexVBO, GL_DYNAMIC_DRAW, 'f');
//
//		GameName.getGLContext()
//			.addBufferBind(texCoordBuffer, GL_ARRAY_BUFFER, textureVBO, GL_DYNAMIC_DRAW, 'f');
//		
//		GameName.getGLContext()
//			.addBufferBind(colorBuffer, GL_ARRAY_BUFFER, colorVBO, GL_DYNAMIC_DRAW, 'f');
//		
//		GameName.getGLContext()
//			.addBufferBind(normalBuffer, GL_ARRAY_BUFFER, normalVBO, GL_DYNAMIC_DRAW, 'f');
//		
//		vertexCount = cubeRender.getVerticeCount(VISABLE_FACES);
//		
//		vboUpdateNeeded = false;
//	}
//
//	public void genVBOids() {
//		int[] ids = GameName.getGLContext().genBufferIds(4);
//		
//		vertexVBO = ids[0];
//		textureVBO = ids[1];
//		colorVBO = ids[2];
//		normalVBO = ids[3];
//		
//		needsVBOids = false;
//	}
//
//	protected void cleanUp_Render3D() {
//		
//	}
}
