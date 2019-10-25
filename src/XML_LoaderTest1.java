import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class XML_LoaderTest1 {
	
	GameBoard testGB;
	
	@Test
	final void testLoadXML() {
		testGB = XML_Loader.LoadXML(".\\Game\\Tests\\GameFileUnitTest_1.xml");
		
		System.out.print(testGB.toString());
		
		fail("Not yet implemented"); // TODO
	}

}
