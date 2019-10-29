package Game;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class XML_LoaderTest1 {
	
	GameBoard testGB;
	
	@Test
	final void testLoadXML() {
		
		try 
		{
			testGB = XML_Loader.LoadXML(".\\Game\\Tests\\GameFileUnitTest_1.xml");
			System.out.println(testGB.toString());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			fail("Failed to load Gameboard");
		}
	}

	
	@Test
	final void testBasicMove() {
		try 
		{
			testGB = XML_Loader.LoadXML(".\\Game\\Tests\\GameUnitTest_Move1.xml");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			fail("Failed to load Gameboard");
		}
		System.out.println(testGB.toString());
		
		// first Tab 
		Card cCard = testGB.Stacks.get(1).getTopCard();
		assertTrue(testGB.ClickMove(cCard));
		
		
		cCard = testGB.Stacks.get(2).getTopCard();
		assertTrue(!testGB.ClickMove(cCard));
		

		System.out.println("----------------------------");
		System.out.println(testGB.toString());
	}
	
	@Test
	final void testBasicGroupMove() {
		// The only way to setup a group move is to define init cards for tabs and multiple moves 
		testGB = XML_Loader.LoadXML(".\\Game\\Tests\\GameUnitTest_Move2.xml");
		
		System.out.println(testGB.toString());
		
		// first Tab 
		Card cCard = testGB.Stacks.get(5).getTopCard();
		assertTrue(testGB.ClickMove(cCard));
		
		System.out.println(testGB.toString());
		
		cCard = testGB.Stacks.get(4).cards.get(0);
		assertTrue(testGB.ClickMove(cCard));
		

		System.out.println("----------------------------");
		System.out.println(testGB.toString());
	}
}
