package com.GameName.Engine.Threads;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameThreadTracker extends JPanel {	
	private static final long serialVersionUID = -7026024720212863950L;
	
	public static final int CHART_OFFSET_X = 3;// 20;
	public static final int CHART_OFFSET_Y = 3;// 20;
	
	private static final int TICK_CHECK_TIME = 1;

	private int chartWidth, chartHeight, chartAmount;
	private int tickSize;
	
	private JTextField tpsLabel;	
	private GameThread thread;
	
	private int count;
	private int[] pastTPS;
	private int[] pastColors;
	private int avgTPS;
	
	public GameThreadTracker(GameThread thread) {
		this.thread = thread;
		
		pastTPS = new int[chartAmount];
		pastColors = new int[pastTPS.length];
		avgTPS = 0;
	}
	
	private void updateChart() {
		int preTotal = 0;
		
		for(int i = 1; i < pastTPS.length; i ++) {
			pastTPS[i - 1] = pastTPS[i];
			pastColors[i - 1] = pastColors[i];
			
			preTotal += pastTPS[i - 1];
		}
		
		if(tpsLabel != null && pastTPS.length != 0) { //TODO: Temporary?
			pastTPS[pastTPS.length - 1] = thread.isLive() ? thread.getTPS() : 0; 
			pastColors[pastColors.length - 1] = tpsLabel.getForeground().getRGB();//
			preTotal += pastTPS[pastTPS.length - 1]; avgTPS = preTotal / pastTPS.length;
		}
	}
	
	public void update() {
		float tpsDiv = (float) thread.getTPS() / (float) thread.getTickRate();
		
		if(tpsLabel != null) {
			tpsLabel.setForeground(thread.isPaused() ? Color.BLUE : 
				tpsDiv > 0.75 ? Color.GREEN : tpsDiv > 0.50 ? Color.YELLOW : Color.RED
			);
			
			if(thread.getTickRate() != ThreadGroup.UNCAPED_TICK_RATE) 
				tpsLabel.setText(thread.getTPS() + " / " + thread.getTickRate());
			else
				tpsLabel.setText(thread.getTPS() + "");
		}
		
		if(++ count > TICK_CHECK_TIME) {
			updateChart();
			count = 0;
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.fillRect(CHART_OFFSET_X - 3, CHART_OFFSET_Y - 3, chartWidth + 6, chartHeight + 6);

		g.setColor(new Color(150, 150, 150));
		g.fillRect(CHART_OFFSET_X, CHART_OFFSET_Y, chartWidth, chartHeight);
		
		float ratio = chartHeight / ((float) thread.getTickRate() + 3f);
		
		for(int i = 0; i < pastTPS.length; i ++) {
			g.setColor(new Color(pastColors[i]));
			g.fillRect(i * tickSize + CHART_OFFSET_X, CHART_OFFSET_Y + chartHeight, tickSize, (int) -(ratio * pastTPS[i]));
		}
		
//		g.setColor(Color.ORANGE);//(int) (Math.random() * 10 * 5 + 10)
//		g.drawLine(CHART_OFFSET, 90 - (int) (ratio * avgTPS), CHART_WIDTH + CHART_OFFSET, 90 - (int) (ratio * avgTPS));
		
		int alfa = 100;
		g.setColor(thread.isLive() ? thread.isPaused() ? new Color(0, 255, 255, alfa) : 
			new Color(255, 255, 255, 0) : new Color(255, 0, 0, alfa));
		g.fillRect(CHART_OFFSET_X, CHART_OFFSET_Y, chartWidth, chartHeight);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		update();
		repaint();
	}

	public int getAvgTPS() {
		return avgTPS;
	}

	public void pause() throws InterruptedException {
		thread.pause();
	}

	public void resume() {
		thread.resume();
	}

	public void restart() {
		thread.restart();
	}
	
	public boolean isPaused() {
		return thread.isPaused();
	}

	public String getName() {
		return thread.getName();
	}
	
	public void setTPSLabel(JTextField field) {
		tpsLabel = field;
	}

	public int getChartWidth() { return chartWidth; }
	public int getChartHeight() { return chartHeight; }
	public int getChartAmount() { return chartAmount; }

	public void setChartHeight(int chartHeight) { 
		this.chartHeight = chartHeight;
	}
	
	public void setChartWidth(int chartWidth) { 
		this.chartWidth = chartWidth;
		this.chartAmount = chartWidth;
		tickSize = Math.round((float) chartWidth / (float) chartAmount);
		
		if(chartWidth <= 0) return;
		pastTPS = new int[chartAmount];
		pastColors = new int[pastTPS.length];
	}
}
