package com.GameName.Main.Debugging;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.GameName.Command.Command;
import com.GameName.Main.GameName;
import com.GameName.Util.StringEffect;
import com.GameName.Util.StringEffectFormater;

public class DebugWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private int lastLog = 0;
	private String lastEntry = "";
	
	private JPanel contentPane;	
	private JSplitPane mainPane;	
	
		private JPanel monitorPanel;	
			private JScrollPane monitorListPanel;
				private ArrayList<DebugPanel> monitors;
			
				private DefaultListModel<DebugPanel> monitorListModel;
				private JList<DebugPanel> monitorList;
		
			private JPanel monitorPanelPanel;
			
			private JPanel findMonitorPanel;
				private JTextField findMonitorInput;
				private JButton findMonitorButton;
		
		private JPanel logPanel;
			private JScrollPane logScrollPanel;
				private JTextPane logTextArea;
			
			private JTextField logInput;
		
		
	public void start(final DebugWindow frame) {
		frame.setVisible(true);
	}
	
	/**
	 * Create the frame.
	 */
	public DebugWindow() {
		
		setTitle("Debug Window");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 910, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		mainPane = new JSplitPane();
		mainPane.setOneTouchExpandable(true);
		contentPane.add(mainPane);
		
		monitorPanel = new JPanel();
		monitorPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Monitors", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		monitorPanel.setPreferredSize(new Dimension(420, 0));
		mainPane.setLeftComponent(monitorPanel);
		monitorPanel.setLayout(new BorderLayout(0, 5));
		
		monitorListPanel = new JScrollPane();
		monitorPanel.add(monitorListPanel, BorderLayout.NORTH);
		
		monitors = new ArrayList<DebugPanel>();
		monitorListModel = new DefaultListModel<DebugPanel>();
		monitorList = new JList<DebugPanel>(monitorListModel);
		monitorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		monitorList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(monitorList.getSelectedValue() == null) return;
				
				monitorPanelPanel.removeAll();
				monitorPanelPanel.add(monitorList.getSelectedValue(), BorderLayout.CENTER);	

				monitorPanelPanel.revalidate();
				monitorPanelPanel.repaint();
			}
		});
		monitorListPanel.setViewportView(monitorList);
		
		monitorPanelPanel = new JPanel();
		monitorPanelPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Monitor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		monitorPanel.add(monitorPanelPanel, BorderLayout.CENTER);
		monitorPanelPanel.setLayout(new BorderLayout(0, 0));
				
		findMonitorPanel = new JPanel();
		findMonitorPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Find Monitor", TitledBorder.TRAILING, TitledBorder.TOP, null, null));
		monitorPanel.add(findMonitorPanel, BorderLayout.SOUTH);
		findMonitorPanel.setLayout(new BorderLayout(5, 0));
		
		findMonitorInput = new JTextField();
		findMonitorInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				findMonitors();
			}
		});
		findMonitorPanel.add(findMonitorInput, BorderLayout.CENTER);
		findMonitorInput.setColumns(10);
		
		findMonitorButton = new JButton("Find Monitor");
		findMonitorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findMonitors();
			}
		});
		findMonitorButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		findMonitorPanel.add(findMonitorButton, BorderLayout.EAST);
		
		logPanel = new JPanel();
		logPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "GameName Log", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mainPane.setRightComponent(logPanel);
		logPanel.setLayout(new BorderLayout(0, 5));
		
		logInput = new JTextField();
		logInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP) {
					logInput.setText(lastEntry);
				}
			}
		});
		logInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.print(logInput.getText()).setType("Input").end();
				lastEntry = logInput.getText();
				
				if(!logInput.getText().startsWith("/")) {
					if(debugCommand(logInput.getText())) {
						logInput.setText("");
						return;
					}
				}				
				
				if(!Command.runCommand(logInput.getText())) {
					Logger.print(" Invalis Command! For more help try /help").setType("Error").end();
				}
				
				logInput.setText("");
			}
		});
		logPanel.add(logInput, BorderLayout.SOUTH);
		logInput.setColumns(20);
		
		logScrollPanel = new JScrollPane();
		logPanel.add(logScrollPanel, BorderLayout.CENTER);
		
		logTextArea = new JTextPane();
		logTextArea.setContentType("text/html");
		logTextArea.setEditable(false);
		logScrollPanel.setViewportView(logTextArea);
	}
	
	public void addPanel(DebugPanel panel) {
		monitors.add(panel);
		monitorListModel.addElement(panel);
	}
	
	public void findMonitors() {
		String monitorName = findMonitorInput.getText();
		monitorListModel.clear();
		
		for(int i = 0; i < monitors.size(); i ++) {
			if(monitors.get(i).getName().contains(monitorName)) {
				monitorListModel.addElement(monitors.get(i));
			}
		}
	}
	
	public void update() {				
		if(Logger.getLog().size() > lastLog) {			
			try {
				StyledDocument doc = logTextArea.getStyledDocument();
				HashMap<String, StringEffect> map = StringEffectFormater.getPairs(Logger.getLog().get(lastLog));
				
				for(String str : map.keySet()) {				
				    doc.insertString(doc.getLength(), str + "\n",
				    	StringEffectFormater.asSimpleAttributeSet(map.get(str))
				    );
				    
				    JScrollBar scrollBar = logScrollPanel.getVerticalScrollBar();
		            BoundedRangeModel model = scrollBar.getModel();
		            
		            boolean scrollBarAtBottom = (model.getExtent() + model.getValue()) == model.getMaximum();
		            boolean scrollLock = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
		            
		            if (scrollBarAtBottom && !scrollLock) {	            	
		            	EventQueue.invokeLater(new Runnable() {
		                    public void run() {
		                        EventQueue.invokeLater(new Runnable() {
		                            public void run() {
		                            	Rectangle visibleRect = logTextArea.getVisibleRect();
		            	                visibleRect.y = logTextArea.getHeight() - visibleRect.height;
		            	                logTextArea.scrollRectToVisible(visibleRect);
		                            }
		                        });
		                    }
		                });	            	
		            }				    
				}
			} catch(Exception e) { e.printStackTrace(); }
			
			lastLog ++;
		}
	}

	
	
	private boolean debugCommand(String input) {
		String[] parm = input.split(" ");
		
		switch(parm[0].toLowerCase()) {
			case "clear": 		
				try {
					logTextArea.getStyledDocument().remove(0, logTextArea.getStyledDocument().getLength());
				} catch (BadLocationException e) {}			
			return true;
			
			case "toggletime": Logger.toggleTime(); return true;
			case "ping": Logger.println("PONG!"); return true;
			case "exit": case "quit": case "stop": GameName.isRunning = false; return true;
				
			default: return false;
		}
	}
}