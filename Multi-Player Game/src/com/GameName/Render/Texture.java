package com.GameName.Render;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import org.newdawn.slick.opengl.TextureLoader;

public class Texture {
	private int textureId;
	private int width, height;
	
	public Texture(File location) { this(location, false); }
	
	public Texture(File location, boolean useMipmap) {
		try {
			org.newdawn.slick.opengl.Texture slickTexture = TextureLoader.getTexture("PNG", new FileInputStream(location));
			
			textureId = slickTexture.getTextureID();
			width = slickTexture.getTextureWidth();
			height = slickTexture.getTextureHeight();
			
			setToDefaultProperties();
			if(useMipmap) setupMipMaping();
			
		} catch (IOException e) {
			System.err.println("Texture " + location + " failed to load");			
			e.printStackTrace();
		}
	}
	
	private void setToDefaultProperties() {
		bind();
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		unbind();
	}
	
	private void setupMipMaping() {
		bind();
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST_MIPMAP_NEAREST);
		
		unbind();
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getTextureId() {
		return textureId;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
