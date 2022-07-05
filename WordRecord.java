/**
* class to handle the words and properties of
* words that drop
* unchanged from skeleton
*/

public class WordRecord {
	private String text;
	private  int x;
	private int y;
	private int maxY;
	private boolean dropped;
	
	private int fallingSpeed;
	private static int maxWait=1500;
	private static int minWait=100;

	public static WordDictionary dict;
	

	/**
	* set up constructor initialising values
	*/
	WordRecord() {
		text="";
		x=0;
		y=0;	
		maxY=300;
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	
	/**
	* set text 
	*/
	WordRecord(String text) {
		this();
		this.text=text;
	}
	
	/**
	*set up constructor 
	* and assign constructor parameters to variables
	*/
	WordRecord(String text,int x, int maxY) {
		this(text);
		this.x=x;
		this.maxY=maxY;
	}
	

	/**
	* set Y values
	* if its > than max Y then its dropped so mark drop as true
	*/
	public synchronized  void setY(int y) {
		if (y>maxY) {
			y=maxY;
			dropped=true;
		}
		this.y=y;
	}
	
	/**
	* set x value
	*/
	public synchronized  void setX(int x) {
		this.x=x;
	}
	
	/**
	* set word
	*/
	public synchronized  void setWord(String text) {
		this.text=text;
	}

	/**
	* get word
	*/
	public synchronized  String getWord() {
		return text;
	}
	
	/**
	* get x value
	*/
	public synchronized  int getX() {
		return x;
	}	
	/**
	* return y
	*/
	public synchronized  int getY() {
		return y;
	}
	/**
	* return speed
	*/
	public synchronized  int getSpeed() {
		return fallingSpeed;
	}

	/*
	* set the position of x and y
	*/
	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	/**
	* reset position to y=0
	*/
	public synchronized void resetPos() {
		setY(0);
	}

	/**
	* reset word to new word in dictionary
	*/
	public synchronized void resetWord() {
		resetPos();
		text=dict.getNewWord();
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 

	}
	
	/** check to see if parameter word matches this.text
	* if it does reset word and return true
	*/
	public synchronized boolean matchWord(String typedText){ 
		if (typedText.equals(this.text)) {
			resetWord();
			return true;
		}
		else
			return false;
	}
	

	/**
	* inc y value by parameter int
	*/
	public synchronized  void drop(int inc) {
		setY(y+inc);
	}
	
	public synchronized  boolean dropped() {
		return dropped;
	}

}
