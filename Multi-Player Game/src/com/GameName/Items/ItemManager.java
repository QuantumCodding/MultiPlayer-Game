package com.GameName.Items;

public class ItemManager {
	private static Item[] items;
	
	public static Item getItemById(int i) {
		return items[i];
	}
	
	public static Item getItemByName(String s) {
		for(Item i : items) {
			if(i.getName().equals(s)) {
				return i;
			}
		}
		
		throw new IndexOutOfBoundsException("No Item with the name " + s);
	}
	
	public static int nextId() {
		for(int i = 0; i < items.length; i ++) {
			if(items[i] == null) {
				return i;
			}
		}
		
		throw new RuntimeException("No More Valid ID\'s!");
	}
}
