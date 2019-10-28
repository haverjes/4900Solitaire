package Game;
// CardStack Interface
import java.util.*; 


interface ICardStack 
{
	List<Card> getCards();
	void setCards(List<Card> a);
	int getxPos();
	void setxPos(int a);
	int getyPos();
	void setyPos(int a);
	CardStack.StackShape getShape();
	void setShape(CardStack.StackShape a);
	boolean getLockCards();
	void setLockCards(boolean a);
}