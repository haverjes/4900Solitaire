package gamePlatform.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import Game.SolitaireEngine;
import gamePlatform.main.Launcher;

public class SelectGameMenu extends Menu{
	
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
				Launcher.mainScreen.setTitle("Binary Star");
				SolitaireEngine.XMLFile = ".\\\\Game\\\\Tests\\\\BinaryStarTest.xml";
				String[] args = {};
				SolitaireEngine.main(args);
			}
		});
	}
	
}
