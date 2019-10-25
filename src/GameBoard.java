import java.util.*;
import java.util.stream.Collectors;  

public class GameBoard 
{
	public List<CardStack> Stacks;
	public List<MoveRule> Rules;
	public List<Card> Cards;
	
	public GameBoard() 
	{
		Stacks = new ArrayList<CardStack>();
		Rules = new ArrayList<MoveRule>();
		Cards = new ArrayList<Card>();
	}
	
	protected void MoveCard(Card card, CardStack destStack) 
	{
		List<Card> cardsToMove = card.stackCallBack.TakeCard(card);
		destStack.PlaceCards(cardsToMove);
		
	}
	
	
	public boolean DragDropMove(Card card, CardStack destStack) 
	{
		if (card.stackCallBack.getLockCards())
			return false;
			
		for (MoveRule rule: this.Rules) 
		{
			if (rule.CheckMove(card, destStack)) 
			{				
				// Process move
				MoveCard(card, destStack);
				return true;
			}
		}
		return false;
	}
	

	// Call DragDropMove() for the card on every cardstack.
	public boolean ClickMove(Card card) 
	{
		if (!card.stackCallBack.getLockCards())
			return false;
		
		for(CardStack cardStack: this.Stacks)
		{
			if (DragDropMove(card, cardStack))
			{
				return true;
			}
		}
		return false;
	}
	
	public void generateCards(int decks) 
	{
		int totalCards = 0;
		for (int deck = 0; deck < decks; deck++) 
		{
			for (int i = 0; i < 4; i++)
			{
				for (int j = 1; j < 14; j++) 
				{
					this.Cards.add(new Card(j, i, totalCards));
					totalCards++;
				}
			}
		}
	}
	
	public void dealCards() 
	{
		// make a copy of the main Cards list to be shrunk and shuffled.
		List<Card> tempDeck = new ArrayList<Card>(Cards);
		
		for (CardStack stack: Stacks)
		{
			if (stack.initialCard != null && stack.initialCard != "")
			{
				// Get the rank and Suit from the initCard string.
				Card.Suit initSuit = Card.convertSuit(stack.initialCard.toCharArray()[stack.initialCard.length() - 1]);
				int initRank = Card.convertRankInt(stack.initialCard.substring(0, stack.initialCard.length() - 1));
				
				// Find cards that match that rank and suit
				List<Card> initCard = tempDeck.stream()
						.filter(c -> c.rank == initRank && c.suit == initSuit)
						.collect(Collectors.toList());
				
				// Put the first math onto the stack and remove it from 
				if (initCard.size() > 0)
				{
					stack.PlaceCard(initCard.get(0));
					tempDeck.remove(initCard.get(0));
				}
				else 
				{
					//TODO: Throw some kind of error.
					// The card specified wasn't found.
				}	
			}
		}
		
		// Deal remaining cards to Tableaus
		Collections.shuffle(tempDeck);  // shuffle the tempDeck

		while (remainingCardCount() > 0)
		{
			for (CardStack curStack: Stacks)
			{
				if (curStack.initCardCount > 0 && curStack.initCardCount - curStack.getCardCount() > 0)  
				{
					// This stack should have initial cards, and not enough have been dealt to it.
					Card c = drawCard(tempDeck);
					if (curStack.initFaceDown > 0 && curStack.initFaceDown - curStack.getCardCount() > 0)
						c.faceUp = false;
					else
						c.faceUp = true;
						
					curStack.PlaceCard(c);
				}
			}
		}
		
		List<CardStack> drawDecks = Stacks.stream().filter(s -> s.Type == CardStack.StackType.DRAW).collect(Collectors.toList());
		if (drawDecks.size() > 0)
		{
			drawDecks.get(0).PlaceCards(tempDeck);		
		}
		
	}
	
	private int remainingCardCount() 
	{
		return Stacks.stream()
				.mapToInt(x -> x.initCardCount - x.getCardCount())
				.sum();
	}
	
	private Card drawCard(List<Card> deck)
	{
		return deck.remove(0);
	}
	
	public String toString() 
	{
		String sRet = "";
		for(CardStack stack: this.Stacks)
			sRet = sRet + "\n" + stack.toString();
		
		return sRet;
	}
}
