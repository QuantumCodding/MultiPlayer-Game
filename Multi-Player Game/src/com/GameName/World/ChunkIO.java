package com.GameName.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.GameName.Engine.Registries.WorldRegistry;

public class ChunkIO {
	public static File saveChunk(Chunk chunk) throws FileNotFoundException, IOException {
		File saveFile = new File(WorldRegistry.getWorld(chunk.getWorldId()).getFileLoc() + "/chunks/" + 
				chunk.getX() + "x" + chunk.getY() + "x" + chunk.getZ() + ".rwc" );
		
		DataOutputStream output = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(saveFile)));
		
		for(int x = 0; x < chunk.getSize(); x ++) {
		for(int y = 0; y < chunk.getSize(); y ++) {
		for(int z = 0; z < chunk.getSize(); z ++) {
			output.writeInt(chunk.getRawCubeMap(x, y, z));
			output.writeInt(chunk.getRawLightMap(x, y, z));
			output.writeInt(chunk.getRawExtraData(x, y, z));
		}}}
		
		output.close();
		return saveFile;
	}
	
	public static Chunk loadChunk(Chunk chunk) throws FileNotFoundException, IOException {
		File loadFile = new File(WorldRegistry.getWorld(chunk.getWorldId()).getFileLoc() + "/chunks/" + 
				chunk.getX() + "x" + chunk.getY() + "x" + chunk.getZ() + ".rwc" );
		
		DataInputStream input = new DataInputStream(new GZIPInputStream(new FileInputStream(loadFile)));
		
		for(int pos = 0; pos < Math.pow(chunk.getSize(), 3); pos ++) {
			chunk.loadCube(pos, input.readInt(), input.readInt(), input.readInt());
		}
		
		input.close();
		return chunk;
	}
}