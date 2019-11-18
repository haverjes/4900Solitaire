package gamePlatform.menus;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gameInterface.GameStatus;
import gamePlatform.main.Launcher;

public class MenuManager {
	
	public static final int START_MENU = 0;
	public static final int PLAT_MENU = 1;
	public static final int SELECT_GAME = 2;
	public static final int SHOW_STATS = 3;
	public static final int LOGIN_MENU = 4;
	public static final int RESUME_GAME = 5;
	public static final Menu[] MENUS = {new StartMenu(), new PlatformMenu(), new SelectGameMenu(), new ShowStatsMenu(), 
			new LoginMenu()};
	
	public static int currMenu;
	public static int prevMenu;
	public static UserLogin currentUser = null;
	public static GameStatus lastGameStatus = null;
	
	public static Menu getCurrentMenu() {
		return MENUS[currMenu];
	}
	
	public static void switchMenu(int newMenu) {
		setPrevMenu(currMenu);
		setCurrMenu(newMenu);
	}
	
	public static void setCurrMenu(int newMenu) {
		currMenu = newMenu;
		Launcher.mainScreen.setContentPane(getCurrentMenu());
		Launcher.mainScreen.setVisible(true);
	}
	
	public static void setPrevMenu(int newPrevMenu) {
		prevMenu = newPrevMenu;
	}
}
