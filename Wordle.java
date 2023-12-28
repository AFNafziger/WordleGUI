package edu.wm.cs.cs301.wordle;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import edu.wm.cs.cs301.wordle.model.WordleModel;
import edu.wm.cs.cs301.wordle.view.WordleFrame;

public class Wordle implements Runnable {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Wordle());
		
		//use cross-platform look and feel so button backgrounds work on Mac
		if (!System.getProperty("os.name").contains("Windows")) {
		try {
		    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		}
	}

	@Override
	public void run() {
		new WordleFrame(new WordleModel(6, 5));//had to adjust this to work with how I'm calling the frames for different difficulties
	}

}
