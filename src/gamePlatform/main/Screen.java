package gamePlatform.main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Game.SolitaireEngine;
import gamePlatform.menus.Menu;

public class Screen extends JFrame{
	
	public Screen() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(SolitaireEngine.TABLE_WIDTH + 100 , SolitaireEngine.TABLE_HEIGHT + 100);
		this.setTitle("Solitaire");
		setVisible(true);
	}
	
	public void changePanel(JPanel newPanel) {
		this.setContentPane(newPanel);
		this.setVisible(true);
	}

}
