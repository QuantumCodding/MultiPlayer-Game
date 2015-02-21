package com.GameName.Console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.GameName.Console.Base.ConsoleTab;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.Tag;
import com.GameName.Util.Tag.Tag.ArrayTypes;
import com.GameName.Util.Tag.Tag.TagTypes;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector3f;

public class TagEditorTab extends ConsoleTab implements ActionListener, ListSelectionListener, KeyListener {

	public TagEditorTab() {
		super("Tag Editor", createImageIcon("res/textures/Console/EditTagIcon.png"));
	}

	private static final long serialVersionUID = 1L;
	private File currentFile;
	
	private JPanel contentPane;
		private JPanel groupPanel;
			private JTextField groupNameField;
			private JComboBox<String> groupComboBox;
			private DefaultComboBoxModel<String> groupComboBoxModel;
			
			private JButton newGroupButton;
			private JButton loadGroupButton;
			private JButton deleteGroupButton;
			
			private JPanel tagInfoPanel;
			
				private JPanel tagNamePanel;			
					private JTextField tagNameField;
					
				private JPanel tagInfoMidPanel;
					private JSeparator tagNameSeparator;
					private JSeparator tagOptionsSeparator;
					
				private JPanel tagDataPanel;
				private JButton saveTagButton;
				private JPanel tagValuePanel;
				private JPanel tagDataTypePanel;
				@SuppressWarnings("rawtypes")
				private JComboBox tagDataTypeSubTypeComboBox;
				private JScrollPane tagDataScrollPane;
				private JList<String> tagDataList;
				private DefaultListModel<String>  tagDataListModel;
					
				private JPanel tagOptionsPanel;
					@SuppressWarnings("rawtypes")
					private JComboBox tagTypeComboBox;

				
			private JPanel tagManagmentPanel;
				private JScrollPane tagListScrollPane;
					private JList<String> tagList;
					private DefaultListModel<String> tagListModel;

			private JPanel tabCreationPanel;
				private JButton newTagButton;
				private JButton deleteTagButton;
							
	private ArrayList<TagGroup> tagGroups; 
	private int currentGroupIndex, currentTagIndex;
	private Tag currentTag;
	private JPanel valuePanel;
	private JTextField valueField;
	private JButton newValueButton;
	private JButton deleteValueButton;
	private JButton saveGroupButton;
	private Component horizontalStrut_6;
	private JPanel groupComboBoxPanel;
	private JPanel groupNamePanel;
	private JPanel groupButtonsPanel;

