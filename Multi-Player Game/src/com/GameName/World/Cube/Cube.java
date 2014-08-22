package com.GameName.World.Cube;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.management.InstanceAlreadyExistsException;

import com.GameName.Render.Texture;

public class Cube {
	private final int TEXTURE_SIZE = 10;
	
	private String name;
	private String displayName;
	private int id;
	
	private int[][] texture;
	
	private boolean isLightSorce;
	private float lightValue;
	private float[] lightColor;
	
	private boolean isSolid;
	private boolean isVisable;
	private float opastity;
	
	private int textureSize;

	private static boolean concluded;
	
	private static ArrayList<CubeRegistry> cubeRegestries;
	private static ArrayList<Cube> unconcludedCubes;
	private static Cube[] cubes;
	private static int[] cubeTexCoords;
	
	private static Texture textureSheet;
	private static int textureSheetSideLength;
			
	public static Cube Air = new AirCube();
	
	public static Cube TestCube = new TestCube();
	public static Cube ColorfulTestCube = new ColorfulTestCube();
	
	public static Cube StoneCube = new StoneCube();
	
	public static Cube GoldCube = new GoldCube();
	public static Cube CopperCube = new CopperCube();

	static {
		cubeRegestries = new ArrayList<CubeRegistry>();
		unconcludedCubes = new ArrayList<Cube>();
	}
		
	protected Cube(String name) {
		this.name = name;
		
		setSolid(true);
		setVisable(true);
		setOpastity(1f);
		
		setLightSorce(false);
		setLightValue(0f);
		
		loadExtraInfo();
		texture = loadTextures();
	}

