package gamePlatform.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import binaryStar.XML_Loader;

public class SelectGameMenu extends Menu{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<JButton> buttons;
	private List<String> games;
	
	public SelectGameMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		
		// TODO: Remove hard coded Engines path.
		games = XML_Loader.getXMLFiles();
		
		buttons = new LinkedList<JButton>();
		
		if (games != null) {
		    for (String game : games) {
		      String gameName = game.substring(0, game.lastIndexOf('.'));
		      gameName = String.join(" ", gameName.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"));

		      // System.out.println(gameName);
		      JButton button = new JButton(gameName);
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
			String gameXML = games.get(i);
			
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String gameName = gameXML.substring(0, gameXML.lastIndexOf('.'));
				    
					MenuManager.currentUser.setSelectedGame(gameName);
					((PlatformMenu) MenuManager.MENUS[MenuManager.PLAT_MENU]).updatePlayResumeText();
					MenuManager.switchMenu(MenuManager.PLAT_MENU);				
				}
			});
		}

	}
}
