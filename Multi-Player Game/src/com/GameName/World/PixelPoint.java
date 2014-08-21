package com.GameName.World;

public class PixelPoint {
	
//	public float x, y, z;
//	public float chunkX, chunkY, chunkZ;
//	public float subX, subY, subZ;
//	
//	public float lightValue;
//	public float opacity;
//	public float[] texture;
//	public float[] color;
//	
//	public boolean isLightSorce;
//	public boolean isSolid;
//	
//	public World w;
//	
//	public PixelPoint(float x, float y, float z, World w) {
//		this.w = w;
//		
//		this.x = x;
//		this.y = y;
//		this.z = z;	
//		
//		subX = x % w.CHUNK_SIZE;
//		subY = y % w.CHUNK_SIZE;
//		subZ = z % w.CHUNK_SIZE;
//		
//		chunkX = (x - subX) / 10;
//		chunkY = (y - subY) / 10;
//		chunkZ = (z - subZ) / 10;
//		
//		isLightSorce = false;
//		isSolid = false;
//		lightValue  = 0;
//		opacity = 1;
//	}
//	
//	/**
//	 * Calculate the new light value of all the pixels around it
//	 * Note: only for light sources
//	 * 
//	 * @param world
//	 */
//	public void calculateLight(World world) {
//		for(float i = 0; i < lightValue; i ++) {
//			for(int j = 0; j < 360; j ++) {
//				
//				int p = Math.round(((float) Math.cos(j) * i) + x);
//				int q = Math.round(((float) Math.sin(j) * i) + z);
//				
//				int heightDif = Math.round(Math.abs(y - world.getPixel(p, y, q).y));
//				float brightness = (lightValue - ((float) i + (heightDif / 2))) / world.MAX_LIGHT;
//				brightness = brightness < (world.AMBIANT_LIGHT / world.MAX_LIGHT) ? (world.AMBIANT_LIGHT / world.MAX_LIGHT): brightness;
//				
////				if(i == 5)System.out.println(j + ": " + "Updating light at: (" + p + ", " + heightMap.get(new Point(x, z)) + ", " + q + ")\n\t Setting Value to: " + (lightValue[0] - i));
//				
//				world.getPixel(p, y, q).lightValue = brightness;
//				
//				int dir = j < 180 ? 1 : -1;
//				if(!world.getPixel(p + dir, y, q + dir).isSolid)
//					calculateShadow(world, p, q, j, i);
//									
//				world.getPixel(p, y, q).updateColor();
//			}
//		}	
//	}
//	
//	/**
//	 * This method calculate the shadow of a pixel and then set the light value of the corresponding pixel to a shadow
//	 * 
//	 * @param world
//	 * @param x
//	 * @param y
//	 * @param rotation
//	 * @param lightValue
//	 */	
//	private void calculateShadow(World world, float p, float q, float j, float i) {
//		if(world.getPixel(((float) Math.cos(j) * (i + 1)) + x, y,((float) Math.sin(j) * (i + 1) + z)).y < world.getPixel(p, y, q).y) {
//			PixelPoint pix = world.getPixel(p, y, q);
//						
//			float slopeX = x + (x - pix.x);
//			float slopeY = y + (y - pix.y);
//			float slopeZ = z + (z - pix.z);
//			
//			int castDistance = 0;
//
//			while(!world.getPixel(pix.x + (slopeX * castDistance), pix.y + (slopeY * castDistance), pix.z + (slopeZ * castDistance)).isSolid) {
//				castDistance += i <= 180 ? 1 : -1;
//			}
//
//			PixelPoint shadowPix = world.getPixel(pix.x + (slopeX * castDistance), pix.y + (slopeY * castDistance), pix.z + (slopeZ * castDistance));
//
//			shadowPix.lightValue -= world.MAX_LIGHT / (world.MAX_LIGHT - Math.abs(castDistance));
//			
//			shadowPix.updateColor();
//		}
//	}
//	
//	public void updateColor() {
//		color[0] = texture[0] * lightValue;
//		color[1] = texture[1] * lightValue;
//		color[2] = texture[2] * lightValue;
//		color[3] = texture[3] * lightValue;
//	}

}
