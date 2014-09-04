package com.GameName.World.Generation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.GameName.World.World;

public class BiomGenerator {
	public static final int HYDRATION_FRQ = 150;
	public static final int MAX_HYDRATION = 5;
	public static final int FREQUENCY_HYDRATION = 500;
	
	public static final int TEMPERATURE_FRQ = 15;
	public static final int MAX_TEMPERATURE = 5;
	public static final int FREQUENCY_TEMPERATURE = 5;
		
	public static Biom[] generate(int height, int width, World w) {
		Random rand = new Random();
		
		int chunkH = height / World.CHUNK_SIZE;
		int chunkW = width / World.CHUNK_SIZE;
		
		Biom[] biomes = new Biom[chunkH * chunkW];
		
		for(int i = 0; i < biomes.length; i ++) {
			biomes[i] = new Biom();
		}
		
		for(int i = 0; i < biomes.length; i ++) {
			biomes[i] = new Biom();
			
			if(i == 0 || i == biomes.length - 1) {
				biomes[i].hydration = rand.nextInt(MAX_HYDRATION * 2) - MAX_HYDRATION;
				biomes[i].hydrationDefined = true;
			
				biomes[i].temperature = rand.nextInt(MAX_TEMPERATURE * 2) - MAX_TEMPERATURE;
				biomes[i].temperatureDefined = true;
				
				continue;
			}
			
			if(rand.nextInt(HYDRATION_FRQ) == 0 && !nearPoint(0, FREQUENCY_HYDRATION, biomes, chunkW, i % chunkW, (int) ((i - (i % chunkW))) / chunkW)) {
				biomes[i].hydration = rand.nextInt(MAX_HYDRATION * 2) - MAX_HYDRATION;
				biomes[i].hydrationDefined = true;
			}

			if(rand.nextInt(TEMPERATURE_FRQ) == 0 && !nearPoint(1, FREQUENCY_TEMPERATURE, biomes, chunkW, i % chunkW, (int) ((i - (i % chunkW))) / chunkW)) {
				biomes[i].temperature = rand.nextInt(MAX_TEMPERATURE * 2) - MAX_TEMPERATURE;
				biomes[i].temperatureDefined = true;
			}
		}
		
		Biom[] b1 = fill(biomes);		
		Biom[] b2 = rotate(b1, chunkH, chunkW);//fill(rotate(biomes, chunkH, chunkW));
		Biom[] avgBiomes = avgArrays(b1, b2);
		
		for(int i = 0; i < avgBiomes.length; i ++) {
			int hydration =   Math.round((float) (avgBiomes[i].hydration + MAX_HYDRATION) / 2f);
			int temperature = Math.round((float) (avgBiomes[i].temperature + MAX_TEMPERATURE) / 2f);
			
			avgBiomes[i].typeId = hydration * temperature >= 25 ? 24: hydration * temperature;
			avgBiomes[i].type = Biom.biomTypes[avgBiomes[i].typeId];
			avgBiomes[i] = generateBiomHeightMap(avgBiomes[i]);
			
//			System.out.println(avgBiomes[i]);
		}
		
		BufferedImage imagei = new BufferedImage((w.getSizeX() / World.CHUNK_SIZE), (w.getSizeZ() / World.CHUNK_SIZE), BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < imagei.getHeight(); i ++) {
			for(int j = 0; j < imagei.getWidth(); j ++) {
				int red, blue;
				if(biomes[j + (i * imagei.getWidth())].hydration > 0) {
					red = 0;
					blue = (255 / BiomGenerator.MAX_HYDRATION) * biomes[j + (i * imagei.getWidth())].hydration;
				} else {
					blue = 0;
					red = (255 / BiomGenerator.MAX_HYDRATION) * -biomes[j + (i * imagei.getWidth())].hydration;
				}
				
				imagei.setRGB(j, i, new Color(red, 0, blue).getRGB());
			}
		}
		try {
			ImageIO.write(imagei, "PNG", new File("C:\\Users\\User\\Desktop\\imagei1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		imagei = new BufferedImage((w.getSizeX() / World.CHUNK_SIZE), (w.getSizeZ() / World.CHUNK_SIZE), BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < imagei.getHeight(); i ++) {
			for(int j = 0; j < imagei.getWidth(); j ++) {
				int red, blue;
				if(b1[j + (i * imagei.getWidth())].hydration > 0) {
					red = 0;
					blue = (255 / BiomGenerator.MAX_HYDRATION) * b1[j + (i * imagei.getWidth())].hydration;
				} else {
					blue = 0;
					red = (255 / BiomGenerator.MAX_HYDRATION) * -b1[j + (i * imagei.getWidth())].hydration;
				}
				
				imagei.setRGB(j, i, new Color(red, 0, blue).getRGB());
			}
		}
		try {
			ImageIO.write(imagei, "PNG", new File("C:\\Users\\User\\Desktop\\imagei2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		imagei = new BufferedImage((w.getSizeX() / World.CHUNK_SIZE), (w.getSizeZ() / World.CHUNK_SIZE), BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < imagei.getHeight(); i ++) {
			for(int j = 0; j < imagei.getWidth(); j ++) {
				int red, blue;
				if(b2[j + (i * imagei.getWidth())].hydration > 0) {
					red = 0;
					blue = (255 / BiomGenerator.MAX_HYDRATION) * b2[j + (i * imagei.getWidth())].hydration;
				} else {
					blue = 0;
					red = (255 / BiomGenerator.MAX_HYDRATION) * -b2[j + (i * imagei.getWidth())].hydration;
				}
				
				imagei.setRGB(j, i, new Color(red, 0, blue).getRGB());
			}
		}
		try {
			ImageIO.write(imagei, "PNG", new File("C:\\Users\\User\\Desktop\\imagei3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return avgBiomes;
	}
	
	private static boolean nearPoint(int type, int checkRange, Biom[] biomes, int width, int locX, int locY) {
		for(int x = -checkRange; x < checkRange; x ++) {
			for(int y = -checkRange; y < checkRange; y ++) {
				if((x + locX) + ((y + locY) * width) < 0 || (x + locX) + ((y + locY) * width) > biomes.length - 1)
					continue;
				
				if(type == 0) {
					if(biomes[(x + locX) + ((y + locY) * width)].hydrationDefined)
						return true;
				} else {
					if(biomes[(x + locX) + ((y + locY) * width)].temperatureDefined)
						return true;
				}
			}
		}
		
		return false;
	}

	private static Biom[] fill(Biom[] biomes) {
		Biom[] b1 = new Biom[biomes.length];
		
		for(int i = 0; i < b1.length; i ++) {
			b1[i] = new Biom();
			
			b1[i].hydration = new Integer(biomes[i].hydration).intValue();
			b1[i].temperature = new Integer(biomes[i].temperature).intValue();
			b1[i].hydrationDefined = new Boolean(biomes[i].hydrationDefined).booleanValue();
			b1[i].temperatureDefined = new Boolean(biomes[i].temperatureDefined).booleanValue();
		}
		
		int nextHydr = b1[nextHydr(0, b1)].hydration;
		int nextHydrId = nextHydr(0, b1);
		int lastHydr = 0;
		int lastHydrId = 0;

		int nextTemp = b1[nextHydr(0, b1)].hydration;
		int nextTempId = nextHydr(0, b1);
		int lastTemp = 0;
		int lastTempId = 0;
		
		for(int i = 0; i < b1.length; i ++) {
			if(b1[i].hydrationDefined) {
				lastHydrId = i;
				lastHydr = b1[i].hydration;
				nextHydrId = nextHydr(i, b1);
				nextHydr = b1[nextHydrId].hydration;
			} else {
				float difference = Math.abs(nextHydr - lastHydr);
				float differenceId = Math.abs(nextHydrId - lastHydrId);
				float rise = difference / differenceId;
				float run = (float) i - (float) lastHydrId;
				int change = Math.round(rise * run);
				
				b1[i].hydration = lastHydr + ((nextHydr < lastHydr) ? -change : change);
			}
			
			if(b1[i].temperatureDefined) {
				lastTempId = i;
				lastTemp = b1[i].temperature;
				nextTempId = nextTemp(i, b1);
				nextTemp = b1[nextTempId].temperature;
			} else {
				float difference = Math.abs(nextTemp - lastTemp);
				float differenceId = Math.abs(nextTempId - lastTempId);
				float rise = difference / differenceId;
				float run = (float) i - (float) lastTempId;
				int change = Math.round(rise * run);
				
				b1[i].temperature = lastTemp +  ((nextTemp < lastTemp) ? -change : change);
			}
		}
		
		return b1;
	}
	
	private static int nextHydr(int id, Biom[] biomes) {
		for(int i = id + 1; i < biomes.length; i ++) {
			if(biomes[i].hydrationDefined)
				return i;
		}
		
		return 0;
	}
	
	private static int nextTemp(int id, Biom[] biomes) {
		for(int i = id + 1; i < biomes.length; i ++) {
			if(biomes[i].temperatureDefined)
				return i;
		}
		
		return 0;
	}	
	
	private static Biom[] rotate(Biom[] b, int height, int width) {
		Biom[] toRep = new Biom[b.length];
		
		for(int i = 0; i < toRep.length; i ++) {
			toRep[i] = new Biom();
			
			toRep[i].hydration = new Integer(b[i].hydration).intValue();
			toRep[i].temperature = new Integer(b[i].temperature).intValue();
			toRep[i].hydrationDefined = new Boolean(b[i].hydrationDefined).booleanValue();
			toRep[i].temperatureDefined = new Boolean(b[i].temperatureDefined).booleanValue();
		}
		
		for(int j = 0; j < height; j ++) {
			for(int i = 0; i < width; i ++) {
				toRep[i + (j * width)] = b[j + (i * width)];
			}		
		}
		
		return toRep;
	}
	
	private static Biom[] avgArrays(Biom[] b1, Biom[] b2) {
		Biom[] avg = new Biom[b1.length];
		
		for(int i = 0; i < avg.length; i ++) {
			avg[i] = new Biom();
			
			avg[i].hydration = Math.round((b1[i].hydration + b2[i].hydration) / 2);
			avg[i].temperature = Math.round((b1[i].temperature + b2[i].temperature) / 2);
		}
		
		return avg;
	}
	
	private static Biom generateBiomHeightMap(Biom b) {		
		if(b.typeId == 0 || b.typeId == 1) {
			
		}
		
		return b;
	}
}

