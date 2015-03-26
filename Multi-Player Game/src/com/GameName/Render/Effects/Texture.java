package com.GameName.Render.Effects;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.GameName.Util.Vectors.Vector3f;

public abstract class Texture implements IRenderEffect {
	protected int textureId;
	protected Vector3f size;
	
	/**
	 * Creates a new Texture for a file location and without MipMapping 
	 * 
	 * @param location The textures location as a String
	 * @throws IOException 
	 */
	public Texture(String location) throws IOException { 
		this(location, false); 
	}
	
	/**
	 * Creates a new Texture from a file location and whether or not to use MipMapping
	 * 
	 * @param location The textures location as a String
	 * @param useMipmap Whether or not the texture should use MipMapping
	 * 
	 * @throws IOException 
	 */
	public Texture(String location, boolean useMipmap) throws IOException {
		this(ImageIO.read(new File(location)), useMipmap);
	}
	
	/**
	 * Takes a BufferedImage and converts it into a Texture in RGBA format
	 * 
	 * @param image The BufferedImage to convert into a texture
	 * @param useMipmap
	 */
	public Texture(BufferedImage image, boolean useMipmap) {}
	
	/**
	 * Creates a Texture from a preload texture
	 * 
	 * @param textureId The texture Id
	 * @param textureName The Textures Name
	 * @param width  The width of the Texture
	 * @param height The height of the Texture
	 */
	public Texture(int textureId, Vector3f size) {
		this.textureId = textureId;
		this.size = size;
	}
	
	protected Texture() {}
	
	protected abstract void setToDefaultProperties();	
	protected abstract void setupMipMaping();
	public abstract TextureType getType();
	
	public int getTextureId() { return textureId; }
	
	public int getWidth() { return (int) size.x; }
	public int getHeight() { return (int) size.y; }
	public int getDepth() { return (int) size.z; }
	
	public static void unbind() {
		for(TextureType type : TextureType.values()) {
			glBindTexture(type.glType(), 0);
		}
	}
	
	public void cleanUp() {
		glDeleteBuffers(textureId);
	}
	
	public static enum TextureType {
		Texture1D(GL_TEXTURE_1D, 1), 
		Texture2D(GL_TEXTURE_2D, 2), 
		Texture3D(GL_TEXTURE_3D, 3);
		
		private int glId, cordCount;
		private TextureType(int glId, int cordCount) {
			this.glId = glId;
			this.cordCount = cordCount;
		}
		
		public int glType() { return glId; }
		public int getCoordCount() { return cordCount; }
		
	}
}
