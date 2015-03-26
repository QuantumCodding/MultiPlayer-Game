package com.GameName.Render;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
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
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.GameName.Console.Base.Logger;
import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.RenderProperties;
import com.GameName.Render.Effects.RenderProperties.RenderPropertiesBuilder;
import com.GameName.Render.Effects.RenderStep;
import com.GameName.Render.Effects.Shader;
import com.GameName.Render.Effects.ShaderRegistry;
import com.GameName.Render.Effects.Texture.TextureType;
import com.GameName.Render.Types_2.IRenderable;
import com.GameName.Render.Types_2.Render;
import com.GameName.Render.Types_2.Render2D;
import com.GameName.Render.Types_2.Render3D;
import com.GameName.Util.IEngine;
//import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
//import static org.lwjgl.opengl.GL11.GL_LINE;
//import static org.lwjgl.opengl.GL11.glPolygonMode;

public class RenderEngine implements IEngine<IRenderable> {
	private ArrayList<Render2D> render2D;
	private ArrayList<Render3D> render3D;
	
	private UnicodeFont font;
		
	private FloatBuffer perspectiveProjectionMatrix;
	private FloatBuffer orthographicProjectionMatrix;

	private GameEngine ENGINE;
	
	public RenderEngine(GameEngine eng) {	
		ENGINE = eng;
		
		render2D = new ArrayList<>();
		render3D = new ArrayList<>();
		
		testRender2DProperties = new RenderPropertiesBuilder()
			.disableDepthTest().disableCullFace().build();
	}
	
	public void setUpOpenGL() {
		enableRenderUtilitys();
		setUpPerspectives();
		setUpFonts(); 
	}
	
	private void enableRenderUtilitys() {
        glClearColor(0.75f, 0, 1, 0); // 0, 0.75f, 1, 0 
        glClearDepth(1.0f);
		
		glShadeModel(GL_SMOOTH);
//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);   
		
        glEnable(GL_RESCALE_NORMAL);        
        glEnable(GL_DEPTH_TEST);
		
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //			GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA
        
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
			
		ENGINE.getPlayer().getCamera().applyPerspectiveMatrix();
		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);		
		
	    glMatrixMode(GL_PROJECTION);
	    
	       glLoadIdentity();	       
	       glOrtho(0, 
	    		   ENGINE.getGameName().getWindow().getWidth(), 
	    		   ENGINE.getGameName().getWindow().getHeight(), 
	    		   0, 1, -1
	           );	       
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
//            Start.cleanUp();
        }
    }
	
	public void render3D() {
//		 glClearColor((float) Math.random(), (float) Math.random(), (float) Math.random(), 0);
		
		glMatrixMode(GL_PROJECTION);
			glLoadMatrix(perspectiveProjectionMatrix);
        glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			
			ENGINE.getPlayer().getCamera().useView();		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glPushMatrix();
		
//			glBegin(GL_QUADS);
//				for(Vector3f vec : ICubeRender.DEFAULT_CUBE) {
//					glColor3f((float) Math.random() * 10 % 10 / 10, (float) Math.random() * 10 % 10 / 10, (float) Math.random() * 10 % 10 / 10);
//					glVertex3f(vec.getX() * 100, vec.getY() * 100, vec.getZ() * 100);
//				}			
//			glEnd();
			

			glRotatef(180, 0, 0, 1);
			glRotatef(90, 0, 1, 0);
//	    	glScalef(World.SCALE, World.SCALE, World.SCALE);
	    	
//			glEnableVertexAttribArray(0); // Position
//			glEnableVertexAttribArray(1); // Texture Data
//			glEnableVertexAttribArray(2); // Color
//			glEnableVertexAttribArray(3); // Normal
			
			glEnableClientState(GL_VERTEX_ARRAY); glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);  glEnableClientState(GL_NORMAL_ARRAY);
			
			RenderStep currentStep = RenderStep.Primary;	
