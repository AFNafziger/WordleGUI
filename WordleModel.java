package edu.wm.cs.cs301.wordle.model;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import edu.wm.cs.cs301.wordle.controller.ReadWordsRunnable;
import edu.wm.cs.cs301.wordle.view.WordleFrame;

public class WordleModel {
	
	private char[] currentWord, guess;

	private int currentColumn, currentRow;
	
	private List<String> wordList;
	
	private final Random random;
	
	private final Statistics statistics;
	
	private WordleResponse[][] wordleGrid;
	
    private int columnCount;
    private int maximumRows;
	
	public WordleModel(int row, int column) {
		this.currentColumn = -1;
		this.currentRow = 0;
		this.columnCount = column;
		this.maximumRows = row;
		this.random = new Random();
		
		createWordList();
		
		this.wordleGrid = initializeWordleGrid();
		this.guess = new char[columnCount];
		this.statistics = new Statistics();
	}
	
	private void createWordList() {
		ReadWordsRunnable runnable = new ReadWordsRunnable(this);
		Thread thread = new Thread(runnable);
	    thread.start();

	    try {
	        thread.join(); // Wait for the thread to finish. Was getting error before
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	
	public void setDifficulty(int rows, int columns) {
	    //System.out.println("setting difficulty to "+rows+" rows and "+columns+" columns");
	    this.maximumRows = rows;
	    this.columnCount = columns;
	    resetGame(rows, columns);
	}
	
	public void resetGame(int rows, int columns) {
		
		
		
		new WordleFrame(new WordleModel(rows, columns));
		this.maximumRows = rows;
	    this.columnCount = columns;
	}
	
	public void initialize() {
		this.wordleGrid = initializeWordleGrid();
		this.currentColumn = -1;
		this.currentRow = 0;
		generateCurrentWord();
		this.guess = new char[columnCount];
	}

	public void generateCurrentWord() {
		String word = getCurrentWord();
		this.currentWord = word.toUpperCase().toCharArray();
		
		//uncomment line below to show secret word on the console for debugging
		//System.out.println("Current word set to " + word);
	}

	private String getCurrentWord() {
		return wordList.get(getRandomIndex());
	}

	private int getRandomIndex() {
		int size = wordList.size();
		return random.nextInt(size);
	}
	
	private WordleResponse[][] initializeWordleGrid() {
	    WordleResponse[][] wordleGrid = new WordleResponse[maximumRows][columnCount];

	    for (int row = 0; row < wordleGrid.length; row++) {
	        for (int column = 0; column < wordleGrid[row].length; column++) {
	            wordleGrid[row][column] = null;
	        }
	    }
	    //System.out.println("wordleGrid dimensions: " + wordleGrid.length + "x" + wordleGrid[0].length);
	    return wordleGrid;
	}
	
	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}
	
	public void setCurrentWord() {
		int index = getRandomIndex();
		currentWord = wordList.get(index).toCharArray();
	}
	
	public void setCurrentColumn(char c) {
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		guess[currentColumn] = c;
		wordleGrid[currentRow][currentColumn] = new WordleResponse(c,
				Color.WHITE, Color.BLACK);
	}
	
	public void backspace() {
		if (this.currentColumn > -1) { //only backspace if there's room
			wordleGrid[currentRow][currentColumn] = null;
			guess[currentColumn] = ' ';
			this.currentColumn--;
			this.currentColumn = Math.max(currentColumn, -1);
		}
	}
	
	public WordleResponse[] getCurrentRow() {
		return wordleGrid[getCurrentRowNumber()];
	}
	
	public int getCurrentRowNumber() {
		return currentRow - 1;
	}
	
	public boolean setCurrentRow() {
		for (int column = 0; column < guess.length; column++) {
			Color backgroundColor = AppColors.GRAY;
			Color foregroundColor = Color.WHITE;
			if (guess[column] == currentWord[column]) {
				backgroundColor = AppColors.GREEN;
			} else if (contains(currentWord, guess, column)) {
				backgroundColor = AppColors.YELLOW;
			}
			
			wordleGrid[currentRow][column] = new WordleResponse(guess[column],
					backgroundColor, foregroundColor);
		}
		
		currentColumn = -1;
		currentRow++;
		guess = new char[columnCount];
		
		return currentRow < maximumRows;
	}
	
	private boolean contains(char[] currentWord, char[] guess, int column) {
		for (int index = 0; index < currentWord.length; index++) {
			if (index != column && guess[column] == currentWord[index]) {
				return true;
			}
		}
		
		return false;
	}
	public boolean validWordTester(String word) {
        return wordList.contains(word); // Check if the word is in the word list
	}
	
	
	public WordleResponse[][] getWordleGrid() {
		return wordleGrid;
	}
	
	public String getWord() {
	    if (currentWord != null) {
	        return new String(currentWord); // Converting the char array to a string
	    }
	    return null;
	}
	
	public interface WordListCallback {
	    void onWordListInitialized();
	}
	
	public String getGuess() {//needed for KeyboardButtonAction's checking if valid word
	    return new String(guess);
	    
	}
	
	public int getMaximumRows() {
		return maximumRows;
	}

	public int getColumnCount() {
		return columnCount;
	}
	
	public int getCurrentColumn() {
		return currentColumn;
	}
	
	public int getCurrentRowOn() {
		return currentRow;
	}
	
	public int getTotalWordCount() {
		return wordList.size();
	}

	public Statistics getStatistics() {
		return statistics;
	}

}
