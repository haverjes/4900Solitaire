package groupSolitaireEngines;

import java.io.File;

import gameInterface.GameStatus;
import gameInterface.SolitaireEngine;
import xmlGameEngine.*;

public class BonanzaCreek implements SolitaireEngine
{
	public GameStatus play(File inFile)
	{
		String file = "BonanzaCreek.xml";
		XMLSolitaireEngine.initGame(file);
		return new GameStatus();
	}

}
