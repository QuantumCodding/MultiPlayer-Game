package com.GameName.Items;

public class ItemStack {
	private Item item;
	private int size;
	
	public ItemStack() {}
	
	public ItemStack(int id, int size) {
		item = ItemManager.getItemById(id);
	}
	
	public Item getItem() {
		return item;
	}

	public int getSize() {
		return size;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + size;
		
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		ItemStack other = (ItemStack) obj;
		
		if (item == null) {
			if (other.item != null)
				return false;
			
		} else if (!item.equals(other.item))
			return false;
		
		if (size != other.size)
			return false;
		
		return true;
	}

}
