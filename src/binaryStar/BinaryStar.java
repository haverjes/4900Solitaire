package binaryStar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JEditorPane;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
import javax.swing.*;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;
//import javax.swing.SwingUtilities;

import gameInterface.*;

/* General plan
 * - The engine loads in the GameBoard and displays everythin
 * - Also monitors user input from the mouse
 * 
 * - Most of the code reused from MySolitaire/Solitaire.java (the provided base code) will be found here.
 * 	 - Some will be moved to the GameBoard, like Timer and Score related stuff.
 */

public class BinaryStar
{
	protected static GameBoard mainGameBoard;
	protected static CardMovementManager mouseManager;
	
	// CONSTANTS
	public static int TABLE_HEIGHT = Card.CARD_HEIGHT * 4;
	public static int TABLE_WIDTH = (Card.CARD_WIDTH * 7) + 100;
	
	//Games that can be played. Hardcoded in for now, but later should be set to what the current user has enabled
	public static String[] XML_Options = {"Binary Star", "Bonanza Creek"};
	
	// MISC TRACKING VARIABLES
	private static boolean timeRunning = false;// timer running?	private static int score = 0;// keep track of the score
	
	// GUI COMPONENTS (top level)
	protected static JFrame mainFrame = new JFrame();
	protected static final JPanel table = new JPanel();
	
	// other components
	private static JEditorPane gameTitle = new JEditorPane("text/html", "");
	private static JButton toggleTimerButton = new JButton("Pause Timer");
	private static JTextField scoreBox = new JTextField();// displays the score
	private static JTextField timeBox = new JTextField();// displays the time
	private static JTextField statusBox = new JTextField();// status messages
	private static GameStatus solitaireStatus;
	private static JMenuBar gameMenu = new JMenuBar();
	private static List<GameOption> gameOptions;
	
	// TIMER UTILITIES
	private static Timer timer = new Timer();
	private static boolean initTimer = false;
	private static ScoreClock scoreClock = new ScoreClock();
	public static boolean isAlive;
	
	// Store last XML File loaded.
	public static String XMLFile;
	protected static String autoSaveFile;
	
	public BinaryStar()
	{
		// Does nothing really, but other groups requested play to not be static.
	}
	
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
			JEditorPane rulesTextPane = new JEditorPane("text/html", "");
			rulesTextPane.setEditable(false);
			String rulesText = mainGameBoard.rulesText;
			rulesTextPane.setText(rulesText);
			ruleFrame.add(rulesTextPane);

			ruleFrame.setVisible(true);
		}
	}
	
	private static final class QuitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			solitaireStatus.setGameStatusFlag(3);
			mainFrame.dispose();
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
		
			int r = j.showOpenDialog(null); 
			  
            if (r == JFileChooser.APPROVE_OPTION) { 
                // set the label to the path of the selected directory 
                LoadGame(j.getSelectedFile().getAbsolutePath()); 
            } 
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

		//private boolean gameOver = false;// easier to negate this than affirm it
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
			statusBox.setText("");
//			System.out.println("Grabbing mouse");
			card = this.getPointedCard(start);
