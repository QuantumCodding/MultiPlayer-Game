package com.GameName.Util.Tag;

public class Tag {
	private String tagName;
	private Object tagInfo;
	
	public Tag(String tagName, Object tagInfo) {
		this.tagName = tagName;
		this.tagInfo = tagInfo;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((tagInfo == null) ? 0 : tagInfo.hashCode());
		result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
		
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Tag other = (Tag) obj;
		
		if (tagInfo == null) {
			if (other.tagInfo != null) {
				return false;
			}
			
		} else if (!tagInfo.equals(other.tagInfo)) {
			return false;
			
		} if (tagName == null) {
			if (other.tagName != null) {
				return false;
			}
			
		} else if (!tagName.equals(other.tagName)) {
			return false;
		}
		
		return true;
	}

	public String toString() {
		return "Tag [tagName=" + tagName + ", tagInfo=" + tagInfo + "]";
	}

	public String getTagString() {
		return DTGGenerator.generateTag(tagName, tagInfo);
	}
	
	public String getTagName() {
		return tagName;
	}

	public Object getTagInfo() {
		return tagInfo;
	}
}