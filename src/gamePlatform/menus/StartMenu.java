package gamePlatform.menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gamePlatform.main.Launcher;

public class StartMenu extends Menu {
	
	private JPanel buttonPanel;
	private JLabel title;
	private JButton login, playOffline, quit;
	
	public StartMenu() {
		super();
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		login = new JButton("Login");
		//login.setMinimumSize(new Dimension(100,50));
		//login.setMaximumSize(new Dimension(100,50));
		login.setAlignmentX(CENTER_ALIGNMENT);
		contentPanel.add(login);
		
		contentPanel.add(Box.createRigidArea(new Dimension(0,15))); //adds empty space between the buttons
		
		playOffline = new JButton("Play Anonymously (Disables Some Features)");
		//playOffline.setMinimumSize(new Dimension(100,50));
		//playOffline.setMaximumSize(new Dimension(100,50));
		playOffline.setAlignmentX(CENTER_ALIGNMENT);
		contentPanel.add(playOffline);
		
		contentPanel.add(Box.createRigidArea(new Dimension(0,15))); //adds empty space between the buttons
		
		quit = new JButton("Quit");
		//quit.setMinimumSize(new Dimension(100,50));
		//quit.setMaximumSize(new Dimension(100,50));
		quit.setAlignmentX(CENTER_ALIGNMENT);
		contentPanel.add(quit);
	}

	@Override
	protected void setActions() {
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.switchMenu(MenuManager.LOGIN_MENU);
			}
		});
		
		playOffline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.switchMenu(MenuManager.SELECT_GAME);
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
