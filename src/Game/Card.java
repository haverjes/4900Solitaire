// Card Class
//import java.util.*;  
package Game;



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

	public String toString() 
	{
		return convertRankStr(this.rank) + convertSuitStr(this.suit);
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
	static public String convertSuitStr(Suit s) 
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
}