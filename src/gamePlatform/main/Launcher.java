package gamePlatform.main;

import java.awt.EventQueue;
import javax.swing.JFrame;
import binaryStar.BinaryStar;
import gamePlatform.menus.MenuManager;

public class Launcher {
	
	public static JFrame mainScreen;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					mainScreen = new JFrame();
					mainScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					mainScreen.setSize(BinaryStar.TABLE_WIDTH + 100 , BinaryStar.TABLE_HEIGHT + 100);
					mainScreen.setTitle("Solitaire");
					mainScreen.setVisible(true);
					MenuManager.switchMenu(MenuManager.START_MENU);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}
}
