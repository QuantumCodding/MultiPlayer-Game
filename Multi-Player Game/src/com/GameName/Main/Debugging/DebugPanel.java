package com.GameName.Main.Debugging;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class DebugPanel extends JPanel {
	private static final long serialVersionUID = 2675193303179574898L;
	private String name;
	
	public DebugPanel(String name) {
		setMinimumSize	(new Dimension(400, 100));
		setPreferredSize(new Dimension(400, 100));
		setMaximumSize	(new Dimension(400, 100));
		
		setSize(new Dimension(400, 100));		
		setLayout(new FlowLayout());
		
		this.name = name;
	}	
	
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
}