	private void loadExtraInfo() {
		File extraInfo = new File("res/textures/cubes/info/" + name + ".txt");
		
		if(!extraInfo.exists()) {
			textureSize = TEXTURE_SIZE;
			
			return;
		}
		
		Scanner scan = null;
		try {
			scan = new Scanner(extraInfo);
			
			while(scan.hasNext()) {
				String line = scan.nextLine();
				String[] info = line.split("=");
				
				if(info.length == 1) continue;
				
				switch(info[0]) {
					case "Texture Size": textureSize = Integer.parseInt(info[1]); break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} finally {
			if(scan != null)
				scan.close();
		}
		
	}

	private int[][] loadTextures() {
		int[][] textures = new int[6][];
		
		try {						
			BufferedImage fullTexture = ImageIO.read(new File("res/textures/cubes/" + name + ".png"));
			
			if(fullTexture == null) throw new IOException("Texture == NULL");
			
			for(int i = 0; i < textures.length; i ++) {
				textures[i] = toIntArray(fullTexture.getSubimage(i * textureSize + i, 0, textureSize, textureSize));
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
				
				textures[i] = toAdd;
			}
		}
		
		return null;
	}
	
	private static int[] toIntArray(BufferedImage image) {
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
	
	public static void regesterCubes() throws InstanceAlreadyExistsException {
		if(concluded) throw new InstanceAlreadyExistsException("Initialization already Concluded! Run clean befor re-concluding");
	
		CubeRegistry regestry = new CubeRegistry();
		
		regestry.addCube(Air); regestry.addCube(TestCube); regestry.addCube(ColorfulTestCube); 
		
		regestry.addCube(StoneCube);
		
		regestry.addCube(GoldCube); regestry.addCube(CopperCube);
		
		cubeRegestries.add(regestry);
	}
	
	public static void concludeInit() throws InstanceAlreadyExistsException {
		if(concluded) throw new InstanceAlreadyExistsException("Initialization already Concluded! Run clean befor re-concluding");
		
		CubeRegistry[] regestries = new CubeRegistry[cubeRegestries.size()];
		regestries = cubeRegestries.toArray(regestries);
		
		for(CubeRegistry reg : regestries) {
			for(Cube cube : reg.toArray()) {
				cube.setId(getUniqueId());
				unconcludedCubes.add(cube);
			} 
		}
		
		cubes = new Cube[unconcludedCubes.size()];
		cubes = unconcludedCubes.toArray(cubes);
		
		unconcludedCubes = null;	cubeRegestries = null;
		
		cubeTexCoords = new int[cubes.length * 2];
		
		try {
			File saveLoc = new File("res/textures/cubes/TextureSheet.png");
			
			ImageIO.write(generateTextureSheet(), "PNG", saveLoc);
			textureSheet = new Texture(saveLoc); //TextureLoader.getTexture("PNG",  new FileInputStream(new File("res/textures/cubes/MapingSheet.png")));//
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int getUniqueId() throws InstanceAlreadyExistsException {
		if(concluded) throw new InstanceAlreadyExistsException("Initialization already Concluded! New ID\'s can not be created");
		
		return unconcludedCubes.size();
	}
	
	public static void addCubeRegistry(CubeRegistry regestry) {
		Cube.cubeRegestries.add(regestry);
	} 
	
	private static BufferedImage generateTextureSheet() {
		int totalArea = 0;
		int maxTextureSize = 0;
		
		for(Cube cube : cubes) {			
			totalArea += Math.pow(cube.getTextureSize(), 2) * 6;
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
		
		int cubesAdded = 0;
		int index = 0;		
		boolean[] added = new boolean[cubes.length];
		int x = 0, y = 0;
		boolean hitEnd = false;
		
		System.out.println("Starting Cycle");
		
		while(cubesAdded < cubes.length) {
//			System.out.println("Loop \t\t[Index: " + index + ", x = " + x + ", y = " + y + ", Cubes Added = " + cubesAdded + ", Max Cubes =" + cubes.length + "]");
			
			if(index >= cubes.length) {
//				System.out.println("At End \t\t[Index: " + index + ", x = " + x + ", y = " + y + ", Cubes Added = " + cubesAdded + ", Max Cubes =" + cubes.length + "]");
				index = 0;
				
				while(added[index]) {
					index ++;
				}
				
				do {
//					System.out.println("Looking Empty \t[Index: " + index + ", x = " + x + ", y = " + y + ", Cubes Added = " + cubesAdded + ", Max Cubes =" + cubes.length + "]");
					
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
			
			Cube cube = cubes[index];
			
			if(cube.getTextureSize() + y >= sideLength) {
				index ++;
				continue;
			}
			
			if(cube.getTextureSize() * 6 + x < sideLength) {
//				System.out.println("Valid Spot\t[Index: " + index + ", x = " + x + ", y = " + y + ", Cubes Added = " + cubesAdded + ", Max Cubes = " + cubes.length + "]");
				
				for(int i = 0; i < 6; i ++) {
					for(int j = 0; j < cube.getTextureSize(); j ++) {
						for(int k = 0; k < cube.getTextureSize(); k ++) {
							spriteSheet[(i * cube.getTextureSize()) + (x + j)][y + k]
								= cube.getTexture()[i][j + (k * cube.getTextureSize())];
						}						
					}
				}
				
				cubeTexCoords[index * 2] = x;
				cubeTexCoords[(index * 2) + 1] = y;
				
				x += cube.getTextureSize() * 6;
//				y += cube.getTextureSize() - 1;
				
				added[index] = true;
				
				index ++;
				cubesAdded ++;
				
				continue;
			}
			
			index ++;
			
//			System.out.println("End \t\t[Index: " + index + ", x = " + x + ", y = " + y + ", Cubes Added = " + cubesAdded + ", Max Cubes =" + cubes.length + "]");
		}
		
		for(x = 0; x < sideLength; x ++) {
			for(y = 0; y < sideLength; y ++) {
				spriteSheetImage.setRGB(x, y, spriteSheet[x][y]);
			}
		}
		
		return spriteSheetImage;
	}

	public String toString() {
		return name + " [Id=" + id + ", DisplayName=" + displayName + "]";
	} 
	
	protected void setLightSorce(boolean isLightSorce) {
		this.isLightSorce = isLightSorce;
	}

	protected void setLightValue(float lightValue) {
		this.lightValue = lightValue;
	}

	protected void setLightColor(float[] lightColor) {
		this.lightColor = lightColor;
	}

	protected void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}

	protected void setVisable(boolean isVisable) {
		this.isVisable = isVisable;
	}

	protected void setOpastity(float opastity) {
		this.opastity = opastity;
	}

	protected void setTextureSize(int textureSize) {
		this.textureSize = textureSize;
	}

	private void setId(int id) {
		this.id = id;
	}

	protected void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public static int[] getCubeTexCoords() {
		return cubeTexCoords;
	}

	public static Texture getTextureSheet() {
		return textureSheet;
	}

	public static int getTextureSheetSideLength() {
		return textureSheetSideLength;
	}

	public static Cube getCubeByID(int id) {
		return cubes[id];
	}
	
	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getId() {
		return id;
	}

	public int[][] getTexture() {
		return texture;
	}

	public boolean isLightSorce() {
		return isLightSorce;
	}

	public float getLightValue() {
		return lightValue;
	}

	public float[] getLightColor() {
		return lightColor;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public boolean isVisable() {
		return isVisable;
	}

	public float getOpastity() {
		return opastity;
	}

	public int getTextureSize() {
		return textureSize;
	}

	public static Cube[] getCubes() {
		return cubes;
	}
	
	public static boolean isConcluded() {
		return concluded;
	}

	public static void cleanUp() {
		
	}
}
