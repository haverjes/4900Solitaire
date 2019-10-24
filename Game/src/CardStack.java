import java.util.*;  
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ListIterator;
import java.util.Vector;

//import javax.swing.JComponent;

public class CardStack //extends JComponent //implements ICardStack
{
	public static enum StackType 
	{
		TAB, FOUNDATION, WASTE, DRAW
	}
	
	public static enum StackShape 
	{
		STACK, FANDOWN
	}
	
	
	public List<Card> cards;
	public int yPos;
	public int xPos;
	
	public StackShape Shape;
	public boolean lockcards;
	
	public String initialCard; 
	public int initFaceDown;
	
	public int firstCardRank; 
	public Card.Suit firstCardSuit; 
	
	public int cardLimit;
	
	public StackType Type;
	
	protected final int SPREAD = 18;
	protected int _x = 0;
	protected int _y = 0;
	
	
	public boolean getLockCards() { return lockcards; }
	public void setLockCards(boolean v) { lockcards = v; }
	
	public CardStack() 
	{
		cards = new ArrayList<Card>();
		
	}
	
	public List<Card> TakeCard(Card card) 
	{
		int nIndex = this.cards.indexOf(card);
		List<Card> retCardList = new ArrayList<Card>();
		int nLastIndex = this.cards.size() - 1;
		
		
		for (int i = nIndex; i < this.cards.size(); i++)
		{
			retCardList.add(this.cards.get(i));
		}
		
		// Remove the cards from the list.
		//this.cards.removeRange(nIndex, nLastIndex);  
		// removeRange is a protected method, presumably because Java is managed by complete assholes.
		this.cards.subList(nIndex, nLastIndex).clear();  // God! java is a stupid language.
		
		
		// Ensure topcard is face up, if stack not empty
		if (this.cards.size() > 0) 
			this.cards.get(nLastIndex).faceUp = true;
		
		return retCardList;
	}

	// Add the list of carsd in order and update each card's Callback member.
	public void PlaceCards(List<Card> cardList) 
	{
		for (Card card: cardList)
		{
			this.cards.add(card);
			card.stackCallBack = this;
		}
	}
	
	public Card getTopCard() 
	{
		return this.cards.get(this.cards.size() - 1);
	}
	
	public int getCardIndex(Card card) 
	{
		return this.cards.indexOf(card);
	}
	
	public int getCardCount() { return cards.size(); }
	
	public void dealCard(Card card) 
	{
		if (getCardCount() < initFaceDown) 
			card.faceUp = false;
		
		cards.add(card);
	}

	/***********************
	*  Drawing methods
	*
	************************/
/*
	public boolean contains(Point p)
	{
		Rectangle rect = new Rectangle(_x, _y, Card.CARD_WIDTH + 10, Card.CARD_HEIGHT * 3);
		return (rect.contains(p));
	}

	public void setXY(int x, int y)
	{
		_x = x;
		_y = y;
		// System.out.println("CardStack SET _x: " + _x + " _y: " + _y);
		setBounds(_x, _y, Card.CARD_WIDTH + 10, Card.CARD_HEIGHT * 3);
	}

	public Point getXY()
	{
		// System.out.println("CardStack GET _x: " + _x + " _y: " + _y);
		return new Point(_x, _y);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (playStack)
		{
			removeAll();
			ListIterator<Card> iter = v.listIterator();
			Point prev = new Point(); // positioning relative to the container
			Point prevWhereAmI = new Point();// abs positioning on the board
			if (iter.hasNext())
			{
				Card c = iter.next();
				// this origin is point(0,0) inside the cardstack container
				prev = new Point();// c.getXY(); // starting deck pos
				add(Solitaire.moveCard(c, prev.x, prev.y));
				// setting x & y position
				c.setWhereAmI(getXY());
				prevWhereAmI = getXY();
			} else
			{
				removeAll();
			}

			for (; iter.hasNext();)
			{
				Card c = iter.next();
				c.setXY(new Point(prev.x, prev.y + SPREAD));
				add(Solitaire.moveCard(c, prev.x, prev.y + SPREAD));
				prev = c.getXY();
				// setting x & y position
				c.setWhereAmI(new Point(prevWhereAmI.x, prevWhereAmI.y + SPREAD));
				prevWhereAmI = c.getWhereAmI();
			}

		}
	}
	*/
	
	static public StackType getStackType(String s) 
	{
		switch (s)
		{
			case "Tab":
				return StackType.TAB;
				// break;
			case "Foundation":
				return StackType.FOUNDATION;
				// break;
			case "Draw":
				return StackType.DRAW;
				// break;
			case "Waste":
				return StackType.WASTE;
				// break;
			default:
				return null;
		}
	}
}