// Card Class
import java.util.*;  


public static enum Suit
	{
		SPADES, CLUBS, DIAMONDS, HEARTS
	}

class Card extends DrawableCard
{

	
	//public int rank;
	//public Suit suit;
	//public boolean faceUp;
	public CardStack stackCallBack;
	
	public int getStackIndex() 
	{
		return stackCallBack.getCardIndex(this);
	}
	
	public boolean isTopCard() 
	{
		return (this == stackCallBack.getTopCard());
	}
	
}