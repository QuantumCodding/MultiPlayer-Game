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
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import com.GameName.Render.Effects.Color.ColorFormat;
import com.GameName.Util.Vectors.Vector3f;

public class Texture2D extends Texture {
	public Texture2D(BufferedImage image, boolean useMipmap) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // 4 Because of RGBA

		 for(int y = 0; y < image.getHeight(); y ++) {
		    for(int x = 0; x < image.getWidth(); x ++) {
		        buffer.putInt(Color.flipByteOrder(Color.convert(image.getRGB(x, y), ColorFormat.ARGB, ColorFormat.RGBA)));
		    }
		}

		buffer.flip();		
		textureId = glGenTextures();	    		

		boolean GL_Texture_2D_state = glIsEnabled(GL_TEXTURE_2D);
		if(!GL_Texture_2D_state) glEnable(GL_TEXTURE_2D);

		bind();
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		unbind();

		size = new Vector3f(image.getWidth(), image.getHeight(), 1);
		
		setToDefaultProperties();
		if(useMipmap) setupMipMaping();

		if(!GL_Texture_2D_state) glDisable(GL_TEXTURE_2D);
	}

	public Texture2D(String textureLocation, boolean useMipmap) throws IOException {
		super(textureLocation, useMipmap);
	}

	protected void setToDefaultProperties() {
		bind();
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		unbind();
	}
	
	protected void setupMipMaping() {
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

	public TextureType getType() {
		return TextureType.Texture2D;
	}
}
