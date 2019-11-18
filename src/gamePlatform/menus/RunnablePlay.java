package gamePlatform.menus;


import java.io.File;
import java.util.concurrent.Callable;

import gameInterface.GameStatus;
import xmlGameEngine.XMLSolitaireEngine;

public class RunnablePlay implements  Callable<GameStatus>  
{
	public File file;
	public GameStatus Status;
	
	
	public RunnablePlay(File inFile) {
		file = inFile;
	}
	
	@Override
	public GameStatus call() throws Exception {
		// TODO Auto-generated method stub
		
		Status = XMLSolitaireEngine.play(file);
		return Status;
		
	}
	
	public synchronized GameStatus get() 
	          throws InterruptedException 
	    { 
	        while (Status.getGameStatusFlag() < 0) 
	            wait(100); 
	  
	        return Status; 
	    } 

}
