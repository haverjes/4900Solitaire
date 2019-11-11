package xmlGameEngine;

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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import gameInterface.*;
import gamePlatform.main.Launcher;
import gamePlatform.menus.MenuManager;


/* General plan
 * - The engine loads in the GameBoard and displays everythin
 * - Also monitors user input from the mouse
 * 
 * - Most of the code reused from MySolitaire/Solitaire.java (the provided base code) will be found here.
 * 	 - Some will be moved to the GameBoard, like Timer and Score related stuff.
 */

public class XMLSolitaireEngine
{
	protected static GameBoard mainGameBoard;
	protected static CardMovementManager mouseManager;
	// CONSTANTS
	public static int TABLE_HEIGHT = Card.CARD_HEIGHT * 4;
	public static int TABLE_WIDTH = (Card.CARD_WIDTH * 7) + 100;
	
	//Games that can be played.  Hardcoded in for now, but later should be set to what the current user has enabled
	public static String[] XML_Options = {"Binary Star", "Bonanza Creek"};
	
	// MISC TRACKING VARIABLES
	private static boolean timeRunning = false;// timer running?	private static int score = 0;// keep track of the score
	private static int time = 0;// keep track of seconds elapsed
	
	// GUI COMPONENTS (top level)
	protected static JFrame mainFrame = new JFrame();
	protected static final JPanel table = new JPanel();
	// other components
	private static JEditorPane gameTitle = new JEditorPane("text/html", "");
	private static JButton showRulesButton = new JButton("Show Rules");
	private static JButton newGameButton = new JButton("New Game");
	
	private static JButton toggleTimerButton = new JButton("Pause Timer");
	private static JButton quitGame = new JButton("Quit");
	private static JComboBox selectXML = new JComboBox(XML_Loader.getXMLFiles().toArray());
	private static JTextField scoreBox = new JTextField();// displays the score
	private static JTextField timeBox = new JTextField();// displays the time
	private static JTextField statusBox = new JTextField();// status messages
	
	private static JButton saveGameButton = new JButton("Save Game");
	private static JButton loadGameButton = new JButton("Load Game");
	
	private static GameStatus solitaireStatus;
	// TIMER UTILITIES
	private static Timer timer = new Timer();
	private static boolean initTimer = false;
	private static ScoreClock scoreClock = new ScoreClock();
	
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
		solitaireStatus.setGameScore(solitaireStatus.getGameScore() + deltaScore);
		String newScore = "Score: " + solitaireStatus.getGameScore();
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
		solitaireStatus.setGameTime(solitaireStatus.getGameTime() + 1);

