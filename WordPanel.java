import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.concurrent.locks.ReentrantLock;

/**
* Class to change the GUI once game is running
* first called to set up the initial page
* called noWords amount of times when start is clicked
* to make on thread for each word that is running on screen
* constrcutor gets passed an additional individual word and a counter value
* that can be used safely to iteratre through WordRecord[]
* updates positions of words on screen
* updates missed, score and caught
*/


public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private final int noWords;
		private final int maxY;
		private Thread t;
		private int counter;
		private static WordRecord wordRecord= new WordRecord();
		private WordRecord word; 
                
                private JLabel labelCaught;
                private JLabel labelScore;
                private JLabel labelMissed; 
                
                private final int totalWords;
                private String caughtWord=""; 
                private static volatile WordApp wa= new WordApp();
		private static volatile Score score= new Score(); 		
		
		private final static AtomicInteger wordCount= new AtomicInteger(); 
		private final static AtomicInteger missed= new AtomicInteger(); 
		
		
		/** used to paint and repaint the Jframe
		* paints the updated positions of the words on pane
		* paint the score, missed and caught as updated
		*if a word is missed it resets it
		* if the total number of words has fallen it resets all the words
		*/ 
		synchronized protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
 		    repaint();
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height); 
		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 20)); 
		    for (int i=0;i<noWords;i++){	    		
		    	if (words[i].getY()<maxY)
		    	{
		    	    g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
		    	}     	
		    	else
		    	{
		    	   incrementCounter();
		    	   if ((getCounter())<=totalWords-noWords)
		    	   {
		    	  		 incrementMissed(); 
		    	  		 labelMissed=wa.missed;
		    	  		 labelMissed.setText("Missed:" + getMissed());
		     	  
		    	       words[i].resetWord(); 
		    	   }
		    	   else
		    	   {
		    	   	/**
		    	   	* ending game, reset screen
		    	   	*/
		    	   	for (int k=0; k<noWords; k++)
		    	   	{
		    	   		done=true; 
		    	   		words[k].resetPos(); ;  
				}
		 
			   }
		    	       	   
		    	}
		    }
		   
		  }
	   
		
		/**
		* safley incremenets counter used to count
		* number of words that have fallen
		*/      
		public void incrementCounter()
		{
			wordCount.incrementAndGet(); 
		}
		
		/**
		* used to safley get counter value
		*/
		public int  getCounter()
		{
			return wordCount.get();
		}
		
		/** 
		* safely incremenents number of missed words
		*/
		public void incrementMissed()
                {
                        missed.incrementAndGet(); 
                }

                /**
                * safley gets number of missed words
                */
                
                public int  getMissed()
                {
                        return missed.get();
                }

		
		/** 
		* constructs a WordPanel object
		* this was the constructor given in the skeleton
		*/
		
		WordPanel(WordRecord[] words, int maxY, int totalWords) {
			this.words=words;
			noWords=words.length;
			done=false;
			this.maxY=maxY;	
			this.totalWords=totalWords;	
		}
		
		/**
		* constructs a WordPanel object
		* in addition to above object has WordRecord and i
		* as parameters, i is used as a counter 
		*/
		WordPanel(WordRecord[] words, WordRecord word, int maxY, int i, int totalWords)
		{
			this.totalWords=totalWords;
			this.maxY=maxY; 
			counter=i;
			noWords=words.length; 
			this.words=words; 
			this.word=word; 
		}
	
                /**
                * creates a new thread every time its called
                */
                public void start()
               	{
               		if (t==null)
               		{
               			if (word.getY()<maxY)
               			{
               				t= new Thread(this);
               				t.start();
				}
			}
               	} 
               	
                /**
                * checks if a word has been dropped
                * if it hasnt it check if it matches the word that
                * the user has entered
                * if it matches the score is updated (including text fields)
                * and the word removed
                * else word is dropped and sleeps for its given speed of time
                */
                public void run() 
                {
                        while ((!done))
                        { 
                                if (!word.dropped())
                                {
                                try
                                {                 
                                 	synchronized (this)
                                 	{
                                 		this.caughtWord=wa.caughtWord;
                                 		wordRecord.setWord(word.getWord());                                 
                                                if (word.matchWord(caughtWord))
                                                { 
                                                	this.caughtWord="";
                                                	wordRecord.drop(maxY); 
                                                	incrementCounter(); 
                                                	score.caughtWord(wordRecord.getWord().length());
                                                	labelScore=wa.scr;  
                                                	labelScore.setText("Score:" + score.getScore());
                                                	labelCaught=wa.caught;
							labelCaught.setText("Caught:" + score.getCaught());
						}
						else
						{
                                                	word.drop(50);
                                                	words[counter]=word;
                                                	Thread.sleep(word.getSpeed());
                                                }
					}
                                                                          
                                }
                                catch (InterruptedException e)
                                {
                                        System.out.println("thread interupted");
                                }
                                
                               }         
                        }                        
                }
	}


