package com.GameName.Util.Tag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.GameName.Util.Vectors.Vector2d;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3d;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.Util.Vectors.Vector4d;
import com.GameName.Util.Vectors.Vector4f;

public class DTGLoader {
	
	public static HashSet<Tag> readDTGFile(File f) throws IOException {
		BufferedReader read = new BufferedReader(new FileReader(f));		
		HashSet<Tag> data = new HashSet<Tag>();
		String line = "";
		
		while((line = read.readLine()) != null) {
			for(String tagGroup : split(line, "[")) {	
				for(String tag : split(tagGroup, " ")) {											
					String[] tagInfo = split(tag, "=");		
					
					String tagName = tagInfo[0];
					char c = tagInfo[1].toCharArray()[0];					
					String info = tagInfo[1];
						
					Object obj = null;						
						if(c == '<') {
							info = removeFirst(removeLast(info));
							String[] numbers = split(info, ",");
							char type = numbers[0].charAt(numbers[0].length() - 1);
							
							if(numbers.length == 2) {
								if(type == 'd')	obj = new Vector2d(asDouble(numbers[0]), asDouble(numbers[1]));	
								else			obj = new Vector2f(asFloat(numbers[0]), asFloat(numbers[1]));
							} else if(numbers.length == 3) {
								if(type == 'd')	obj = new Vector3d(asDouble(numbers[0]), asDouble(numbers[1]), asDouble(numbers[2]));	
								else			obj = new Vector3f(asFloat(numbers[0]), asFloat(numbers[1]), asFloat(numbers[2]));								
							} else if(numbers.length == 4) {
								if(type == 'd')	obj = new Vector4d(asDouble(numbers[0]), asDouble(numbers[1]), asDouble(numbers[2]), asDouble(numbers[3]));	
								else			obj = new Vector4f(asFloat(numbers[0]), asFloat(numbers[1]), asFloat(numbers[2]), asFloat(numbers[3]));								
							}
							
						} else if(c == '\'') {
							info = removeLast(info);
							obj = new Character(info.charAt(1));
							
						} else if(c == '\"') {
							info = removeLast(info);
							obj = info.substring(1);
							
						} else if(c == '{') {
							info = removeLast(info);
							String[] values = split(info.substring(1), ",");
							char type = values[0].charAt(values[0].length() - 1);
														
							if(type == 'f')		  {ArrayList<Float>    array = new ArrayList<Float>(); 		for(String value : values) array.add(new Float(asFloat(value)));     obj = array;}
							else if(type == 'd')  {ArrayList<Double>   array = new ArrayList<Double>();		for(String value : values) array.add(new Double(asDouble(value)));   obj = array;}
							else if(type == 'i')  {ArrayList<Integer>  array = new ArrayList<Integer>();	for(String value : values) array.add(new Integer(asInt(value)));     obj = array;}
							else if(type == 'l')  {ArrayList<Long>     array = new ArrayList<Long>();		for(String value : values) array.add(new Long(asLong(value)));       obj = array;}
							else if(type == 'b')  {ArrayList<Byte>     array = new ArrayList<Byte>();		for(String value : values) array.add(new Byte(asByte(value)));       obj = array;}
							else if(type == 's')  {ArrayList<Short>    array = new ArrayList<Short>();	    for(String value : values) array.add(new Short(asShort(value)));     obj = array;}
							else if(type == '\"') {ArrayList<String>   array = new ArrayList<String>();	    for(String value : values) array.add(new String(value));   		     obj = array;}
							else if(type == '\'') {ArrayList<Character>array = new ArrayList<Character>();	for(String value : values) array.add(new Character(value.charAt(0)));obj = array;}
							
						} else {
							char type = info.charAt(info.length() - 1);
							
							if(type == 'f')		 obj = new Float(asFloat(info));
							else if(type == 'd') obj = new Double(asDouble(info));
							else if(type == 'i') obj = new Integer(asInt(info));
							else if(type == 'l') obj = new Long(asLong(info));
							else if(type == 'b') obj = new Byte(asByte(info));
							else if(type == 's') obj = new Short(asShort(info));
						}
						
					data.add(new Tag(tagName, obj));
				}
			}
		}
				
		read.close();
		
		for(Tag tag : data) System.out.println(tag);
		return data;
	}
	
	public static void saveDTGFile(File f, ArrayList<String> tagLines) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		
		for(String tagLine : tagLines) {
			writer.write(tagLine);
			writer.newLine();
			writer.flush();
		}
		
		writer.close();
	}
	
	private static double asDouble(String as) 	{return Double.parseDouble(removeLast(as));}
	private static float  asFloat(String as) 	{return Float.parseFloat(removeLast(as));}
	private static int    asInt(String as) 		{return Integer.parseInt(removeLast(as));}
	private static short  asShort(String as) 	{return Short.parseShort(removeLast(as));}
	private static long   asLong(String as) 	{return Long.parseLong(removeLast(as));}
	private static byte   asByte(String as) 	{return Byte.parseByte(removeLast(as));}
	
	protected static String removeLast(String s)  {return s.substring(0, s.length() - 1);}
	protected static String removeFirst(String s)  {return s.substring(1);}
	
	private static String[] split(String toSplit, String spl) {
		if(!toSplit.contains(spl)) return new String[] {toSplit};
		
		ArrayList<String> splits = new ArrayList<String>();
		
		String toAdd = "";
		char[] check = new char[spl.length()];
		
		for(int i = 0; i < toSplit.length(); i ++) {
			
			for(int j = 0; j < check.length; j ++) {
				check[j] = toSplit.charAt(i + j);
			}
			
			if(Arrays.equals(check, spl.toCharArray())) {
				splits.add(toAdd);
				toAdd = "";
				
				i += spl.length() - 1;
			} else {
				toAdd += toSplit.charAt(i);
			}
		}
		
		splits.add(toAdd);		
		return splits.toArray(new String[splits.size()]);
	}
}