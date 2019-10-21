public class GameBoard 
{
	
	// TODO:  Handle Group Movement -- DONE
	protected void MoveCard(Card card, CardStack, destStack) 
	{
		List<Card> cardsToMove = card.StackCallBack.TakeCard(card);
		destStack.PlaceCards(cardsToMove);
		
		//card.StackCallBack = destStack;  // Moved to Stack.PlaceCards()
	}
	
	
	public boolean DragDropMove(Card card, CardStack, destStack) 
	{
		if (card.StackCallBack.LockCards)
			return false;
			
		for (MoveRule rule: this.MoveRules) 
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
		if (!card.StackCallBack.LockCards)
			return false;
		
		for(Cardstack cardStack: this.CardStacks)
		{
			if (DragDropMove(card, cardStack))
			{
				return true;
			}
		}
		return false;
	}
	
	
	
}
