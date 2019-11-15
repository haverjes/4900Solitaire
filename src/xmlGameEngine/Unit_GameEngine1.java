package xmlGameEngine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Unit_GameEngine1 {

	@Test
	final void test() {
		
		SolitaireEngine.XMLFile = ".\\Game\\Tests\\GameUnitTest_Move1.xml";
		
		SolitaireEngine.main(null);
		
	}

}
