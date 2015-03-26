package com.GameName.Console.Base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.GameName.Util.Time;

public class BasicLog extends ConsoleTab implements ActionListener {
	private static final long serialVersionUID = 2564319646507739146L;
	
	public static final Color DEFAULT_COLOR = new Color(0, 0, 0);
	public static final Font DEFAULT_FONT = new Font("Tahoma", Font.PLAIN, 12);
	public static final Font DEFAULT_FONT_BOLD = new Font("Tahoma", Font.BOLD, 12);
	public static final Font DEFAULT_FONT_ITALIC = new Font("Tahoma", Font.ITALIC, 12);
	
	private JButton saveButton;
	private JButton copyButton;
	private JButton clearButton;

	private JCheckBox scrollLockCheckBox;

	private JTextPane logPane;
	private ArrayList<String> lines, inputs;	
	private DefaultStyledDocument document;
	private StyleContext context;
	private JScrollPane logScrollPane;
	private SmartScroller scroller;
	private JTextField inputField;

	private boolean addTimeStamp;
	private String oldInput;
	private int lastImput;
	
	public BasicLog(String name, Icon icon) {
		super(name, icon);
	}
	
	public void addComponents() {
		setLayout(new BorderLayout(0, 0));
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		add(horizontalStrut_1, BorderLayout.EAST);
		
		JPanel inputPanel = new JPanel();
		add(inputPanel, BorderLayout.SOUTH);
		inputPanel.setBackground(new Color(225, 225, 225));
		inputPanel.setLayout(new BorderLayout(0, 0));
		
		inputField = new JTextField();
		inputPanel.add(inputField, BorderLayout.CENTER);
		inputField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		inputField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					Container parent = getParent();
					while((parent = parent.getParent()) != null && !(parent instanceof JFrame)) {}
					if(parent != null) parent.setVisible(false);
					return;
				}
				
				if(inputs.size() == 0) return;
				
				if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					if(e.getKeyCode() == KeyEvent.VK_UP) 
						lastImput --;
					else
						lastImput ++;
					
					if(lastImput < 0) lastImput = 0;
					else if(lastImput >= inputs.size()) lastImput = inputs.size();
					
