package com.GameName.Render.Effects;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glTexImage1D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import com.GameName.Render.Effects.Color.ColorFormat;
import com.GameName.Util.Vectors.Vector3f;

public class Texture1D extends Texture {
	public Texture1D(BufferedImage image, boolean useMipmap) {
		super();
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * 4); // 4 Because of RGBA

	    for(int x = 0; x < image.getWidth(); x ++) {	            	
	    	buffer.putInt(Color.flipByteOrder(Color.convert(image.getRGB(x, 0), ColorFormat.ARGB, ColorFormat.RGBA)));
	    }

		buffer.flip();		
		textureId = glGenTextures();	    		

		boolean GL_Texture_1D_state = glIsEnabled(GL_TEXTURE_1D);
		if(!GL_Texture_1D_state) glEnable(GL_TEXTURE_1D);

		bind();
			glTexImage1D(GL_TEXTURE_1D, 0, GL_RGBA, image.getWidth(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		unbind();

		size = new Vector3f(image.getWidth(), 1, 1);
		
		setToDefaultProperties();
		if(useMipmap) setupMipMaping();

		if(!GL_Texture_1D_state) glDisable(GL_TEXTURE_1D);
	}

	protected void setToDefaultProperties() {
		bind();
		
		glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		unbind();
	}
	
	protected void setupMipMaping() {
		bind();
		
		glGenerateMipmap(GL_TEXTURE_1D);
		
		glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
		glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST_MIPMAP_NEAREST);
		
		unbind();
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_1D, textureId);
	}
	
	public static void unbind() {
		glBindTexture(GL_TEXTURE_1D, 0);
	}

	public TextureType getType() {
		return TextureType.Texture1D;
	}
}