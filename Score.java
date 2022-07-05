/**
* used to keep track of score, missed words and caught words
*/

public class Score {
	private int missedWords;
	private int caughtWords;
	private int gameScore;
	
	Score() {
		missedWords=0;
		caughtWords=0;
		gameScore=0;
	}
		
	/**
	* returns missed words
	*/
	public synchronized int getMissed() {
		return missedWords;
	}

	/**
	* returns caught words
	*/
	public synchronized int getCaught() {
		return caughtWords;
	}
	
	/**
	* returns total number of words dropped
	*/
	public synchronized int getTotal() {
		return (missedWords+caughtWords);
	}

	public synchronized int getScore() {
		return gameScore;
	}
	/**
	* incs missedWords
	*/
	public synchronized void missedWord() {
		missedWords++;
	}

	/**
	* incs caughtWords and updates gameScore by length
	*/
	public synchronized void caughtWord(int length) {
		caughtWords++;
		gameScore+=length;
	}
	/**
	* resets all variables to 0
	*/
	public synchronized void resetScore() {
		caughtWords=0;
		missedWords=0;
		gameScore=0;
	}
}
