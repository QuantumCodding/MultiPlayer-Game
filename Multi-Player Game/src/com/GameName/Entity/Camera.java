package com.GameName.Entity;

import static org.lwjgl.opengl.ARBDepthClamp.GL_DEPTH_CLAMP;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.opengl.GLContext;

public class Camera {
	public float x;
	public float y;
	public float z;
	
	public float rotX;
	public float rotY;
	public float rotZ;
	
	private float fov;
	private float aspect;
	private float near;
	private float farRender;
	
	public Camera(float fov, float aspect, float near, float farRender)
	{		
		this.fov = fov;
		this.aspect = aspect;
		this.near = near;
		this.farRender = farRender;
				
	}
	
	public void applyOrthographicMatrix() {
        glPushAttrib(GL_TRANSFORM_BIT);
        
	        glMatrixMode(GL_PROJECTION);
	        glLoadIdentity();
	        glOrtho(-aspect, aspect, -1, 1, 0, farRender);
	        
        glPopAttrib();
    }
	
	public void applyPerspectiveMatrix() {
        glPushAttrib(GL_TRANSFORM_BIT);
        
	        glMatrixMode(GL_PROJECTION);
	        glLoadIdentity();
	        gluPerspective(fov, aspect, near, farRender);
	        
        glPopAttrib();
    }
	
	public void applyOptimalStates() {
        if (GLContext.getCapabilities().GL_ARB_depth_clamp) {
            glEnable(GL_DEPTH_CLAMP);
        }
    }
	
	public void useView() {
		glPushAttrib(GL_TRANSFORM_BIT);
			glMatrixMode(GL_MODELVIEW);
			
			glRotatef(rotX, 1, 0, 0);
			glRotatef(rotY, 0, 1, 0);
			glRotatef(rotZ, 0, 0, 1);
			
			glTranslatef(x, y, z);
		glPopAttrib();
	}
	
	public void moveZ(float amt) { //((rotZ % 180 > 45) && (rotZ % 180 < 135) ? 180 : 0)
		z += amt * Math.sin(Math.toRadians(rotY + 90));
		x += amt * Math.cos(Math.toRadians(rotY + 90));
	}

	public void moveX(float amt) {
		z += amt * Math.sin(Math.toRadians(rotY));
		x += amt * Math.cos(Math.toRadians(rotY));
	}

	public void rotateY(float amt) {
		rotY += amt;
	}

	public void rotateX(float amt) {
		rotX += amt;
//		int max = 220, min = 130;
//			
//			if(rotX <= max && rotX >= min) 
//				rotX += amt;
//			else if(rotX >= min) 
//				rotX = max; 
//			else 
//				rotX = min;
	}

	public void rotateZ(float amt) {
		rotZ += amt;
	}
	
	public void moveUp(float amt) {
		y += amt;
	}	
}
