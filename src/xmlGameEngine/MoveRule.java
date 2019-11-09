package xmlGameEngine;
//import java.util.*;  

import java.io.Serializable;

public class MoveRule implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum CardSequence {	UP, DOWN, UPORDOWN }

	enum SuitRequirement {SAME, NONE, ALTCOLOR }
	
	
	public CardStack.StackType destStackType;
	public CardSequence cardSequence;
	public SuitRequirement suitPattern;
	public boolean allowGroup;
	public boolean rankRollover;
	
	
	public MoveRule(String destStack, String seq, String suitReq, boolean groupMove, boolean rollover)
	{
		destStackType = CardStack.StackType.valueOf(destStack.toUpperCase());
		cardSequence = CardSequence.valueOf(seq.toUpperCase());
		suitPattern = SuitRequirement.valueOf(suitReq.toUpperCase());
		allowGroup = groupMove;
		rankRollover = rollover;
	}
	
	// There are a lot of return points to make early failures more efficient.
	public boolean CheckMove(Card card, CardStack destStack) 
	{
		
		
		if (destStack.Type != this.destStackType)
			return false;
		
		// get card index, if group move isn't allowed
		if (!this.allowGroup && card != card.stackCallBack.getTopCard()) 
		{
			return false;
		}
		
		
		
		
		// Check destination stack traits
		if (destStack.cards.isEmpty())
		{
			
			if (destStack.firstCardRank > 0
				&& destStack.firstCardRank != card.rank)
			{
				return false;
			}
			
			if (destStack.firstCardSuit != null
				&& destStack.firstCardSuit != card.suit) 
			{
				return false;
			}
			
			
		}
		else 
		{
			if (destStack.cardLimit >= destStack.cards.size() + 1)
				return false;
			
			Card lastCard = destStack.getTopCard();
			
			switch (this.suitPattern) 
			{
				case ALTCOLOR:
					if (!IsAltColor(card, lastCard))
						return false;
					break;
				case SAME:
					if (card.suit != lastCard.suit)
						return false;
					break;
				default:
					// Nothing to do here.
			}
			
			int diff = RankDiff(card, lastCard);
			switch (this.cardSequence) 
			{
				case UP:
					if (diff != 1)  
						return false;
					break;
				case DOWN:
					if (diff != -1)  
						return false;
					break;
				case UPORDOWN:
					if (diff != 1  && diff != -1)  
						return false;
					break;
				default:
					// Nothing to do here.
			}
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
		if (card.suit == Card.Suit.SPADES || card.suit == Card.Suit.CLUBS)
			return true;
		
		return false;
	}

	int RankDiff(Card newCard, Card topCard) 
	{
		// TopCard - NewCard		
		int diff = newCard.rank - topCard.rank;
		
		// if abs(diff) == 12, then one is a King and the other an Ace.  
		// Get the rollover value by reversing the sign.
		if (this.rankRollover && (diff == 12 || diff == -12)) 
		{
			diff = diff / 12 * -1; 
		}
		
		return diff;
	}

}