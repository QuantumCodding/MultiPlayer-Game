package com.GameName.Render.Effects;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.TextureLoader;

public class Texture {
	private String textureName;
	private int textureId;
	private int width, height;
	
	/**
	 * Creates a new Texture for a file location and without MipMapping 
	 * 
	 * @param location The textures location as a String
	 */
	public Texture(String location) { 
		this(location, false); 
	}
	
	/**
	 * Creates a new Texture from a file location and whether or not to use MipMapping
	 * 
	 * @param location The textures location as a String
	 * @param useMipmap Whether or not the texture should use MipMapping
	 */
	public Texture(String location, boolean useMipmap) {
		this(location, useMipmap, "PNG");
	}
	
	/**
	 * Creates a new Texture from a file location and whether or not to use MipMapping
	 * 
	 * @param location The textures location as a String
	 * @param useMipmap Whether or not the texture should use MipMapping
	 * @param fileType What the file extension is
	 */
	public Texture(String location, boolean useMipmap, String fileType) {
		textureName = location.substring(location.lastIndexOf("/"));
		
		try {
			org.newdawn.slick.opengl.Texture slickTexture = getTexture(location, fileType);
			
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
	
	private org.newdawn.slick.opengl.Texture getTexture(String fileLocation, String fileType) throws IOException {
		File location = new File(fileLocation + "." + fileType.toLowerCase());
		
		switch(fileType.toUpperCase()) {
			case "PNG": 
			case "JPG":
			case "BMP":
				return TextureLoader.getTexture(fileType, new FileInputStream(location));
			
			default: System.err.println(fileType.toUpperCase() + " is not supported"); return null;
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

	protected String getTextureName() {
		return textureName;
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
	
	public void cleanUp() {
		glDeleteBuffers(textureId);
	}
}
