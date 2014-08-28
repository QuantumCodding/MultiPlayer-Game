package com.GameName.World.Generation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class WorldGen {

	private final int caveBirth = 5;
	private final int caveDie = 7;
	private final int caveFilter = 3;
	private final float caveInit = 0.7f;
	
	private final double heightSplit = 0.5;
//	private final int heightLoop = 5;
	
	private double[][] heightMap;
	private boolean[][][] caveMap;
	public boolean[][][] map;
	
	public int sizeX, sizeY, sizeZ;
	
	public WorldGen(int sizeX, int sizeY, int sizeZ) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		
		caveMap = new boolean[sizeX][sizeY][sizeZ];
		
		map = generateMap();
	}
	
	public static void main(String[] args) throws Exception {		
		System.out.print("Generating Map : ");
		WorldGen gen = new WorldGen(9, 100, 9);
		boolean[][][] map = gen.map;
		System.out.println("Done");
		
		System.out.print("Generating Files : ");
		for(int y = 0; y < 100; y ++) {
			BufferedImage img = new BufferedImage(gen.sizeX, gen.sizeZ, BufferedImage.TYPE_INT_RGB);
			
			for(int x = 0; x < img.getWidth(); x ++) {
				for(int z = 0; z < img.getHeight(); z ++) {	
					int c = map[x][y][z] ? 0 : 255; //x%2==y%2 && z%2==y%2
					
					img.setRGB(x, z, new Color(c, c, c).getRGB());
				}
			}
			
			ImageIO.write(img, "png", new File("C:\\Users\\User\\Desktop\\WorldImg\\" + y + ".png")); //4-HRobotics
		}
		
		BufferedImage img = new BufferedImage(gen.sizeX, gen.sizeZ, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < img.getWidth(); x ++) {
			for(int z = 0; z < img.getHeight(); z ++) {	
				double c = (int) ((gen.heightMap[x][z]) / 10d);
//				System.out.println(c);
				
				c *= 255d / 50d;
				if(c > 255) c = 255;
				if(c < 0) c = 0;
//				System.out.println(c);
				img.setRGB(x, z, new Color((int) c, (int) c, (int) c).getRGB());
			}
		}
		
		ImageIO.write(img, "png", new File("C:\\Users\\User\\Desktop\\WorldImg\\" + "HeightMap" + ".png"));
		
		System.out.println("Done");
	}
	
	public boolean[][][] generateMap() {
		map = new boolean[sizeX][sizeY][sizeZ];
		
		heightMap = generateHeightMap();
		caveMap = generateCave();
		
		for(int x = 0; x < sizeX; x ++) {
			for(int y = 0; y < sizeY; y ++) {
				for(int z = 0; z < sizeZ; z ++) {
					map[x][y][z] = y <= heightMap[x][z];// & caveMap[x][y][z];
//					System.out.println(x + ", " + y + ", " + z);// + " : " + heightMap[x][z]);
				}
			}
		}
		
		return map;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean[][][] generateCave() {
		caveMap = initGen();
		
		for(int i = 0; i < caveFilter; i ++) {
			caveMap = cycle();
		}
		
		return caveMap;
	}
	
	private boolean[][][] initGen() {
		for(int x = 0; x < sizeX; x ++) {
			for(int y = 0; y < sizeY; y ++) {
				for(int z = 0; z < sizeZ; z ++) {
					caveMap[x][y][z] = Math.random() > caveInit;
				}
			}
		}
		
		return caveMap;
	}
	
	private boolean[][][] cycle() {
		boolean[][][] newMap = (boolean[][][])caveMap.clone();
		
		for(int x = 0; x < sizeX; x ++) {
			for(int y = 0; y < sizeY; y ++) {
				for(int z = 0; z < sizeZ; z ++) {
					int neighboringAround = getNeighboringCount(newMap, x, y, z);
					
					if(caveMap[x][y][z]) {
						newMap[x][y][z] = (neighboringAround < caveDie);
					} else {
						newMap[x][y][z] = neighboringAround > caveBirth;
					}
				}
			}
		}
		
		return newMap;		
	}
	
	private int getNeighboringCount(boolean[][][] map, int x, int y, int z) {
		int count = 0;
		
		for(int i = -1; i < 2; i ++) {
			for(int j = -1; j < 2; j ++) {
				for(int k = -1; k < 2; k ++) {
					
					if(i == 0 && j == 0 && k == 0)
						continue;
					else if(i + x < 0 || j + y < 0 || k + z < 0 || i + x >= sizeX || j + y >= sizeY || k + z >= sizeZ)
						count ++;				
					else if(map[x + i][y + j][z + k]) 
						count ++;					
				}
			}
		}
		
		return count;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public double[][] generateHeightMap() {
		try{
		BufferedWriter write = new BufferedWriter(new FileWriter(new File("C:\\Users\\User\\Desktop\\WorldImg\\HeightMap.txt")));
		
//		System.out.print("\n Height Map Stared : ");
		
		double size = (int) (Math.pow(2, Math.round((Math.log(Math.max(sizeX, sizeZ) - 1) / Math.log(2)) + 0.4))); //(int) (Math.pow(Math.max(sizeX, sizeZ), 2));
		heightMap = new double[(int) size + 1][(int) size + 1];
		write.write("Size: " + size);
		write.newLine();
		
		double cornerHeight = Math.random();		
		heightMap[0][0] = cornerHeight;
		heightMap[(int) size][0] = cornerHeight;
		heightMap[0][(int) size] = cornerHeight;
		heightMap[(int) size][(int) size] = cornerHeight;
		
		write.write("Corrner Height: " + cornerHeight);
		write.newLine();
		
//		System.out.println(size);
		
        double space = size - 1;
		for(int i = 0; i < (size + 1) / 2; i ++) {//(Math.sqrt(size) - 3) / 2
//			System.out.println(i);
			write.write("Loop " + i + " out of " + ((size - 1) / 2));
			write.newLine();
			
			for(int x = 0; x < Math.round(size / space); x ++) {
				for(int z = 0; z < Math.round(size / space); z ++) {
					double[] diamondOut = diamondStep((int)space  * x, (int)space * z, (int)space);
					heightMap[(int)diamondOut[1]][(int)diamondOut[2]] = diamondOut[0];	
					System.out.println(diamondOut[0]);
					
					write.write("\tDiamond Step");
					write.newLine();
					write.write("\t\tPoint[ " + diamondOut[1] + ", " + diamondOut[2] + " ]");
					write.newLine();
					write.write("\t\tHeight[ " + diamondOut[0] + " ]");
					write.newLine();
					
					double[][] squareOut = squareStep((int)space  * x, (int)space * z, (int)space / 2);
					
					for(int j = 0; j < squareOut.length; j ++) {
						heightMap[(int) squareOut[j][1]][(int) squareOut[j][2]] = squareOut[j][0];	
						System.out.println("\t" + squareOut[j][0]);
						
						write.write("\tSquare Step");
						write.newLine();
						write.write("\t\tPoint[ " + squareOut[j][1] + ", " + squareOut[j][2] + " ]");
						write.newLine();
						write.write("\t\tHeight[ " + squareOut[j][0] + " ]");
						write.newLine();
						write.newLine();
					}
				}
			}

            space /= 2;
            write.write("\tSpace[ " + space + " ]");
			write.newLine();
			write.newLine();
            
		}
		
		heightMap = raiseMap();
		
//		System.out.print("Done");
		
		write.flush();
		write.close();
		} catch(Exception e) {}
		return heightMap;
	}
	
	private double[] diamondStep(int startX, int startZ, int space) {
		double avgHeight = avg(new Double[] {
				heightMap[startX][startZ],
				heightMap[startX + space][startZ], 
				heightMap[startX][startZ + space],
				heightMap[startX + space][startZ + space]
		});
		
		double heightAdjust = (Math.random() > (heightSplit) ? 1 : -1) * Math.random();		// - (((100 / sizeY) * avgHeight) / 2)
		return new double[]{avgHeight + heightAdjust, startX + (space / 2), startZ + (space / 2)};	
	}
	
	private double[][] squareStep(int startX, int startZ, int space) {
		List<Double> avgNumbers = new ArrayList<Double>();
		double[][] toRep = new double[4][3];
		
		try{ avgNumbers.add(heightMap[startX][startZ]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
		try{ avgNumbers.add(heightMap[startX + space][startZ + space]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
		try{ avgNumbers.add(heightMap[startX + (2 * space)][startZ]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
		try{ avgNumbers.add(heightMap[startX + space][startZ - space]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}

		double avgHeight = avg(toArray(avgNumbers));		
		double heightAdjust = (Math.random() > (heightSplit) ? 1 : -1) * Math.random(); // - (((100 / sizeY) * avgHeight) / 2)
		toRep[0] = new double[]{avgHeight + heightAdjust, startX + space, startZ};
		
				try{ avgNumbers.add(heightMap[startX + (2 * space)][startZ]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
				try{ avgNumbers.add(heightMap[startX + space][startZ + space]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
				try{ avgNumbers.add(heightMap[startX + (3 * space)][startZ + space]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
				try{ avgNumbers.add(heightMap[startX + (2 * space)][startZ + (2 * space)]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
				
				avgHeight = avg(toArray(avgNumbers));		
				heightAdjust = (Math.random() > (heightSplit) ? 1 : -1) * Math.random(); // - (((100 / sizeY) * avgHeight) / 2)
				toRep[1] = new double[]{avgHeight + heightAdjust, startX + (2 * space), startZ + space};
		
		try{ avgNumbers.add(heightMap[startX][startZ + space]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
		try{ avgNumbers.add(heightMap[startX + space][startZ + space]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
		try{ avgNumbers.add(heightMap[startX - space][startZ + space]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
		try{ avgNumbers.add(heightMap[startX][startZ + (2 * space)]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
		
		avgHeight = avg(toArray(avgNumbers));		
		heightAdjust = (Math.random() > (heightSplit) ? 1 : -1) * Math.random(); // - (((100 / sizeY) * avgHeight) / 2)
		toRep[2] = new double[]{avgHeight + heightAdjust, startX, startZ + space};
		
				try{ avgNumbers.add(heightMap[startX][startZ + (2 * space)]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
				try{ avgNumbers.add(heightMap[startX + space][startZ + space]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
				try{ avgNumbers.add(heightMap[startX + (2 * space)][startZ + (2 * space)]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
				try{ avgNumbers.add(heightMap[startX + space][startZ + (3 * space)]); } catch(IndexOutOfBoundsException e) {/*System.out.println("Error");*/}
				
				avgHeight = avg(toArray(avgNumbers));		
				heightAdjust = (Math.random() > (heightSplit) ? 1 : -1) * Math.random(); // - (((100 / sizeY) * avgHeight) / 2)
				toRep[3] = new double[]{avgHeight + heightAdjust, startX + space, startZ + (2 * space)};
		
		return toRep;
	}
	
	private double avg(Double[] values) {
		double avg = 0;
		
		for(int i = 0; i < values.length; i ++)
			avg += values[i].doubleValue();
		
		avg /= values.length;
		
		return avg;
	}
	
	private Double[] toArray(List<Double> arraylist) {		
		Double[] array = new Double[arraylist.size()];
		
		for(int i = 0; i < array.length; i ++) {
			array[i] = arraylist.get(i);
		}
		
		return array;
	}
	
	private double[][] raiseMap() {
		double newMap[][] = (double[][])heightMap.clone();
		
		for(int x = 0; x < newMap.length; x ++) {
			for(int z = 0; z < newMap[x].length; z ++) {
				newMap[x][z] *= 10;
				newMap[x][z] += sizeY / 2;

//				System.out.println(newMap[x][z]);
			}
		}
		
		return newMap;
	}
}
