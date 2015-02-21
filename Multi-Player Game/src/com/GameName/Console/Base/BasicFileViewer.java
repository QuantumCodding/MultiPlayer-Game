package com.GameName.Console.Base;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;


public class BasicFileViewer extends ConsoleTab implements ActionListener {
	private static final long serialVersionUID = -9071158131667577707L;
	
	private File defaultLocation;
	
	private JButton copyButton; 
	private JButton loadButton;
	
	private ArrayList<File> files;
	private JComboBox<String> filesComboBox;
	
	private DefaultStyledDocument document;
	private JTextPane fileTextPane;
	private JButton findButton;
	private JTextField searchBar;

	public BasicFileViewer(String name, Icon icon) {
		super(name, icon);
	}
	
	public void addComponents() {
		setLayout(new BorderLayout(0, 0));
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		add(horizontalStrut_1, BorderLayout.EAST);
		
		JPanel utilPanel = new JPanel();
		utilPanel.setBackground(new Color(225, 225, 225));
		add(utilPanel, BorderLayout.NORTH);
		utilPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel utilFilesComboBoxPanel = new JPanel();
		utilFilesComboBoxPanel.setBackground(new Color(225, 225, 225));
		utilPanel.add(utilFilesComboBoxPanel, BorderLayout.WEST);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(1);
		utilFilesComboBoxPanel.add(horizontalStrut_2);
		
		files = new ArrayList<>();
		filesComboBox = new JComboBox<>();
		filesComboBox.setBackground(new Color(225, 225, 225));
		utilFilesComboBoxPanel.add(filesComboBox);
		
		JPanel utilCopyLoadPanel = new JPanel();
		utilCopyLoadPanel.setBackground(new Color(225, 225, 225));
		utilPanel.add(utilCopyLoadPanel, BorderLayout.EAST);
		
		copyButton = new JButton("Copy");
		copyButton.setBackground(new Color(225, 225, 225));
		utilCopyLoadPanel.add(copyButton);
		
		loadButton = new JButton("Load");
		loadButton.setBackground(new Color(225, 225, 225));
		utilCopyLoadPanel.add(loadButton);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(1);
		utilCopyLoadPanel.add(horizontalStrut_3);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		utilPanel.add(horizontalGlue, BorderLayout.CENTER);
		
		JScrollPane fileTextPaneScrollPane = new JScrollPane();
		fileTextPaneScrollPane.setBackground(new Color(225, 225, 225));
		fileTextPaneScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(fileTextPaneScrollPane, BorderLayout.CENTER);

		document = new DefaultStyledDocument();
		fileTextPane = new JTextPane(document);
		fileTextPane.setEditable(false);
		fileTextPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		fileTextPaneScrollPane.setViewportView(fileTextPane);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(new Color(225, 225, 225));
		add(searchPanel, BorderLayout.SOUTH);
		searchPanel.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut = Box.createVerticalStrut(5);
		searchPanel.add(verticalStrut, BorderLayout.NORTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(5);
		searchPanel.add(verticalStrut_1, BorderLayout.SOUTH);
		
		JPanel searchLabelPanel = new JPanel();
		searchLabelPanel.setBackground(new Color(225, 225, 225));
		searchPanel.add(searchLabelPanel, BorderLayout.WEST);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(5);
		searchLabelPanel.add(horizontalStrut_4);
		
		JLabel searchLabel = new JLabel("Search: ");
		searchLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		searchLabelPanel.add(searchLabel);
		
		JPanel findButtonPanel = new JPanel();
		findButtonPanel.setBackground(new Color(225, 225, 225));
		searchPanel.add(findButtonPanel, BorderLayout.EAST);
		
		findButton = new JButton("Find");
		findButton.setBackground(new Color(225, 225, 225));
		findButtonPanel.add(findButton);
		
		Component horizontalStrut_5 = Box.createHorizontalStrut(5);
		findButtonPanel.add(horizontalStrut_5);
		
		JPanel searchBarPanel = new JPanel();
		searchBarPanel.setBackground(new Color(225, 225, 225));
		searchPanel.add(searchBarPanel, BorderLayout.CENTER);
		searchBarPanel.setLayout(new BorderLayout(0, 0));
		
		Component horizontalStrut_6 = Box.createHorizontalStrut(1);
		searchBarPanel.add(horizontalStrut_6, BorderLayout.WEST);
		
		Component verticalStrut_2 = Box.createVerticalStrut(5);
		searchBarPanel.add(verticalStrut_2, BorderLayout.NORTH);
		
		searchBar = new JTextField();
		searchBar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchBar.setColumns(10);
		searchBarPanel.add(searchBar);
		
		Component verticalStrut_3 = Box.createVerticalStrut(5);
		searchBarPanel.add(verticalStrut_3, BorderLayout.SOUTH);
		
		Component horizontalStrut_7 = Box.createHorizontalStrut(1);
		searchBarPanel.add(horizontalStrut_7, BorderLayout.EAST);

		findButton.addActionListener(this);
		searchBar.addActionListener(this);
		copyButton.addActionListener(this);
		loadButton.addActionListener(this);
		filesComboBox.addActionListener(this);
	}

	private boolean loading;
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == findButton || event.getSource() == searchBar) {
			JOptionPane.showMessageDialog(null, "Searching is Broken.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			
//			try {
//				int caretPosition = fileTextPane.getCaretPosition();
//				String sample = document.getText(caretPosition,
//						document.getLength() - caretPosition);
//				int index = sample.indexOf(searchBar.getText());
//				if(index == -1) {
//					sample = document.getText(0, caretPosition);
//					index = sample.indexOf(searchBar.getText()) + 
//							caretPosition;
//				}
//				
//				if(index == -1) {
//					JOptionPane.showMessageDialog(null, "String \"" + searchBar.getText() + "\" was not Found", 
//							"String not Found", JOptionPane.INFORMATION_MESSAGE);		
//					return;
//				}
//				System.out.println(index);
//				fileTextPane.requestFocusInWindow();
//				fileTextPane.setCaretPosition(index + searchBar.getText().length());
//				fileTextPane.select(index, index + searchBar.getText().length());
//			} catch(BadLocationException e) {}
			
		} else if(event.getSource() == loadButton) {
			JFileChooser fileChoser = new JFileChooser();
			fileChoser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChoser.setSelectedFile(defaultLocation); File currentFile;
			
			int returnVal = fileChoser.showOpenDialog(this);			
			if (returnVal == JFileChooser.APPROVE_OPTION)
				currentFile = fileChoser.getSelectedFile();
			else
				return;
			
			filesComboBox.removeAllItems();
			loading = true;
			for(File file : currentFile.listFiles()) {
				if(file.isDirectory()) continue;
				
				filesComboBox.addItem(file.getName());
				files.add(file);
			}
			
			loading = false;
			
		} else if(event.getSource() == filesComboBox && !loading) {
			if(filesComboBox.getSelectedIndex() == -1) return;
			
			try {
				String full = "", read;
				BufferedReader reader = new BufferedReader(new FileReader(
						files.get(filesComboBox.getSelectedIndex())
					));
				
				while((read = reader.readLine()) != null) 
					full += read + "\n";
				
				document.remove(0, document.getLength());
				document.insertString(0, full, null);
				
				reader.close();
			} catch (IOException | BadLocationException e) {
				e.printStackTrace();
			}
			
		} else if(event.getSource() == copyButton) {
			try {
				StringSelection stringSelection = new StringSelection(document.getText(0, document.getLength()));				
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setDefaultLocation(File defaultLocation) {
		this.defaultLocation = defaultLocation;
	}
}
