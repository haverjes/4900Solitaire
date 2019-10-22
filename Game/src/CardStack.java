public class CardStack 
{
	public List<Card> Cards;
	public int yPos;
	public int xPos;
	
	public StackShape Shape;
	public boolean lockCards;
	
	public String initialCard; 
	
	public List<Card> Stack.TakeCard(Card card) 
	{
		int nIndex = this.Cards.indexOf(card);
		List<Card> retCardList = new ArrayList<Card>();
		int nLastIndex = this.Cards.size() - 1;
		
		
		for (int i = nIndex; i < this.Cards.size(); i++)
		{
			retCardList.add(this.Cards.get(i));
		}
		
		// Remove the cards from the list.
		this.Cards.removeRange(nIndex, nLastIndex)
		
		// Ensure topcard is face up, if stack not empty
		if (this.Cards.size() > 0) 
			this.Cards.get(nLastIndex).faceUp = true;
		
		return retCardList;
	}

	// Add the list of carsd in order and update each card's Callback member.
	public void Stack.PlaceCards(List<Card> cardList) 
	{
		for (Card card: cardList)
		{
			this.Cards.add(card);
			card.StackCallBack = this;
		}
	}
	
	Card getTopCard() 
	{
		return this.Cards.get(this.Cards.size() - 1);
	}
	
	public int getCardIndex(Card card) 
	{
		return nIndex = this.Cards.indexOf(card);
	}
}