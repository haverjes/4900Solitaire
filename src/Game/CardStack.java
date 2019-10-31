package Game;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.util.ListIterator;

import javax.swing.JComponent;

public class CardStack extends JComponent //implements ICardStack
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


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
	public int initCardCount;
	
	public int firstCardRank; 
	public Card.Suit firstCardSuit; 
	
	public int cardLimit;
	
	public StackType Type;
	
	public static final int SPREAD = 18;
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
//		int nLastIndex = this.cards.size() - 1;
		
		
		for (int i = nIndex; i < this.cards.size(); i++)
		{
			retCardList.add(this.cards.get(i));
		}
		
		// Remove the cards from the list.
		this.cards.removeAll(retCardList);
		
		
		// Ensure topcard is face up, if stack not empty
		if (this.cards.size() > 0) 
			this.getTopCard().faceUp = true;
		
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
	
	public void PlaceCard(Card card) 
	{	
			this.cards.add(card);
			card.stackCallBack = this;
	}
	
	public Card getTopCard() 
	{
		if (cards.size() > 0)
			return this.cards.get(this.cards.size() - 1);
		else
			return null;
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
		else
			card.faceUp = true;
		cards.add(card);
		card.stackCallBack = this;
	}

	public String toString() 
	{
		String sRet = String.valueOf(this.cards.size());
		for(Card card: this.cards)
			sRet = sRet + " - " + card.toString();
		
		return sRet;
	}
	/***********************
	*  Drawing methods
	*
	************************/
	protected int getStackHeight() 
	{
		return Card.CARD_HEIGHT + this.getCardCount() * SPREAD;
	}
	public boolean contains(Point p)
	{
		int bottom = _y - getStackHeight();
		Rectangle rect = new Rectangle(xPos, yPos, Card.CARD_WIDTH + 10, getStackHeight());
		return (rect.contains(p));
	}

	public void setXY(int x, int y)
	{
		_x = x;
		_y = y;
		xPos = x;
		yPos = y;
		// System.out.println("CardStack SET _x: " + _x + " _y: " + _y);
		int bottom = _y + getStackHeight();
		//setBounds(_x, bottom, Card.CARD_WIDTH + 10, getStackHeight());
		setBounds(_x, _y, Card.CARD_WIDTH + 10, getStackHeight());
	}

	public Point getXY()
	{
		// System.out.println("CardStack GET _x: " + _x + " _y: " + _y);
		return new Point(xPos, yPos);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.Shape == StackShape.FANDOWN)
		{
			removeAll();
			ListIterator<Card> iter = cards.listIterator();
			Point prev = new Point(); // positioning relative to the container
			Point prevWhereAmI = new Point();// abs positioning on the board
			if (iter.hasNext())
			{
				Card c = iter.next();
				// this origin is point(0,0) inside the cardstack container
				prev = new Point();// c.getXY(); // starting deck pos
				add(SolitaireEngine.moveCard(c, prev.x, prev.y));
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
				add(SolitaireEngine.moveCard(c, prev.x, prev.y + SPREAD));
				prev = c.getXY();
				// setting x & y position
				c.setWhereAmI(new Point(prevWhereAmI.x, prevWhereAmI.y + SPREAD));
				prevWhereAmI = c.getWhereAmI();
			}
			
//			prevWhereAmI = getXY();
//			for (int nIndex = 0; nIndex < cards.size(); nIndex++)
//			{
//				
//				Card curCard = cards.get(nIndex);
//				prevWhereAmI = new Point(0, (nIndex * SPREAD));
//				add(SolitaireEngine.moveCard(curCard, prevWhereAmI.x, prevWhereAmI.y));
//				curCard.setWhereAmI(prevWhereAmI);
//			}
			

		}
		else
		{
			removeAll();
			if (cards.size() > 0)
			{
				Point prev = new Point(); // positioning relative to the container
				add(SolitaireEngine.moveCard(this.getTopCard(), prev.x, prev.y));
			}
			else
			{
				// draw back of card if empty
				Graphics2D g2d = (Graphics2D) g;
				RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, Card.CARD_WIDTH, Card.CARD_HEIGHT,
						Card.CORNER_ANGLE, Card.CORNER_ANGLE);
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.fill(rect);
				g2d.setColor(Color.black);
				g2d.draw(rect);
			}
		}
	}

	
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
	
	static public StackShape getStackShape(String s) 
	{
		switch (s)
		{
			case "FanDown":
				return StackShape.FANDOWN;
				// break;
			default:
				return StackShape.STACK;
		}
	}
}