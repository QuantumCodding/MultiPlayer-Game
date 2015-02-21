package com.GameName.Console.Base;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ConsoleWindow extends JFrame implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 8345401150570190067L;
	
	private JPanel contentPane;
	private JScrollPane currentTabPanel;
	
	private JButton killButton;
	private JButton helpButton;
	private JButton hideButton;
	
	private JList<String> tabList;
	private IconListRender<String> tabListRender;
	private DefaultListModel<String> tabListModel;
	protected ArrayList<ConsoleTab> tabListArray;
	
	private JLabel tabLabel;
	private JLabel tabIconLabel;	

	public static void start(final ConsoleWindow window) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
					window.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	public ConsoleWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(ClassNotFoundException | InstantiationException | 
				IllegalAccessException | UnsupportedLookAndFeelException e) {}
		
		setTitle("GameName Console");
		setSize(650, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel utilPanel = new JPanel();
		contentPane.add(utilPanel, BorderLayout.SOUTH);
		utilPanel.setLayout(new BorderLayout(0, 0));
		
		JSeparator separator = new JSeparator();
		utilPanel.add(separator, BorderLayout.NORTH);
		
		JPanel utilButtonPanel = new JPanel();
		utilPanel.add(utilButtonPanel, BorderLayout.CENTER);
		utilButtonPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel utilKillInstancePanel = new JPanel();
		utilButtonPanel.add(utilKillInstancePanel, BorderLayout.WEST);
		
		killButton = new JButton("Kill Instance");
		utilKillInstancePanel.add(killButton);
		
		JPanel utilHelpHidePanel = new JPanel();
		utilButtonPanel.add(utilHelpHidePanel, BorderLayout.EAST);
		
		hideButton = new JButton("Hide");
		utilHelpHidePanel.add(hideButton);
		
		helpButton = new JButton("Help");
		utilHelpHidePanel.add(helpButton);
		
		JPanel mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("13dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("13dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("13dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("13dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("17dlu"),
				ColumnSpec.decode("8dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("8dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("8dlu"),
				RowSpec.decode("5dlu"),}));

		tabListArray = new ArrayList<>();
		tabListModel = new DefaultListModel<>();
		tabListRender = new IconListRender<>(new HashMap<String, Icon>());
		tabList = new JList<>(tabListModel);
		tabList.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tabList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabList.setCellRenderer(tabListRender);
		tabList.setBorder(new LineBorder(new Color(0, 0, 0)));
		mainPanel.add(tabList, "2, 2, 9, 29, fill, fill");
		
		JPanel tabLabelPanel = new JPanel();
		mainPanel.add(tabLabelPanel, "13, 2, 40, 1, fill, fill");
		tabLabelPanel.setLayout(new BorderLayout(0, 0));
		
		tabLabel = new JLabel("Tab Label");
		tabLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		tabLabelPanel.add(tabLabel, BorderLayout.CENTER);
		
		tabIconLabel = new JLabel();
		tabLabelPanel.add(tabIconLabel, BorderLayout.EAST);
		
		JSeparator separator_1 = new JSeparator();
		mainPanel.add(separator_1, "13, 4, 40, 1");
		
		currentTabPanel = new JScrollPane();
		currentTabPanel.setBackground(new Color(225, 225, 225));
		currentTabPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		mainPanel.add(currentTabPanel, "13, 6, 40, 25, fill, fill");
//		currentTabPanel.setLayout(new BorderLayout(0, 0));
		
		tabList.addListSelectionListener(this);
		
		killButton.addActionListener(this);
		helpButton.addActionListener(this);
		hideButton.addActionListener(this);
	}
	
	public void addTab(ConsoleTab tab) {
		tabListArray.add(tab);
		tabListModel.addElement(tab.getName());
		tabListRender.addIcon(tab.getName(), tab.getIcon());
		
		if(tabListArray.size() == 1) {
			tabList.setSelectedIndex(0);
			changeSelectedIndex(0);
		}
	}
	
	private void changeSelectedIndex(int index) {
		ConsoleTab tab = tabListArray.get(index);
		tabLabel.setText(tab.getName());
		tabIconLabel.setIcon(tab.getIcon());
		
		tab.setSize(currentTabPanel.getSize());
		currentTabPanel.setViewportView(tab);
		tab.setPreferredSize(currentTabPanel.getSize());
	}
	
	protected void killInstance() { System.exit(0); }
	protected void help() { JOptionPane.showMessageDialog(null, "I'm sorry. Were all going to die. You've killed us all!", "You're screwed", JOptionPane.ERROR_MESSAGE); }

	public void valueChanged(ListSelectionEvent event) {
		if(event.getSource() == tabList) {
			changeSelectedIndex(tabList.getSelectedIndex());
		}
	}

	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == killButton) killInstance();
		else if(event.getSource() == helpButton) help();
		else if(event.getSource() == hideButton) setVisible(false);
	}
	
	public void setTab(ConsoleTab tab) {
		for(int i = 0; i < tabListArray.size(); i ++) {
			if(tabListArray.get(i) == tab) {
				tabList.setSelectedIndex(i);
				return;
			}
		}
	}
}
