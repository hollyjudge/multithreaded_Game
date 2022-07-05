import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Window;
import java.util.Scanner;
import java.util.concurrent.*;

public class WordApp {

	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;
	static String caughtWord = ""; 
	static WordDictionary dict = new WordDictionary(); 
	static WordRecord[] words;
	static volatile boolean done; 
	static 	Score score = new Score();
	static WordPanel w;
	public static JLabel scr;
	public static JLabel caught;
	public static JLabel missed;
	public static JFrame frame; 	
	
	/**
	* sets up GUI
	*/
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {

    	frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
    	JPanel g = new JPanel();
    	g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
    	g.setSize(frameX,frameY);
    	
    	w = new WordPanel(words,yLimit, totalWords);
	w.setSize(frameX,yLimit+100);
	 
	 g.add(w); 
	    
	 JPanel txt = new JPanel();
     	 txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
     	 caught =new JLabel("Caught: " + score.getCaught() + "    ");
     	 missed =new JLabel("Missed:" + score.getMissed()+ "    ");
     	 scr =new JLabel("Score:" + score.getScore()+ "    ");    
     	 txt.add(caught);
	 txt.add(missed);
	 txt.add(scr);
  
	 final JTextField textEntry = new JTextField("",20);
	 textEntry.addActionListener(new ActionListener()
	   {
	      public void actionPerformed(ActionEvent evt) {
	         String text = textEntry.getText();
	         caughtWord=text; 
	         textEntry.setText("");
	         textEntry.requestFocus();
	         }
	   });
	   
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	   JPanel b = new JPanel();
	   b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   JButton startB = new JButton("Start");	
		
		startB.addActionListener(new ActionListener()
		{
		   /*
		   * creates a new word panel object for each word i
		   * calls .start() which will create a thread
		   * of this in word panel
		   */
		   
		   public void actionPerformed(ActionEvent e)
		   {
		      for (int i=0; i<noWords; i++)
		      { 
		      	WordPanel wp= new WordPanel (words, words[i], yLimit, i, totalWords);
		      	wp.start(); 
		      }
		      
		      textEntry.requestFocus();  
		   }
		 
		});
		
		JButton endB = new JButton("End");	
				
		endB.addActionListener(new ActionListener()
		{
		   /**
		   * resets score
		   * intended to refresh the page so that you can play
		   * again but that doesnt work
		   */
		   	
		   public void actionPerformed(ActionEvent e)
		   {
		      score.resetScore(); 
		      frame.getContentPane().invalidate(); 
		      frame.getContentPane().validate(); 
		      frame.getContentPane().repaint();   
		      textEntry.requestFocus();
		   }
		   
		});
		
		JButton quitB= new JButton("Quit");
		
		quitB.addActionListener(new ActionListener()
		{
			/**
			* resets score and shutsdown program
			*/
			public void actionPerformed(ActionEvent e)
			{
			 score.resetScore(); 
			 System.exit(0);
			}
		});
		
		b.add(startB);
		b.add(endB);
		b.add(quitB);
		
		g.add(b);
    	
		frame.setLocationRelativeTo(null);
		frame.add(g); 
		frame.setContentPane(g);     
		frame.setVisible(true); 
	}

   public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
			}
			
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;
	}

	/**
	* reads in parameters and assigns them to variables
	* invokes invokeLater()
	* calls method to set up GUI
	* creates the WordRecord array of words
	*/
	
	public static void main(String[] args) {
    	
		totalWords=Integer.parseInt(args[0]);  
		noWords=Integer.parseInt(args[1]);
		assert(totalWords>=noWords); 
		String[] tmpDict=getDictFromFile(args[2]); 
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);
		
		WordRecord.dict=dict; 
		
		words = new WordRecord[noWords];  
		
		/**
		* enhances UI on ubuntu running OS
		*/
		System.setProperty("sun.java2d.opengl", "true"); 
	
                /**
                * to ensure thread saftey with swing and GUI
                */
                SwingUtilities.invokeLater(new Runnable()
                {
                	public void run()
                	{
                                setupGUI(frameX, frameY, yLimit);  
                	}
                });                 

		int x_inc=(int)frameX/noWords-10;
	  	
		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}
	}
}