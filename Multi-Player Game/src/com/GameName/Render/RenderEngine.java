package com.GameName.Render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
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
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
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
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.GameName.Main.GameName;
import com.GameName.Main.Start;
import com.GameName.Main.Debugging.Logger;
import com.GameName.Render.Effects.Shader;
import com.GameName.Render.Effects.ShaderRegistry;
import com.GameName.Render.Types.Render2D;
import com.GameName.Render.Types.Render3D;
import com.GameName.Render.Types.Renderable;
import com.GameName.Util.IEngine;
import com.GameName.World.World;
//import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
//import static org.lwjgl.opengl.GL11.GL_LINE;
//import static org.lwjgl.opengl.GL11.glPolygonMode;

public class RenderEngine implements IEngine<Renderable> {
	private ArrayList<Render2D> render2D;
	private ArrayList<Render3D> render3D;
	
	private UnicodeFont font;
		
	private FloatBuffer perspectiveProjectionMatrix;
	private FloatBuffer orthographicProjectionMatrix;

	public RenderEngine() {	
		render2D = new ArrayList<Render2D>();
		render3D = new ArrayList<Render3D>();
		
		setUpOpenGL();  
		setUpShaders();
	}
	
	public void setUpOpenGL() {
		enableRenderUtilitys();
		setUpPerspectives();
		setUpFonts(); 
	}
	
	private void enableRenderUtilitys() {
        glClearColor(0, 0.75f, 1, 0);
        glClearDepth(1.0f);
		
		glShadeModel(GL_SMOOTH);
//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);   
		
        glEnable(GL_RESCALE_NORMAL);
        
        glEnable(GL_DEPTH_TEST);
//        glDepthFunc(GL_LEQUAL);//QUAL;
		
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);
        glCullFace(GL_FRONT);

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
	       glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);	       
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
	
	public void renderTest(Renderable... test) {
		glMatrixMode(GL_PROJECTION);
			glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
			
		GameName.player.getAccess().getCamera().useView();		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
//			glScalef(100, 100, 100);
			if(test != null) {
				glEnableVertexAttribArray(0); // Position
				glEnableVertexAttribArray(1); // Texture Data
				glEnableVertexAttribArray(2); // Color
				glEnableVertexAttribArray(3); // Normal
							
				for(Renderable render : test) {
					glPushMatrix();
						render.render();
					glPopMatrix();
					
					glTranslatef(0, 0, -20);
				}// glScalef(0.9f, 0.9f, 0.9f);}
				
				glDisableVertexAttribArray(0); // Position						
			    glDisableVertexAttribArray(1); // Texture Data
			    glDisableVertexAttribArray(2); // Color
			    glDisableVertexAttribArray(3); // Normal
			}
	}
	
	public void render3D() {
		glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
		
		glMatrixMode(GL_PROJECTION);
			glLoadMatrix(perspectiveProjectionMatrix);
        glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			
		GameName.player.getAccess().getCamera().useView();		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glPushMatrix();
		
//		glBegin(GL_QUADS);
//			for(Vector3f vec : ICubeRender.DEFAULT_CUBE) {
//				glColor3f((float) Math.random() * 10 % 2 / 10, (float) Math.random() * 10 % 1 / 10, (float) Math.random() * 10 % 1 / 10);
//				glVertex3f(vec.getX(), vec.getY(), vec.getZ());
//			}
//		
//		glEnd();
		
		glRotatef(180, 0, 1, 0);
    	glScalef(World.SCALE, World.SCALE, World.SCALE);
        	    		
		glColor3f((float) Math.random() * 10 % 2 / 10, (float) Math.random() * 10 % 1 / 10, (float) Math.random() * 10 % 1 / 10);//3
		
		glEnableVertexAttribArray(0); // Position
		glEnableVertexAttribArray(1); // Texture Data
		glEnableVertexAttribArray(2); // Color
		glEnableVertexAttribArray(3); // Normal
		
		for(int i = 0; i < render3D.size(); i ++) {
			glPushMatrix();
				render3D.get(i).render();
//				Logger.println(render3D.get(i).getName());
			glPopMatrix();
		}
        
	    glDisableVertexAttribArray(0); // Position						
	    glDisableVertexAttribArray(1); // Texture Data
	    glDisableVertexAttribArray(2); // Color
	    glDisableVertexAttribArray(3); // Normal
	    
		glPopMatrix();
	}
	
	public void render2D() {		
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		
		glMatrixMode(GL_PROJECTION);
			glLoadMatrix(orthographicProjectionMatrix);
        glMatrixMode(GL_MODELVIEW);		
        	glLoadIdentity();       
        
		glPushMatrix();
		
			glEnableVertexAttribArray(0); // Position
			glEnableVertexAttribArray(1); // Texture Data
		
			for(int i = 0; i < render2D.size(); i ++) {
				render2D.get(i).draw();
			}
			
			glEnableVertexAttribArray(0); // Position
			glEnableVertexAttribArray(1); // Texture Data
			
			
			//TODO: Remove Test Code
			float x = oneDecimal(GameName.player.getAccess().getPos().getX());
			float y = oneDecimal(GameName.player.getAccess().getPos().getY());
			float z = oneDecimal(GameName.player.getAccess().getPos().getZ());
			
			glTranslatef(0, 540, 0);
			
			org.newdawn.slick.Color col = new org.newdawn.slick.Color(Color.BLUE.getRGB());	        
			
	        font.drawString(0, 0, "FPS: " + GameName.getFPS() + ", " + GameName.player.getAccess().getSelectedCube().getX() + " " + GameName.player.getAccess().getSelectedCube().getY() + " " + GameName.player.getAccess().getSelectedCube().getZ(), col); // 
	        font.drawString(0, 20, x + "," + y + "," + z, col);
	        font.drawString(0, 40, oneDecimal(GameName.player.getAccess().getRot().getX()) + "," + oneDecimal(GameName.player.getAccess().getRot().getY()) + "," + oneDecimal(GameName.player.getAccess().getRot().getZ()), col);

		glPopMatrix();

        glDisable(GL_TEXTURE_2D);
        		
		glPushMatrix();
			glColor3f(1, 0, 0);
		
			glBegin(GL_LINES);
			
			glVertex2f(Display.getWidth() / 2, 0);
			glVertex2f(Display.getWidth() / 2, Display.getHeight());
			
			glEnd();
			
			glBegin(GL_LINES);
			
			glVertex2f(0, Display.getHeight() / 2);
			glVertex2f(Display.getWidth(), Display.getHeight() / 2);
			
			glEnd();
		glPopMatrix();
	}

	private void setUpShaders() {	
		ShaderRegistry reg = new ShaderRegistry();
		
		reg.addShader(new Shader("BasicRender3DShader", false));
		reg.addShader(new Shader("BasicRender2DShader", false));
		
		ShaderRegistry.addRegistry(reg);
	}
	
	public static float oneDecimal(double in) {
		return ((float)((int)(in * 10))) / 10f; 	
	}

	public void cleanUp() {		
		
	}

	public void add(Renderable obj) {
		if(obj instanceof Render2D) render2D.add((Render2D) obj);
		else if(obj instanceof Render3D) render3D.add((Render3D) obj);
		else Logger.print("Unknown Type: " + obj).setType("ERROR").end();
	}

	public void remove(Renderable obj) {
		if(obj instanceof Render2D) render2D.remove((Render2D) obj);
		else if(obj instanceof Render3D) render3D.remove((Render3D) obj);
	}

	public void step(float delta) {
		render3D();
		render2D();
	}
}