		String stringtime = "Seconds: " + solitaireStatus.getGameTime();
		timeBox.setText(stringtime);
		timeBox.repaint();
	}

	protected static void startTimer()
	{
		scoreClock = new ScoreClock();
		// set the timer to update every second
		
		if (!initTimer)
		{
			timer.scheduleAtFixedRate(scoreClock, 1000, 1000);
			initTimer = true;
		}
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
			JFrame frame = (JFrame)SwingUtilities.getRoot(getTable());
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
	
	private static final class QuitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			MenuManager.switchMenu(MenuManager.START_MENU);
		}
	}
	
	private static final class SaveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			JFileChooser j = new JFileChooser("."); 
			  
			// Open the save dialog 
			int r = j.showSaveDialog(null); 
			  
            if (r == JFileChooser.APPROVE_OPTION) { 
                // set the label to the path of the selected directory 
                SaveGame(j.getSelectedFile().getAbsolutePath()); 
            } 
		}
	}
	
	private static final class LoadListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			JFileChooser j = new JFileChooser("."); 
			  
			// Open the save dialog 
		
			int r = j.showSaveDialog(null); 
			  
            if (r == JFileChooser.APPROVE_OPTION) { 
                // set the label to the path of the selected directory 
                LoadGame(j.getSelectedFile().getAbsolutePath()); 
            } 
		}
	}
	
	private static final class XML_Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String fileSelected = (String)selectXML.getSelectedItem();

			XMLFile = fileSelected;
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

		private boolean gameOver = false;// easier to negate this than affirm it
		private Point start = null;// where mouse was clicked
		private Point stop = null;// where mouse was released
		private Card card = null; // card to be moved
		
		private CardStack source = null;
		private CardStack dest = null;
		private CardStack transferStack;
		private int cursorOffsetX;
		private int cursorOffsetY;
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			start = e.getPoint();
			boolean stopSearch = false;
			statusBox.setText("");
			System.out.println("Grabbing mouse");
			
			//TODO: ClickCard
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
							System.out.println("Grabbed card: " + card.toString());
							
							stopSearch = true;
							
							break;
						}
					}
				}
			}
		}
		
		protected void getTransferStack(Card c)
		{
			cursorOffsetX = start.x - c.getWhereAmI().x ;
			cursorOffsetY = start.y - c.getWhereAmI().y ;
			
			transferStack = c.stackCallBack.TakeSubStack(card);
			table.add(transferStack, 0);
			transferStack.repaint();
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
						if (validMoveMade)
						{
							source.ShowTopCard();
						}
						if((source.Type == CardStack.StackType.TAB) && (dest.Type == CardStack.StackType.FOUNDATION)) {
							setScore(1);
						}
						
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

			if (transferStack != null)
			{
				
				source.PlaceCards(transferStack);
				transferStack.erase();
				table.remove(transferStack);
				transferStack = null;
				table.repaint();
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
			
			gameOver = false;
		}// end mousePressed()
		
		@Override
	    public void mouseClicked(MouseEvent e){
			CardStack pointedStack = getPointedStack(e.getPoint()) ;
			if (pointedStack == null)
				return;
			
			
			if (pointedStack.Type == CardStack.StackType.DRAW)
			{
				pointedStack.Draw();
			}
			else if(e.getClickCount()==2)
			{
				Card card = getPointedCard(e.getPoint());
				CardStack stack = card.stackCallBack;
	        	if (card != null && card.isTopCard())
	        	{
	        		if (mainGameBoard.ClickMove(card))
	        		{
	        			stack.ShowTopCard();
	        		}
	        		else
	        		{
	        			statusBox.setText("No valid moves for this card.");
	        		}
	        		card.stackCallBack.repaint();
					
	        	}
	        }
			table.repaint();
	    }
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			
			if (transferStack == null && card != null)
				getTransferStack(card);
			if (transferStack != null) 
			{
				Point p = e.getPoint();
				System.out.println("Dragging " + p.toString());
//				transferStack.xPos = p.x;
//				transferStack.yPos = p.y;
				transferStack.setXY(p.x - cursorOffsetX, p.y - cursorOffsetY);
				
				table.repaint();
				transferStack.repaint();
			}
		}
		
		protected Card getPointedCard(Point p) 
		{
			
			for (int x = 0; x < mainGameBoard.Stacks.size(); x++)
			{
				
				source = mainGameBoard.Stacks.get(x);
				// pinpointing exact card pressed
				
				if (source.contains(p) ) 
				{
					System.out.println("In stack: " + source.toString());
					
					for (Component ca : source.getComponents())
					{
						Card c = (Card) ca;
	
						if (c.contains(p)  && c.faceUp)
						{
							card = c;
							
							System.out.println("Grabbed card: " + card.toString());
							return c;
							
						}
					}
				}
				
			}
			return null;
		}
		protected CardStack getPointedStack(Point p) 
		{
			
			for (int x = 0; x < mainGameBoard.Stacks.size(); x++)
			{
				
				source = mainGameBoard.Stacks.get(x);
				// pinpointing exact card pressed
				
				if (source.contains(p) ) 
				{
					return source;
				}
				
			}
			return null;
		}
		
		
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
            solitaireStatus = mainGameBoard.status;
            in.close(); 
            file.close(); 
            playNewGame();
            
        } 
  
        catch (IOException ex) { 
            System.out.println("IOException is caught"); 
            System.out.println(ex);
        } 
  
        catch (ClassNotFoundException ex) { 
            System.out.println("ClassNotFoundException" + 
                                " is caught"); 
            System.out.println(ex);
        } 
		
	}
	
	
	//TODO: Separate code for creating a new game from adding everything in the gameboard to the JFrame
	//		- Will be nice to have separate when we implement Save/Load features.
	private static void playNewGame(String sXMLFile)
	{
		mainGameBoard = XML_Loader.LoadXML(sXMLFile);
		playNewGame();
	}
	
	private static void playNewGame()
	{
		// reset stacks if user starts a new game in the middle of one
		
		table.removeAll();


		// Load the gameboard using XML_Loader
		
		for (int x = 0; x < mainGameBoard.Stacks.size(); x++)
		{
			CardStack stack = mainGameBoard.Stacks.get(x);
			stack.setXY(stack.xPos, stack.yPos);
			table.add(stack);
		}
		

		solitaireStatus.setGameTime(0);
		timer = new Timer();
		

		scoreBox.setText("Score: ");
		scoreBox.setEditable(false);
		scoreBox.setOpaque(false);

		timeBox.setText("Seconds: 0");
		timeBox.setEditable(false);
		timeBox.setOpaque(false);

		startTimer();

		
		selectXML.setEditable(false);
		
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
		table.add(quitGame);
		table.add(selectXML);
		table.add(saveGameButton);
		table.add(loadGameButton);
		table.repaint();
		
		System.out.println("Done setting up");
	}
	
	
	static public void setFrameSize() 
	{
		// get max x value of all stacks, set WIDTH to MaxX + CARDWIDTH
		// repeat for y using.
		JFrame frame = (JFrame)SwingUtilities.getRoot(getTable());
		
		int frameW = mainGameBoard.Stacks.stream().mapToInt(v -> v.xPos).max().orElse(0) + Card.CARD_WIDTH + Card.CORNER_ANGLE;
		int frameH = mainGameBoard.Stacks.stream().mapToInt(v -> v.yPos).max().orElse(0) + (3 * Card.CARD_HEIGHT + 40);

		if (frameH < TABLE_HEIGHT)
			frameH = TABLE_HEIGHT;
		
		if (frameW < TABLE_WIDTH)
			frameW = TABLE_WIDTH;
		
		frame.setTitle(mainGameBoard.GameTitle);
		frame.setSize(frameW, frameH);
		newGameButton.setBounds(0, frameH - 70, 120, 30);
		showRulesButton.setBounds(120, frameH - 70, 120, 30);
		quitGame.setBounds(240, frameH-70, 120, 30);
		scoreBox.setBounds(360, frameH - 70, 120, 30);
		timeBox.setBounds(480, frameH - 70, 120, 30);
		
		toggleTimerButton.setBounds(600, frameH - 70, 120, 30);
		selectXML.setBounds(720, frameH - 70, 120, 30);
		statusBox.setBounds(840, frameH - 70, frameH - 840, 30);
		saveGameButton.setBounds(840, frameH - 140, 120, 30);
		loadGameButton.setBounds(960, frameH - 140, 120, 30);
		
	}
	
	public static void main(String[] args)
	{

		Container contentPane;
//		
		mainFrame.setSize(TABLE_WIDTH, TABLE_HEIGHT);
		solitaireStatus = new GameStatus();
		contentPane = mainFrame.getContentPane();
		contentPane.add(table);
//		contentPane.add(getScrollTable());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String file;
		if (args.length > 0)
			file = args[0];
		else 
		{
			file = "BonanzaCreek.xml";
//			file = ".\\Game\\Tests\\FoundationTest.xml";
			//file = "BinaryStarTest.xml";
		}
		mainFrame.setVisible(true);
		
		initGame(file);
		mainGameBoard.status = solitaireStatus;
	}
	
	public static void initGame(String file)
	{

		table.setLayout(null);
		table.setBackground(new Color(0, 180, 0));

		
		loadGameButton.addActionListener(new LoadListener());
		saveGameButton.addActionListener(new SaveListener());
		toggleTimerButton.addActionListener(new ToggleTimerListener());
		newGameButton.addActionListener(new NewGameListener());
		showRulesButton.addActionListener(new ShowRulesListener());
		quitGame.addActionListener(new QuitListener());
		selectXML.addActionListener(new XML_Listener());
		
		playNewGame(file);
		mouseManager = new CardMovementManager();
		table.addMouseListener(mouseManager);
		table.addMouseMotionListener(mouseManager);

		
	}

	public GameStatus play(File inFile)
	{
		solitaireStatus = new GameStatus();
		
		solitaireStatus.setGameSaveFile(inFile);
		initGame("BinaryStar.xml");
		mainGameBoard.status = solitaireStatus;
		LoadGame(inFile.getAbsolutePath());
		
		return solitaireStatus;
	}
	public static JPanel getTable() {

		return table;
	}
	
	public static JScrollPane getScrollTable() {
		JScrollPane pane = new JScrollPane(table);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return pane;
	}
}
