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
import com.GameName.Main.Debugging.Logger;
import com.GameName.Physics.Coalition.BoundingArea;
import com.GameName.Physics.Coalition.BoundingBox;
import com.GameName.Render.Effects.Texture;
import com.GameName.Render.Effects.TextureRegistry;
import com.GameName.Util.Time;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3f;

public class Cube {
	private static DefaultCubeRender defaultCubeRender;
	
	/**
	 * Use TextureRegistry.accessByName("Cube Texture")
	 */
	@Deprecated
	private static Texture textureSheet;
	private static int textureSheetSideLength;
	private static int textureSheetMaxFrames;
	
	static {
		defaultCubeRender = new DefaultCubeRender();
	}
	
	private final int TEXTURE_SIZE = 10;
	
	private String name;
	private String displayName;
	private int id;
	
	private BoundingArea boundingArea;
	private ICubeRender render;
	
	//Texture Colors: Frame, Face, Colors
	private int[][][] texture;
	private int frames;
	private Vector2f sheetPosition;
	private Vector2f texturesPerLine, textureSpacing;
	private File textureLocation;
	
	private boolean isLightSorce;
	private float lightValue;
	private float[] lightColor;
	
	private boolean isSolid;
	private boolean isVisable;
	private float opacity;
	
	private int textureSize;
	
	private static int[] cubeTexCoords;
				
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
			textureSize = TEXTURE_SIZE;
			textureLocation = new File("res/textures/cubes/" + name + ".png");
			textureSpacing = new Vector2f(1, 0);
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
			
			if(textureSize == 0) textureSize = TEXTURE_SIZE;
			
			if(sheetPosition == null && textureLocation != null) throw new InvalidParameterException(
					"Cube " + name + " is using a texture sheet, but was not given a sheet position"); 
			if(textureLocation == null) textureLocation = new File("res/textures/cubes/" + name + ".png");
			
			if(textureSpacing == null) textureSpacing = new Vector2f(1, 0);
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
		int[][][] textures = new int[frames][6][];
		
