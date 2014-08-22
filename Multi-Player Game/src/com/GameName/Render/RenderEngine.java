package com.GameName.Render;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import com.GameName.Main.GameName;
import com.GameName.Main.Start;
import com.GameName.Render.Shader.Shader;
import com.GameName.Util.Time;
import com.GameName.World.World;
import com.GameName.World.Cube.Cube;

public class RenderEngine {
	
	private UnicodeFont font;
		
	private int[][][] worldRenderIds;	
	
	private Shader textureShader;
	private Shader basicShader;
	
	private FloatBuffer perspectiveProjectionMatrix;
	private FloatBuffer orthographicProjectionMatrix;

	public RenderEngine() {		
		RenderUtil.init();
		
		setUpOpenGL();  
		setUpShaders();
		setUpVBOs();
	}
	
	public void setUpOpenGL() {
		enableRenderUtilitys();
		setUpPerspectives();
		setUpFonts(); 
	}
	
	private void enableRenderUtilitys() {
        glClearColor(0, 0.75f, 1, 0);
		
		glShadeModel(GL_SMOOTH);
//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);   
		
        glEnable(GL_NORMALIZE);
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);	        
        glEnable(GL_TEXTURE_2D);
		
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CCW);
        glCullFace(GL_BACK);

        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
        
	}
	
	public void setUpPerspectives() {
		perspectiveProjectionMatrix = BufferUtils.createFloatBuffer(16);
		orthographicProjectionMatrix = BufferUtils.createFloatBuffer(16);
		
		glViewport(0, 0, Display.getWidth(), Display.getHeight());		
			
		GameName.player.getAccess().getCamera().applyPerspectiveMatrix();
		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);		
		
	    glMatrixMode(GL_PROJECTION);
	    
	       glLoadIdentity();
	       
//	       GameName.player.cam.applyOrthographicMatrix();
	       glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
//	       glOrtho(0, Start.WIDTH, Start.HEIGHT, 0, 1, -1);
	       
	       glGetFloat(GL_PROJECTION_MATRIX, orthographicProjectionMatrix);
	       
	    glLoadMatrix(perspectiveProjectionMatrix);
	    glMatrixMode(GL_MODELVIEW);
	}
	
	@SuppressWarnings("unchecked")
	private void setUpFonts() {
        java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 18);
        font = new UnicodeFont(awtFont);
        
        font.getEffects().add(new ColorEffect(java.awt.Color.white));
        font.addAsciiGlyphs();
        
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
            Start.cleanUp();
        }
    }
		
	public void render3D() {
//		System.out.println("Here");
		
		glLoadIdentity();
		GameName.player.getAccess().getCamera().useView();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glPushMatrix();
			glDisable(GL_LIGHTING);
			
			glEnableVertexAttribArray(0); // Position
			glEnableVertexAttribArray(5); // Texture Data
			glEnableVertexAttribArray(6); // Color
			
		    glEnable(GL_TEXTURE_2D);
			
		    	Cube.getTextureSheet().bind();
		    	basicShader.bind();
		    	
		    	World renderingWorld = GameName.player.getAccess().getCurrentWorld();

		        glRotatef(180, 0, 1, 0);
		    	glTranslatef(0, -(renderingWorld.getSizeY() * (World.SCALE * 0.1f)), 0);
		    	glScalef(World.SCALE, World.SCALE, World.SCALE);
		        
        		int[][] chunkData = worldRenderIds[renderingWorld.getId()];
		        for(int i = 0; i < chunkData.length; i ++) {
		        	glColor3f(1, 1, 1);
		        	
		        	glBindBuffer(GL_ARRAY_BUFFER, chunkData[i][0]);
		        	glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		        			        	
		        	glBindBuffer(GL_ARRAY_BUFFER, chunkData[i][1]);
		        	glVertexAttribPointer(5, 2, GL_DOUBLE, false, 0, 0);
		        	
		        	glBindBuffer(GL_ARRAY_BUFFER, chunkData[i][2]);
		        	glVertexAttribPointer(6, 3, GL_FLOAT, false, 0, 0);

		        	glDrawArrays(GL_QUADS, 0, chunkData[i][3]);
		        }     
		        
		        Shader.unbind();
		        Texture.unbind();
		        
		    glDisableVertexAttribArray(0); // Position						
		    glDisableVertexAttribArray(5); // Texture Data
		    glDisableVertexAttribArray(6); // Color
			
		    glDisable(GL_TEXTURE_2D);
	        
		    glEnableClientState(GL_VERTEX_ARRAY);  
		    glPushMatrix();
		    
		    glPopMatrix();		    
	        glDisableClientState(GL_VERTEX_ARRAY);
	                
        glPopMatrix();
	}
	
	public void render2D() {
		glMatrixMode(GL_PROJECTION);
			glLoadMatrix(orthographicProjectionMatrix);
        glMatrixMode(GL_MODELVIEW);		
        	glLoadIdentity();        
        
		glPushMatrix();
			glDisable(GL_TEXTURE_2D);
			glDisable(GL_DEPTH_TEST);
			glDisable(GL_CULL_FACE);
			
			GameName.guiManager.render();
			
			float x = oneDecimal(GameName.player.getAccess().getPos().getX());
			float y = oneDecimal(GameName.player.getAccess().getPos().getY());
			float z = oneDecimal(GameName.player.getAccess().getPos().getZ());
			
			glTranslatef(0, 540, 0);
			
			org.newdawn.slick.Color col = new org.newdawn.slick.Color(Color.BLUE.getRGB());	        
			
	        font.drawString(0, 0, "FPS: " + GameName.getFPS(), col);// + ", " + GameName.player.getAccess().getSelectedCube().getX() + " " + GameName.player.getAccess().getSelectedCube().getY() + " " + GameName.player.getAccess().getSelectedCube().getZ(), col); // 
	        font.drawString(0, 20, x + "," + y + "," + z, col);
	        font.drawString(0, 40, oneDecimal(GameName.player.getAccess().getRot().getX()) + "," + oneDecimal(GameName.player.getAccess().getRot().getY()) + "," + oneDecimal(GameName.player.getAccess().getRot().getZ()), col);

	        glDisable(GL_TEXTURE_2D);
	        glEnable(GL_DEPTH_TEST);
	        glEnable(GL_CULL_FACE);
		glPopMatrix();
		
		glMatrixMode(GL_PROJECTION);
			glLoadMatrix(perspectiveProjectionMatrix);
        glMatrixMode(GL_MODELVIEW);
	}

	private void setUpShaders() {
		textureShader = new Shader("TextureShader", false);
//		textureShader.addUniform("sampler");
		
		basicShader = new Shader("BasicShader", false);
	}
	
	private void setUpVBOs() {
		System.out.println("Starting Render : ");	
		double time = Time.getTime();
			
		worldRenderIds = new int[GameName.worlds.size()][][];
		
		for(int i = 0; i < worldRenderIds.length; i ++) {
			System.out.println("Generating World " + i + ": " + GameName.worlds.get(i));
			worldRenderIds[i] = RenderUtil.generateWorldRender(GameName.worlds.get(i));
		}
		
		System.out.println("Done In " + oneDecimal(((double) Time.getTime() - time) / Time.getSECONDS()) + " Seconds");
	}
	
	public void updataChunk(int x, int y, int z, int worldId) {
		World w = GameName.worlds.get(worldId);
		worldRenderIds[worldId][z + (y * w.getChunkZ()) + (x * w.getChunkZ()  * w.getChunkY())] = 
				RenderUtil.generateChunk(x, y, z, w);
	}
	
	public static float oneDecimal(double in) {
		return ((float)((int)(in * 10))) / 10f; 	
	}

	public void cleanUp() {
		for(int i = 0; i < worldRenderIds.length; i ++) {
			for(int j = 0; j < worldRenderIds[i].length; j ++) {
				glDeleteBuffers(worldRenderIds[i][j][0]);
				glDeleteBuffers(worldRenderIds[i][j][1]);
				glDeleteBuffers(worldRenderIds[i][j][2]);
				glDeleteBuffers(worldRenderIds[i][j][3]);
			}
		}
		
		textureShader.cleanUp();
		basicShader.cleanUp();
	}
}


