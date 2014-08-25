package com.GameName.Main.Debugging;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class DebugPanel extends JPanel {
	private static final long serialVersionUID = 2675193303179574898L;
	
	public DebugPanel() {
		setMinimumSize	(new Dimension(400, 100));
		setPreferredSize(new Dimension(400, 100));
		setMaximumSize	(new Dimension(400, 100));
		
		setSize(new Dimension(400, 100));		
		setLayout(new FlowLayout());
	}	
}
