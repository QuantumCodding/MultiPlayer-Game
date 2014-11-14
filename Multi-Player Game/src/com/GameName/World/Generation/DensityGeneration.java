package com.GameName.World.Generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.GameName.Cube.Cube;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.Tag;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class DensityGeneration {

	private int seed, scale;
	private World world;
	private Random r;
	private HashMap<Vector3f, DensityNode> nodes = new HashMap<Vector3f, DensityNode>();
	
	public DensityGeneration(int seedI, int scaleI, World worldI) {
		seed = seedI;
		scale = scaleI;
		world = worldI;
		r = new Random(seed);
		pullNodes();
	}
	
	public Chunk generate(int scale,int x,int y,int z) {
		Chunk out = new Chunk(scale, world.getId(), x, y, z);;
		addToNodeFile(new DensityNode(x, y, z, r.nextFloat()));
		ArrayList<DensityNode> nearNodes = new ArrayList<DensityNode>();
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				for(int k=0; k<2; k++) {
					nearNodes.add(getNode(x+i, y+j, z+k));
				}
			}
		}
		for(int i=0; i<nearNodes.size(); i++) {
			nearNodes.set(i, new DensityNode(nearNodes.get(i).getX()*10, nearNodes.get(i).getY()*10, nearNodes.get(i).getZ()*10, nearNodes.get(i).getValue()));
		}
		for(int i=0; i<World.CHUNK_SIZE; i++) {
			for(int j=0; j<World.CHUNK_SIZE; j++) {
				for(int k=0; k<World.CHUNK_SIZE; k++) {
					out.setCube(i, j, k, Cube.Air);
				}
			}
		}
		
		
		
		
		
		
		return out;
	}
			
			
	private void addToNodeFile(DensityNode n) {
		Tag x = new Tag("X", n.getX());
		Tag y = new Tag("Y", n.getY());
		Tag z = new Tag("Z", n.getZ());
		Tag value = new Tag("Value", n.getValue());
		Tag[] info = {x, y, z};
		TagGroup node = new TagGroup(new Tag("type","DensityNode"), info);
		HashSet<TagGroup> tagSets = new HashSet<TagGroup>();
		try {
			tagSets = DTGLoader.readDTGFile(new File(world.getFileLoc()+"/generation/node.dtg"));
			if(!nodeExists(info)) {
				tagSets.add(node);
				ArrayList<TagGroup> tagSetArray = new ArrayList<TagGroup>();
				for(TagGroup t : tagSets) {tagSetArray.add(t);}
				try { DTGLoader.saveDTGFile(new File(world.getFileLoc()+"/generation/node.dtg"), tagSetArray); } catch (IOException e1) {e1.printStackTrace();}
			}
		} catch (IOException e) {
			tagSets.add(node);
			ArrayList<TagGroup> tagSetArray = new ArrayList<TagGroup>();
			for(TagGroup t : tagSets) {tagSetArray.add(t);}
			try { DTGLoader.saveDTGFile(new File(world.getFileLoc()+"/generation/node.dtg"), tagSetArray); } catch (IOException e1) {e1.printStackTrace();}
		}
	}
	
	private void pullNodes() {
		HashSet<TagGroup> tags = new HashSet<TagGroup>();
		try { tags = DTGLoader.readDTGFile(new File(world.getFileLoc()+"/generation/node.dtg"));
		} catch (IOException e) { e.printStackTrace(); }
		
		for(TagGroup t : tags) {
			nodes.put(new Vector3f((Integer)t.getTagByName("X").getTagInfo(), (Integer)t.getTagByName("Y").getTagInfo(), (Integer)t.getTagByName("Z").getTagInfo()), new DensityNode((Integer)t.getTagByName("X").getTagInfo(), (Integer)t.getTagByName("Y").getTagInfo(), (Integer)t.getTagByName("Z").getTagInfo(), (Integer)t.getTagByName("Value").getTagInfo()));
		}
	}
	
	private boolean nodeExists(Tag[] in) {
		HashSet<TagGroup> tagSets = new HashSet<TagGroup>();
		try { tagSets = DTGLoader.readDTGFile(new File(world.getFileLoc()+"/generation/node.dtg")); } catch (IOException e) { e.printStackTrace(); }
		return tagSets.contains(new TagGroup(new Tag("type","DensityNode"), in));
	}
	
	private DensityNode getNode(int x, int y, int z) {
		return nodes.get(new Vector3f(x, y, z));
	}
}
