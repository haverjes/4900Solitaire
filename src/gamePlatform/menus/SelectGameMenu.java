package gamePlatform.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;

import gameInterface.GameStatus;

public class SelectGameMenu extends Menu{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<JButton> buttons;
	private File[] games;
	
	public SelectGameMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		File gamesDirectory = new File(Paths.get(".","src","groupSolitaireEngines").toString());
		games = gamesDirectory.listFiles();
		
		buttons = new LinkedList<JButton>();
		
		if (games != null) {
		    for (File game : games) {
		      String gameName = Paths.get(game.toString()).getFileName().toString();
		      System.out.println(gameName);
		      JButton button = new JButton(gameName.substring(0, gameName.lastIndexOf('.')));
		      buttons.add(button);
		      contentPanel.add(button);
		    }
		  } else {
		    // Handle the case where dir is not really a directory.
		    // Checking dir.isDirectory() above would not be sufficient
		    // to avoid race conditions with another process that deletes
		    // directories.
		  }	
		
		contentPanel.add(back);
	}
	
	@Override
	protected void setActions() {
		super.setActions();
		
		System.out.println(buttons.isEmpty());
		for(int i = 0; i < buttons.size(); i++)
		{
			JButton button = buttons.get(i);
			File classFile = games[i];
			
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						URL url = classFile.toURI().toURL();
						URL[] urls = new URL[] {url};
						URLClassLoader cl = new URLClassLoader(urls);
					
						String gameName = Paths.get(classFile.toString()).getFileName().toString();
						gameName = gameName.substring(0, gameName.lastIndexOf('.'));
						
						MenuManager.currentUser.setSaveGameType(gameName);
						
						String userSaveDirectoryPath = Paths.get(".","UserSaveFolders",MenuManager.currentUser.getUserName()).toString();
						File userSaveDirectory = new File(userSaveDirectoryPath);
						
						if(!userSaveDirectory.exists())
						{
							if(!userSaveDirectory.isDirectory())
							{
								userSaveDirectory.delete();
							}
						
							userSaveDirectory.mkdir();
						}
						File inFile = new File(
								Paths.get(userSaveDirectory.toString(),
										MenuManager.currentUser.getUserName() + ".save").toString());
						
						// Load Classes and start the game here
						// MenuManager.lastGameStatus = engine.play(inFile);
						MenuManager.lastGameStatus = new GameStatus();
						
						cl.close();
						
						int statusFlag = MenuManager.lastGameStatus.getGameStatusFlag();
								
						if (statusFlag >= 0 && statusFlag <= 2)
						{
							String userStatsDirectoryPath = Paths.get(".","UserStatistics",MenuManager.currentUser.getUserName()).toString();
							File userStatsDirectory = new File(userStatsDirectoryPath);
							if(!userSaveDirectory.exists())
							{
								if(!userSaveDirectory.isDirectory())
								{
									userSaveDirectory.delete();
								}
								userSaveDirectory.mkdir();
							}
						
							File statsFile = new File(
								Paths.get(userStatsDirectory.toString(),
										MenuManager.currentUser.getUserName()+ "_"
										+ MenuManager.currentUser.getSaveGameType() + ".stats").toString());
						
							if(statsFile.exists() && !statsFile.isFile())
							{
								statsFile.delete();
							}
						
							Stats userStats;
						
							if(statsFile.exists())
							{
								try {
									FileInputStream inFileStream = new FileInputStream(statsFile);
									ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
								
									userStats = (Stats) inObjectStream.readObject();
								
									inObjectStream.close();
									inFileStream.close();
									
									System.out.println("User stats load successful for " + MenuManager.currentUser.getUserName());
								}
								catch(ClassNotFoundException e1) {
									userStats = new Stats();
									System.out.println("Error: Problem occured while casting user data to object.");
								}
							}
							else 
							{
								userStats = new Stats();
							}
							
							boolean win;
						
							if (statusFlag == 0 || statusFlag == 1)
							{
								win = false;
							}
							else
							{
								win = true;
							}

							userStats.updateStats(win,
											MenuManager.lastGameStatus.getGameScore(),
											MenuManager.lastGameStatus.getGameTime());
						
							FileOutputStream outFileStream = new FileOutputStream(statsFile);
							ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
						
							outObjectStream.writeObject(MenuManager.currentUser);
						
							outObjectStream.close();
							outFileStream.close();
						}
					}catch(IOException e1) {
						System.out.println("Error Loading Class File.");
					}
				}
			});
		}
	}
}
