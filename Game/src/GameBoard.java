import java.util.*;  

public class GameBoard 
{
	public List<CardStack> Stacks;
	public List<MoveRule> Rules;
	public List<Card> Cards;
	
	
	
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
		if (!card.stackCallBack.getLockCards)
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
	
	
	
}
