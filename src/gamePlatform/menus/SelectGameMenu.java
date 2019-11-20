package gamePlatform.menus;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

import binaryStar.XML_Loader;

public class SelectGameMenu extends Menu{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<JButton> buttons;
	private List<String> games;
	protected static int buttonsPerRow = 1;
	
	public SelectGameMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		
		// TODO: Remove hard coded Engines path.
		games = XML_Loader.getXMLFiles();
		
		int numGames = games.size();
		
		JPanel gridPanel = new JPanel();
		gridPanel.setBackground(new Color(0, 180, 0));
		GridBagLayout selectGameLayout = new GridBagLayout();
		GridBagConstraints selectGameConstraints = new GridBagConstraints();
		gridPanel.setLayout(selectGameLayout);
		selectGameConstraints.fill = GridBagConstraints.HORIZONTAL;
		selectGameConstraints.anchor = GridBagConstraints.PAGE_START;
		selectGameConstraints.gridx = 0;
		selectGameConstraints.gridy = 0;
		selectGameConstraints.weightx = 0;
		selectGameConstraints.insets = new Insets(0,300,0,300);
		
		buttons = new LinkedList<JButton>();
		
		if (games != null) {
		    for (String game : games) {
		      String gameName = game.substring(0, game.lastIndexOf('.'));
		      gameName = String.join(" ", gameName.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"));

		      // System.out.println(gameName);
		      JButton button = new JButton(gameName);
		      
		      buttons.add(button);
		      gridPanel.add(button, selectGameConstraints);
		      
		      if((selectGameConstraints.gridx+1) % buttonsPerRow == 0)
		      {
		    	  selectGameConstraints.gridx = 0;
		    	  selectGameConstraints.gridy++;
		      }
		      else
		      {
		    	  selectGameConstraints.gridx++;
		      }
		    }
		  }
		
		selectGameConstraints.gridx = (int) Math.ceil(buttonsPerRow/2);
		selectGameConstraints.gridy++;
		
		if (selectGameConstraints.gridx % 2 == 0) {
			selectGameConstraints.gridwidth = 2;
		}
		
		selectGameConstraints.fill = GridBagConstraints.NONE;
		selectGameConstraints.insets = new Insets(50,350,10,350);
		
		gridPanel.add(back, selectGameConstraints);
		
		contentPanel.setLayout(new FlowLayout());
		contentPanel.add(gridPanel);
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
					MenuManager.currentUser.saveUserSettings();
					((PlatformMenu) MenuManager.MENUS[MenuManager.PLAT_MENU]).updatePlayResumeText();
					MenuManager.switchMenu(MenuManager.PLAT_MENU);
				}
			});
		}

	}
}
