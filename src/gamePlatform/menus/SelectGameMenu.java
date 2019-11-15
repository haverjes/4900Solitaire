package gamePlatform.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import gamePlatform.main.Launcher;
import xmlGameEngine.SolitaireEngine;

public class SelectGameMenu extends Menu{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton playBinStar;
	
	public SelectGameMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		playBinStar = new JButton("Play BinaryStar");
		contentPanel.add(playBinStar);
		
		contentPanel.add(back);
	}
	
	@Override
	protected void setActions() {
		super.setActions();
		
		playBinStar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.mainScreen.setContentPane(SolitaireEngine.getTable());
				//Launcher.mainScreen.setTitle("Binary Star");
				String file = "BinaryStarTest.xml";
				SolitaireEngine.initGame(file);
			}
		});
	}
	
}
