package groupSolitaireEngines;

import java.io.File;

import binaryStar.*;
import gameInterface.GameStatus;
import gameInterface.SolitaireEngine;

public class BonanzaCreek implements SolitaireEngine
{
	public GameStatus play(File inFile)
	{
		String file = "BonanzaCreek.xml";
		BinaryStar.initGame(file);
		return new GameStatus();
	}

}
