package com.GameName.Console.Base;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public abstract class ConsoleTab extends JPanel {
	private static final long serialVersionUID = -9215556867788808917L;
	
	private String name;
	private Icon icon;

	public ConsoleTab(String name, Icon icon) {
		this.name = name;
		this.icon = icon;
		
		setBackground(new Color(225, 225, 225));
		addComponents();
	}

	public abstract void addComponents();

	public static ImageIcon createImageIcon(String path) {
		try {
			Image img = ImageIO.read(new File(path));
			
			if (img != null) {
				return new ImageIcon(img, null);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		} catch(IOException e) {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public String getName() { return name; }
	public Icon getIcon() { return icon; }
}
