package Game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


/* General plan
 * - The engine loads in the GameBoard and displays everythin
 * - Also monitors user input from the mouse
 * 
 * - Most of the code reused from MySolitaire/Solitaire.java (the provided base code) will be found here.
 * 	 - Some will be moved to the GameBoard, like Timer and Score related stuff.
 */

public class SolitaireEngine 
{
	protected static GameBoard mainGameBoard;
	
	// CONSTANTS
	public static int TABLE_HEIGHT = Card.CARD_HEIGHT * 4;
	public static int TABLE_WIDTH = (Card.CARD_WIDTH * 7) + 100;
	
	// GUI COMPONENTS (top level)
	private static final JFrame frame = new JFrame("Klondike Solitaire");
	protected static final JPanel table = new JPanel();
	// other components
	private static JEditorPane gameTitle = new JEditorPane("text/html", "");
	private static JButton showRulesButton = new JButton("Show Rules");
	private static JButton newGameButton = new JButton("New Game");
	private static JButton toggleTimerButton = new JButton("Pause Timer");
	private static JTextField scoreBox = new JTextField();// displays the score
	private static JTextField timeBox = new JTextField();// displays the time
	private static JTextField statusBox = new JTextField();// status messages
	
	
	// TIMER UTILITIES
	private static Timer timer = new Timer();
	private static ScoreClock scoreClock = new ScoreClock();

	// MISC TRACKING VARIABLES
	private static boolean timeRunning = false;// timer running?
	private static int score = 0;// keep track of the score
	private static int time = 0;// keep track of seconds elapsed
	
	// Store last XML File loaded.
	public static String XMLFile;
	
	// moves a card to abs location within a component
	protected static Card moveCard(Card c, int x, int y)
	{
		c.setBounds(new Rectangle(new Point(x, y), new Dimension(Card.CARD_WIDTH + 10, Card.CARD_HEIGHT + 10)));
		c.setXY(new Point(x, y));
		return c;
	}
	
	protected static void setScore(int deltaScore)
	{
		score += deltaScore;
		String newScore = "Score: " + score;
		scoreBox.setText(newScore);
		scoreBox.repaint();
	}
	
