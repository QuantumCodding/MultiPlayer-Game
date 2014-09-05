package com.GameName.Render;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NORMALIZE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glLoadMatrix;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.GameName.Main.GameName;
import com.GameName.Main.Start;
import com.GameName.Render.Shader.Shader;
import com.GameName.Util.Time;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class RenderEngine {
	
	private UnicodeFont font;
	
//	private Shader textureShader;
	public Shader basicShader;
	
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
	        glRotatef(180, 0, 1, 0);
	    	glTranslatef(0, -(GameName.player.getAccess().getCurrentWorld().getSizeY() * (World.SCALE * 0.1f)), 0);
		
		    glBegin(GL_LINES);
		    
		    Vector3f pos2 = GameName.player.getAccess().getSelectedCube().add(0.5f).multiply(GameName.player.getAccess().getAdjust()), 
		    		pos1 = pos2.subtract(new Vector3f(0, 10, 0));
		    
		    glColor3f(1, 0, 0);
		    glVertex3f(pos1.getX(), pos1.getY(), pos1.getZ());
		    
		    glColor3f(1, 0, 0);
		    glVertex3f(pos2.getX(), pos2.getY(), pos2.getZ());
		    
		    glEnd();
		glPopMatrix();		    
		
		glPushMatrix();
			glBegin(GL_LINES);
				glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
			
				float f1, f2, f3, f4;		
				
				for(int rotY = 0; rotY < 360; rotY ++) {
				for(int rotX = 0; rotX < 360; rotX ++) {
					
					int radius = 10;			
					
					f1 = (float)  Math.cos(Math.toRadians(rotY));
					f2 = (float)  Math.sin(Math.toRadians(rotY));
					f3 = (float)  Math.cos(Math.toRadians(rotX));                  
					f4 = (float)  Math.sin(Math.toRadians(rotX));             
					
					Vector3f loadPos = new Vector3f(
							Math.round(f2 * f3 * radius), 
							Math.round(f4 * radius), 
							Math.round(f1 * f3 * radius))
						.add(GameName.player.getAccess().getCurrentWorld().getLoadedWorld().getAccess().getCenter());
					
//					glVertex3f(loadPos.getX(), loadPos.getY(), loadPos.getZ());
				}}
			glEnd();
		glPopMatrix();
		
		for(Chunk chunk : GameName.player.getAccess().getCurrentWorld().getChunks()) {
			chunk.render();
		}
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
//		textureShader = new Shader("TextureShader", false);
//		textureShader.addUniform("sampler");
		
		basicShader = new Shader("BasicShader", false);
	}
	
	private void setUpVBOs() {
		System.out.println("Starting Render : ");	
		double time = Time.getTime();
		
		System.out.println("Done In " + oneDecimal(((double) Time.getTime() - time) / Time.getSECONDS()) + " Seconds");
	}
	
	public static float oneDecimal(double in) {
		return ((float)((int)(in * 10))) / 10f; 	
	}

	public void cleanUp() {		
//		textureShader.cleanUp();
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
