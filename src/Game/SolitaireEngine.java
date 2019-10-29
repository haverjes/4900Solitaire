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
	public static final int TABLE_HEIGHT = Card.CARD_HEIGHT * 4;
	public static final int TABLE_WIDTH = (Card.CARD_WIDTH * 7) + 100;
	
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
	
	//private static final Card newCardButton = new Card();// reveal waste card

	// moves a card to abs location within a component
	protected static Card moveCard(Card c, int x, int y)
	{
		c.setBounds(new Rectangle(new Point(x, y), new Dimension(Card.CARD_WIDTH + 10, Card.CARD_HEIGHT + 10)));
		c.setXY(new Point(x, y));
		return c;
	}
	
	// Most of the code from MySolitaire is used here, instead pulling the rules from the GameBoard
	private static class ShowRulesListener implements ActionListener
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
	private static class CardMovementManager extends MouseAdapter
	{
		private Card prevCard = null;// tracking card for waste stack
		private Card movedCard = null;// card moved from waste stack
		private boolean sourceIsFinalDeck = false;
		private boolean putBackOnDeck = true;// used for waste card recycling
		private boolean checkForWin = false;// should we check if game is over?
		private boolean gameOver = true;// easier to negate this than affirm it
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
			

			/*
			 * Here we use transferStack to temporarily hold all the cards above
			 * the selected card in case player wants to move a stack rather
			 * than a single card
			 */
			for (int x = 0; x < mainGameBoard.Stacks.size(); x++)
			{
				if (stopSearch)
					break;
				source = mainGameBoard.Stacks.get(x);
				// pinpointing exact card pressed
				for (Component ca : source.getComponents())
				{
					Card c = (Card) ca;
					if (c.getFaceStatus() && source.contains(start))
					{
						transferStack.putFirst(c);
					}
					if (c.contains(start) && source.contains(start) && c.getFaceStatus())
					{
						card = c;
						stopSearch = true;
						System.out.println("Transfer Size: " + transferStack.showSize());
						break;
					}
				}

			}
			// SHOW (WASTE) CARD OPERATIONS
			// display new show card
			if (newCardButton.contains(start) && deck.showSize() > 0)
			{
				if (putBackOnDeck && prevCard != null)
				{
					System.out.println("Putting back on show stack: ");
					prevCard.getValue();
					prevCard.getSuit();
					deck.putFirst(prevCard);
				}

				System.out.print("poping deck ");
				deck.showSize();
				if (prevCard != null)
					table.remove(prevCard);
				Card c = deck.pop().setFaceup();
				table.add(Solitaire.moveCard(c, SHOW_POS.x, SHOW_POS.y));
				c.repaint();
				table.repaint();
				prevCard = c;
			}

			// preparing to move show card
			if (newCardPlace.contains(start) && prevCard != null)
			{
				movedCard = prevCard;
			}

			// FINAL (FOUNDATION) CARD OPERATIONS
			for (int x = 0; x < NUM_FINAL_DECKS; x++)
			{

				if (final_cards[x].contains(start))
				{
					source = final_cards[x];
					card = source.getLast();
					transferStack.putFirst(card);
					sourceIsFinalDeck = true;
					break;
				}
			}
			putBackOnDeck = true;

		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			stop = e.getPoint();
			// used for status bar updates
			boolean validMoveMade = false;

			// SHOW CARD MOVEMENTS
			if (movedCard != null)
			{
				// Moving from SHOW TO PLAY
				for (int x = 0; x < NUM_PLAY_DECKS; x++)
				{
					dest = playCardStack[x];
					// to empty play stack, only kings can go
					if (dest.empty() && movedCard != null && dest.contains(stop)
							&& movedCard.getValue() == Card.Value.KING)
					{
						System.out.print("moving new card to empty spot ");
						movedCard.setXY(dest.getXY());
						table.remove(prevCard);
						dest.putFirst(movedCard);
						table.repaint();
						movedCard = null;
						putBackOnDeck = false;
						setScore(5);
						validMoveMade = true;
						break;
					}
					// to populated play stack
					if (movedCard != null && dest.contains(stop) && !dest.empty() && dest.getFirst().getFaceStatus()
							&& validPlayStackMove(movedCard, dest.getFirst()))
					{
						System.out.print("moving new card ");
						movedCard.setXY(dest.getFirst().getXY());
						table.remove(prevCard);
						dest.putFirst(movedCard);
						table.repaint();
						movedCard = null;
						putBackOnDeck = false;
						setScore(5);
						validMoveMade = true;
						break;
					}
				}
				// Moving from SHOW TO FINAL
				for (int x = 0; x < NUM_FINAL_DECKS; x++)
				{
					dest = final_cards[x];
					// only aces can go first
					if (dest.empty() && dest.contains(stop))
					{
						if (movedCard.getValue() == Card.Value.ACE)
						{
							dest.push(movedCard);
							table.remove(prevCard);
							dest.repaint();
							table.repaint();
							movedCard = null;
							putBackOnDeck = false;
							setScore(10);
							validMoveMade = true;
							break;
						}
					}
					if (!dest.empty() && dest.contains(stop) && validFinalStackMove(movedCard, dest.getLast()))
					{
						System.out.println("Destin" + dest.showSize());
						dest.push(movedCard);
						table.remove(prevCard);
						dest.repaint();
						table.repaint();
						movedCard = null;
						putBackOnDeck = false;
						checkForWin = true;
						setScore(10);
						validMoveMade = true;
						break;
					}
				}
			}// END SHOW STACK OPERATIONS

			// PLAY STACK OPERATIONS
			if (card != null && source != null)
			{ // Moving from PLAY TO PLAY
				for (int x = 0; x < NUM_PLAY_DECKS; x++)
				{
					dest = playCardStack[x];
					// MOVING TO POPULATED STACK
					if (card.getFaceStatus() == true && dest.contains(stop) && source != dest && !dest.empty()
							&& validPlayStackMove(card, dest.getFirst()) && transferStack.showSize() == 1)
					{
						Card c = null;
						if (sourceIsFinalDeck)
							c = source.pop();
						else
							c = source.popFirst();

						c.repaint();
						// if playstack, turn next card up
						if (source.getFirst() != null)
						{
							Card temp = source.getFirst().setFaceup();
							temp.repaint();
							source.repaint();
						}

						dest.setXY(dest.getXY().x, dest.getXY().y);
						dest.putFirst(c);

						dest.repaint();

						table.repaint();

						System.out.print("Destination ");
						dest.showSize();
						if (sourceIsFinalDeck)
							setScore(15);
						else
							setScore(10);
						validMoveMade = true;
						break;
					} else if (dest.empty() && card.getValue() == Card.Value.KING && transferStack.showSize() == 1)
					{// MOVING TO EMPTY STACK, ONLY KING ALLOWED
						Card c = null;
						if (sourceIsFinalDeck)
							c = source.pop();
						else
							c = source.popFirst();

						c.repaint();
						// if playstack, turn next card up
						if (source.getFirst() != null)
						{
							Card temp = source.getFirst().setFaceup();
							temp.repaint();
							source.repaint();
						}

						dest.setXY(dest.getXY().x, dest.getXY().y);
						dest.putFirst(c);

						dest.repaint();

						table.repaint();

						System.out.print("Destination ");
						dest.showSize();
						setScore(5);
						validMoveMade = true;
						break;
					}
					// Moving STACK of cards from PLAY TO PLAY
					// to EMPTY STACK
					if (dest.empty() && dest.contains(stop) && !transferStack.empty()
							&& transferStack.getFirst().getValue() == Card.Value.KING)
					{
						System.out.println("King To Empty Stack Transfer");
						while (!transferStack.empty())
						{
							System.out.println("popping from transfer: " + transferStack.getFirst().getValue());
							dest.putFirst(transferStack.popFirst());
							source.popFirst();
						}
						if (source.getFirst() != null)
						{
							Card temp = source.getFirst().setFaceup();
							temp.repaint();
							source.repaint();
						}

						dest.setXY(dest.getXY().x, dest.getXY().y);
						dest.repaint();

						table.repaint();
						setScore(5);
						validMoveMade = true;
						break;
					}
					// to POPULATED STACK
					if (dest.contains(stop) && !transferStack.empty() && source.contains(start)
							&& validPlayStackMove(transferStack.getFirst(), dest.getFirst()))
					{
						System.out.println("Regular Stack Transfer");
						while (!transferStack.empty())
						{
							System.out.println("popping from transfer: " + transferStack.getFirst().getValue());
							dest.putFirst(transferStack.popFirst());
							source.popFirst();
						}
						if (source.getFirst() != null)
						{
							Card temp = source.getFirst().setFaceup();
							temp.repaint();
							source.repaint();
						}

						dest.setXY(dest.getXY().x, dest.getXY().y);
						dest.repaint();

						table.repaint();
						setScore(5);
						validMoveMade = true;
						break;
					}
				}
				// from PLAY TO FINAL
				for (int x = 0; x < NUM_FINAL_DECKS; x++)
				{
					dest = final_cards[x];

					if (card.getFaceStatus() == true && source != null && dest.contains(stop) && source != dest)
					{// TO EMPTY STACK
						if (dest.empty())// empty final should only take an ACE
						{
							if (card.getValue() == Card.Value.ACE)
							{
								Card c = source.popFirst();
								c.repaint();
								if (source.getFirst() != null)
								{

									Card temp = source.getFirst().setFaceup();
									temp.repaint();
									source.repaint();
								}

								dest.setXY(dest.getXY().x, dest.getXY().y);
								dest.push(c);

								dest.repaint();

								table.repaint();

								System.out.print("Destination ");
								dest.showSize();
								card = null;
								setScore(10);
								validMoveMade = true;
								break;
							}// TO POPULATED STACK
						} else if (validFinalStackMove(card, dest.getLast()))
						{
							Card c = source.popFirst();
							c.repaint();
							if (source.getFirst() != null)
							{

								Card temp = source.getFirst().setFaceup();
								temp.repaint();
								source.repaint();
							}

							dest.setXY(dest.getXY().x, dest.getXY().y);
							dest.push(c);

							dest.repaint();

							table.repaint();

							System.out.print("Destination ");
							dest.showSize();
							card = null;
							checkForWin = true;
							setScore(10);
							validMoveMade = true;
							break;
						}
					}

				}
			}// end cycle through play decks

			// SHOWING STATUS MESSAGE IF MOVE INVALID
			if (!validMoveMade && dest != null && card != null)
			{
				statusBox.setText("That Is Not A Valid Move");
			}
			// CHECKING FOR WIN
			if (checkForWin)
			{
				boolean gameNotOver = false;
				// cycle through final decks, if they're all full then game over
				for (int x = 0; x < NUM_FINAL_DECKS; x++)
				{
					dest = final_cards[x];
					if (dest.showSize() != 13)
					{
						// one deck is not full, so game is not over
						gameNotOver = true;
						break;
					}
				}
				if (!gameNotOver)
					gameOver = true;
			}

			if (checkForWin && gameOver)
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
			sourceIsFinalDeck = false;
			checkForWin = false;
			gameOver = false;
		}// end mousePressed()
	}

	public static void SaveGame(String filename)
	{
		try { 
			  
            // Saving of object in a file 
            FileOutputStream file = new FileOutputStream 
                                           (filename); 
            ObjectOutputStream out = new ObjectOutputStream 
                                           (file); 
  
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
            FileInputStream file = new FileInputStream 
                                         (filename); 
            ObjectInputStream in = new ObjectInputStream 
                                         (file); 
  
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
}
