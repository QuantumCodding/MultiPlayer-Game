package com.GameName.World;

public class Biom {
	public static String[] biomTypes = new String[] {
		//	 1             2                3              4               5	         Hydration
	/*T 1 */ "Tundra    ", "Tundra       ", "Artic      ", "Mountin     ", "Glacier    ",
	/*E 2 */ "Vally     ", "Tiga         ", "Tiga       ", "Mountin     ", "Dence Tiga ",
	/*M 3 */ "Wastland  ", "Dry Grassland", "Grassland  ", "Forest      ", "Swamp Land ",
	/*P 4 */ "Wastland  ", "Plains       ", "Plains     ", "Dense Forest", "Rain Forest",
	/*  5 */ "Salt Flat ", "Desert       ", "Lush Desert", "Savana      ", "Tropics    ",
	};
	
	public int hydration;
	public int temperature;
	
	public boolean hydrationDefined;
	public boolean temperatureDefined;
	
	public String type;
	public int typeId;
	
	public String toString() {
		return "Biom[Type=" + type.trim() + ", Hydration=" + hydration + ", Temperature=" + temperature + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hydration;
		result = prime * result + (hydrationDefined ? 1231 : 1237);
		result = prime * result + temperature;
		result = prime * result + (temperatureDefined ? 1231 : 1237);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Biom other = (Biom) obj;
		
		if (hydration != other.hydration) return false;
		if (hydrationDefined != other.hydrationDefined) return false;
		if (temperature != other.temperature) return false;
		if (temperatureDefined != other.temperatureDefined) return false;
		
		return true;
	}
}
