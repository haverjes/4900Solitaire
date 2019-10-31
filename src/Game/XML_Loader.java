package Game;
//XML_Loader.java
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;	

public class XML_Loader 
{
	public static GameBoard LoadXML(String file) 
	{
		GameBoard retGB = new GameBoard();
		
		try {
			File xmlFile = new File(file);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xDoc = builder.parse(xmlFile);
			
			
			
			xDoc.getDocumentElement().normalize();
			Element root = xDoc.getDocumentElement();
			
			Element xboard = (Element) root.getElementsByTagName("Board").item(0);
			Element xrules = (Element) root.getElementsByTagName("MoveRules").item(0);
			
			// Set the number of card decks to use and generate the card list.
			Element xDeck = (Element) xboard.getElementsByTagName("Deck").item(0);
			retGB.generateCards(Integer.parseInt(xDeck.getAttribute("numDecks")));
			
			// Build stacks
			NodeList xStackDefs = xboard.getElementsByTagName("Stack");
			for (int i = 0; i < xStackDefs.getLength(); i++ )
			{
				Node xStack = xStackDefs.item(i);
				retGB.Stacks.add(MakeCardStack((Element)xStack));
			}
			
			// Get the game rules (CDATA, so requires extra functions because Java handle XML ssssooooo well.
			Element xTextRule = (Element) xboard.getElementsByTagName("Rules").item(0);
			retGB.rulesText = getCharacterDataFromElement(xTextRule);
			
			// Build rules
			NodeList xMoveDefs = xrules.getElementsByTagName("MoveRule");
			for (int i = 0; i < xMoveDefs.getLength(); i++ )
			{
				Node xRule = xMoveDefs.item(i);
				retGB.Rules.add(MakeNewRule((Element)xRule));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		retGB.dealCards();
		
		return retGB;
	}
	
	public static String getCharacterDataFromElement(Element e) {
		if (e == null)
			return "";
		
	    NodeList list = e.getChildNodes();
	    String data;

	    for(int index = 0; index < list.getLength(); index++){
	        if(list.item(index) instanceof CharacterData){
	            CharacterData child = (CharacterData) list.item(index);
	            data = child.getData();

	            if(data != null && data.trim().length() > 0)
	                return child.getData();
	        }
	    }
	    return "";
	}
	
	public static CardStack MakeCardStack(Element xStack) 
	{
		CardStack newStack = new CardStack();
		
//		String sShape = xStack.getAttribute("shape");
//		if (sShape == "FanDown") 
//			newStack.Shape = CardStack.StackShape.FANDOWN;
//		else
//			newStack.Shape = CardStack.StackShape.STACK;
		newStack.Shape = CardStack.getStackShape(xStack.getAttribute("shape"));
		newStack.Type = CardStack.getStackType(xStack.getAttribute("type"));
		
		newStack.xPos = Integer.parseInt(xStack.getAttribute("xpos"));
		newStack.yPos = Integer.parseInt(xStack.getAttribute("ypos"));
		
		
		if (xStack.hasAttribute("initialCard")) 
			newStack.initialCard = xStack.getAttribute("initialCard");
		
		if (xStack.hasAttribute("firstCard")) 
		{
			String sFirstCard = xStack.getAttribute("firstCard");
			newStack.firstCardRank = Card.convertRankInt(sFirstCard.substring(0, sFirstCard.length() - 1));
			newStack.firstCardSuit = Card.convertSuit(sFirstCard.substring(sFirstCard.length() - 1));
		}
		
		if (xStack.hasAttribute("cardCount")) 
		{
			newStack.initCardCount = Integer.parseInt(xStack.getAttribute("cardCount"));
		}
		
		if (xStack.hasAttribute("cardLimit")) 
		{
			newStack.cardLimit = Integer.parseInt(xStack.getAttribute("cardLimit"));
		}
		
		
		
		if (xStack.hasAttribute("initFaceDown")) 
		{
			newStack.initFaceDown = Integer.parseInt(xStack.getAttribute("initFaceDown"));
		}
		// Continue for each attribute
		
		return newStack;
	}
	
	public static MoveRule MakeNewRule(Element xRule) 
	{
		boolean allowGroup = (xRule.hasAttribute("allowGroup") && Integer.parseInt(xRule.getAttribute("allowGroup")) > 0) ;
		boolean rankRollover = (xRule.hasAttribute("rankRollover") && Integer.parseInt(xRule.getAttribute("rankRollover")) > 0);
		MoveRule newRule = new MoveRule(xRule.getAttribute("destType"),
			xRule.getAttribute("cardSequence"),
			xRule.getAttribute("suitRequirement"),
			allowGroup, 
			rankRollover);
		
		return newRule;
	}
}