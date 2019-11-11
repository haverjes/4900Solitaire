package gamePlatform.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gameInterface.GameStatus;
import gamePlatform.main.Launcher;

public class PlatformMenu extends Menu{
	
	//private Screen screen;
	//private JPanel main;
	private JButton gameSelect, resumeGame, showStatistics, logout, quit;
	
	public PlatformMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		gameSelect = new JButton("Select Game");
		//gameSelect.setBounds(100,100,100,100);
		contentPanel.add(gameSelect);
		
		resumeGame = new JButton("Play/Resume");
		//resumeGame.setBounds(100,300,100,100);
		contentPanel.add(resumeGame);
		
		showStatistics = new JButton("Show Statistics");
		//showStatistics.setBounds(100, 500, 100, 100);
		contentPanel.add(showStatistics);
		
		logout = new JButton("Logout");
		contentPanel.add(logout);
		
		quit = new JButton("Quit");
		//quit.setBounds(100, 900, 100, 100);
		contentPanel.add(quit);
	}
	
	@Override
	protected void setActions() {
		gameSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.switchMenu(MenuManager.SELECT_GAME);
			}
		});
		
		resumeGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String userName = MenuManager.currentUser.getUserName();
				String selectedGame = MenuManager.currentUser.getSelectedGame();
				String userSaveDirectory = Paths.get(".","UserSaveFolders",userName).toString();
				
				
				if (true)
				{
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


				}
				else
				{
					
				}
			}
		});
		
		showStatistics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.switchMenu(MenuManager.SHOW_STATS);
			}
		});
		
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.currentUser = null;
				MenuManager.switchMenu(MenuManager.START_MENU);
			}
		});
		
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}
	
	
}
