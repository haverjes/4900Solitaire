import java.util.*;  






public class MoveRule 
{
	enum CardSequence {	UP, DOWN, UPORDOWN }

	enum SuitRequirement {SAME, NONE, ALTCOLOR }
	
	
	public CardStack.StackType destStackType;
	public CardSequence cardSequence;
	public SuitRequirement suitPattern;
	public boolean allowGroup;
	public boolean rankRollover;
	
	// There are a lot of return points to make early failures more efficient.
	public boolean CheckMove(Card card, CardStack destStack) 
	{
		
		
		if (destStack.Type != this.destStackType)
			return false;
		
		// get card index, if group move isn't allowed
		if (!this.GroupMoveAllowed && card != card.StackCallBack.Cards.last()) 
		{
			return false;
		}
		
		Card lastCard = destStack.getTopCard();
		
		switch (this.SuitRequirement) 
		{
			case suitPattern.ALTCOLOR:
				if (!IsAltColor(card, lastCard))
					return false;
				break;
			case suitPattern.SAME:
				if (card.Suit != lastCard.Suit)
					return false;
				break;
			default:
				// Nothing to do here.
		}
		
		int diff = RankDiff(newCard, lastCard);
		switch (this.cardSequence) 
		{
			case cardSequence.UP:
				if (diff != 1)  // TODO:  Add Rollover
					return false;
				break;
			case cardSequence.DOWN:
				if (diff != -1)  // TODO:  Add Rollover
					return false;
				break;
			case cardSequence.UPORDOWN:
				if (diff != 1  && diff != -1)  // TODO:  Add Rollover
					return false;
				break;
			default:
				// Nothing to do here.
		}
		
		
		// Check destination stack traits
		if (destStack.IstEmpty() 
				&& destStack.firstCard != "")
		{
			if (destStack.firstCard.Length == 2)
			{
			// Sp]lit firstCard into 2 chars.  First one is Rank, second char is suit
				if (firstCard[0] != '*' 
					&& firstCard[0] != card.rank)
				{
					return false;
				}
				
				if (firstCard[0] != '*') 
				{
					switch (firstCard[0])
					{
						case "S":
							if (card.suit != Suit.SPADES) 
								return false;
							break;
						case "C":
							if (card.suit != Suit.CLUBS) 
								return false;
							break;
						case "D":
							if (card.suit != Suit.DIAMONDS) 
								return false;
							break;
						case "H":
							if (card.suit != Suit.HEARTS) 
								return false;
							break;
					}
				}
			}
			else 
			{
				// Special rules like "FirstCardPlayed" for bald eagle
			}
			
		}
		else 
		{
			if (destStack.cardLimit >= destStack.Cards.size() + 1)
				return false;
		}
		
		return true;
	}

	boolean IsAltColor(Card newCard, Card topCard)
	{
		//if (newCard.Suit == topCard.Suit) 
		if (IsBlack(newCard) == IsBlack(topCard))
			return false;
		else
			return true;
	}

	boolean IsBlack(Card card)
	{
		if (card.Suit == Suit.SPADES || card.Suit == Suit.CLUBS)
			return true;
		
		return false;
	}

	int RankDiff(Card newCard, Card topCard) 
	{
		// TopCard - NewCard		
		int diff = newCard.Rank - topCard.Rank;
		
		// if abs(diff) == 12, then one is a King and the other an Ace.  
		// Get the rollover value by reversing the sign.
		if (this.RankRollover && (diff == 12 || diff == -12)) 
		{
			diff = diff / 12 * -1; 
		}
		
		return diff;
	}

}