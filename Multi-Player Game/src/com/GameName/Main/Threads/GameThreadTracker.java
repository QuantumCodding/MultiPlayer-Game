package com.GameName.Main.Threads;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.GameName.Main.Debugging.DebugPanel;

public class GameThreadTracker extends DebugPanel {	
	private static final long serialVersionUID = 1L;

	private static final int CHART_OFFSET = 20;
	private static final int CHART_WIDTH = 360;
	private static final int CHART_AMOUNT = 360;
	private static final int TICK_SIZE = Math.round((float) CHART_WIDTH / (float) CHART_AMOUNT);
	private static final int TICK_CHECK_TIME = 1;
	
	private JLabel nameLabel;
	private JTextField tpsLabel;
	private JToggleButton pauseButton;
	private JButton restartButton;
	
	private GameThread gameThread;
	
	private int count;
	private int[] pastTPS;
	private int[] pastColors;
	private int avgTPS;
	
	public GameThreadTracker(final GameThread gameThread) {
		super();
		
		this.gameThread = gameThread;
		Font font = new Font("", 0, 15);
		Font font2 = new Font("", 0, 20);
		
		pastTPS = new int[CHART_AMOUNT];
		pastColors = new int[pastTPS.length];
		avgTPS = 0;
		
		nameLabel = new JLabel(gameThread.getName());	nameLabel.setFont(font2);	nameLabel.setAlignmentX(Box.LEFT_ALIGNMENT);
		tpsLabel = new JTextField();					tpsLabel.setFont(font2);	tpsLabel.setAlignmentX(Box.LEFT_ALIGNMENT);
		pauseButton = new JToggleButton("Pause");		pauseButton.setFont(font);	pauseButton.setAlignmentX(Box.RIGHT_ALIGNMENT);
		restartButton = new JButton("Restart");			restartButton.setFont(font);restartButton.setAlignmentX(Box.LEFT_ALIGNMENT);
		
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pauseButton.isSelected()) {
					try {
						gameThread.pause();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} else {
					gameThread.resume();
				}
			}
		});
		
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gameThread.restart();
			}
		});
		
		tpsLabel.setEditable(false);
				
		add(nameLabel); add(tpsLabel); add(pauseButton); add(restartButton);		
		update();
	}
	
	public void update() {
		float tpsDiv = (float) gameThread.getTPS() / (float) gameThread.getTickRate();
		
		tpsLabel.setForeground(tpsDiv > 0.5 ? Color.GREEN : tpsDiv > 0.25 ? Color.YELLOW : Color.RED);
		
		if(gameThread.getTickRate() != ThreadManager.UNCAPED_TICK_RATE) 
			tpsLabel.setText(gameThread.getTPS() + " / " + gameThread.getTickRate());
		else
			tpsLabel.setText(gameThread.getTPS() + "");
		
		if(count ++ > TICK_CHECK_TIME) {
			updateChart();
			count = 0;
		}
	}
	
	private void updateChart() {
		int preTotal = 0;
		
		for(int i = 1; i < pastTPS.length; i ++) {
			pastTPS[i - 1] = pastTPS[i];
			pastColors[i - 1] = pastColors[i];
			
			preTotal += pastTPS[i - 1];
		}
		
		pastTPS[pastTPS.length - 1] = gameThread.isLive() ? gameThread.getTPS() : 0; 
		pastColors[pastColors.length - 1] = tpsLabel.getForeground().getRGB();//
		
		preTotal += pastTPS[pastTPS.length - 1]; avgTPS = preTotal / pastTPS.length;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(gameThread.isLive() ? gameThread.isPaused() ? Color.BLUE : Color.DARK_GRAY : Color.RED);
		g.fillRect(CHART_OFFSET - 3, 50 - 3, CHART_WIDTH + 6, 40 + 6);

		g.setColor(new Color(150, 150, 150));
		g.fillRect(CHART_OFFSET, 50, CHART_WIDTH, 40);
		
		float ratio = 40 / ((float) gameThread.getTickRate() + 5f);
		
		for(int i = 0; i < pastTPS.length; i ++) {
			g.setColor(new Color(pastColors[i]));
			g.fillRect(i * TICK_SIZE + CHART_OFFSET, 90, TICK_SIZE, (int) -(ratio * pastTPS[i]));
		}
		
		g.setColor(Color.ORANGE);//(int) (Math.random() * 10 * 5 + 10)
		g.drawLine(CHART_OFFSET, 90 - (int) (ratio * avgTPS), CHART_WIDTH + CHART_OFFSET, 90 - (int) (ratio * avgTPS));
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		repaint();
	}
}
