package com.GameName.Util;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class StringEffectFormater {
	
	/**
	 *  Applies a StringEffect to a String
	 *
	 *  @param str The String to apply the Effect to
	 *  @param effect The Effect to apply to the String
	 *  @param lineEnd Extra chars to apply to the end of the string ex. "\n", "\t"
	 */
	public static String addEffect(String str, StringEffect effect, String lineEnd) {
		lineEnd.replaceAll("\n", "<br>");		
		return  effect.addEffects(str) + lineEnd;
	}

	public static StringEffect getEffect(String str) {
		StringEffect toRep = new StringEffect();
		
		if(str.contains("<color=")) {
			toRep.setColor(hex2Rgb(str.substring(str.indexOf("<color=") + 1, str.indexOf("<color=") + 9)));
		}
		
		if(str.contains("<b>")) {
			toRep.setBold();
		}
		
		if(str.contains("<i>")) {
			toRep.setItalics();
		}
		
		return toRep;
	}
	
	public static SimpleAttributeSet asSimpleAttributeSet(StringEffect effect) {
		SimpleAttributeSet attSet = new SimpleAttributeSet();
		
		if(effect.getEffects().contains("<color=")) {
			StyleConstants.setForeground(attSet, hex2Rgb(effect.getEffects().substring(
					effect.getEffects().indexOf("<color=") + 7,
					effect.getEffects().substring(effect.getEffects().indexOf("<color=")).indexOf('>')
				)));
		}
		
		if(effect.getEffects().contains("<b>")) {
			StyleConstants.setBold(attSet, true);
		}
		
		if(effect.getEffects().contains("<i>")) {
			StyleConstants.setItalic(attSet, true);
		}
		
		return attSet;
	}
	
	public static HashMap<String, StringEffect> getPairs(String line) {
		HashMap<String, StringEffect> set = new HashMap<String, StringEffect>();
		
		StringEffect effect = new StringEffect();
		
		String str = "";
		String build = "";
		
		boolean allert = false, atEnd = false;
		
		for(char c : line.toCharArray()) {
			build += c;
			
			if(!allert) {
				if(c == '<') {
					allert = true;
					
				} else {
					if(atEnd) {
						set.put(str, effect);
						
						effect = new StringEffect();
						str = "";
						
						atEnd = false;
					}
					
					str += c;
				}
				
			} else if(c == '>') {
				allert = false;
				
				if(build.contains("<color=")) {
					effect.setColor(hex2Rgb(build.substring(
							build.indexOf("<color=") + 7, 
							build.indexOf("<color=") + 15)//15)
						));
					
				} else if(build.contains("<i>")) {
					effect.setItalics();
					
				} else if(build.contains("<b>")) {
					effect.setBold();
					
				} else if(build.contains("<br>")) {
					str += "\n";
					set.put(str, effect);
					str = "";
					
				} else if(!atEnd && (build.contains("</b>") || build.contains("</i>") || build.contains("</color>"))) {
					atEnd = true;
				}
				
				build = "";				
			
			} else {
				if(atEnd && !build.startsWith("</")) {
					set.put(str, effect);
					
					effect = new StringEffect();
					str = "";
					
					atEnd = false;
				}
			}
		}

		if(!str.trim().equals("")) set.put(str, effect);
		return set;
	}
	
	public static String[] getLines(String line) {
		line = removeEffectTags(line);
		return line.split("<br>");
	}
	
	public static String removeEffectTags(String line) {
		String toRep = line;
		
		if(line.contains("<color=")) {
			toRep.replace(				
					toRep.substring(line.indexOf("<color=") - 7, line.indexOf("<color=") + 9)
			, "");
			
			toRep.replaceAll("</color>", "");
		}
		
		if(line.contains("<i>")) {
			toRep.replaceAll("<i>", "");
			toRep.replaceAll("</i>", "");
		}
		
		if(line.contains("<b>")) {
			toRep.replaceAll("<b>", "");
			toRep.replaceAll("</b>", "");
		}
		
		return toRep;
	}
	
	private static Color hex2Rgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
}