	private static class ScoreClock extends TimerTask
	{
		@Override
		public void run()
		{
			updateTimer();
		}
	}
	private static class NewGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			//TODO: Create window to select XML file.
			playNewGame(XMLFile);
		}

	}

	// GAME TIMER UTILITIES
	protected static void updateTimer()
	{
		time += 1;
		// every 10 seconds elapsed we take away 2 points
		if (time % 10 == 0)
		{
			setScore(-2);
		}
		String stringtime = "Seconds: " + time;
		timeBox.setText(stringtime);
		timeBox.repaint();
	}

	protected static void startTimer()
	{
		scoreClock = new ScoreClock();
		// set the timer to update every second
		timer.scheduleAtFixedRate(scoreClock, 1000, 1000);
		timeRunning = true;
	}

	// the pause timer button uses this
	protected static void toggleTimer()
	{		
		if (timeRunning && scoreClock != null)
		{
			scoreClock.cancel();
			timeRunning = false;
		} else
		{
			startTimer();
		}
	}
	
	private static class ToggleTimerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			toggleTimer();
			if (!timeRunning)
			{
				toggleTimerButton.setText("Start Timer");
			} else
			{
				toggleTimerButton.setText("Pause Timer");
			}
		}

	}

	// Most of the code from MySolitaire is used here, instead pulling the rules from the GameBoard
	public static final class ShowRulesListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JDialog ruleFrame = new JDialog(frame, true);
			ruleFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			ruleFrame.setSize(TABLE_HEIGHT, TABLE_WIDTH);
			JScrollPane scroll;
			JEditorPane rulesTextPane = new JEditorPane("text/html", "");
			rulesTextPane.setEditable(false);
			String rulesText = mainGameBoard.rulesText;
			rulesTextPane.setText(rulesText);
			ruleFrame.add(scroll = new JScrollPane(rulesTextPane));

			ruleFrame.setVisible(true);
		}
	}
	
	/*
	 * This class handles all of the logic of moving the Card components
	 * All this class needs to do is identify what card has been selected, and 
	 * which stack the player tries to drop it on.
	 * 
	 * All the rules for validating a move, moving the cards, and handling group moves are
	 * done in the GameBoard object
	 */
	public static final class CardMovementManager extends MouseAdapter
	{
		// Some of these are culture specific to MySolitaire

		private boolean checkForWin = false;// should we check if game is over?
		private boolean gameOver = false;// easier to negate this than affirm it
		private Point start = null;// where mouse was clicked
		private Point stop = null;// where mouse was released
		private Card card = null; // card to be moved
		
		private CardStack source = null;
		private CardStack dest = null;



		@Override
		public void mousePressed(MouseEvent e)
		{
			start = e.getPoint();
			boolean stopSearch = false;
			statusBox.setText("");
			System.out.println("Grabbing mouse");
			
			for (int x = 0; x < mainGameBoard.Stacks.size() && !stopSearch ; x++)
			{
				
				source = mainGameBoard.Stacks.get(x);
				// pinpointing exact card pressed
				
				if (source.contains(start) ) 
				{
					System.out.println("In stack: " + source.toString());
					
					for (Component ca : source.getComponents())
					{
						Card c = (Card) ca;
	
						if (c.contains(start)  && c.faceUp)
						{
							card = c;
							stopSearch = true;
							System.out.println("Grabbed card: " + card.toString());
							break;
						}
					}
				}

			}
			

		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			stop = e.getPoint();
			// used for status bar updates
			boolean validMoveMade = false;

			System.out.println("Releasing mouse");
			// PLAY STACK OPERATIONS
			if (card != null && source != null)
			{ // Moving from PLAY TO PLAY
				for (int x = 0; x < mainGameBoard.Stacks.size(); x++)
				{
					dest = mainGameBoard.Stacks.get(x);
					

					// MOVING TO POPULATED STACK
					if (card.faceUp == true && dest.contains(stop) && source != dest )
					{
						
						validMoveMade = mainGameBoard.DragDropMove(card, dest);
						
						dest.repaint();
						table.repaint();
						
						//TODO: Implement scoring in GameBoard?
						break;
					} 
					
				}
				
			}// end cycle through play decks

			// SHOWING STATUS MESSAGE IF MOVE INVALID
			if (!validMoveMade && dest != null && card != null)
			{
				statusBox.setText("That Is Not A Valid Move");
			}

			// CHECKING FOR WIN				
			if (mainGameBoard.checkVictory())
			{
				gameOver = true;
			}


			if (gameOver)
			{
				JOptionPane.showMessageDialog(table, "Congratulations! You've Won!");
				statusBox.setText("Game Over!");
			}
			// RESET VARIABLES FOR NEXT EVENT
			start = null;
			stop = null;
			source = null;
			dest = null;
			card = null;
			toggleTimer();
			//sourceIsFinalDeck = false;
			checkForWin = false;
			gameOver = false;
	
		}// end mousePressed()
	}

	public static void SaveGame(String filename)
	{
		try { 
			  
            // Saving of object in a file 
            FileOutputStream file = new FileOutputStream(filename); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
  
            // Method for serialization of object 
            out.writeObject(mainGameBoard); 
  
            out.close(); 
            file.close(); 
  
            System.out.println("Object has been serialized\n"
                              + "Data before Deserialization."); 


        } 
  
        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
        } 
		
	}
	public static void LoadGame(String filename)
	{
		try { 
			  
            // Reading the object from a file 
            FileInputStream file = new FileInputStream(filename); 
            ObjectInputStream in = new ObjectInputStream(file); 
  
            // Method for deserialization of object 
            mainGameBoard = (GameBoard)in.readObject(); 
  
            in.close(); 
            file.close(); 
            
            
        } 
  
        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
        } 
  
        catch (ClassNotFoundException ex) { 
            System.out.println("ClassNotFoundException" + 
                                " is caught"); 
        } 
		
	}
	
	
	//TODO: Separate code for creating a new game from adding everything in the gameboard to the JFrame
	//		- Will be nice to have separate when we implement Save/Load features.
	private static void playNewGame(String sXMLFile)
	{
		// reset stacks if user starts a new game in the middle of one
		table.removeAll();
		


		// Load the gameboard using XML_Loader
		mainGameBoard = XML_Loader.LoadXML(sXMLFile);
		for (int x = 0; x < mainGameBoard.Stacks.size(); x++)
		{
			CardStack stack = mainGameBoard.Stacks.get(x);
			stack.setXY(stack.xPos, stack.yPos);
			table.add(stack);
		}
		

		time = 0;
		timer = new Timer();
		newGameButton.setBounds(0, TABLE_HEIGHT - 70, 120, 30);
		showRulesButton.setBounds(120, TABLE_HEIGHT - 70, 120, 30);

		
		// Just a basic text box from the origial code.  
		// Preserved in comments for reference should we want to add our own text.
//		gameTitle.setText("<b>Shamari's Solitaire</b> <br> COP3252 <br> Spring 2012");
//		gameTitle.setEditable(false);
//		gameTitle.setOpaque(false);
//		gameTitle.setBounds(245, 20, 100, 100);

		scoreBox.setBounds(240, TABLE_HEIGHT - 70, 120, 30);
		scoreBox.setText("Score: 0");
		scoreBox.setEditable(false);
		scoreBox.setOpaque(false);

		timeBox.setBounds(360, TABLE_HEIGHT - 70, 120, 30);
		timeBox.setText("Seconds: 0");
		timeBox.setEditable(false);
		timeBox.setOpaque(false);

		startTimer();

		toggleTimerButton.setBounds(480, TABLE_HEIGHT - 70, 125, 30);
		
		statusBox.setBounds(605, TABLE_HEIGHT - 70, 180, 30);
		statusBox.setEditable(false);
		statusBox.setOpaque(false);

		setFrameSize(); 
		
		table.add(statusBox);
		table.add(toggleTimerButton);
		table.add(gameTitle);
		table.add(timeBox);
		table.add(newGameButton);
		table.add(showRulesButton);
		table.add(scoreBox);
		table.repaint();
		
		System.out.println("Done setting up");
	}
	
	
	static public void setFrameSize() 
	{
		// get max x value of all stacks, set WIDTH to MaxX + CARDWIDTH
		// repeat for y using.
		
		int frameW = mainGameBoard.Stacks.stream().mapToInt(v -> v.xPos).max().orElse(0) + Card.CARD_WIDTH + Card.CORNER_ANGLE;
		int frameH = mainGameBoard.Stacks.stream().mapToInt(v -> v.yPos).max().orElse(0) + (3 * Card.CARD_HEIGHT + 40);

		if (frameH < TABLE_HEIGHT)
			frameH = TABLE_HEIGHT;
		
		if (frameW < TABLE_WIDTH)
			frameW = TABLE_WIDTH;
		
		
		frame.setSize(frameW, frameH);
		newGameButton.setBounds(0, frameH - 70, 120, 30);
		showRulesButton.setBounds(120, frameH - 70, 120, 30);
		scoreBox.setBounds(240, frameH - 70, 120, 30);
		timeBox.setBounds(360, frameH - 70, 120, 30);
		toggleTimerButton.setBounds(480, frameH - 70, 125, 30);

		statusBox.setBounds(605, frameH - 70, frameW - 605, 30);
		
		
	}
	
	public static void main(String[] args)
	{

		Container contentPane;

		frame.setSize(TABLE_WIDTH, TABLE_HEIGHT);

		table.setLayout(null);
		table.setBackground(new Color(0, 180, 0));

		contentPane = frame.getContentPane();
		contentPane.add(table);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		XMLFile = ".\\Game\\Tests\\BonanzaCreek.xml";
		
		toggleTimerButton.addActionListener(new ToggleTimerListener());
		newGameButton.addActionListener(new NewGameListener());
		showRulesButton.addActionListener(new ShowRulesListener());
		
		playNewGame(XMLFile);

		table.addMouseListener(new CardMovementManager());
		table.addMouseMotionListener(new CardMovementManager());

		frame.setVisible(true);

	}
	
}