		try {						
			BufferedImage fullTexture = ImageIO.read(textureLocation);
			
			if(fullTexture == null) throw new IOException("Texture == NULL");
			
			for(int frame = 0; frame < frames; frame ++) {
				for(int i = 0; i < textures[frame].length; i ++) {
					
					int xStart = (int) ((sheetPosition.getX() * textureSize) + (sheetPosition.getX() * textureSpacing.getX()));
					int yStart = (int) ((sheetPosition.getY() * textureSize) + (sheetPosition.getY() * textureSpacing.getY()));
					
					for(int x = 0; x < texturesPerLine.getX(); x ++) {
					for(int y = 0; y < texturesPerLine.getY(); y ++) {		
					
						int xOffset = (x * textureSize) + (x * (int) textureSpacing.getX());
						int yOffset = (y * textureSize) + (y * (int) textureSpacing.getY());
						
						textures[frame][i] = getImageAsColors(fullTexture.getSubimage(
							xStart + xOffset, 	yStart + yOffset, 						
							textureSize, 		textureSize)
						);
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
		
		try {
			File saveLoc = new File("res/textures/cubes/TextureSheet.png");
			
			ImageIO.write(generateTextureSheet(), "PNG", saveLoc);
			textureSheet = new Texture(saveLoc.getAbsolutePath()); //TextureLoader.getTexture("PNG",  new FileInputStream(new File("res/textures/cubes/MapingSheet.png")));//
			
			TextureRegistry reg = new TextureRegistry();			
			reg.addTexture(textureSheet);
			TextureRegistry.addRegistry(reg);
			
		} catch (IOException e) {
			e.printStackTrace();
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
	 * Returns all the possible textures of this cube
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
	 * 	Returns an int[] of all the colors in a BufferedImage
	 */
	private static int[] getImageAsColors(BufferedImage image) {
	    int width = image.getWidth();
	    int height = image.getHeight();
	    	    
	    int[] toRep = new int[width * height];
	    
	    for(int x = 0; x < width; x ++) {
	    	for(int y = 0; y < height; y ++) {
	    		toRep[x + (y * width)] = image.getRGB(x, y);
	    	}
	    }
		      
	    return toRep;
	}
	
	/**
	 * 	Creates a BufferedImage of all cube textures laded out in a sheet that is a perfect square
	 */	
	private static BufferedImage generateTextureSheet() {
		long startTime = Time.getTime(); //TODO: Remove Timer
		
		int totalArea = 0;
		int maxTextureSize = 0;
		
		Cube[] cubes = CubeRegistry.getCubes();
		
		for(Cube cube : cubes) {			
			totalArea += Math.pow(cube.getTextureSize(), 2) * 6 * cube.getFrames();
			
			if(textureSheetMaxFrames < cube.getTextureSize()) textureSheetMaxFrames = cube.getFrames();
			if(maxTextureSize < cube.getTextureSize()) maxTextureSize = cube.getTextureSize();
		}
		
		int sideLength = (int) Math.ceil(Math.sqrt(totalArea));
		sideLength = sideLength < maxTextureSize * 6 ? maxTextureSize * 6 : sideLength;
		
		while((sideLength & -sideLength) != sideLength) {
			sideLength ++;
		}
		
		BufferedImage spriteSheetImage = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_RGB);
		int[][] spriteSheet = new int[sideLength][sideLength];
		textureSheetSideLength = sideLength;
		
		int emptyColor = new Color(15, 4, 15).getRGB();
		
		for(int x = 0; x < sideLength; x ++) {
			for(int y = 0; y < sideLength; y ++) {
				spriteSheet[x][y] = emptyColor;
			}
		}
		
		cubeTexCoords = new int[cubes.length * textureSheetMaxFrames * 2];
		
		int cubesAdded = 0;
		int index = 0;		
		boolean[] added = new boolean[cubes.length * textureSheetMaxFrames];
		int x = 0, y = 0;
		boolean hitEnd = false;
		
		for(Cube cube : cubes) {
			for(int i = 0; i < textureSheetMaxFrames; i ++) {
				added[i] = cube.frames < i;
			}
		}
				
		while(cubesAdded < added.length) {
			if(index >= added.length) {
				index = 0;
				
				while(added[index]) {
					index ++;
				}
				
				do {
					if(x < sideLength - 1) {
						x ++;
						
					} else if(y < sideLength - 1) {
						y ++;
						x = 0;
						
					} else {
						if(hitEnd) {
							throw new IndexOutOfBoundsException("Failed to Creat Sprite Sheet");
						}
						
						y = 0;
						x = 0;
						
						hitEnd = true;
					}
				} while(spriteSheet[x][y] != emptyColor);
				
				hitEnd = false;
			}
			
			while(added[index]) {
				index ++;
			}
			
			Cube cube = cubes[index / textureSheetMaxFrames];
			
			if(cube.getTextureSize() + y >= sideLength) {
				index ++;
				continue;
			}
			
			if(cube.getTextureSize() * 6 + x < sideLength) {
				for(int i = 0; i < 6; i ++) {
					for(int j = 0; j < cube.getTextureSize(); j ++) {
						for(int k = 0; k < cube.getTextureSize(); k ++) {
							spriteSheet[(i * cube.getTextureSize()) + (x + j)][y + k]
								= cube.getTextures()
								[index % textureSheetMaxFrames]
										[i]
												[j + (k * cube.getTextureSize())];
						}						
					}
				}
				
				cubeTexCoords[index * 2] = x;
				cubeTexCoords[(index * 2) + 1] = y;
				
				x += cube.getTextureSize() * 6;
				added[index] = true;
				
				index ++;
				cubesAdded ++;
				
				continue;
			}
			
			index ++;
		}
		
		for(x = 0; x < sideLength; x ++) {
			for(y = 0; y < sideLength; y ++) {
				spriteSheetImage.setRGB(x, y, spriteSheet[x][y]);
			}
		}
		
		Logger.print("Texture Sheet made in: " + ((double) (Time.getTime() - startTime) / (double) Time.getSECONDS())).setType("Setup").end(); //TODO: Remove Timer
		return spriteSheetImage;
	}
	
	/**
	 * 	Returns an array of all the texture coordinates for each cube
	 */	
	public static int[] getCubeTexCoords() {
		return cubeTexCoords;
	}

	/**
	 * 	Returns the Texture Sheet
	 *	@see TextureRegistry.accessByName("Cube Texture")
	 */	
	@Deprecated
	public static Texture getTextureSheet() {
		return textureSheet;
	}

	/**
	 * 	Returns the size of the side of the Texture sheet
	 */	
	public static int getTextureSheetSideLength() {
		return textureSheetSideLength;
	}

	/**
	 * 	Returns the maximum number of frames a cube can has
	 */	
	public static int getTextureSheetMaxFrames() {
		return textureSheetMaxFrames;
	}

	/**
	 * 	Returns a cube based on its ID
	 */	
	public static Cube getCubeByID(int id) {
		return CubeRegistry.getCubes()[id];
	}
	
	/**
	 * 	Returns a cube array based on a set of id's
	 */	
	public static Cube[] getCubesByID(int... ids) {
		Cube[] cubes = new Cube[ids.length];
		
		for(int i = 0; i < ids.length; i ++) {
			cubes[i] = CubeRegistry.getCubes()[ids[i]];
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