//			if (card != null)
//				System.out.println("Grabbed card: " + card.toString());
		}
		
		protected void getTransferStack(Card c)
		{
			cursorOffsetX = start.x - c.getWhereAmI().x ;
			cursorOffsetY = start.y - c.getWhereAmI().y ;
			
			transferStack = c.stackCallBack.TakeSubStack(card);
			table.add(transferStack, 0);
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			stop = e.getPoint();
			// used for status bar updates
			boolean validMoveMade = false;

//			System.out.println("Releasing mouse");
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
						
							if((source.Type == CardStack.StackType.TAB) && (dest.Type == CardStack.StackType.FOUNDATION)) {
								setScore(1);
							}
						}
						dest.repaint();
						table.repaint();
						
						//TODO: Implement scoring in GameBoard?
						break;
					} 
					
				}
				
			}// end cycle through play decks

			// SHOWING STATUS MESSAGE IF MOVE INVALID
			if (!validMoveMade && dest != null && card != null && dest != source && card.stackCallBack != source)
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
			CheckForVictory();
			
			// RESET VARIABLES FOR NEXT EVENT
			start = null;
			stop = null;
			source = null;
			dest = null;
			card = null;
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
				
	        	if (card != null && card.isTopCard())
	        	{
	        		CardStack stack = card.stackCallBack;
	        		if (mainGameBoard.ClickMove(card))
	        		{
	        			stack.ShowTopCard();
	        			if((stack.Type == CardStack.StackType.TAB) && (card.stackCallBack.Type == CardStack.StackType.FOUNDATION)) {
							setScore(1);
						}
	        		}
	        		else
	        		{
	        			statusBox.setText("No valid moves for this card.");
	        		}
	        		card.stackCallBack.repaint();
	        		CheckForVictory();
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
				transferStack.setXY(p.x - cursorOffsetX, p.y - cursorOffsetY);
				
				table.repaint();
				transferStack.repaint();
			}
		}
		
		protected void CheckForVictory()
		{
			AutoSaveGame();
			if (mainGameBoard.checkVictory())
			{
				JOptionPane.showMessageDialog(table, "Congratulations! You've Won!");
				
				statusBox.setText("Game Over!");
				solitaireStatus.setGameStatusFlag(2);
				
				//TODO: Any other interaction with the platform required?  Or even possible?
				mainFrame.dispose();
				
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
//					System.out.println("In stack: " + source.toString());
					
					for (Component ca : source.getComponents())
					{
						Card c = (Card) ca;
	
						if (c.contains(p)  && c.faceUp)
						{
							card = c;
							
//							System.out.println("Grabbed card: " + card.toString());
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

	public static void AutoSaveGame()
	{
		SaveGame(autoSaveFile);
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
            
            mainGameBoard.status.setGameSaveFile(new File(filename));
            autoSaveFile = filename;
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
            mainGameBoard.status.setGameSaveFile(new File(filename));
            solitaireStatus = mainGameBoard.status;
            in.close(); 
            file.close(); 
            autoSaveFile = filename;
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
	

	private static void playNewGame(String sXMLFile)
	{
		mainGameBoard = XML_Loader.LoadXML(sXMLFile);
		solitaireStatus = new GameStatus();
		mainGameBoard.status = solitaireStatus;
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

		
		
		statusBox.setEditable(false);
		statusBox.setOpaque(false);

		setFrameSize(); 
		
		table.add(statusBox);
		table.add(toggleTimerButton);
		table.add(gameTitle);
		table.add(timeBox);
		table.add(scoreBox);
		
		table.repaint();
		
		System.out.println("Done setting up");
	}
	
	
	static public void setFrameSize() 
	{
		// get max x value of all stacks, set WIDTH to MaxX + CARDWIDTH
		// repeat for y using.
		Card.ResetCardSize();
		JFrame frame = (JFrame)SwingUtilities.getRoot(getTable());
		
		int frameW = mainGameBoard.Stacks.stream().mapToInt(v -> v.xPos).max().orElse(0) + Card.CARD_WIDTH + Card.CORNER_ANGLE;
		int frameH = getMinFrameHeight();

		frame.setTitle(mainGameBoard.GameTitle);
		frame.setSize(frameW, frameH);
		
		//int menuHeight = frame.getJMenuBar().getHeight();
		int menuHeight = 23;
		scoreBox.setBounds(0, frameH - 70 - menuHeight, 120, 30);
		timeBox.setBounds(120, frameH - 70 - menuHeight, 120, 30);
		
		toggleTimerButton.setBounds(240, frameH - 70 - menuHeight, 120, 30);
		statusBox.setBounds(360, frameH - 70 - menuHeight, frameW - 360, 30);
		
	}
	
	public static int getMinFrameHeight() {
		
		CardStack csBottomStack = mainGameBoard.getLowestStack();
		int nCardMultiplier = 1;
		int nMargin = 100;
		if (csBottomStack.Shape == CardStack.StackShape.FANDOWN)
		{
			nCardMultiplier = 3;
			nMargin = 40;
		}
		//int frameH = mainGameBoard.Stacks.stream().mapToInt(v -> v.yPos).max().orElse(0) + (3 * Card.CARD_HEIGHT + 40);
		int frameH = csBottomStack.yPos + (nCardMultiplier * Card.CARD_HEIGHT + nMargin);
		int tempH = csBottomStack.yPos;
		int nScreenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		while (frameH > nScreenHeight)
		{
			Card.CARD_HEIGHT = Card.CARD_HEIGHT - 25;
			frameH = tempH + (nCardMultiplier * Card.CARD_HEIGHT + nMargin);	
		}
		return frameH;
	}
	
	public static void main(String[] args)
	{

		String file;
		autoSaveFile = "autosave.save";
		if (args.length > 0)
			file = args[0];
		else 
		{
			file = "BinaryStar.xml";
		}
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initGame(file);
		
	}
	
	public static void initGame(String file)
	{
		isAlive = true;
		mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	solitaireStatus.setGameStatusFlag(3);
            	isAlive = false;
    			mainFrame.dispose();
    			//System.exit(0);
            }
        });
		Container contentPane;
		mainFrame.setSize(TABLE_WIDTH, TABLE_HEIGHT);
		
		contentPane = mainFrame.getContentPane();
		contentPane.add(table);
		
		mainFrame.setVisible(true);
		table.setLayout(null);
		table.setBackground(new Color(0, 180, 0));
		
		BuildMenu();

		toggleTimerButton.addActionListener(new ToggleTimerListener());
		XMLFile = file;
		playNewGame(file);
		mouseManager = new CardMovementManager();
		table.addMouseListener(mouseManager);
		table.addMouseMotionListener(mouseManager);
		
	}

	public GameStatus play(File inFile)
	{
		solitaireStatus = new GameStatus();
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		solitaireStatus.setGameSaveFile(inFile);
		initGame("BinaryStar.xml");
		mainGameBoard.status = solitaireStatus;
		autoSaveFile = inFile.getPath();
		
		if (inFile.exists())
			LoadGame(inFile.getAbsolutePath());	
			
		
		
		
		return solitaireStatus;
	}
	
	
	public static JPanel getTable() {

		return table;
	}
	
	public static void BuildMenu()
	{
		JFrame frame = (JFrame)SwingUtilities.getRoot(getTable());
		frame.setJMenuBar(gameMenu);
		gameMenu.removeAll();
		JMenu menuFile = new JMenu("File"); 
		JMenu menuHelp = new JMenu("Help"); 
		JMenu menuNewGame = new JMenu("New Game"); 
  
        // create menuitems 
		JMenuItem miSave = new JMenuItem("Save Game"); 
		JMenuItem miLoad = new JMenuItem("Load Game"); 
		JMenuItem miQuit = new JMenuItem("Quit"); 
		JMenuItem miShowRules = new JMenuItem("Show Rules"); 
        
		menuFile.add(menuNewGame);
		menuFile.add(miSave);
		menuFile.add(miLoad);
		menuFile.add(miQuit);
		menuHelp.add(miShowRules);
		
		miSave.addActionListener(new SaveListener());
		miLoad.addActionListener(new LoadListener());
		miQuit.addActionListener(new QuitListener());
		miShowRules.addActionListener(new ShowRulesListener());
		
		gameOptions = XML_Loader.GetGameOptions();
		for (GameOption gOption: gameOptions) 
		{
			JMenuItem newMenuItem = new JMenuItem(gOption.name); 
			newMenuItem.addActionListener(new MenuGameListener());
			newMenuItem.putClientProperty("option", gOption);
			menuNewGame.add(newMenuItem);
		}
		
		gameMenu.add(menuFile);
		gameMenu.add(menuHelp);
		
	}
	
	private static class MenuGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			//TODO: Create window to select XML file.
			JMenuItem item = (JMenuItem)e.getSource();
			GameOption option = (GameOption) item.getClientProperty("option");
			XMLFile = option.file;
			playNewGame(XMLFile);
		}
	}
}