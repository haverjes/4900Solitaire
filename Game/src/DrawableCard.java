import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;



class DrawableCard extends JPanel
{
	
	
	public int rank;
	public Suit suit;
	public boolean faceUp;

	
	private Point _location; // location relative to container

	private Point whereAmI; // used to create abs postion rectangle for contains
	// functions

	private int x; // used for relative positioning within CardStack Container
	private int y;

	private final int x_offset = 10;
	private final int y_offset = 20;
	private final int new_x_offset = x_offset + (CARD_WIDTH - 30);
	final static public int CARD_HEIGHT = 150;
	final static public int CARD_WIDTH = 100;
	final static public int CORNER_ANGLE = 25;

	DrawableCard(Suit cardSuit, int value)
	{
		suit = cardSuit;
		rank = value;
		faceUp = false;
		_location = new Point();
		x = 0;
		y = 0;
		_location.x = x;
		_location.y = y;
		whereAmI = new Point();
	}

	DrawableCard()
	{
		_suit = Card.Suit.CLUBS;
		rank = 1;
		faceUp = false;
		_location = new Point();
		x = 0;
		y = 0;
		_location.x = x;
		_location.y = y;
		whereAmI = new Point();
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

	public Boolean getFaceStatus()
	{
		return faceUp;
	}

	public void setXY(Point p)
	{
		x = p.x;
		y = p.y;

	}

	

	public Card setFaceup()
	{
		faceUp = true;
		return this;
	}

	public Card setFacedown()
	{
		faceUp = false;
		return this;
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
		g.drawString(value, _location.x + new_x_offset, _location.y + y_offset);
		g.drawString(value, _location.x + new_x_offset, _location.y + y_offset + CARD_HEIGHT - 25);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		RoundRectangle2D rect2 = new RoundRectangle2D.Double(_location.x, _location.y, CARD_WIDTH, CARD_HEIGHT, CORNER_ANGLE, CORNER_ANGLE);
		g2d.setColor(Color.WHITE);
		g2d.fill(rect2);
		g2d.setColor(Color.black);
		g2d.draw(rect2);
		// DRAW THE CARD SUIT AND VALUE IF FACEUP
		if (faceUp)
		{
			switch (_suit)
			{
			case HEARTS:
				drawSuit(g2d, "Hearts", Color.RED);
				break;
			case DIAMONDS:
				drawSuit(g2d, "Diamonds", Color.RED);
				break;
			case SPADES:
				drawSuit(g2d, "Spades", Color.BLACK);
				break;
			case CLUBS:
				drawSuit(g2d, "Clubs", Color.BLACK);
				break;
			}
			int new_x_offset = x_offset + (CARD_WIDTH - 30);
			switch (rank)
			{
			case 1:
				drawValue(g2d, "A");
				break;
			case 2:
				drawValue(g2d, "2");
				break;
			case 3:
				drawValue(g2d, "3");
				break;
			case 4:
				drawValue(g2d, "4");
				break;
			case 5:
				drawValue(g2d, "5");
				break;
			case 6:
				drawValue(g2d, "6");
				break;
			case 7:
				drawValue(g2d, "7");
				break;
			case 8:
				drawValue(g2d, "8");
				break;
			case 9:
				drawValue(g2d, "9");
				break;
			case 10:
				drawValue(g2d, "10");
				break;
			case 11:
				drawValue(g2d, "J");
				break;
			case 12:
				drawValue(g2d, "Q");
				break;
			case 13:
				drawValue(g2d, "K");
				break;
			}
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

}// END Card