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
	
}