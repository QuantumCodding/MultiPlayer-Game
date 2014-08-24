package com.GameName.Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.lwjgl.opengl.Display;

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
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
		scrollPanel.setPreferredSize(new Dimension(200, 300));
		
		scrollPane = new JScrollPane(scrollPanel);
		
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
		tempPanel.add(FPSLabel); tempPanel.add(scrollPane);
		frame.add(tempPanel);
		
		frame.setSize(400, 400);
		frame.setVisible(true);
	}
	
	public void add(JPanel panel) {
		scrollPanel.add(panel);
		
		if(swap) panel.setBackground(Color.LIGHT_GRAY);
		
		swap = !swap;
	}
	
	public void reload() {
		scrollPane.setViewportView(scrollPanel);
	}
	
	public void tick() {
		FPSLabel.setText("FPS: " + GameName.getFPS());
		frame.setTitle(Display.getTitle() + " | FPS: " + GameName.getFPS());
	}
}
