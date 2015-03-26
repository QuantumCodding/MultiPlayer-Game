package com.GameName.Util.Tag;

public class TagGroup {
	private Tag idTag;
	private Tag[] tags;
	
	public TagGroup(Tag idTag, Tag... tags) {
		this.idTag = idTag;
		this.tags = tags;
	}

	public Tag getIdTag() {
		return idTag;
	}
	
	public void setIdTag(Tag idTag) {
		this.idTag = idTag;
	}
	
	public Tag[] getTags() {
		return tags;
	}
	
	public void setTags(Tag[] tags) {
		this.tags = tags;
	}	
	
	public void setTag(int index, Tag tag) {
		tags[index] = tag;
	}
	
	public void addTag(Tag tag) {
		for(int i = 0; i < tags.length; i ++) {
			if(tags[i] == null) {
				tags[i] = tag;
				return;
			}
		}
		
		Tag[] tags2 = new Tag[tags.length + 1];
		System.arraycopy(tags, 0, tags2, 0, tags.length);
		tags2[tags.length] = tag; tags = tags2;
	} 
	
	public void removeTag(int index) {
		tags[index] = null;
	}
	
	public Tag getTagByName(String name) {
		for(Tag tag : tags) {
			if(tag.getName().equals(name)) {
				return tag;
			}
		}
		
		return null;
	}
	
	public boolean containsTag(String name) {
		return getTagByName(name) != null;
	}
	
	public String toString() {
		String toRep = idTag + "[";
		
		for(Tag tag : tags) {
			toRep += tag;
		}
		
		return toRep + "]";
	}
}
