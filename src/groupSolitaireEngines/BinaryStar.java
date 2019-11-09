package groupSolitaireEngines;

import java.io.File;

import gameInterface.GameStatus;
import gameInterface.SolitaireEngine;
import xmlGameEngine.*;

public class BinaryStar implements SolitaireEngine
{
	public GameStatus play(File inFile)
	{
		String file = "BinaryStarTest.xml";
		XMLSolitaireEngine.initGame(file);
		return new GameStatus();
	}

}
