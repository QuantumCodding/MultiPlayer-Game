package com.GameName.Render.Effects;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
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
		textureName = location.contains("/") ? location.substring(location.lastIndexOf("/") + 1) : 
					location.contains("\\") ? location.substring(location.lastIndexOf("\\") + 1) : location;
					
		textureName = textureName.contains(".") ? textureName.substring(0, textureName.lastIndexOf(".")) : textureName;	
		location = location.contains(".") ? location.substring(0, location.lastIndexOf(".")) : location;
		
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
	
	/**
	 * Creates a Texture from a preload texture
	 * 
	 * @param textureId The texture Id
	 * @param textureName The Textures Name
	 * @param width  The width of the Texture
	 * @param height The height of the Texture
	 */
	public Texture(int textureId, String textureName, int width, int height) {
		this.textureId = textureId;
		this.textureName = textureName;
		
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Takes a BufferedImage and converts it into a Texture in RGBA format
	 * 
	 * @param image The BufferedImage to convert into a texture
	 * @param useMipmap
	 */
	public Texture(BufferedImage image, String name, boolean useMipmap) {	
		this.textureName = name;
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // 4 Because of RGBA
		
		 for(int y = 0; y < image.getHeight(); y ++) {
            for(int x = 0; x < image.getWidth(); x ++) {	            	
                int pixel = pixels[y * image.getWidth() + x];
                
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));             // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));     // Alpha component. Only for RGBA
            }
        }

	    buffer.flip();		
	    textureId = glGenTextures();	    		
	    
	    boolean GL_Texture_2D_state = glIsEnabled(GL_TEXTURE_2D);
	    if(!GL_Texture_2D_state) glEnable(GL_TEXTURE_2D);
	    
	    bind();
	    	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		unbind();
	    
		width = image.getWidth();
		height = image.getHeight();
		
		setToDefaultProperties();
		if(useMipmap) setupMipMaping();

	    if(!GL_Texture_2D_state) glDisable(GL_TEXTURE_2D);
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