	private JTabbedPane tabbedPane;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addComponents() {
		tagGroups = new ArrayList<>();
		
		currentGroupIndex = currentTagIndex = -1;
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		
		contentPane = new JPanel();
		tabbedPane.addTab("New Tag", null, contentPane, null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		groupPanel = new JPanel();
		groupPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Tag Group", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(groupPanel, BorderLayout.NORTH);
		groupPanel.setLayout(new BorderLayout(0, 0));
		
		groupNamePanel = new JPanel();
		groupPanel.add(groupNamePanel, BorderLayout.WEST);
		
		JLabel groupNameLabel = new JLabel("Group Name: ");
		groupNamePanel.add(groupNameLabel);
		groupNameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		groupNameField = new JTextField();
		groupNamePanel.add(groupNameField);
		groupNameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		groupNameField.setColumns(10);
		
		groupComboBoxPanel = new JPanel();
		groupPanel.add(groupComboBoxPanel, BorderLayout.CENTER);
		
		groupComboBox = new JComboBox<String>();
		groupComboBoxPanel.add(groupComboBox);
		groupComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		groupComboBoxModel = new DefaultComboBoxModel<>();
		groupComboBox.setModel(groupComboBoxModel);
		
//		addGroup(new TagGroup(new Tag("Id", "This is a Test"), new Tag[] {new Tag("Test", "Test String"), new Tag("ArrAyTast", new Byte[] {(byte) 5, (byte) 6})}));		
	
		
		groupButtonsPanel = new JPanel();
		groupPanel.add(groupButtonsPanel, BorderLayout.SOUTH);
		
		newGroupButton = new JButton("New");
		groupButtonsPanel.add(newGroupButton);
		newGroupButton.setForeground(new Color(34, 139, 34));
		newGroupButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		loadGroupButton = new JButton("Load");
		groupButtonsPanel.add(loadGroupButton);
		loadGroupButton.setForeground(new Color(51, 51, 255));
		loadGroupButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		deleteGroupButton = new JButton("Delete");
		groupButtonsPanel.add(deleteGroupButton);
		deleteGroupButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		deleteGroupButton.setForeground(new Color(204, 0, 0));
		
		tagManagmentPanel = new JPanel();
		contentPane.add(tagManagmentPanel, BorderLayout.CENTER);
		tagManagmentPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Tag Manager", TitledBorder.TRAILING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tagManagmentPanel.setLayout(new BorderLayout(0, 0));
		
		tagListScrollPane = new JScrollPane();
		tagListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tagManagmentPanel.add(tagListScrollPane, BorderLayout.CENTER);
		tagList = new JList<String>();
		tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tagList.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tagListModel = new DefaultListModel<String>();
		tagList.setModel(tagListModel);
		tagListScrollPane.setViewportView(tagList);
		
		tabCreationPanel = new JPanel();
		tagManagmentPanel.add(tabCreationPanel, BorderLayout.SOUTH);
		
		newTagButton = new JButton("New Tag");
		newTagButton.setForeground(new Color(51, 153, 0));
		newTagButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tabCreationPanel.add(newTagButton);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(30);
		tabCreationPanel.add(horizontalStrut_3);
		
		deleteTagButton = new JButton("Delete Tag");
		deleteTagButton.setForeground(new Color(153, 0, 0));
		deleteTagButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tabCreationPanel.add(deleteTagButton);
		
		saveGroupButton = new JButton("Save Groups");
		contentPane.add(saveGroupButton, BorderLayout.SOUTH);
		saveGroupButton.setForeground(new Color(255, 69, 0));
		saveGroupButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		tagInfoPanel = new JPanel();
		tabbedPane.addTab("Edit Tag", null, tagInfoPanel, null);
		tagInfoPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Tag Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tagInfoPanel.setLayout(new BorderLayout(0, 0));
		
		tagNamePanel = new JPanel();
		tagInfoPanel.add(tagNamePanel, BorderLayout.NORTH);
		
		JLabel tagNameLabel = new JLabel("Tag Name:   ");
		tagNameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		tagNamePanel.add(tagNameLabel);
		
		tagNameField = new JTextField();
		tagNameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tagNamePanel.add(tagNameField);
		tagNameField.setColumns(10);
		
		horizontalStrut_6 = Box.createHorizontalStrut(10);
		tagNamePanel.add(horizontalStrut_6);
		
		saveTagButton = new JButton("Save Tag");
		tagNamePanel.add(saveTagButton);
		saveTagButton.setForeground(new Color(0, 51, 204));
		saveTagButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		tagOptionsPanel = new JPanel();
		tagInfoPanel.add(tagOptionsPanel, BorderLayout.SOUTH);
		tagOptionsPanel.setLayout(new BorderLayout(0, 0));
		
		tagInfoMidPanel = new JPanel();
		tagInfoPanel.add(tagInfoMidPanel, BorderLayout.CENTER);
		tagInfoMidPanel.setLayout(new BorderLayout(0, 0));
		
		tagNameSeparator = new JSeparator();
		tagInfoMidPanel.add(tagNameSeparator, BorderLayout.NORTH);
		
		tagOptionsSeparator = new JSeparator();
		tagInfoMidPanel.add(tagOptionsSeparator, BorderLayout.SOUTH);
		
		tagDataPanel = new JPanel();
		tagInfoMidPanel.add(tagDataPanel, BorderLayout.CENTER);
		tagDataPanel.setLayout(new BorderLayout(0, 0));
		
		tagDataTypePanel = new JPanel();
		tagDataTypePanel.setBackground(new Color(255, 255, 153));
		tagDataPanel.add(tagDataTypePanel, BorderLayout.NORTH);
		
		JLabel tagDataTypeLabel = new JLabel("Data Type: ");
		tagDataTypeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tagDataTypePanel.add(tagDataTypeLabel);
		
		tagDataTypeSubTypeComboBox = new JComboBox();
		tagDataTypeSubTypeComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		tagTypeComboBox = new JComboBox<>();
		tagDataTypePanel.add(tagTypeComboBox);
		tagTypeComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tagTypeComboBox.setModel(new DefaultComboBoxModel(TagTypes.values()));
		tagDataTypePanel.add(tagDataTypeSubTypeComboBox);
		
		tagValuePanel = new JPanel();
		tagValuePanel.setBackground(new Color(255, 255, 153));
		tagDataPanel.add(tagValuePanel);
		tagValuePanel.setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		tagValuePanel.add(verticalStrut_1, BorderLayout.NORTH);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		tagValuePanel.add(horizontalStrut_2, BorderLayout.WEST);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		tagValuePanel.add(horizontalStrut_4, BorderLayout.EAST);
		
		tagDataScrollPane = new JScrollPane();
		tagDataScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		tagValuePanel.add(tagDataScrollPane, BorderLayout.CENTER);
		
		tagDataList = new JList();
		tagDataList.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tagDataListModel = new DefaultListModel<String>();
		tagDataList.setModel(tagDataListModel);
		tagDataScrollPane.setViewportView(tagDataList);
		
		valuePanel = new JPanel();
		valuePanel.setBackground(new Color(255, 255, 153));
		tagValuePanel.add(valuePanel, BorderLayout.SOUTH);
		
		newValueButton = new JButton("New Value");
		newValueButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		newValueButton.setForeground(new Color(0, 102, 0));
		valuePanel.add(newValueButton);
		
		JLabel valueLabel = new JLabel("Value: ");
		valueLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		valuePanel.add(valueLabel);
		
		valueField = new JTextField();
		valueField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		valuePanel.add(valueField);
		valueField.setColumns(10);
		
		deleteValueButton = new JButton("Delete Value");
		deleteValueButton.setForeground(new Color(153, 0, 0));
		deleteValueButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		valuePanel.add(deleteValueButton);

		
		tagTypeComboBox.addActionListener(this);
		saveTagButton.addActionListener(this);
		deleteGroupButton.addActionListener(this);
		loadGroupButton.addActionListener(this);
		
		newGroupButton.addActionListener(this);groupComboBox.addActionListener(this);groupNameField.addKeyListener(this);
		tagNameField.addActionListener(this);
		
		tagDataTypeSubTypeComboBox.addActionListener(this);
		tagList.addListSelectionListener(this);
		
		valueField.addKeyListener(this);
		valueField.addActionListener(this);
		tagDataList.addListSelectionListener(this);
		
		newValueButton.addActionListener(this);
		deleteValueButton.addActionListener(this);
		
		newTagButton.addActionListener(this);
		deleteTagButton.addActionListener(this);
		saveGroupButton.addActionListener(this);
		
		tagTypeComboBox.addActionListener(this);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setSubSelection(char type) {
		tagDataTypeSubTypeComboBox.setVisible(true);
		
		switch(type) {
			case 'A': tagDataTypeSubTypeComboBox.setModel(new DefaultComboBoxModel(ArrayTypes.values())); break;
			default:  tagDataTypeSubTypeComboBox.setVisible(false); break;
		}
	}

	public void addGroup(TagGroup group) {
		tagGroups.add(group);
		groupComboBoxModel.addElement((String) group.getIdTag().getInfo());
	}
	
	public void valueChanged(ListSelectionEvent event) {
		if(event.getSource() == tagList) {
			currentTagIndex = tagList.getSelectedIndex();
			if(currentTagIndex == -1) return;
			currentTag = tagGroups.get(currentGroupIndex).getTags()[currentTagIndex];
			
			tagNameField.setText(currentTag.getName());
			tagTypeComboBox.setSelectedIndex(tagTypeToIndex(currentTag.getType()));
			
			if(currentTag.getType() == TagTypes.Array) {
				tagDataTypeSubTypeComboBox.setSelectedIndex(arraySubToIndex(currentTag.getArrayType()));
			}
			
			tagDataListModel.clear();
			if(currentTag.getType() == TagTypes.Vector) {
				if(currentTag.getInfo() == null) return;
				
				tagDataListModel.addElement(" X: " + ((Vector3f) currentTag.getInfo()).getX());
				tagDataListModel.addElement(" Y: " + ((Vector3f) currentTag.getInfo()).getY());
				tagDataListModel.addElement(" Z: " + ((Vector3f) currentTag.getInfo()).getZ());
				
			} else if(currentTag.getType() == TagTypes.Array) {
				Object[] array = (Object[]) currentTag.getInfo();
				int nullCount = 0;
				
				for(int i = 0; i < array.length; i ++)	{
					if(array[i] == null) {
						nullCount ++;
						continue;
					}
					
					tagDataListModel.addElement(" " + (i + 1 - nullCount) + ": " + array[i].toString());
				}
				
			} else {
				if(currentTag.getInfo() == null) return;
				tagDataListModel.addElement(" Value: " + currentTag.getInfo().toString());
			}
			
		} else if(event.getSource() == tagDataList) {
			if(!tagDataListModel.isEmpty() && tagDataList.getSelectedIndex() != -1)
				valueField.setText(tagDataListModel.get(tagDataList.getSelectedIndex()).split(": ")[1]);
		}
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == tagTypeComboBox) {
			TagTypes type = TagTypes.values()[tagTypeComboBox.getSelectedIndex()];
			
			if(type == TagTypes.Array) {setSubSelection('A');}
			else if(type == TagTypes.Vector) {setSubSelection('V');}
			else {setSubSelection('N');}
			
			tagDataListModel.clear();
			
			switch((TagTypes) tagTypeComboBox.getSelectedItem()) {
				case Unknown: break;

				case Array: 
					tagDataListModel.addElement(" 1: 0");
				break;
				
				case Vector: 
					tagDataListModel.addElement(" X: 0"); 
					tagDataListModel.addElement(" Y: 0"); 
					tagDataListModel.addElement(" Z: 0"); 
				break;
				
				default: 
					tagDataListModel.addElement(" Value: 0");
				break;			
			}
			
		} else if(event.getSource() == groupComboBox) {
			currentGroupIndex = groupComboBox.getSelectedIndex();
			if(currentGroupIndex == -1) return;
			groupNameField.setText(groupComboBoxModel.getElementAt(currentGroupIndex));
			
			tagListModel.clear();
			if(tagGroups.get(currentGroupIndex) != null)
				for(Tag tag : tagGroups.get(currentGroupIndex).getTags()) {
					if(tag != null) {
						tagListModel.addElement(tag.getName());
					}
				}
			
		} else if(event.getSource() == saveTagButton) {
			tagListModel.set(currentTagIndex, tagNameField.getText());			
			Object info = null;
			if(tagTypeComboBox.getSelectedItem() == TagTypes.Array) {
				ArrayTypes type = (ArrayTypes) tagDataTypeSubTypeComboBox.getSelectedItem();
				String[] elements = new String[tagDataListModel.size()]; 
				for(int i = 0; i < elements.length; i ++) elements[i] = tagDataListModel.get(i);
				Object[] convertedData = convertData(elements, type);
				if(convertedData == null) return;
				
				switch(type) {
					case Byte: info = (Byte[]) convertedData; break;
					case Short: info = (Short[]) convertedData; break;
					case Integer: info = (Integer[]) convertedData; break;
					case Long: info = (Long[]) convertedData; break;
					case Float: info = (Float[]) convertedData; break;
					case Double: info = (Double[]) convertedData; break;
					case Boolean: info = (Boolean[]) convertedData; break;
					case Character: info = (Character[]) convertedData; break;
					case String: info = (String[]) convertedData; break;
				}
				
				tabbedPane.setSelectedIndex(0);
				
			} else if(tagTypeComboBox.getSelectedItem() == TagTypes.Vector) {
				info = new Vector3f(
						Float.parseFloat(tagDataListModel.get(0).split(": ")[1]), 
						Float.parseFloat(tagDataListModel.get(1).split(": ")[1]),
						Float.parseFloat(tagDataListModel.get(2).split(": ")[1])
					);
				
			} else {	
				String s = tagDataListModel.get(0).split(": ")[1];
				try {
					switch((TagTypes) tagTypeComboBox.getSelectedItem()) {
						case Byte: info = Byte.parseByte(s); break;         
						case Short: info = Short.parseShort(s); break;      
						case Integer: info = Integer.parseInt(s); break;    
						case Long: info = Long.parseLong(s); break;         
						case Float: info = Float.parseFloat(s); break;      
						case Double: info = Double.parseDouble(s); break;   
						case Boolean: info = Boolean.parseBoolean(s); break;
						case Character: info = s.charAt(0); break;          
						case String: info = s; break;
					
						default: break;                       
					}		
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			tagGroups.get(currentGroupIndex).setTag(currentTagIndex, new Tag(tagNameField.getText(), info));
			
			
		} else if(event.getSource() == newValueButton) {
			if((TagTypes) tagTypeComboBox.getSelectedItem() != TagTypes.Array) return;
			tagDataListModel.addElement(" " + (tagDataListModel.size() + 1) + ": 0");
			
		} else if(event.getSource() == deleteValueButton) {
			if((TagTypes) tagTypeComboBox.getSelectedItem() != TagTypes.Array || 
				tagDataList.getSelectedIndex() < 0 || tagDataListModel.size() < 1) return;
			tagDataListModel.remove(tagDataList.getSelectedIndex());
			
			for(int i = 0; i < tagDataListModel.size(); i ++) {
				tagDataListModel.set(i, " " + (i + 1) + ": " + tagDataListModel.get(i).split(": ")[1]);
			}
			
		} else if(event.getSource() == newGroupButton) {
			addGroup(new TagGroup(new Tag("GroupId", "NewTagGroup_" + groupComboBoxModel.getSize()), new Tag[0]));
			
		} else if(event.getSource() == newTagButton) {
			if(currentGroupIndex == -1) return;
			
			Tag newTag = new Tag("NewTag", null);
			tagGroups.get(currentGroupIndex).addTag(newTag);
			tagListModel.addElement(newTag.getName());
			
			tagList.setSelectedIndex(tagListModel.getSize() - 1);
			tabbedPane.setSelectedIndex(1);
			
		} else if(event.getSource() == deleteTagButton) {
			tagGroups.get(currentGroupIndex).removeTag(currentTagIndex);
			tagListModel.remove(currentTagIndex);

			tagDataListModel.clear();
			tagNameField.setText("");
			valueField.setText("");
			
			
		} else if(event.getSource() == deleteGroupButton) {
			if(currentGroupIndex == -1 || groupComboBoxModel.getSize() == 0) return;
			
			tagGroups.remove(currentGroupIndex);
			groupComboBoxModel.removeElementAt(currentGroupIndex);
			
			tagDataListModel.clear();
			tagListModel.clear();
			tagNameField.setText("");
			valueField.setText("");
			groupNameField.setText("");
			
			currentGroupIndex = -1;
			
		} else if(event.getSource() == saveGroupButton) {
//			if(currentFile == null) {
//				JOptionPane.showMessageDialog(null, "No File Loaded", "Error", JOptionPane.ERROR_MESSAGE);
				
				JFileChooser fileChoser = new JFileChooser();
				fileChoser.setSelectedFile(currentFile);
				int returnVal = fileChoser.showOpenDialog(this);			
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					currentFile = fileChoser.getSelectedFile();
//					System.out.println(currentFile);
				} else {
					return;
				}
//			}
			
			try {
				OutputStream write = DTGLoader.getOutputStream(currentFile);
				DTGLoader.writeAll(write, tagGroups);	
				write.close();
				
				JOptionPane.showMessageDialog(null, "Group Saved Completed", "Save Status", JOptionPane.INFORMATION_MESSAGE);
			} catch(IOException e) {
				JOptionPane.showMessageDialog(null, "Failed to Save Groups", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			
		} else if(event.getSource() == loadGroupButton) {
			JFileChooser fileChoser = new JFileChooser();
			int returnVal = fileChoser.showOpenDialog(this);			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				currentFile = fileChoser.getSelectedFile();
				System.out.println(currentFile);
			}
			
			try {
				InputStream read = DTGLoader.getInputStream(currentFile);
				
				for(TagGroup group : DTGLoader.readAll(read)) {
					addGroup(group);
					System.out.println("Group: " + group.getIdTag().getInfo());				
				}
				
				read.close();
			} catch(IOException e) {
				JOptionPane.showMessageDialog(null, "Failed to Load Groups", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}	

	public void keyPressed(KeyEvent event) {
		
	}

	public void keyReleased(KeyEvent event) { keyEvent(event); } 
	public void keyTyped(KeyEvent event) { keyEvent(event); }
	
	private void keyEvent(KeyEvent event) {
		if(event.getSource() == valueField) {
			if(currentTag == null || currentTagIndex == -1) return;
			tagDataListModel.set(tagDataList.getSelectedIndex(), 
					tagDataListModel.get(tagDataList.getSelectedIndex()).split(": ")[0] + ": " + valueField.getText());
			
		} else if(event.getSource() == groupNameField) {
			if(currentGroupIndex == -1) return;

			TagGroup group = tagGroups.get(currentGroupIndex);
			group.setIdTag(new Tag("GroupId", groupNameField.getText()));
			tagGroups.set(currentGroupIndex, group);
			groupComboBoxModel.removeAllElements();			
			
			for(TagGroup addGroup : tagGroups) {
				groupComboBoxModel.addElement((String) addGroup.getIdTag().getInfo());
			}
			
			groupComboBox.setSelectedItem((String) group.getIdTag().getInfo());
		}
	}
	
	private Object[] convertData(String[] data, ArrayTypes type) {
		Object[] dataOut;
		switch(type) {
			case Byte: dataOut = new Byte[data.length]; break;
			case Short: dataOut = new Short[data.length]; break;
			case Integer: dataOut = new Integer[data.length]; break;
			case Long: dataOut = new Long[data.length]; break;
			case Float: dataOut = new Float[data.length]; break;
			case Double: dataOut = new Double[data.length]; break;
			case Boolean: dataOut = new Boolean[data.length]; break;
			case Character: dataOut = new Character[data.length]; break;
			case String: dataOut = new String[data.length]; break;
			
			default: dataOut = new Object[data.length]; break;
		}
		
		for(int i = 0; i < data.length; i ++) {
			String s = data[i].split(": ")[1];
			try {
				switch(type) {
					case Byte: dataOut[i] = Byte.parseByte(s); break;
					case Short: dataOut[i] = Short.parseShort(s); break;
					case Integer: dataOut[i] = Integer.parseInt(s); break;
					case Long: dataOut[i] = Long.parseLong(s); break;
					case Float: dataOut[i] = Float.parseFloat(s); break;
					case Double: dataOut[i] = Double.parseDouble(s); break;
					case Boolean: dataOut[i] = Boolean.parseBoolean(s); break;
					case Character: dataOut[i] = s.charAt(0); break;
					case String: dataOut[i] = s; break;
				}
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		return dataOut;
	}
	
	private int tagTypeToIndex(TagTypes type) {
		for(int i = 0; i < TagTypes.values().length; i ++) {
			if(TagTypes.values()[i] == type) {
				return i;
			}
		}
		
		return -1;
	}
	
	private int arraySubToIndex(ArrayTypes type) {
		for(int i = 0; i < ArrayTypes.values().length; i ++) {
			if(ArrayTypes.values()[i] == type) {
				return i;
			}
		}
		
		return -1;
	}
}
