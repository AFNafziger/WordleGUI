package edu.wm.cs.cs301.wordle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JPanel;

import edu.wm.cs.cs301.wordle.model.AppColors;
import edu.wm.cs.cs301.wordle.model.WordleModel;

public class DistributionPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private double[] percentages;
	
	private int[] counts;
	private int lastValue;
	
	private final WordleModel model;

	public DistributionPanel(WordleFrame view, WordleModel model) {
		this.model = model;
		calculatePercentages();
		this.setPreferredSize(new Dimension(500, 200));
	}
	
	private void calculatePercentages() {//altered to work with easy mode difficulties
		List<Integer> wordsGuessed = model.getStatistics().getWordsGuessed();

	    if (wordsGuessed == null || wordsGuessed.isEmpty()) {
	        //If empty or null data
	        counts = new int[model.getMaximumRows()];
	        percentages = new double[model.getMaximumRows()]; 
	        return;
	    }

	    counts = new int[model.getMaximumRows()];
	    for (int value : wordsGuessed) {
	        if (value >= 0 && value < model.getMaximumRows()) {
	            counts[value]++;
	            lastValue = value;
	        }
	    }

	    int maxCount = 0;
	    for (int count : counts) {
	        maxCount = Math.max(maxCount, count);
	    }

	    percentages = new double[model.getMaximumRows()];
	    for (int i = 0; i < model.getMaximumRows(); i++) {
	        percentages[i] = (double) counts[i] / maxCount;
	    }
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
		Font textFont = AppFonts.getTextFont();
		g2d.setFont(textFont);
		FontMetrics metrics = g2d.getFontMetrics(textFont);
		
		int margin = metrics.getHeight() / 3;
		int x = 20;
		int x1 = x + 20;
		int y = 20;
		int y1 = getWidth() - 30;
		int y2 = 20;
		int difference = y1 - y - y2;

		for (int index = 0; index < model.getMaximumRows(); index++) {
			String text = Integer.toString(index + 1);
			g2d.setColor(Color.BLACK);
			g2d.drawString(text, x, y + 2);

			if (index == lastValue
					&& model.getStatistics().getCurrentStreak() > 0) {
				g2d.setColor(AppColors.GREEN);
			} else {
				g2d.setColor(AppColors.GRAY);
			}
			
			int pixelWidth = (int) (Math.round(percentages[index] * difference)
					+ y2);
			g2d.fillRect(x1, y - metrics.getHeight() + margin, pixelWidth,
					metrics.getHeight());
			g2d.setColor(Color.WHITE);
			text = String.format("%,d", counts[index]);
			int textWidth = metrics.stringWidth(text);
			g2d.drawString(Integer.toString(counts[index]),
					x1 + pixelWidth - textWidth - 6, y + 2);
			
			y += metrics.getHeight() + margin;
		}
		
	}

}
