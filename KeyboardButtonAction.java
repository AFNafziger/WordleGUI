package edu.wm.cs.cs301.wordle.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import edu.wm.cs.cs301.wordle.model.AppColors;
import edu.wm.cs.cs301.wordle.model.WordleModel;
import edu.wm.cs.cs301.wordle.model.WordleResponse;
import edu.wm.cs.cs301.wordle.view.StatisticsDialog;
import edu.wm.cs.cs301.wordle.view.WordleFrame;

public class KeyboardButtonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private final WordleFrame view;
	
	private final WordleModel model;

	public KeyboardButtonAction(WordleFrame view, WordleModel model) {
		this.view = view;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		String text = button.getActionCommand();
		switch (text) {
		case "Enter":
			
			String enteredWord = new String(model.getGuess()).trim().toLowerCase();
			
			//System.out.println("Entered word "+ enteredWord);
			if (!model.validWordTester(enteredWord)){//Checks if the word is valid according to database
				return;
			}
			
			if (model.getCurrentColumn() >= (model.getColumnCount() - 1)) {
				boolean moreRows = model.setCurrentRow();
				WordleResponse[] currentRow = model.getCurrentRow();
				int greenCount = 0;
				for (WordleResponse wordleResponse : currentRow) {
					view.setColor(Character.toString(wordleResponse.getChar()),
							wordleResponse.getBackgroundColor(), 
							wordleResponse.getForegroundColor());
					if (wordleResponse.getBackgroundColor().equals(AppColors.GREEN)) {
						greenCount++;
					} 
				}
				
				if (greenCount >= model.getColumnCount()) {
					view.repaintWordleGridPanel();
					model.getStatistics().incrementTotalGamesPlayed();
					int currentRowNumber = model.getCurrentRowNumber();
					model.getStatistics().addWordsGuessed(currentRowNumber);
					int currentStreak = model.getStatistics().getCurrentStreak();
					model.getStatistics().setCurrentStreak(++currentStreak);
					new StatisticsDialog(view, model);
				} else if (!moreRows) {
					view.repaintWordleGridPanel();
					model.getStatistics().incrementTotalGamesPlayed();
					model.getStatistics().setCurrentStreak(0);
					new StatisticsDialog(view, model);
				} else {
					view.repaintWordleGridPanel();
				}
			}
			break;
		case "Backspace":
			model.backspace();
			view.repaintWordleGridPanel();
			break;
		default:
			model.setCurrentColumn(text.charAt(0));
			view.repaintWordleGridPanel();
			break;
		}
		
	}

}
