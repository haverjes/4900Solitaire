// CardStack Interface

enum StackShape 
{
	STACK, FANDOWN
}

interface ICardStack 
{
	List<Card> cards;
	int xPos;
	int yPos;
	StackShape shape;
	boolean lockCards;
}