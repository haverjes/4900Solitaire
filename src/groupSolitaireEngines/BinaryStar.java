package groupSolitaireEngines;

import java.io.File;

import gameInterface.GameStatus;
import gameInterface.SolitaireEngine;
import xmlGameEngine.*;

public class BinaryStar implements SolitaireEngine
{
//	public GameStatus play()
//	{
//		// TODO: Create new file
//		return play(newFile);
//		
//	}
	
	public GameStatus play(File inFile)
	{
		GameStatus myStatus = new GameStatus();
		
		myStatus.setGameSaveFile(inFile);
		
		
		
		if (inFile != null && inFile.length() > 0)
		{
			String file = "BinaryStarTest.xml";
			XMLSolitaireEngine.initGame(file);
			XMLSolitaireEngine.LoadGame(inFile.getAbsolutePath());
			
		}
		else
		{
			String file = "BinaryStarTest.xml";
			XMLSolitaireEngine.initGame(file);
		}
		return new GameStatus();
	}

}
