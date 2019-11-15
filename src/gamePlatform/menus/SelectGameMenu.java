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
import javax.swing.JLabel;
import javax.swing.JList;

<<<<<<< HEAD
import gameInterface.GameStatus;
=======
import gamePlatform.main.Launcher;
import xmlGameEngine.XMLSolitaireEngine;
>>>>>>> refs/heads/New

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
		
		
		// TODO: Remove hard coded Engines path.
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
		
<<<<<<< HEAD
		System.out.println(buttons.isEmpty());
		for(int i = 0; i < buttons.size(); i++)
		{
			JButton button = buttons.get(i);
			File classFile = games[i];
			
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String gameName = Paths.get(classFile.toString()).getFileName().toString();
					gameName = gameName.substring(0, gameName.lastIndexOf('.'));
					
					MenuManager.currentUser.setSelectedGame(gameName);
					// TODO: MenuManager Switch back to main menu.
				}
			});
		}
=======
		playBinStar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.mainScreen.setContentPane(XMLSolitaireEngine.getTable());
				//Launcher.mainScreen.setTitle("Binary Star");
				String file = "BinaryStarTest.xml";
				XMLSolitaireEngine.initGame(file);
			}
		});
>>>>>>> refs/heads/New
	}
}
