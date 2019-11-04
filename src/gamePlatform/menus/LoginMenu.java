package gamePlatform.menus;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gamePlatform.main.Launcher;

public class LoginMenu extends Menu {
	
	private JPanel entryFields;
	private JButton login;
	private String input;
	private JLabel username, invalidInputPrompt;
	private JTextField usernameEntry;
		
	public LoginMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		//contentPanel.setLayout(new GridLayout(2,3));
		username = new JLabel("Username: ");
		contentPanel.add(username);
		
		usernameEntry = new JTextField(15);
		contentPanel.add(usernameEntry);
		
		login = new JButton("Login");
		contentPanel.add(login);
		
		invalidInputPrompt = new JLabel();
		contentPanel.add(invalidInputPrompt);
		
		contentPanel.add(back);
	}
	
	@Override
	protected void setActions() {
		super.setActions();
		
		//For now, it just checks if the textfield is empty
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!usernameEntry.getText().isEmpty()) {
					MenuManager.switchMenu(MenuManager.PLAT_MENU);
					usernameEntry.setText("");
				}
				else {
					System.out.println("Enter a username");
					usernameEntry.setText("");
				}
			}
		});
	}
}
