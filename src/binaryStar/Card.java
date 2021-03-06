// Card Class
//import java.util.*;  
package binaryStar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;
//TODO: Copy all the drawing related code from MySolitaire\Card.java and adapt to match new class structure.


class Card extends JPanel 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum Suit
	{
		SPADES, CLUBS, DIAMONDS, HEARTS
	}

	private final int x_offset = 10;
	private final int y_offset = 20;
	private final int new_x_offset = x_offset + (CARD_WIDTH - 20);
	static public int CARD_HEIGHT = 150;
	static public int CARD_WIDTH = 100;
	static final public int DEF_CARD_HEIGHT = 150;
	static final public int DEF_CARD_WIDTH = 100;
	final static public int CORNER_ANGLE = 25;
	
	public int rank;
	public Suit suit;
	public boolean faceUp;
	
	public CardStack stackCallBack;
	public int ID;
	
	private Point _location; // location relative to container
	private Point whereAmI; // used to create abs postion rectangle for contains
	// functions
	private int x; // used for relative positioning within CardStack Container
	private int y;
	
	
	public Card(int r, int s, int id)
	{
		rank = r;
		suit = Suit.values()[s];
		ID = id;
		_location = new Point();
		x = 0;
		y = 0;
		_location.x = x;
		_location.y = y;
		whereAmI = new Point();
	}
	
	public int getStackIndex() 
	{
		return stackCallBack.getCardIndex(this);
	}
	
	public boolean isTopCard() 
	{
		return (this == stackCallBack.getTopCard());
	}

	public String toString() 
	{
		String sRet = convertRankStr(this.rank) + convertSuitASCII(this.suit);
		if (!this.faceUp)
			sRet = sRet.toLowerCase();
		return sRet;
	}
	
	static public String convertRankStr(int r) 
	{
		switch (r)
			{
			case 1:
				return "A";
				// break;
			case 11:
				return "J";
				// break;
			case 12:
				return "Q";
				// break;
			case 13:
				return "K";
				// break;
			default:
				return String.valueOf(r);
			}
	}
	
	static public int convertRankInt(String r) 
	{
		switch (r)
			{
			case "A":
				return 1;
				// break;
			case "J":
				return 11;
				// break;
			case "Q":
				return 12;
				// break;
			case "K":
				return 13;
				// break;
			default:
				return Integer.parseInt(r);
			}
	}
	
	static public Suit convertSuit(String s) 
	{
		return convertSuit(s.charAt(0));
	}
	
	static public Suit convertSuit(char s) 
	{
		switch (s)
		{
			case 'S':
					return Suit.SPADES;
				// break;
			case 'C':
					return Suit.CLUBS;
				// break;
			case 'D':
					return Suit.DIAMONDS;
				// break;
			case 'H':
					return Suit.HEARTS;
				// break;
			default:
				return null;
		}
	}
	
	static public String convertSuitASCII(Suit s) 
	{
		switch (s)
		{
			case SPADES:
					return "S";
				// break;
			case CLUBS:
					return "C";
				// break;
			case DIAMONDS:
					return "D";
				// break;
			case HEARTS:
					return "H";
				// break;
			default:
				return null;
		}
		
	}	
	
	static public String convertSuitStr(Suit s) 
	{

		switch (s)
		{
			case SPADES:
					return Character.toString((char)9824);
				// break;
			case CLUBS:
					return Character.toString((char)9831);
				// break;
			case DIAMONDS:
					return Character.toString((char)9830);
				// break;
			case HEARTS:
					return Character.toString((char)9825);
				// break;
			default:
				return null;
		}
	}	

	
	public void setWhereAmI(Point p)
	{
		whereAmI = p;
	}

	public Point getWhereAmI()
	{
		return whereAmI;
	}

	public Point getXY()
	{
		return new Point(x, y);
	}

	public void setXY(Point p)
	{
		x = p.x;
		y = p.y;

	}
	
	public void drawCard()
	{
		if (stackCallBack == null)
			return;
		int stackX = this.stackCallBack.xPos;
		int stackY = this.stackCallBack.yPos;
		
		int newY = stackY + (this.getStackIndex() * CardStack.SPREAD);
				
		setBounds(new Rectangle(new Point(stackX, newY), new Dimension(Card.CARD_WIDTH + 10, Card.CARD_HEIGHT + 10)));
		setXY(new Point(stackX, newY));
	}
	
	@Override
	public boolean contains(Point p)
	{
		Rectangle rect = new Rectangle(whereAmI.x, whereAmI.y, Card.CARD_WIDTH, Card.CARD_HEIGHT);
		return (rect.contains(p));
	}

	private void drawSuit(Graphics2D g, String suit, Color color)
	{
		g.setColor(color);
		g.drawString(suit, _location.x + x_offset, _location.y + y_offset);
		g.drawString(suit, _location.x + x_offset, _location.y + CARD_HEIGHT - 5);
	}

	private void drawValue(Graphics2D g, String value)
	{
		int stringWidth = g.getFontMetrics().stringWidth(value);
		g.drawString(value, _location.x + new_x_offset - stringWidth, _location.y + y_offset);
		g.drawString(value, _location.x + new_x_offset - stringWidth, _location.y + y_offset + CARD_HEIGHT - 25);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Font currentFont = new Font (g.getFont().getFontName(), Font.PLAIN, 20);
//		cardFont = currentFont.deriveFont(currentFont.getSize() * 20);
		g.setFont(currentFont);
		
		Graphics2D g2d = (Graphics2D) g;
		RoundRectangle2D rect2 = new RoundRectangle2D.Double(_location.x, _location.y, CARD_WIDTH, CARD_HEIGHT,
				CORNER_ANGLE, CORNER_ANGLE);
		g2d.setColor(Color.WHITE);
		g2d.fill(rect2);
		g2d.setColor(Color.black);
		g2d.draw(rect2);
		// DRAW THE CARD SUIT AND VALUE IF FACEUP
		if (this.faceUp)
		{

			Color cColor;
			if (suit == Suit.DIAMONDS ||suit == Suit.HEARTS)
				cColor =  Color.RED;
			else
				cColor =  Color.BLACK;
			
			drawSuit(g2d, convertSuitStr(suit), cColor);
			//int new_x_offset = x_offset + (CARD_WIDTH - 30);
			
			drawValue(g2d, convertRankStr(rank));
			
			
		} else
		{
			// DRAW THE BACK OF THE CARD IF FACEDOWN
			RoundRectangle2D rect = new RoundRectangle2D.Double(_location.x, _location.y, CARD_WIDTH, CARD_HEIGHT,
					CORNER_ANGLE, CORNER_ANGLE);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fill(rect);
			g2d.setColor(Color.black);
			g2d.draw(rect);
		}

	}
	
	public static void ResetCardSize()
	{
		CARD_WIDTH = DEF_CARD_WIDTH;
		CARD_HEIGHT = DEF_CARD_HEIGHT;
	}

}