//			glColor3f((float) Math.random(), (float) Math.random(), (float) Math.random());
			glColor3f(1, 1, 1);
			
			do {
				for(int i = 0; i < render3D.size(); i ++) {
					if(render3D.get(i).getRender().getRenderProperties().getRenderStep().index() == currentStep.index()) {
						render3D.get(i).render();
					}
				}
			} while((currentStep = currentStep.nextStep()) != RenderStep.Compleated);
	        
			glDisableClientState(GL_VERTEX_ARRAY); glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);  glDisableClientState(GL_NORMAL_ARRAY);
			
//		    glDisableVertexAttribArray(0); // Position						
//		    glDisableVertexAttribArray(1); // Texture Data
//		    glDisableVertexAttribArray(2); // Color
//		    glDisableVertexAttribArray(3); // Normal
			
		glPopMatrix();
	}
	
	private RenderProperties testRender2DProperties;
	public void render2D() {		
		glMatrixMode(GL_PROJECTION);
			glLoadMatrix(orthographicProjectionMatrix);
        glMatrixMode(GL_MODELVIEW);		
        	glLoadIdentity();       
        
		glPushMatrix();
		
//			glEnableVertexAttribArray(0); // Position
//			glEnableVertexAttribArray(1); // Texture Data
		
			glEnableClientState(GL_VERTEX_ARRAY); 
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			glEnableClientState(GL_VERTEX_ARRAY);
		
			for(int i = 0; i < render2D.size(); i ++) {
				render2D.get(i).render();
			}
			
			glDisableClientState(GL_VERTEX_ARRAY); 
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			glDisableClientState(GL_VERTEX_ARRAY);
			
//			glEnableVertexAttribArray(0); // Position
//			glEnableVertexAttribArray(1); // Texture Data
			
		glPopMatrix();
			
		//TODO: Remove Test Code
		testRender2DProperties.apply(); {
			glPushMatrix();
		
				float x = oneDecimal(ENGINE.getPlayer().getPosition().getX());
				float y = oneDecimal(ENGINE.getPlayer().getPosition().getY());
				float z = oneDecimal(ENGINE.getPlayer().getPosition().getZ());
				
				glTranslatef(0, 540, 0);
				
				org.newdawn.slick.Color col = new org.newdawn.slick.Color(Color.BLUE.getRGB());	        
				
		        font.drawString(0, 0, "FPS: " + ENGINE.getGameName().getFPS() + ", " + ENGINE.getPlayer().getSelectedCube().getX() + " " + ENGINE.getPlayer().getSelectedCube().getY() + " " + ENGINE.getPlayer().getSelectedCube().getZ(), col); // 
		        font.drawString(0, 20, x + "," + y + "," + z, col);
		        font.drawString(0, 40, oneDecimal(ENGINE.getPlayer().getRotation().x) + "," + oneDecimal(ENGINE.getPlayer().getRotation().y) + "," + oneDecimal(ENGINE.getPlayer().getRotation().z), col);
	
			glPopMatrix();
	
	        glDisable(TextureType.Texture2D.glType());
	        		
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
		} testRender2DProperties.reset();
		
	}

	public void setUpShaders() {	
		ShaderRegistry reg = new ShaderRegistry();
		
		reg.addShader(new Shader("BasicRender3DShader", false));
		reg.addShader(new Shader("BasicRender2DShader", false));
		
		ShaderRegistry.addRegistry(reg);
	}
//	
	public static float oneDecimal(double in) {
		return ((float)((int)(in * 10))) / 10f; 	
	}

	public void cleanUp() {		
		
	}

	public void add(IRenderable obj) {
		Render render = obj.getRender();
		
		if(render instanceof Render2D) render2D.add((Render2D) render);
		else if(render instanceof Render3D) render3D.add((Render3D) render);
		else Logger.addLine("Unknown Type: " + render, Logger.ERROR);
	}

	public void remove(IRenderable obj) {
		Render render = obj.getRender();
	
		if(render instanceof Render2D) render2D.remove((Render2D) render);
		else if(render instanceof Render3D) render3D.remove((Render3D) render);
	}

	public void step(float delta) {
		render3D();
		render2D();
	}
}