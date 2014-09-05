package com.GameName.Util.Tag;

import java.util.HashSet;

import com.GameName.Util.Vectors.Vector2d;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3d;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.Util.Vectors.Vector4d;
import com.GameName.Util.Vectors.Vector4f;

public class DTGGenerator {
	public static String generateTag(String tagName, Vector2d data) {return tagName + "=<"+data.getX() + "d," + data.getY() + "d> ";}
	public static String generateTag(String tagName, Vector2f data) {return tagName + "=<"+data.getX() + "f," + data.getY() + "f> ";}
	public static String generateTag(String tagName, Vector3d data) {return tagName + "=<"+data.getX() + "d," + data.getY() + "d," + data.getZ() + "d> ";}
	public static String generateTag(String tagName, Vector3f data) {return tagName + "=<"+data.getX() + "f," + data.getY() + "f," + data.getZ() + "f> ";}
	public static String generateTag(String tagName, Vector4d data) {return tagName + "=<"+data.getX() + "d," + data.getY() + "d," + data.getZ() + "d," + data.getW() + "d> ";}
	public static String generateTag(String tagName, Vector4f data) {return tagName + "=<"+data.getX() + "f," + data.getY() + "f," + data.getZ() + "f," + data.getW() + "f> ";}

	public static String generateTag(String tagName, float data)  {return tagName + "=" + data + "f ";}   public static String generateTag(String tagName, Float data)  {return tagName + "=" + data.floatValue() 	+ "f ";}
	public static String generateTag(String tagName, double data) {return tagName + "=" + data + "d ";}   public static String generateTag(String tagName, Double data) {return tagName + "=" + data.doubleValue() 	+ "d ";}
	public static String generateTag(String tagName, long data)   {return tagName + "=" + data + "l ";}   public static String generateTag(String tagName, Long data)   {return tagName + "=" + data.longValue() 	+ "l ";}
	public static String generateTag(String tagName, short data)  {return tagName + "=" + data + "s ";}   public static String generateTag(String tagName, Short data)  {return tagName + "=" + data.shortValue() 	+ "s ";}
	public static String generateTag(String tagName, int data)    {return tagName + "=" + data + "i ";}   public static String generateTag(String tagName, Integer data){return tagName + "=" + data.intValue() 	+ "i ";}
	public static String generateTag(String tagName, byte data)   {return tagName + "=" + data + "b ";}   public static String generateTag(String tagName, Byte data)   {return tagName + "=" + data.byteValue() 	+ "b ";}
	
	public static String generateTag(String tagName, char data)   	{return tagName + "=\'" + data 				+ "\' ";}
	public static String generateTag(String tagName, Character data){return tagName + "=\'" + data.charValue() 	+ "\' ";}
	public static String generateTag(String tagName, String data) 	{return tagName + "=\"" + data 				+ "\" ";}
	
	public static String generateTag(String tagName, float[] data)  {return tagName + "=" + arrayFormat(toStringArray(data), 'f') + " ";}
	public static String generateTag(String tagName, double[] data) {return tagName + "=" + arrayFormat(toStringArray(data), 'd') + " ";}
	public static String generateTag(String tagName, long[] data)   {return tagName + "=" + arrayFormat(toStringArray(data), 'l') + " ";}
	public static String generateTag(String tagName, short[] data)  {return tagName + "=" + arrayFormat(toStringArray(data), 's') + " ";}
	public static String generateTag(String tagName, int[] data)    {return tagName + "=" + arrayFormat(toStringArray(data), 'i') + " ";}
	public static String generateTag(String tagName, byte[] data)   {return tagName + "=" + arrayFormat(toStringArray(data), 'b') + " ";}
	public static String generateTag(String tagName, char[] data)   {return tagName + "=" + arrayFormat(toStringArray(data), '\'') + " ";}
	public static String generateTag(String tagName, String[] data) {return tagName + "=" + arrayFormat(              data , '\"') + " ";}

