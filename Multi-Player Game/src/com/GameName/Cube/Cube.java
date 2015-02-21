package com.GameName.Cube;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.GameName.Cube.Collision.DefaultCubeCollisionBox;
import com.GameName.Cube.Collision.ICubeCollisionBox;
import com.GameName.Cube.Render.DefaultCubeRender;
import com.GameName.Cube.Render.ICubeRender;
import com.GameName.Engine.ResourceManager.Materials;
import com.GameName.Engine.Registries.CubeRegistry;
import com.GameName.Physics.Material;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector3f;

public class Cube {
	private static DefaultCubeRender defaultCubeRender;
	private final int DEFAULT_TEXTURE_SIZE = 10;
	private static int nextId;
	
	static {
		defaultCubeRender = new DefaultCubeRender();
	}
	
	private String name;
	private String displayName;
	private int id;
	
	private Material material;
	private ICubeRender render;
	private ICubeCollisionBox collisionBox;
	
	/** Texture Colors: Frame, Face, Colors */
	private int[][][] texture;
	private int frames;
	private Vector3f sheetPosition;
	private Vector3f texturesPerLine, textureSpacing;
	private int textureSize;
	private File textureLocation;
	
	private boolean isLightSorce;
	private float lightValue;
	private float[] lightColor;
	
	private boolean isSolid;
	private boolean isLiquid;
	private boolean isVisable;
	private float opacity;
	
	protected Cube(String name) {
		this.name = name;
		setDisplayName(name);
		
		setSolid(true);
		setLiquid(false);
		setVisable(true);
		setOpacity(1f);
		
		setLightSorce(false);
		setLightValue(0f);
		
		setCollisionBox(getCollisionBox());
		setMaterial(Materials.Stone);
		setRender(getDefaultRender());
		
		loadExtraInfo();
		texture = loadTextures(frames);
	}

