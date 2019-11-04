package gamePlatform.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gamePlatform.main.Launcher;

public class PlatformMenu extends Menu{
	
	//private Screen screen;
	//private JPanel main;
	private JButton gameSelect, resumeGame, showStatistics, logout, quit;
	
	public PlatformMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		gameSelect = new JButton("Select Game");
		//gameSelect.setBounds(100,100,100,100);
		contentPanel.add(gameSelect);
		
		resumeGame = new JButton("Resume Game");
		//resumeGame.setBounds(100,300,100,100);
		contentPanel.add(resumeGame);
		
		showStatistics = new JButton("Show Statistics");
		//showStatistics.setBounds(100, 500, 100, 100);
		contentPanel.add(showStatistics);
		
		logout = new JButton("Logout");
		contentPanel.add(logout);
		
		quit = new JButton("Quit");
		//quit.setBounds(100, 900, 100, 100);
		contentPanel.add(quit);
	}
	
	@Override
	protected void setActions() {
		gameSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.switchMenu(MenuManager.SELECT_GAME);
			}
		});
		
		resumeGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//create menu for the a list of saved games
			}
		});
		
		showStatistics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.switchMenu(MenuManager.SHOW_STATS);
			}
		});
		
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.switchMenu(MenuManager.START_MENU);
			}
		});
		
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}
	
	
}
