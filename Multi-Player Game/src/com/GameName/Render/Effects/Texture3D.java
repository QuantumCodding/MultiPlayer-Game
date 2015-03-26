package com.GameName.Render.Effects;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetTexImage;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL12.glTexImage3D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.GameName.Render.Effects.Color.ColorFormat;
import com.GameName.Util.Side;
import com.GameName.Util.Vectors.Vector3f;

public class Texture3D extends Texture {
	public Texture3D(ByteBuffer buffer, Vector3f size, boolean useMipmap) {
		super();	
		textureId = glGenTextures();	    		

		boolean GL_Texture_3D_state = glIsEnabled(GL_TEXTURE_3D);
		if(!GL_Texture_3D_state) glEnable(GL_TEXTURE_3D);

		bind();
			glTexImage3D(GL_TEXTURE_3D, 0, GL_RGBA, (int) size.x, (int) size.y, (int) size.z, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		unbind();

		this.size = size;
		
		setToDefaultProperties();
		if(useMipmap) setupMipMaping();

		if(!GL_Texture_3D_state) glDisable(GL_TEXTURE_3D);
	}

	protected void setToDefaultProperties() {
		bind();
		
		glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_R, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		unbind();
	}
	
	protected void setupMipMaping() {
		bind();
		
		glGenerateMipmap(GL_TEXTURE_3D);
		
		glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
		glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_NEAREST_MIPMAP_NEAREST);
		
		unbind();
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_3D, textureId);
	}
	
	public static void unbind() {
		glBindTexture(GL_TEXTURE_3D, 0);
	}

	public TextureType getType() {
		return TextureType.Texture3D;
	}
	
	public void save(File dir) {
		if(!dir.exists()) new File(dir.getParent()).mkdirs(); 
		
		ByteBuffer buffer = BufferUtils.createByteBuffer((int) (size.x * size.y * size.z * 4));
		
		glEnable(GL_TEXTURE_3D); bind();
		glGetTexImage(GL_TEXTURE_3D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glDisable(GL_TEXTURE_3D);
		
		for(int depth = 0; depth < getDepth(); depth ++) {
			File saveLoc = new File(dir + "-" + depth + ".png");
			BufferedImage writeImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			for(int x = 0; x < getWidth();  x ++) {
			for(int y = 0; y < getHeight(); y ++) {
				writeImg.setRGB(x, y, Color.convert(Color.flipByteOrder(buffer.getInt()),
						ColorFormat.RGBA, ColorFormat.ARGB));
			}}
			
			try {
				ImageIO.write(writeImg, "png", saveLoc);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class Texture3DLoader {
		private Vector3f size;
		private ByteBuffer buffer;
		
		public Texture3DLoader(Vector3f size) {
			this.size = size;
			buffer = BufferUtils.createByteBuffer((int) (size.x * size.y * size.z * 4));
		}
		
		public ByteBuffer loadColorFilledTexture(Color fillColor, BufferedImage[] sides) {
			int fillColorRGB = fillColor.get(ColorFormat.ARGB);
			
			for(int x = 0; x < size.x; x ++) {
			for(int y = 0; y < size.y; y ++) {
			for(int z = 0; z < size.z; z ++) {
				int pixel = fillColorRGB;
				
				if(z == 0) pixel = sides[Side.BackFace.index()].getRGB(x, y); 	else 	
				if(y == 0) pixel = sides[Side.BottomFace.index()].getRGB(x, z);	else 	
				if(x == 0) pixel = sides[Side.LeftFace.index()].getRGB(z, y); 	else 
				if(z == size.z-1) pixel = sides[Side.FrontFace.index()].getRGB(x, y); else 	
				if(y == size.y-1) pixel = sides[Side.TopFace.index()].getRGB(x, z);   else 	
				if(x == size.x-1) pixel = sides[Side.RightFace.index()].getRGB(z, y);
					
				buffer.putInt(Color.flipByteOrder(Color.convert(pixel, ColorFormat.ARGB, ColorFormat.RGBA)));
			}}}
			
			buffer.flip();
			return buffer;
		}
		
		public ByteBuffer loadLayeredTexture(BufferedImage[] layers) {
			for(int x = 0; x < size.x; x ++) {
			for(int y = 0; y < size.y; y ++) {
			for(int layer = 0; layer < size.z; layer ++) {
				buffer.putInt(Color.flipByteOrder(Color.convert(layers[layer].getRGB(x, y), 
						ColorFormat.ARGB, ColorFormat.RGBA)));
			}}}
			
			buffer.flip();
			return buffer;
		}
		
		
	}
}
