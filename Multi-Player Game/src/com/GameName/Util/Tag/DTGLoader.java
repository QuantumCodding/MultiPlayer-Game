package com.GameName.Util.Tag;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.GameName.Util.Vectors.Vector3f;

public class DTGLoader {	
	
	public static OutputStream getOutputStream(File f) throws IOException {
		if(!f.getAbsolutePath().endsWith(".dtg")) throw new IOException("DTG files must end with \".dtg\"");
		return new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(f)));
	}
	
	public static InputStream getInputStream(File f) throws IOException {
		if(!f.getAbsolutePath().endsWith(".dtg")) throw new IOException("DTG files must end with \".dtg\"");
		return new BufferedInputStream(new GZIPInputStream(new FileInputStream(f)));
	}
	
	/*	5 = Start of TagGroups
	 *  1 = Start of TagGroup
	 *  2 = Start of Tag
	 * 	0 = Information Divider
	 *  3 = End of Tag
	 *  4 = End of TagGroup
	 *  6 = End of TagGroups
	 */
	
	public static ArrayList<TagGroup> readAll(InputStream read) throws IOException {
		ArrayList<TagGroup> groups = new ArrayList<>();
		
		byte in = -1; int index = 0;
		byte[] bytes = new byte[2048];
		String tagName = null, unprossesedInfo;
		
		Tag groupIdTag = null; boolean readingId = false;
		ArrayList<Tag> tags = new ArrayList<>();
		
		while((in = (byte) read.read()) != 6) {
			switch(in) {
				case 5: break;
			
				//Tag Group
				case 1: tags.clear(); readingId = true; break;
				case 4: groups.add(new TagGroup(groupIdTag, tags.toArray(new Tag[tags.size()]))); break;
			
				//Tag
				case 2: bytes = new byte[2048]; break;
				case 0: tagName = new String(bytes).trim(); bytes = new byte[2048]; break;
				case 3: unprossesedInfo = new String(bytes).trim(); 
					Tag tag = new Tag(tagName, processInfo(unprossesedInfo));
//					System.out.println(tagName);
					
					if(readingId) {
						groupIdTag = tag;
						readingId = false;
					} else {
						tags.add(tag);
					}
				break;
				
				default: bytes[index] = in; index ++; break;
			}	
		} 
		
		return groups;
	} 
	
	public static void writeAll(OutputStream write, ArrayList<TagGroup> groups) throws IOException {
		write.write((byte) 5);
		
		for(TagGroup group : groups) {
			if(group == null) continue;
			
			write.write((byte) 1);			
			writeTag(write, group.getIdTag());
			
			for(Tag tag : group.getTags()) {
				writeTag(write, tag);
			}		
			
			write.write((byte) 4);			
		}	
		
		write.write((byte) 6);
	}
	
	public static void writeTag(OutputStream write, Tag tag) throws IOException {
		if(tag.getInfo() == null || tag == null) return;
		
		write.write((byte) 2);
		write.write(tag.getName().getBytes());
		write.write((byte) 0);
		write.write(DTGGenerator.generateTag(tag.getInfo()).getBytes());
		write.write((byte) 3);
	}
	
	private static Object processInfo(String in) {
		if(in.isEmpty()) return null;
		char infoType = in.charAt(0);
		in = in.substring(1, in.length());
		if(in.isEmpty()) return null;
		
		switch(infoType) {
			case 'S': return in;
			
			case 'b': return Byte.parseByte(in);
			case 's': return Short.parseShort(in);
			case 'i': return Integer.parseInt(in);
			case 'l': return Long.parseLong(in);
			
			case 'f': return Float.parseFloat(in);
			case 'd': return Double.parseDouble(in);

			case 'c': return in.charAt(0);
			case 'B': return Boolean.parseBoolean(in);
						
			case 'A': return processArray(in);
			case 'V': return processVector(in);
			
			default: return null;
		}
	}
	
	private static Object processArray(String in) {
		char arrayType = in.charAt(0);
		in = in.substring(1, in.length());
		if(in.isEmpty()) return null;
		
		String[] elements = in.split(",");	
		switch(arrayType) {
			case 'S': return elements;
			
			case 'b': Byte[] arrayb = new Byte[elements.length]; 		for(int i = 0; i < elements.length; i ++) arrayb[i] = Byte.parseByte(elements[i]); return arrayb;
			case 's': Short[] arrays = new Short[elements.length]; 		for(int i = 0; i < elements.length; i ++) arrays[i] = Short.parseShort(elements[i]); return arrays;
			case 'i': Integer[] arrayi = new Integer[elements.length]; 	for(int i = 0; i < elements.length; i ++) arrayi[i] = Integer.parseInt(elements[i]); return arrayi;
			case 'l': Long[] arrayl = new Long[elements.length]; 		for(int i = 0; i < elements.length; i ++) arrayl[i] = Long.parseLong(elements[i]); return arrayl;
			
			case 'f': Float[] arrayf = new Float[elements.length]; 		for(int i = 0; i < elements.length; i ++) arrayf[i] = Float.parseFloat(elements[i]); return arrayf;  
			case 'd': Double[] arrayd = new Double[elements.length];	for(int i = 0; i < elements.length; i ++) arrayd[i] = Double.parseDouble(elements[i]); return arrayd;
			
			case 'c': Character[] arrayc = new Character[elements.length]; 	for(int i = 0; i < elements.length; i ++) arrayc[i] = in.charAt(0); return arrayc;  
			case 'B': Boolean[] arrayB = new Boolean[elements.length];		for(int i = 0; i < elements.length; i ++) arrayB[i] = Boolean.parseBoolean(elements[i]); return arrayB;
			
			default: return null;
		}
	}
	
	private static Object processVector(String in) {	
		if(in.isEmpty()) return null;
		
		String[] numbers = in.split(",");		
		return new Vector3f(Float.parseFloat(numbers[0]), Float.parseFloat(numbers[1]), Float.parseFloat(numbers[2]));
	}
}