	/**
	 * 	Loads all extra information about this cube
	 */	
	private void loadExtraInfo() {
		File extraInfo = new File("res/textures/cubes/info/" + name + ".dtg");
		//TODO: Add animated Textures	 -- Needs testing
		//TODO: Add multi-layer Textures -- Not Started
		//TODO: Add texture sheet loader -- Needs testing
		
		textureSize = 0; textureLocation = null; textureSpacing = null; frames = 0;
		
		if(!extraInfo.exists()) {
			textureSize = DEFAULT_TEXTURE_SIZE;
			textureLocation = new File("res/textures/cubes/" + name + ".png");
			textureSpacing = new Vector3f(1, 1, 0);
			texturesPerLine = new Vector3f(6, 1, 0);
			sheetPosition = new Vector3f(0, 0, 0);
			frames = 1;
			
			return;
		}
		
		try {
			ArrayList<TagGroup> info = DTGLoader.readAll(DTGLoader.getInputStream(extraInfo));
			
			for(TagGroup group : info) {
				if(group.getIdTag().getInfo().equals("textureData")) {
					
					if(group.containsTag("textureSize")) {
						textureSize = (Integer) group.getTagByName("textureSize").getInfo();
					}				
					if(group.containsTag("textureSpacing")) {
						textureSpacing = (Vector3f) group.getTagByName("textureSpacing").getInfo();
					}				
					if(group.containsTag("texturesPerLine")) {
						texturesPerLine = (Vector3f) group.getTagByName("texturesPerLine").getInfo();
					}
					
				} else if(group.getIdTag().getInfo().equals("textureSheet")) {
					
					if(group.containsTag("sheetPosition")) {
						sheetPosition = (Vector3f) group.getTagByName("sheetPosition").getInfo();
					}					
					if(group.containsTag("sheetName")) {
						textureLocation = new File(
								"res/textures/cubes/" + 
								(String) group.getTagByName("sheetName").getInfo()
								+ ".png"
						);
					}
					
				} else if(group.getIdTag().getInfo().equals("textureAnimation")) {
					
					if(group.containsTag("frames")) {
						frames = (Integer) group.getTagByName("frames").getInfo();
					}
					
				}
			}
			
			if(textureSize == 0) textureSize = DEFAULT_TEXTURE_SIZE;
			
			if(sheetPosition == null && textureLocation != null) throw new InvalidParameterException(
					"Cube " + name + " is using a texture sheet, but was not given a sheet position"); 
			if(textureLocation == null) textureLocation = new File("res/textures/cubes/" + name + ".png");
			
			if(textureSpacing == null) textureSpacing = new Vector3f(1, 1, 0);
			if(texturesPerLine == null) texturesPerLine = new Vector3f(6, 1, 0);
			
			if(frames == 0) frames = 1;
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * 	Loads the textures for this cube
	 * 	@param frames How many different frames the cube has
	 */	
	private int[][][] loadTextures(int frames) {
		int[][][] textures = new int[frames][6][textureSize * textureSize];
		
		try {						
			BufferedImage fullTexture = ImageIO.read(textureLocation);
			
			if(fullTexture == null) throw new IOException("Texture == NULL");

			int xStart = (int) ((sheetPosition.x * textureSize) + (sheetPosition.x * textureSpacing.x));
			int yStart = (int) ((sheetPosition.y * textureSize) + (sheetPosition.y * textureSpacing.y));	
			
			for(int frame = 0; frame < frames; frame ++) {
				for(int i = 0; i < textures[frame].length; i ++) {					
					
					int x = i % (int) texturesPerLine.x, y = (int) (i / texturesPerLine.x);					
					int xOffset = (x * textureSize) + (x * (int) textureSpacing.x);
					int yOffset = (y * textureSize) + (y * (int) textureSpacing.y);
					
					for(int posX = 0; posX < textureSize; posX ++) {
					for(int posY = 0; posY < textureSize; posY ++) {
						textures[frame][i][posX + (posY * textureSize)] =
							fullTexture.getRGB(xStart + xOffset + posX, yStart + yOffset + posY);
					}}					
				}
			}
			
			fullTexture = null;
			
			return textures;			
			
		} catch (IOException e) {
			System.err.println("The cube texture " + name + " was not found or successfully loaded");
			
			for(int i = 0; i < textures.length; i ++) {
				int[] toAdd = new int[textureSize * textureSize];
				
				for(int j = 0; j < textureSize * textureSize; j ++) {
					toAdd[j] = j < textureSize || j > textureSize * 2 ? new Color(0, 0, 100).getRGB() : new Color(100, 0, 0).getRGB();
				}
				
				for(int frame = 0; frame < frames; frame ++) {
					textures[frame][i] = toAdd;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 	Returns the default CollisionBox
	 */		
	private ICubeCollisionBox getCollisionBox() {
		return new DefaultCubeCollisionBox();
	}
	
	/**
	 *  Generates the default render for a Cube
	 */
	private ICubeRender getDefaultRender() {
		return defaultCubeRender;
	}
	
	/**
	 * 	Registers the cube with a unique ID
	 */	
	public void concludeInit() {		
		setId(nextId ++);
	}
		
	
	/**
	 * 	Returns the cube's basic information as a String
	 */	
	public String toString() {
		return name + " [Id=" + id + ", DisplayName=" + displayName + "]";
	} 
	
	/**
	 * Returns the name of this cube
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name that this cube displays in game
	 * @param metadata The metadata of the cube	
	 */
	public String getDisplayName(int metadata) {
		return displayName;
	}

	/**
	 * Returns the id of this cube
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns all the possible textures of this cube <br>
	 * 			Frame, Face, Colors
	 */
	public int[][][] getTextures() {
		return texture;
	}

	/**
	 * Returns whether this cube emits light
	 * @param metadata The metadata of the cube	
	 */
	public boolean isLightSorce(int metadata) {
		return isLightSorce;
	}

	/**
	 * Returns the intensity of the light this cube emits
	 * @param metadata The metadata of the cube	
	 */
	public float getLightValue(int metadata) {
		return lightValue;
	}

	/**
	 * Returns the light color this cube emits
	 * @param metadata The metadata of the cube	
	 */
	public float[] getLightColor(int metadata) {
		return lightColor;
	}

	/**
	 * Returns if this cube can be collided with
	 * @param metadata The metadata of the cube	
	 */
	public boolean isSolid(int metadata) {
		return isSolid;
	}
	
	/**
	 * Returns if this cube is a liquid
	 * @param metadata The metadata of the cube	
	 */
	public boolean isLiquid(int metadata) {
		return isLiquid;
	}

	/**
	 * Returns if this cube can be rendered
	 * @param metadata The metadata of the cube	
	 */
	public boolean isVisable(int metadata) {
		return isVisable;
	}

	/**
	 * Returns the opacity of this cube
	 * @param metadata The metadata of the cube	
	 */
	public float getOpacity(int metadata) {
		return opacity;
	}
	
	/**
	 * Returns the Texture size for this cube
	 * @param metadata The metadata of the cube
	 * 
	 */
	public int getTextureSize() {
		return textureSize;
	}
	
	/**
	 * Returns the number of Frames this cube has	
	 */
	public int getFrames() {
		return frames;
	}
	
	/**
	 * Returns the Frame ID from metadata
	 * @param metadata The metadata of the cube
	 */
	public int getFrameFromMetadata(int metadata) {
		return metadata;
	}
	
	/**
	 * Returns the CollisionBox for this cube
	 * @param metadata The metadata of the cube	
	 */
	public ICubeCollisionBox getCollisionBox(int metadata) {
		return collisionBox;
	}
	
	/**
	 * Returns the render for this cube
	 * @param metadata The metadata of the cube
	 */
	public ICubeRender getRender(int metadata) {
		return render;
	}
	
	/**
	 * Returns the Material for this cube
	 * @param metadata The metadata of the cube
	 */
	public Material getMaterial(int metadata) {
		return material;
	}
	
	/**
	 * 	Sets whether or not this cube emits light
	 */	
	protected void setLightSorce(boolean isLightSorce) {
		this.isLightSorce = isLightSorce;
	}

	/**
	 * 	Sets the intensity of the light that this cube emits
	 */	
	protected void setLightValue(float lightValue) {
		this.lightValue = lightValue;
	}

	/**
	 * 	Sets the color of light that this cube will emit 
	 */	
	protected void setLightColor(float[] lightColor) {
		this.lightColor = lightColor;
	}

	/**
	 * 	Sets whether or not this cube is solid (Used for Physics)
	 */	
	protected void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}
	
	/**
	 * 	Sets whether or not this cube is a liquid (Used for Physics)
	 */	
	protected void setLiquid(boolean isLiquid) {
		this.isLiquid = isLiquid;
	}

	/**
	 * 	Sets whether or not this cube should be rendered
	 */	
	protected void setVisable(boolean isVisable) {
		this.isVisable = isVisable;
	}
	
	/**
	 * 	Sets the Opacity for this cube
	 */	
	protected void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	/**
	 * 	Sets the resolution for this cube
	 */	
	protected void setTextureSize(int textureSize) {
		this.textureSize = textureSize;
	}
	
	/**
	 * 	Sets the name that will be displayed in game for this cube
	 */	
	protected void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * 	Sets the CollisionBox for this cube
	 */	
	protected void setCollisionBox(ICubeCollisionBox collisionBox) {
		this.collisionBox = collisionBox;
	}

	/**
	 *  Sets the Render for this cube
	 */
	protected void setRender(ICubeRender render) {
		this.render = render;
	}
	
	/**
	 *  Sets the Material for this cube
	 */
	protected void setMaterial(Material material) {
		this.material = material;
	}
	
	/**
	 * 	Sets the ID of this cube
	 */	
	private void setId(int id) {
		this.id = id;
	}

	/**
	 * 	Returns a cube based on its ID
	 * 	If the ID is -1 it returns NULL
	 */	
	public static Cube getCubeByID(int id) {
		if(id == -1) return null;
		return CubeRegistry.getCubes()[id];
	}
	
	/**
	 * 	Returns a cube array based on a set of id's
	 */	
	public static Cube[] getCubesByID(int... ids) {
		Cube[] cubes = new Cube[ids.length];
		
		for(int i = 0; i < ids.length; i ++) {
			cubes[i] = getCubeByID(ids[i]); //CubeRegistry.getCubes()[ids[i]];
		}
		
		return cubes;
	}

	/**
	 * 	Returns an array of all cubes
	 */	
	public static Cube[] getCubes() {
		return CubeRegistry.getCubes();
	}
		
	public static void cleanUp() {
		
	}
}
