package gamePlatform.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;

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
		      // System.out.println(gameName);
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
					MenuManager.switchMenu(MenuManager.PLAT_MENU);				}
			});
		}

	}
}