//glPushMatrix();
//	//                Back                  Front                 Left                  Right                 Bottom                Top
//	byte[][] xAdds = {{0, 1, 1}, {0, 0, 1}, {0, 1, 1}, {0, 0, 1}, {0, 0, 0}, {0, 0, 0}, {1, 1, 1}, {1, 1, 1}, {0, 1, 1}, {0, 0, 1}, {0, 1, 1}, {0, 0, 1}};
//	byte[][] yAdds = {{0, 0, 1}, {0, 1, 1}, {0, 0, 1}, {0, 1, 1}, {0, 0, 1}, {0, 1, 1}, {0, 0, 1}, {0, 1, 1}, {0, 0, 0}, {0, 0, 0}, {1, 1, 1}, {1, 1, 1}};
//	byte[][] zAdds = {{0, 0, 0}, {0, 0, 0}, {1, 1, 1}, {1, 1, 1}, {0, 1, 1}, {0, 0, 1}, {0, 1, 1}, {0, 0, 1}, {0, 0, 1}, {0, 1, 1}, {0, 0, 1}, {0, 1, 1}};
//
//	for(int i = 0; i < xAdds.length; i ++) {
//		glColor3d(Math.random(), Math.random(), Math.random());
//		glBegin(GL_TRIANGLES);
//		for(int j = 0; j < xAdds[i].length; j ++) {
//			glVertex3f(xAdds[i][j], yAdds[i][j], zAdds[i][j]);
//		}
//		glEnd();
//	}
//glPopMatrix();

//               Back           Front         Left          Right         Bottom        Top
//byte[][] xAdds = {{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}, {0, 1, 1, 0}, {0, 1, 1, 0}};
//byte[][] yAdds = {{0, 0, 1, 1}, {0, 0, 1, 1}, {0, 0, 1, 1}, {0, 0, 1, 1}, {0, 0, 0, 0}, {1, 1, 1, 1}};
//byte[][] zAdds = {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 1, 1}, {0, 0, 1, 1}};
