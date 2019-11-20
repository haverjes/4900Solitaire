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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import binaryStar.XML_Loader;

public class SettingsMenu extends Menu {
	
	private JCheckBox showFavorites;
	private List<JCheckBox> checkBoxes;
	private List<String> games;
	protected static int togglesPerRow = 1;
	
	public SettingsMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		games = XML_Loader.getXMLFiles();
						
		JPanel settings = new JPanel();
		settings.setLayout(new GridBagLayout());
		GridBagConstraints settingsConstraints = new GridBagConstraints();
		settingsConstraints.anchor = GridBagConstraints.CENTER;
		settingsConstraints.insets = new Insets(0,0,0,0);
		settingsConstraints.gridx = 0;
		settingsConstraints.gridy = 0;
				
		JPanel favoritesToggles = new JPanel();
		favoritesToggles.setLayout(new GridBagLayout());
		GridBagConstraints favoritesConstraints = new GridBagConstraints();
		favoritesConstraints.gridx = 0;
		favoritesConstraints.gridy = 0;
		favoritesConstraints.anchor = GridBagConstraints.LINE_START;
		showFavorites = new JCheckBox("Show Only Favorites");
		favoritesToggles.add(showFavorites,favoritesConstraints);

		JLabel favoritesTitle = new JLabel("Select Your Favorites:");
		favoritesConstraints.anchor = GridBagConstraints.CENTER;
		favoritesConstraints.insets = new Insets(20,0,0,0);
		favoritesConstraints.gridy++;
		favoritesToggles.add(favoritesTitle,favoritesConstraints);

		favoritesConstraints.anchor = GridBagConstraints.LINE_START;
		favoritesConstraints.insets = new Insets(0,0,0,0);
		favoritesConstraints.gridy++;
		
		checkBoxes = new LinkedList<JCheckBox>();

		if (games != null) {
		    for (String game : games) {
		      String gameName = game.substring(0, game.lastIndexOf('.'));
		      gameName = String.join(" ", gameName.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"));

		      // System.out.println(gameName);
		      JCheckBox checkBox = new JCheckBox(gameName);
		      
		      checkBoxes.add(checkBox);
		      favoritesToggles.add(checkBox, favoritesConstraints);
		      
		      if((favoritesConstraints.gridx+1) % togglesPerRow == 0)
		      {
		    	  favoritesConstraints.gridx = 0;
		    	  favoritesConstraints.gridy++;
		      }
		      else
		      {
		    	  favoritesConstraints.gridx++;
		      }  
		    }
		}
		
		settingsConstraints.gridy++;
		settingsConstraints.insets = new Insets(0,0,0,0);
		settings.add(favoritesToggles, settingsConstraints);
		
		settingsConstraints.gridy++;
		settingsConstraints.insets = new Insets(20,0,0,0);
		settings.add(back, settingsConstraints);
		settings.setBackground(new Color(0, 180, 0));
		
		contentPanel.setLayout(new FlowLayout());
		contentPanel.add(settings);
	}
	
	@Override
	protected void setActions() {
		super.setActions();
		
		showFavorites.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MenuManager.currentUser.setShowOnlyFavorites(showFavorites.isSelected());
				MenuManager.currentUser.saveUserSettings();
			}
		});

		for(int i = 0; i < checkBoxes.size(); i++)
		{
			JCheckBox checkBox = checkBoxes.get(i);
			String gameXML = games.get(i);
			
			checkBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String gameName = gameXML.substring(0, gameXML.lastIndexOf('.'));
									    
					if (checkBox.isSelected() && !MenuManager.currentUser.isFavorite(gameName))
					{
						MenuManager.currentUser.addUserFavorite(gameName);
					}
					else if (!checkBox.isSelected() && MenuManager.currentUser.isFavorite(gameName))
					{
						MenuManager.currentUser.removeUserFavorite(gameName);
					}
					
					MenuManager.currentUser.saveUserSettings();
				}
			});
		}

	}
	
	public void updateSelections()
	{
		showFavorites.setSelected(MenuManager.currentUser.isShowOnlyFavorites());
		
		for(int i = 0; i < checkBoxes.size(); i++)
		{
			JCheckBox checkBox = checkBoxes.get(i);
			String gameXML = games.get(i);
			String gameName = gameXML.substring(0, gameXML.lastIndexOf('.'));
			
			checkBox.setSelected(MenuManager.currentUser.isFavorite(gameName));
		}
	}


}
