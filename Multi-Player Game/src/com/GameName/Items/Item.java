package com.GameName.Items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public abstract class Item {

	private String name;
	private int id;
	
	public Item(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	protected abstract String getTextureName();

	public Texture getTexture() {
		try {
			return TextureLoader.getTexture("png", new FileInputStream(new File("res/textures/items/" + getTextureName() + ".png")));
		} catch (IOException e) {
			System.out.println("The texture " + getTextureName() + " was not found or successfully loaded for Item " + name);
			e.printStackTrace();
		}
		
		return null;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Item other = (Item) obj;
		
		if (id != other.id)
			return false;
		
		if (name == null) {
			if (other.name != null)
				return false;
			
		} else if (!name.equals(other.name))
			return false;
		
		return true;
	}
}
