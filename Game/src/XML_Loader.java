//XML_Loader.java
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class XML_Loader 
{
	public static GameBoard LoadXML(String file) 
	{
		GameBoard retGB = new GameBoard();
		
		File xmlFile = new File(file);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document xDoc = builder.parse(xmlFile);
		
		// Generate cards
		// Build stacks
		
		Element root = xDoc.getDocumentElement();
		
		retGB.generateCards(root.getElementsByTagName("Board")[0].getElementsByTagName("Decks").getAttribute("numDecks"));
		
		Element[] xStackDefs = root.getElementsByTagName("Board")[0].getElementsByTagName("Stack");
		for (Element xStack: xStackDefs )
		{
			retGB.Stacks.add(MakeCardStack(xStack));
		}
		
		Element[] xMoveDefs = root.getElementsByTagName("MoveRules")[0].getElementsByTagName("MoveRule");
		for (Element xRule: xMoveDefs )
		{
			retGB.Rules.add(MakeNewRule(xRule));
		}
		
		return retGB;
	}
	
	
	public static CardStack MakeCardStack(Element xStack) 
	{
		CardStack newStack = new CardStack();
		
		String sShape = xStack.getAttribute("shape");
		if (sShape == "FanDown") 
			newStack.Shape = CardStack.StackShape.FANDOWN;
		else
			newStack.Shape = CardStack.StackShape.STACK;
		
		newStack.Type = CardStack.getStackType(xStack.getAttribute("type"));
		
		newStack.xPos = String.valueOf(xStack.getAttribute("xpos"));
		newStack.yPos = String.valueOf(xStack.getAttribute("ypos"));
		
		
		if (xStack.hasAttribute("initialCard")) 
			newStack.initialCard = xStack.getAttribute("initialCard");
		
		if (xStack.hasAttribute("firstCard")) 
		{
			char[] sFirstCard = xStack.getAttribute("firstCard").toCharArray();
			newStack.firstCardRank = Card.convertRankInt(sFirstCard[0]);
			newStack.firstCardSuit = Card.convertSuit(sFirstCard[1]);
		}
		
		if (xStack.hasAttribute("cardCount")) 
		{
			newStack.cardCount = String.valueOf(xStack.getAttribute("cardCount"));
		}
		
		if (xStack.hasAttribute("cardLimit")) 
		{
			newStack.cardLimit = String.valueOf(xStack.getAttribute("cardLimit"));
		}
		
		
		
		if (xStack.hasAttribute("initFaceDown")) 
		{
			newStack.initFaceDown = String.valueOf(xStack.getAttribute("initFaceDown"));
		}
		// Continue for each attribute
		
		return newStack;
	}
	
	public static MoveRule MakeNewRule(Element xRule) 
	{
		boolean allowGroup = (String.valueOf(xRule.getAttribute("allowGroup")) > 0) ? true : false;
		boolean rankRollover = (String.valueOf(xRule.getAttribute("rankRollover")) > 0) ? true : false;
		MoveRule newRule = new MoveRule(xRule.getAttribute("destType"),
			xRule.getAttribute("cardSequence"),
			xRule.getAttribute("cardSequence"),
			allowGroup, 
			rankRollover);
		
		return newRule;
	}
}