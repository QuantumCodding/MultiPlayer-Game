package com.GameName.Console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.GameName.Console.Base.ConsoleTab;
import com.GameName.Engine.Threads.GameThreadTracker;

public class ThreadStatusTab extends ConsoleTab implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = -3660258176007969521L;
	
	private ArrayList<GameThreadTracker> threads;
	private DefaultListModel<String> threadsListModel;
	private JList<String> threadsList;
	
	private JPanel threadCanvas;
	private JCheckBox pauseButton;
	private JButton restartButton;
	private JLabel threadNameLabel;
	private JTextField tpsField;

	public ThreadStatusTab() {
		super("Thread Status", createImageIcon("res/textures/Console/ThreadIcon.png"));
	}
	
	public void addComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane);
		
		JPanel currentThreadPanel = new JPanel();
		splitPane.setRightComponent(currentThreadPanel);
		currentThreadPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel utilPanel = new JPanel();
		currentThreadPanel.add(utilPanel, BorderLayout.NORTH);
		utilPanel.setLayout(new BorderLayout(0, 0));
		
		threadNameLabel = new JLabel();
		threadNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		utilPanel.add(threadNameLabel, BorderLayout.CENTER);
		
		pauseButton = new JCheckBox("Paused");
		pauseButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		utilPanel.add(pauseButton, BorderLayout.EAST);
		
		Component horizontalStrut = Box.createHorizontalStrut(5);
		utilPanel.add(horizontalStrut, BorderLayout.WEST);
		
		JPanel utilTPSResetPanel = new JPanel();
		utilPanel.add(utilTPSResetPanel, BorderLayout.SOUTH);
		utilTPSResetPanel.setLayout(new BorderLayout(0, 0));
		
		tpsField = new JTextField();
		tpsField.setText("");
		tpsField.setBackground(new Color(102, 102, 102));
		tpsField.setFont(new Font("Tahoma", Font.BOLD, 14));
		tpsField.setEditable(false);
		utilTPSResetPanel.add(tpsField, BorderLayout.WEST);
		tpsField.setColumns(4);
		
		restartButton = new JButton("Restart");
		utilTPSResetPanel.add(restartButton, BorderLayout.CENTER);
		restartButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		threadCanvas = new JPanel();
		threadCanvas.setBorder(null);
		threadCanvas.setBackground(new Color(225, 225, 225));
		currentThreadPanel.add(threadCanvas, BorderLayout.CENTER);
		
		JScrollPane threadsListScrollPane = new JScrollPane();
		threadsListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane.setLeftComponent(threadsListScrollPane);
		
		threads = new ArrayList<>();
		threadsListModel = new DefaultListModel<>();
		threadsList = new JList<>(threadsListModel);
		threadsListScrollPane.setViewportView(threadsList);
		
		restartButton.addActionListener(this);
		pauseButton.addActionListener(this);
		threadsList.addListSelectionListener(this);
	}

	public void addThread(GameThreadTracker tracker) {
		threads.add(tracker); 
		threadsListModel.addElement(tracker.getName());
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() ==  pauseButton) {
			if(pauseButton.isSelected()) {
				try {
					threads.get(threadsList.getSelectedIndex()).pause();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} else {
				threads.get(threadsList.getSelectedIndex()).resume();
			}
			
		} else if(event.getSource() ==  restartButton) {
			threads.get(threadsList.getSelectedIndex()).restart();
		}
	}

	private void setCurrentThread(int index) {
		GameThreadTracker tracker = threads.get(index);
		
		threadCanvas.removeAll(); 
		tracker.setSize(threadCanvas.getSize());
		tracker.setLocation(0, 0);
		
		tracker.setChartWidth(threadCanvas.getWidth() - GameThreadTracker.CHART_OFFSET_X * 2);
		tracker.setChartHeight(threadCanvas.getHeight() - GameThreadTracker.CHART_OFFSET_Y * 2);
		
		threadCanvas.add(tracker);
		
		threadNameLabel.setText(tracker.getName());
		pauseButton.setSelected(tracker.isPaused());
		tracker.setTPSLabel(getTPSField());
	}

	public void valueChanged(ListSelectionEvent event) {
		if(event.getSource() == threadsList) {
			setCurrentThread(threadsList.getSelectedIndex());
		}
	}
	
	public JTextField getTPSField() { return tpsField; }
}
