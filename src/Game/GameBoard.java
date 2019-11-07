package Game;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;  

public class GameBoard implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<CardStack> Stacks;
	public List<MoveRule> Rules;
	public List<Card> Cards;
	public String rulesText;
	public String GameTitle;
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
		System.out.println("Attempting to move " + card.toString() + " to " + destStack.toString());
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
		if (card.stackCallBack.getLockCards())
			return false;
		
		List<CardStack.StackType> validDestStacks = movableStackTypes();
		
		for(CardStack cardStack: this.Stacks)
		{
			if (validDestStacks.contains(cardStack.Type) && DragDropMove(card, cardStack))
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
			for (int suitIndex = 0; suitIndex < 4; suitIndex++)
			{
				for (int rankIndex = 1; rankIndex < 14; rankIndex++) 
				{
					this.Cards.add(new Card(rankIndex, suitIndex, totalCards));
					totalCards++;
				}
			}
		}
	}
	
	protected List<CardStack.StackType> movableStackTypes() 
	{
		List<CardStack.StackType> olRet = new ArrayList<CardStack.StackType>();
		for(MoveRule rule: this.Rules)
		{
			if (!olRet.contains(rule.destStackType))
				olRet.add(rule.destStackType);
		}
		return olRet;
		
	}
	public void dealCards() 
	{
		// make a copy of the main Cards list to be shrunk and shuffled.
		List<Card> tempDeck = new ArrayList<Card>(Cards);
		
		for (CardStack stack: Stacks)
		{
			if (stack.Type == CardStack.StackType.DRAW)
			{
				//List<CardStack> stacks = Stacks.stream().filter(s -> s.id == stack.drawToStack).collect(Collectors.toList());
				//stack.wasteStack = stacks.get(0);
				String wasteID = stack.drawToStack;
				for (CardStack wstack: Stacks)
				{
					//if (wasteID == wstack.id) 
					if (wasteID.compareTo(wstack.id) == 0)
					{
						stack.wasteStack = wstack;
						break;
					}
				}
				
				
			}
			
			if (stack.initialCard != null && stack.initialCard != "")
			{
				// Get the rank and Suit from the initCard string.
				Card.Suit initSuit = Card.convertSuit(stack.initialCard.toCharArray()[stack.initialCard.length() - 1]);
				int initRank = Card.convertRankInt(stack.initialCard.substring(0, stack.initialCard.length() - 1));
				
				// Find cards that match that rank and suit
				List<Card> initCards = tempDeck.stream()
						.filter(c -> c.rank == initRank && c.suit == initSuit)
						.collect(Collectors.toList());
				
				// Put the first math onto the stack and remove it from 
				if (initCards.size() > 0)
				{
					Card initCard = initCards.get(0);
					initCard.faceUp = true;
					stack.PlaceCard(initCard);
					tempDeck.remove(initCard);
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

					curStack.dealCard(drawCard(tempDeck));
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

	public boolean checkVictory() {
		
		// Take a count of all Tableaus that have any cards.  If Count > 0, game not over yet.
		return this.Stacks.stream().filter(s -> s.Type == CardStack.StackType.TAB && s.getCardCount() > 0).count() == 0;
	}
	
	public void redraw() 
	{
		for (CardStack stack: this.Stacks)
		{
			stack.repaint(); 
		}
	}
}
