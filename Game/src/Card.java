// Card Class
import java.util.*;  




class Card 
{

	public static enum Suit
	{
		SPADES, CLUBS, DIAMONDS, HEARTS
	}
	public int rank;
	public Suit suit;
	public boolean faceUp;
	
	public CardStack stackCallBack;
	public int ID;
	
	public Card(int r, int s, int id)
	{
		rank = r;
		suit = Suit.values()[s];
		ID = id;
	}
	
	public int getStackIndex() 
	{
		return stackCallBack.getCardIndex(this);
	}
	
	public boolean isTopCard() 
	{
		return (this == stackCallBack.getTopCard());
	}

/*
	static public char convertRankLetter(int r) 
	{
		switch (r)
			{
			case 1:
				return 'A';
				break;
			case 2:
				return '2';
				break;
			case 3:
				return '3';
				break;
			case 4:
				return '4';
				break;
			case 5:
				return '5';
				break;
			case 6:
				return '6';
				break;
			case 7:
				return '7';
				break;
			case 8:
				return '8';
				break;
			case 9:
				return '9';
				break;
			case 10:
				return '10';
				break;
			case 11:
				return 'J';
				break;
			case 12:
				return 'Q';
				break;
			case 13:
				return 'K';
				break;
			}
	}
	*/
	static public int convertRankInt(char r) 
	{
		switch (r)
			{
			case 'A':
				return 1;
				// break;
			case 'J':
				return 11;
				// break;
			case 'Q':
				return 12;
				// break;
			case 'K':
				return 13;
				// break;
			default:
				return Character.getNumericValue(r);
			}
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
	
}