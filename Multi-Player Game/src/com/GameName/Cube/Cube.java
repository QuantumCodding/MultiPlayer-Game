package com.GameName.Cube;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashSet;

import javax.imageio.ImageIO;

import com.GameName.Cube.Cubes.AirCube;
import com.GameName.Cube.Cubes.ColorfulTestCube;
import com.GameName.Cube.Cubes.CopperCube;
import com.GameName.Cube.Cubes.GoldCube;
import com.GameName.Cube.Cubes.StoneCube;
import com.GameName.Cube.Cubes.TestCube;
import com.GameName.Cube.Render.DefaultCubeRender;
import com.GameName.Cube.Render.ICubeRender;
import com.GameName.Physics.Coalition.BoundingArea;
import com.GameName.Physics.Coalition.BoundingBox;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3f;

public class Cube {
	private static DefaultCubeRender defaultCubeRender;
	
	static {
		defaultCubeRender = new DefaultCubeRender();
	}
	
	private final int DEFAULT_TEXTURE_SIZE = 10;
	
	private String name;
	private String displayName;
	private int id;
	
	private BoundingArea boundingArea;
	private ICubeRender render;
	
	/** Texture Colors: Frame, Face, Colors */
	private int[][][] texture;
	private int frames;
	private Vector2f sheetPosition;
	private Vector2f texturesPerLine, textureSpacing;
	private int textureSize;
	private File textureLocation;
	
	private boolean isLightSorce;
	private float lightValue;
	private float[] lightColor;
	
	private boolean isSolid;
	private boolean isVisable;
	private float opacity;
				
	public static Cube Air = new AirCube();
	
	public static Cube TestCube = new TestCube();
	public static Cube ColorfulTestCube = new ColorfulTestCube();
	
	public static Cube StoneCube = new StoneCube();
	
	public static Cube GoldCube = new GoldCube();
	public static Cube CopperCube = new CopperCube();
	
	protected Cube(String name) {
		this.name = name;
		setDisplayName(name);
		
		setSolid(true);
		setVisable(true);
		setOpacity(1f);
		
		setLightSorce(false);
		setLightValue(0f);
		
		setBoundingArea(getDefaultBoundingArea());
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
			textureSpacing = new Vector2f(1, 1);
			texturesPerLine = new Vector2f(6, 1);
			sheetPosition = new Vector2f(0, 0);
			frames = 1;
			
			return;
		}
		
		try {
			HashSet<TagGroup> info = DTGLoader.readDTGFile(extraInfo);
			
			for(TagGroup group : info) {
				if(group.getIdTag().getTagInfo().equals("textureData")) {
					
					if(group.containsTag("textureSize")) {
						textureSize = (Integer) group.getTagByName("textureSize").getTagInfo();
					}				
					if(group.containsTag("textureSpacing")) {
						textureSpacing = (Vector2f) group.getTagByName("textureSpacing").getTagInfo();
					}				
					if(group.containsTag("texturesPerLine")) {
						texturesPerLine = (Vector2f) group.getTagByName("texturesPerLine").getTagInfo();
					}
					
				} else if(group.getIdTag().getTagInfo().equals("textureSheet")) {
					
					if(group.containsTag("sheetPosition")) {
						sheetPosition = (Vector2f) group.getTagByName("sheetPosition").getTagInfo();
					}					
					if(group.containsTag("sheetName")) {
						textureLocation = new File(
								"res/textures/cubes/" + 
								(String) group.getTagByName("sheetName").getTagInfo()
								+ ".png"
						);
					}
					
				} else if(group.getIdTag().getTagInfo().equals("textureAnimation")) {
					
					if(group.containsTag("frames")) {
						frames = (Integer) group.getTagByName("frames").getTagInfo();
					}
					
				}
			}
			
			if(textureSize == 0) textureSize = DEFAULT_TEXTURE_SIZE;
			
			if(sheetPosition == null && textureLocation != null) throw new InvalidParameterException(
					"Cube " + name + " is using a texture sheet, but was not given a sheet position"); 
			if(textureLocation == null) textureLocation = new File("res/textures/cubes/" + name + ".png");
			
			if(textureSpacing == null) textureSpacing = new Vector2f(1, 1);
			if(texturesPerLine == null) texturesPerLine = new Vector2f(6, 1);
			
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

			int xStart = (int) ((sheetPosition.getX() * textureSize) + (sheetPosition.getX() * textureSpacing.getX()));
			int yStart = (int) ((sheetPosition.getY() * textureSize) + (sheetPosition.getY() * textureSpacing.getY()));	
			
			for(int frame = 0; frame < frames; frame ++) {
				for(int i = 0; i < textures[frame].length; i ++) {					
					
					int x = i % (int) texturesPerLine.getX(), y = (int) (i / texturesPerLine.getX());					
					int xOffset = (x * textureSize) + (x * (int) textureSpacing.getX());
					int yOffset = (y * textureSize) + (y * (int) textureSpacing.getY());
					
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
	 * 	Returns the default BoundingArea
	 */		
	private BoundingArea getDefaultBoundingArea() {
		BoundingArea bound = new BoundingArea();		
		bound.add(new BoundingBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));
		
		return bound;
	}
	
	/**
	 *  Generates the default render for a Cube
	 */
	private ICubeRender getDefaultRender() {
		return defaultCubeRender;
	}
	
	/**
	 * 	Registers all default cubes
	 */	
	public static void regesterCubes() {
		CubeRegistry regestry = new CubeRegistry();
		
		regestry.addCube(Air); regestry.addCube(TestCube); regestry.addCube(ColorfulTestCube); 		
		regestry.addCube(StoneCube);		
		regestry.addCube(GoldCube); regestry.addCube(CopperCube);
		
		CubeRegistry.addRegistry(regestry);
	}
	
	/**
	 * 	Registers all cubes and generates the texture sheet
	 */	
	public static void concludeInit() {		
		for(int i = 0; i < CubeRegistry.getCubes().length; i ++) {
			CubeRegistry.getCubes()[i].setId(i);
		}
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
	 * Returns the BoundingArea for this cube
	 * @param metadata The metadata of the cube	
	 */
	public BoundingArea getBoundingArea(int metadata) {
		return boundingArea;
	}
	
	/**
	 * Returns the render for this cube
	 * @param metadata The metadata of the cube
	 */
	public ICubeRender getRender(int metadata) {
		return render;
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
	 * 	Sets the BoundingArea for this cube (Used for coalition detection)
	 */	
	protected void setBoundingArea(BoundingArea boundingArea) {
		this.boundingArea = boundingArea;
	}

	/**
	 *  Sets the Render for this cube
	 */
	protected void setRender(ICubeRender render) {
		this.render = render;
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
