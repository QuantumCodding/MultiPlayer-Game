package com.GameName.Util.Tag;

import com.GameName.Util.Vectors.Vector3f;


public class DTGGenerator {
	public static String generateTag(Vector3f data) {return "V" + data.getX() + "," + data.getY() + "," + data.getZ();}

	public static String generateTag(float data)  {return "f" + data;}
	public static String generateTag(double data) {return "d" + data;}
	public static String generateTag(long data)   {return "l" + data;}
	public static String generateTag(short data)  {return "s" + data;}
	public static String generateTag(int data)    {return "i" + data;}
	public static String generateTag(byte data)   {return "b" + data;}
	public static String generateTag(boolean data){return "B" + data;}
	
	public static String generateTag(Float data)  {return "f" + data.floatValue();}  
	public static String generateTag(Double data) {return "d" + data.doubleValue();} 
	public static String generateTag(Long data)   {return "l" + data.longValue();}   
	public static String generateTag(Short data)  {return "s" + data.shortValue();}  
	public static String generateTag(Integer data){return "i" + data.intValue();}    
	public static String generateTag(Byte data)   {return "b" + data.byteValue();}   
	public static String generateTag(Boolean data){return "B" + data.booleanValue();}
	
	public static String generateTag(char data)   	{return "c" + data;}
	public static String generateTag(Character data){return "c" + data.charValue();}
	public static String generateTag(String data) 	{return "S" + data;}

	public static String generateTag(float[] data)  {String toRep = "Af"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;} 	
	public static String generateTag(double[] data) {String toRep = "Ad"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;} 	
	public static String generateTag(long[] data)   {String toRep = "Al"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;} 	
	public static String generateTag(short[] data)  {String toRep = "As"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;} 	
	public static String generateTag(int[] data)    {String toRep = "Ai"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;} 	
	public static String generateTag(byte[] data)   {String toRep = "Ab"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;} 	
	public static String generateTag(char[] data)   {String toRep = "Ac"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;} 	
	public static String generateTag(boolean[] data){String toRep = "AB"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;} 	
	
	public static String generateTag(Float[] data)    {String toRep = "Af"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	public static String generateTag(Double[] data)   {String toRep = "Ad"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	public static String generateTag(Long[] data)     {String toRep = "Al"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	public static String generateTag(Short[] data)    {String toRep = "As"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	public static String generateTag(Integer[] data)  {String toRep = "Ai"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	public static String generateTag(Byte[] data)     {String toRep = "Ab"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	public static String generateTag(Character[] data){String toRep = "Ac"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	public static String generateTag(Boolean[] data)  {String toRep = "AB"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	public static String generateTag(String[] data)   {String toRep = "AS"; for(int i = 0; i < data.length; i ++) toRep += data[i] + ","; return toRep;}
	
	public static String generateTag(Object data) {		
		if(data.getClass().equals(Float.class)) 	{return generateTag((Float) data);}
		if(data.getClass().equals(Double.class)) 	{return generateTag((Double) data);}
		if(data.getClass().equals(Long.class)) 		{return generateTag((Long) data);}
		if(data.getClass().equals(Integer.class))	{return generateTag((Integer) data);}
		if(data.getClass().equals(Short.class)) 	{return generateTag((Short) data);}
		if(data.getClass().equals(Byte.class)) 		{return generateTag((Byte) data);}

		if(data.getClass().equals(Boolean.class))   {return generateTag((Boolean) data);}
		if(data.getClass().equals(Character.class)) {return generateTag((Character) data);}
		if(data.getClass().equals(String.class)) 	{return generateTag((String) data);}

		if(data.getClass().equals(Vector3f.class)) {return generateTag((Vector3f) data);}
		
		if(data.getClass().equals(Float[].class)) 	{return generateTag((Float[]) data);}
		if(data.getClass().equals(Double[].class)) 	{return generateTag((Double[]) data);}
		if(data.getClass().equals(Long[].class)) 	{return generateTag((Long[]) data);}
		if(data.getClass().equals(Integer[].class))	{return generateTag((Integer[]) data);}
		if(data.getClass().equals(Short[].class)) 	{return generateTag((Short[]) data);}
		if(data.getClass().equals(Byte[].class)) 	{return generateTag((Byte[]) data);}
		
		if(data.getClass().equals(Boolean[].class))   {return generateTag( (Boolean[]) data);}
		if(data.getClass().equals(Character[].class)) {return generateTag( (Character[]) data);}
		if(data.getClass().equals(String[].class))    {return generateTag( (String[]) data);}
		
		System.out.println(data);
		return null;
	}
}