					inputField.setText(lastImput == inputs.size() ? oldInput : inputs.get(lastImput));
				} else {
					oldInput = inputField.getText();
				}
			}
		});
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(10);
		inputPanel.add(horizontalStrut_4, BorderLayout.WEST);
		
		Component horizontalStrut_5 = Box.createHorizontalStrut(10);
		inputPanel.add(horizontalStrut_5, BorderLayout.EAST);
		
		Component verticalStrut = Box.createVerticalStrut(5);
		inputPanel.add(verticalStrut, BorderLayout.SOUTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(5);
		inputPanel.add(verticalStrut_1, BorderLayout.NORTH);
		
		JPanel utilPanel = new JPanel();
		utilPanel.setBackground(new Color(225, 225, 225));
		add(utilPanel, BorderLayout.NORTH);
		utilPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel utilScrollLockPanel = new JPanel();
		utilScrollLockPanel.setBackground(new Color(225, 225, 225));
		utilPanel.add(utilScrollLockPanel, BorderLayout.WEST);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(1);
		utilScrollLockPanel.add(horizontalStrut_3);
		
		scrollLockCheckBox = new JCheckBox("Scroll Lock");
		scrollLockCheckBox.setBackground(new Color(225, 225, 225));
		scrollLockCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 11));
		utilScrollLockPanel.add(scrollLockCheckBox);
		
		JPanel utilCopyUpClearPanel = new JPanel();
		utilCopyUpClearPanel.setBackground(new Color(225, 225, 225));
		utilPanel.add(utilCopyUpClearPanel, BorderLayout.EAST);
		
		copyButton = new JButton("Copy");
		copyButton.setBackground(new Color(225, 225, 225));
		utilCopyUpClearPanel.add(copyButton);
		
		saveButton = new JButton("Save");
		saveButton.setBackground(new Color(225, 225, 225));
		utilCopyUpClearPanel.add(saveButton);
		
		clearButton = new JButton("Clear");
		clearButton.setBackground(new Color(225, 225, 225));
		utilCopyUpClearPanel.add(clearButton);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(1);
		utilCopyUpClearPanel.add(horizontalStrut_2);
		
		logScrollPane = new JScrollPane();
		scroller = new SmartScroller(logScrollPane, scrollLockCheckBox);
		logScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(logScrollPane, BorderLayout.CENTER);
		
		lines = new ArrayList<>(); inputs = new ArrayList<>();
		document = new DefaultStyledDocument();
		context = new StyleContext();
		logPane = new JTextPane(document);
		logPane.setEditable(false);
		logPane.setFont(DEFAULT_FONT);
		logScrollPane.setViewportView(logPane);
		
		addStyle("default", DEFAULT_COLOR, DEFAULT_FONT);
		addStyle("input", new Color(0, 150, 0), DEFAULT_FONT);
		addStyle("warning", new Color(225, 204, 0), DEFAULT_FONT);
		addStyle("error", new Color(255, 0, 0), new Font(DEFAULT_FONT.getFontName(), Font.BOLD, DEFAULT_FONT.getSize()));
		
		copyButton.addActionListener(this);
		saveButton.addActionListener(this);
		clearButton.addActionListener(this);
		inputField.addActionListener(this);
		
		scrollLockCheckBox.addActionListener(this);				
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == copyButton) {
			try {
				StringSelection stringSelection = new StringSelection(document.getText(0, document.getLength()));				
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			
		} else if(event.getSource() == saveButton) {
			JFileChooser fileChoser = new JFileChooser();
			File currentFile;
			
			int returnVal = fileChoser.showOpenDialog(this);			
			if (returnVal == JFileChooser.APPROVE_OPTION)
				currentFile = fileChoser.getSelectedFile();
			else
				return;
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
				writer.write(document.getText(0, document.getLength()));
				writer.close();
			} catch(IOException | BadLocationException e) {
				e.printStackTrace();
			}
			
		} else if(event.getSource() == clearButton) {
			clearLog();
			
		} else if(event.getSource() == scrollLockCheckBox) {
			scroller.isLocked = scrollLockCheckBox.isSelected();
		
		} else if(event.getSource() == inputField) {
			String input = inputField.getText();
			inputField.setText(""); inputs.add(input);
			addLine(input, "input"); lastImput = inputs.size();
			boolean result = evaluateInput(input);
			if(!result) invalidCommand();
		}
	}	

	public void addLine(String text) { 
		addLine(text, "default");}
	public void addLine(String text, String styleName) { 
		try {
			Style style = context.getStyle(styleName);
			if(addTimeStamp) text = Time.getTime() + ": " + text;
			document.insertString(document.getLength(), text + "\n", style);
			lines.add(text);
		} catch (BadLocationException e) {}	
		
	}
	
	public String getLine(int line) {
		return lines.get(line);
	}

	public String addStyle(String styleName, Color color) {
		return addStyle(styleName, color, DEFAULT_FONT);}
	public String addStyle(String styleName, Font font) {
		return addStyle(styleName, DEFAULT_COLOR, font);}
	public String addStyle(String styleName, Color color, Font font) {
		Style style = context.addStyle(styleName, null);
		StyleConstants.setForeground(style, color);
		
		StyleConstants.setFontFamily(style, font.getFamily());
		StyleConstants.setFontSize(style, font.getSize());
		StyleConstants.setBold(style, font.getStyle() == Font.BOLD || font.getStyle() == (Font.BOLD | Font.ITALIC));
		StyleConstants.setItalic(style, font.getStyle() == Font.ITALIC || font.getStyle() == (Font.BOLD | Font.ITALIC));
		
		return styleName;
	}
	
	protected boolean evaluateInput(String input) {
		return false;
	}
	
	protected void invalidCommand() {
		addLine("[ERROR] Invalid Command! For help please type /help", "error");
	}
	
	public boolean usingTimeStamp() { return addTimeStamp; }
	public void setUseTimeStamp(boolean use) {
		addTimeStamp = use;
	}
	
	public void clearLog() {
		try {
			document.remove(0, document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void focusOnInputField() {
		inputField.requestFocus(true);
		inputField.setFocusable(true);
		inputField.requestFocusInWindow();
		inputField.requestFocus();
	}
}
