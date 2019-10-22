public class MoveRule 
{
	// There are a lot of return points to make early failures more efficient.
	public boolean MoveRule.CheckMove(Card card, CardStack destStack) 
	{
		
		if (destStack.Type != this.DestStackType)
			false;
		
		// get card index, if group move isn't allowed
		if (!this.GroupMoveAllowed && card != card.StackCallBack.Cards.last()) 
		{
			return false;
		}
		
		Card lastCard = destStack.getTopCard();
		
		switch (this.SuitRequirement) 
		{
			case SuitPattern.ALTCOLOR:
				if (!IsAltColor(card, lastCard))
					return false;
				break;
			case SuitPattern.SAME:
				if (card.Suit != lastCard.Suit)
					return false;
				break;
			default:
				// Nothing to do here.
		}
		
		int diff = RankDiff(newCard, lastCard)
		switch (this.CardSequence) 
		{
			case CardSequence.UP:
				if (diff != 1)  // TODO:  Add Rollover
					return false;
				break;
			case CardSequence.DOWN:
				if (diff != -1)  // TODO:  Add Rollover
					return false;
				break;
			case CardSequence.UPORDOWN:
				if (diff != 1  && diff != -1)  // TODO:  Add Rollover
					return false;
				break;
			default:
				// Nothing to do here.
		}
		
		
		// Check destination stack traits
		if (destStack.IstEmpty() 
				&& destStack.initialCard != "")
		{
			if (destStack.initialCard.Length == 2)
			{
			// Sp]lit initialCard into 2 chars.  First one is Rank, second char is suit
				if (initCard[0] != '*' 
					&& initCard[0] != card.Rank)
				{
					return false;
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
		
		// if abs(diff) == 13, then one is a King and the other an Ace.  
		// Get the rollover value by reversing the sign.
		if (this.RankRollover && (diff == 13 || diff == -13)) 
		{
			diff = diff / 13 * -1; 
		}
		
		return diff;
	}

}