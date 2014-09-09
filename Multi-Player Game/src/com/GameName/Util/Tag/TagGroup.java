package com.GameName.Util.Tag;

public class TagGroup {
	
	private Tag idTag;
	private Tag[] tags;
	
	public TagGroup(Tag idTag, Tag[] tags) {
		this.idTag = idTag;
		this.tags = tags;
	}

	public Tag getIdTag() {
		return idTag;
	}

	public Tag[] getTags() {
		return tags;
	}
	
	public Tag getTagByIndex(int index) {
		return  tags[index];
	}
	
	public Tag getTagByName(String name) {
		for(Tag tag : tags) {
			if(tag.getTagName().equals(name)) {
				return tag;
			}
		}
		
		return null;
	}
	
	public boolean containsTag(String name) {
		return getTagByName(name) != null;
	}
	
	public String toString() {
		String toRep = idTag.getTagString() + "[";
		
		for(Tag tag : tags) {
			toRep += tag.getTagString();
		}
		
		return toRep + "]";
	}
}
