//XML_Loader.java
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
			MakeCardStack(xStack);
			
		}
		
	}
	
	
	public static CardStack MakeCardStack(Element xStack) 
	{
		CardStack newStack = new CardStack();
		
		string sShape = xStack.getAttribute("shape");
		if (sShape == "FanDown") 
			newStack.Type = CardStack.StackShape.FANDOWN;
		else
			newStack.Type = CardStack.StackShape.STACK;
		
		// Continue for each attribute
		
		
	}
}