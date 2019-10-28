package Game;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class XML_LoaderTest1 {
	
	GameBoard testGB;
	
	@Test
	final void testLoadXML() {
		
		try {
			testGB = XML_Loader.LoadXML(".\\Game\\Tests\\GameFileUnitTest_1.xml");
			
			System.out.println(testGB.toString());

		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	final void testBasicMove() {
		testGB = XML_Loader.LoadXML(".\\Game\\Tests\\GameUnitTest_Move1.xml");
		
		System.out.println(testGB.toString());
		
		Card cCard = testGB.Stacks.get(1).getTopCard();
		
		assertTrue(testGB.ClickMove(cCard));
		
		
		cCard = testGB.Stacks.get(2).getTopCard();
		assertTrue(!testGB.ClickMove(cCard));
		
		//fail("Not yet implemented"); // TODO
		System.out.println("----------------------------");
		System.out.println(testGB.toString());
	}
}
