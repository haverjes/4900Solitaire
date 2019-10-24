import java.util.*;  

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
	
}
