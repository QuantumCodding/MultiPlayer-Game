package com.GameName.Main.Debugging;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.lwjgl.opengl.Display;

import com.GameName.Main.GameName;

public class DebugWindow {
	private JFrame frame;
	private JLabel FPSLabel;
	
	private JPanel scrollPanel;
	private JScrollPane scrollPane;
	
	private boolean swap;
	
	public DebugWindow() {
		frame = new JFrame(Display.getTitle() + " | FPS: " + GameName.getFPS());
		FPSLabel = new JLabel("FPS: " + GameName.getFPS()); FPSLabel.setFont(new Font("", 1, 15));
		
		scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridLayout(0, 2)); //BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
				
		scrollPane = new JScrollPane(scrollPanel);		
//		scrollPane.setMinimumSize(new Dimension(400, 300));
//		scrollPane.setPreferredSize(new Dimension(400, 300));
//		scrollPane.setMaximumSize(new Dimension(400, 300));
		
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
		tempPanel.add(FPSLabel); tempPanel.add(scrollPane);
		frame.add(tempPanel);
		
		frame.setSize(410, 410);
		frame.setVisible(true);
	}
	
	public void add(JPanel panel) {
		scrollPanel.add(panel);
		
		if(swap) panel.setBackground(Color.LIGHT_GRAY);
		
		swap = !swap;
	}
	
	public void reload() {
		scrollPane.setViewportView(scrollPanel);
		frame.pack();
	}
	
	public void tick() {
		FPSLabel.setText("FPS: " + GameName.getFPS());
		frame.setTitle(Display.getTitle() + " | FPS: " + GameName.getFPS());
	}
}
