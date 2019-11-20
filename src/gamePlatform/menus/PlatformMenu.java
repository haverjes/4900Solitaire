package gamePlatform.menus;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import binaryStar.BinaryStar;
import gamePlatform.main.Launcher;

public class PlatformMenu extends Menu{
	
	private JButton gameSelect, resumeGame, showStatistics, logout, quit;
	protected static String defaultGame = "BinaryStar";
	private Timer t;
	
	public PlatformMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		JPanel gridPanel = new JPanel();
		
		GridBagLayout platformMenuLayout = new GridBagLayout();
		GridBagConstraints platformMenuConstraints = new GridBagConstraints();
		gridPanel.setLayout(platformMenuLayout);
		platformMenuConstraints.fill = GridBagConstraints.HORIZONTAL;
		platformMenuConstraints.anchor = GridBagConstraints.PAGE_START;
		platformMenuConstraints.gridx = 0;
		platformMenuConstraints.gridy = 0;
		platformMenuConstraints.weighty = 0;
		platformMenuConstraints.insets = new Insets(0,300,0,300);
		
		gameSelect = new JButton("Select Game");
		//gameSelect.setBounds(100,100,100,100);
		gridPanel.add(gameSelect, platformMenuConstraints);
		
		resumeGame = new JButton("Play Default");
		//resumeGame.setBounds(100,300,100,100);
		platformMenuConstraints.gridx = 0;
		platformMenuConstraints.gridy = 1;
		gridPanel.add(resumeGame, platformMenuConstraints);
		
		showStatistics = new JButton("Show Statistics");
		//showStatistics.setBounds(100, 500, 100, 100);
		platformMenuConstraints.gridx = 0;
		platformMenuConstraints.gridy = 2;
		gridPanel.add(showStatistics, platformMenuConstraints);
		
		logout = new JButton("Logout");
		platformMenuConstraints.gridx = 0;
		platformMenuConstraints.gridy = 3;
		gridPanel.add(logout, platformMenuConstraints);
		
		quit = new JButton("Quit");
		//quit.setBounds(100, 900, 100, 100);
		platformMenuConstraints.gridx = 0;
		platformMenuConstraints.gridy = 5;
		platformMenuConstraints.fill = GridBagConstraints.NONE;
		platformMenuConstraints.insets = new Insets(50,300,0,300);		
		gridPanel.add(quit, platformMenuConstraints);
		gridPanel.setBackground(new Color(0, 180, 0));

		contentPanel.setLayout(new FlowLayout());
		contentPanel.add(gridPanel);
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
				String userSaveDirectory = Paths.get(UserLogin.userSaveFolders,userName).toString();
								
				if (selectedGame == null)
				{
					MenuManager.currentUser.setSelectedGame(defaultGame);
					selectedGame = MenuManager.currentUser.getSelectedGame();
				}
				
				File inFile = new File(
						Paths.get(userSaveDirectory.toString(),
								userName + "_" + selectedGame + ".save").toString());
					
				// Load Classes and start the game here
				// File engineLocation = new File(SolitairePlatformConstants.enginePckgDir);
				Launcher.mainScreen.setVisible(false);
				
				// Start running the game engine
				// Magic happens, God knows (maybe) what it looks like.
				BinaryStar engine = new BinaryStar();
								
				MenuManager.lastGameStatus = engine.play(inFile, (selectedGame + ".xml"));
				
				
				t = new Timer(500, new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				    	if (MenuManager.lastGameStatus != null && MenuManager.lastGameStatus.getGameStatusFlag() >= 0)
						{
				    		onGameEnd();
						}
				    }
				});
				t.start();
				
			}
		});
		
		showStatistics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				((ShowStatsMenu) MenuManager.MENUS[MenuManager.SHOW_STATS]).buildStatsTable();
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
	
	public void onGameEnd()
	{
		Launcher.mainScreen.setVisible(true);
		int statusFlag = MenuManager.lastGameStatus.getGameStatusFlag();
					
		if (statusFlag >= 0 && statusFlag <= 2)
		{
			String userStatsDirectoryPath = Paths.get(".","UserStatistics",MenuManager.currentUser.getUserName()).toString();
			File userStatsDirectory = new File(userStatsDirectoryPath);
			
			File statsFile = new File(
					Paths.get(userStatsDirectory.toString(),
							MenuManager.currentUser.getUserName()+ "_"
							+ MenuManager.currentUser.getSelectedGame() + ".stats").toString());
			
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
						
						// System.out.println("User stats load successful for " + MenuManager.currentUser.getUserName());
					}
					catch(ClassNotFoundException | IOException e1) {
						userStats = new Stats();
						System.out.println("Error: Problem occured while casting user data to object.");
					}
				}
				else 
				{
					userStats = new Stats();
					
					userStats.setGameType(String.join(" ", 
							MenuManager.currentUser.getSelectedGame().split(
									"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])")));
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
				try {
					FileOutputStream outFileStream = new FileOutputStream(statsFile);
					ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
					
					outObjectStream.writeObject(userStats);
					
					outObjectStream.close();
					outFileStream.close();
				}
				catch(Exception e1)
				{
					System.out.println("Error writing stats file.");
				}
				
				MenuManager.lastGameStatus.getGameSaveFile().delete();
				MenuManager.lastGameStatus = null;
			}
			else if (statusFlag > 2)
			{
				
			}
			else // statusFlag < 0
			{
			
			}
			((PlatformMenu) MenuManager.MENUS[MenuManager.PLAT_MENU]).updatePlayResumeText();
			t.stop();
	}
	
	public void updatePlayResumeText()
	{
		String userSaveDirectory = MenuManager.currentUser.getUserSaveFolder();
		String userName = MenuManager.currentUser.getUserName();
		String selectedGame = MenuManager.currentUser.getSelectedGame();
		
		if (selectedGame == null)
		{
			resumeGame.setText("Play Default");
		}
		else
		{
			File saveFile = new File(
					Paths.get(userSaveDirectory,
							userName + "_" + selectedGame + ".save").toString());
					
			String gameNameForButt = String.join(" ", selectedGame.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"));
			// hehe this name
			
			if (saveFile.exists())
			{
				resumeGame.setText("Resume " + gameNameForButt);
			}
			else
			{
				resumeGame.setText("Play " + gameNameForButt);
			}
		}
	}
}