	private static String[] toStringArray(float[] data)  {String[] toRep = new String[data.length]; for(int i = 0; i < data.length; i ++) toRep[i] = data[i] + ""; return toRep;}
	private static String[] toStringArray(double[] data) {String[] toRep = new String[data.length]; for(int i = 0; i < data.length; i ++) toRep[i] = data[i] + ""; return toRep;}
	private static String[] toStringArray(long[] data)   {String[] toRep = new String[data.length]; for(int i = 0; i < data.length; i ++) toRep[i] = data[i] + ""; return toRep;}
	private static String[] toStringArray(short[] data)  {String[] toRep = new String[data.length]; for(int i = 0; i < data.length; i ++) toRep[i] = data[i] + ""; return toRep;}
	private static String[] toStringArray(int[] data)    {String[] toRep = new String[data.length]; for(int i = 0; i < data.length; i ++) toRep[i] = data[i] + ""; return toRep;}
	private static String[] toStringArray(byte[] data)   {String[] toRep = new String[data.length]; for(int i = 0; i < data.length; i ++) toRep[i] = data[i] + ""; return toRep;}
	private static String[] toStringArray(char[] data)   {String[] toRep = new String[data.length]; for(int i = 0; i < data.length; i ++) toRep[i] = data[i] + ""; return toRep;}
		
	private static String arrayFormat(String[] data, char end) {
		String toRep = "{";
		
		for(String t : data) {
			if(end == '\'' || end == '\"') toRep += end;				
			toRep += (t + "") + end + ",";
		}
		
		return DTGLoader.removeLast(toRep) + "}";
	}
	
	public static String generateTagLine(String[] tags) {
		String tagLine = tags[0] + "[";
		
		for(int i = 1; i < tags.length; i ++) {
			tagLine += tags[i] + " ";
		}
		
		return DTGLoader.removeLast(tagLine) + "]";
	}
	
	public static String generateTagLine(Tag[] tags) {
		String tagLine = tags[0].getTagString() + "[";
		
		for(int i = 1; i < tags.length; i ++) {
			tagLine += tags[i].getTagString() + " ";
		}
		
		return DTGLoader.removeLast(tagLine) + "]";
	}
	
	public static String generateTag(String tagName, Object data) {		
		if(data.getClass().equals(Float.class)) 	{return generateTag(tagName, (Float) data);}
		if(data.getClass().equals(Double.class)) 	{return generateTag(tagName, (Double) data);}
		if(data.getClass().equals(Long.class)) 		{return generateTag(tagName, (Long) data);}
		if(data.getClass().equals(Integer.class))	{return generateTag(tagName, (Integer) data);}
		if(data.getClass().equals(Short.class)) 	{return generateTag(tagName, (Short) data);}
		if(data.getClass().equals(Byte.class)) 		{return generateTag(tagName, (Byte) data);}
		
		if(data.getClass().equals(Character.class)) {return generateTag(tagName, (Character) data);}
		if(data.getClass().equals(String.class)) 	{return generateTag(tagName, (String) data);}

		if(data.getClass().equals(Vector2f.class)) {return generateTag(tagName, (Vector2f) data);}
		if(data.getClass().equals(Vector2d.class)) {return generateTag(tagName, (Vector2d) data);}
		if(data.getClass().equals(Vector3f.class)) {return generateTag(tagName, (Vector3f) data);}
		if(data.getClass().equals(Vector3d.class)) {return generateTag(tagName, (Vector3d) data);}
		if(data.getClass().equals(Vector4f.class)) {return generateTag(tagName, (Vector4f) data);}
		if(data.getClass().equals(Vector4d.class)) {return generateTag(tagName, (Vector4d) data);}
		
		if(data.getClass().equals(Float[].class)) 	{return generateTag(tagName, (Float[]) data);}
		if(data.getClass().equals(Double[].class)) 	{return generateTag(tagName, (Double[]) data);}
		if(data.getClass().equals(Long[].class)) 	{return generateTag(tagName, (Long[]) data);}
		if(data.getClass().equals(Integer[].class))	{return generateTag(tagName, (Integer[]) data);}
		if(data.getClass().equals(Short[].class)) 	{return generateTag(tagName, (Short[]) data);}
		if(data.getClass().equals(Byte[].class)) 	{return generateTag(tagName, (Byte[]) data);}
		
		if(data.getClass().equals(Character[].class)) {return generateTag(tagName, (Character[]) data);}
		if(data.getClass().equals(String[].class))    {return generateTag(tagName, (String[]) data);}
		
		return null;
	}
	
	public static String generateTagLine(HashSet<Tag> tags) {
		String tagLine = "";
		
		boolean first = true;
		for(Tag tag : tags) {
			tagLine += tag.getTagString();
			
			if(first) {
				tagLine += "[";
				first = false;
				
			} else {
				tagLine += " ";
			}
		}
		
		return tagLine + "]";
	}
}
