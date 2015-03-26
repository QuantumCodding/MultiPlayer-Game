package com.GameName.Util.Tag;

public class Tag {
	private String name;
	private Object info;
	
	private TagTypes type;
	private ArrayTypes arrayType;
	
	public Tag(String name, Object info) {
		this.name = name;
		this.info = info;
		
		type = getTagType(info);		
		arrayType = getArrayType(info);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Tag other = (Tag) obj;
		
		if (info == null) {
			if (other.info != null) {
				return false;
			}
			
		} else if (!info.equals(other.info)) {
			return false;
			
		} if (name == null) {
			if (other.name != null) {
				return false;
			}
			
		} else if (!name.equals(other.name)) {
			return false;
		}
		
		return true;
	}

	public Tag clone() {
		return new Tag(name, info);
	}
	
	public String getName() {
		return name;
	}

	public Object getInfo() {
		return info;
	}
	
	public TagTypes getType() {return type;}
	public ArrayTypes getArrayType() {return arrayType;}

	public String toString() {
		return "Tag [name=" + name + ", info=" + info + "]";
	}
	
	public enum TagTypes {
		Byte, Short, Integer, Long, Float, Double, 
		Character, Boolean,	String, 
		Array, Vector, Unknown		
	}
	
	public enum ArrayTypes {
		Byte, Short, Integer, Long, Float, Double, 
		Character, Boolean,	String
	}
	
	public static TagTypes getTagType(Object data) {
		if(data == null) {return TagTypes.Unknown;}
		
		if(data.getClass().equals(Float.class)) 	{return TagTypes.Float;}
		if(data.getClass().equals(Double.class)) 	{return TagTypes.Double;}
		if(data.getClass().equals(Long.class)) 		{return TagTypes.Long;}
		if(data.getClass().equals(Integer.class))	{return TagTypes.Integer;}
		if(data.getClass().equals(Short.class)) 	{return TagTypes.Short;}
		if(data.getClass().equals(Byte.class)) 		{return TagTypes.Byte;}

		if(data.getClass().equals(Boolean.class)) 	{return TagTypes.Boolean;}
		if(data.getClass().equals(Character.class)) {return TagTypes.Character;}
		if(data.getClass().equals(String.class)) 	{return TagTypes.String;}
                                                              
//		if(data.getClass().equals(MathVec3f.class)) {return TagTypes.Vector;}
		                                                      
		if(data.getClass().equals(Float[].class)) 	{return TagTypes.Array;}
		if(data.getClass().equals(Double[].class)) 	{return TagTypes.Array;}
		if(data.getClass().equals(Long[].class)) 	{return TagTypes.Array;}
		if(data.getClass().equals(Integer[].class))	{return TagTypes.Array;}
		if(data.getClass().equals(Short[].class)) 	{return TagTypes.Array;}
		if(data.getClass().equals(Byte[].class)) 	{return TagTypes.Array;}

		if(data.getClass().equals(Boolean[].class))   {return TagTypes.Array;}
		if(data.getClass().equals(Character[].class)) {return TagTypes.Array;}
		if(data.getClass().equals(String[].class))    {return TagTypes.Array;}
		
		return TagTypes.Unknown;
	}
	
	public static ArrayTypes getArrayType(Object data) {
		if(data == null) {return null;}
		
		if(data.getClass().equals(Float[].class)) 	{return ArrayTypes.Float;}
		if(data.getClass().equals(Double[].class)) 	{return ArrayTypes.Double;}
		if(data.getClass().equals(Long[].class)) 	{return ArrayTypes.Long;}
		if(data.getClass().equals(Integer[].class))	{return ArrayTypes.Integer;}
		if(data.getClass().equals(Short[].class)) 	{return ArrayTypes.Short;}
		if(data.getClass().equals(Byte[].class)) 	{return ArrayTypes.Byte;}

		if(data.getClass().equals(Boolean[].class))   {return ArrayTypes.Boolean;}
		if(data.getClass().equals(Character[].class)) {return ArrayTypes.Character;}
		if(data.getClass().equals(String[].class))    {return ArrayTypes.String;}
		
		return null;
	}
}
