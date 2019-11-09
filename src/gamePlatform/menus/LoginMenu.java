package gamePlatform.menus;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

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
					String userName = usernameEntry.getText();
					
					// Nullify current user in case there was one.
					MenuManager.currentUser = new UserLogin();
					
					File userSaveFile = new File(Paths.get(".",MenuManager.currentUser.userSettingsFolder,userName+".bin").toString());
					if (userSaveFile.exists())
					{
						try {
							FileInputStream inFileStream = new FileInputStream(userSaveFile);
							ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
						
							MenuManager.currentUser = (UserLogin)  inObjectStream.readObject();
							inObjectStream.close();
							inFileStream.close();
							System.out.println("User data load successful for " + MenuManager.currentUser.getUserName());
						}
						catch(IOException e) {
							System.out.println("Error: Problem occured while reading saved user data.");
						}
						catch(ClassNotFoundException e) {
							System.out.println("Error: Problem occured while casting user data to object.");
						}
					}
					else
					{
						MenuManager.currentUser = new UserLogin(userName);
						
						try {
							FileOutputStream outFileStream = new FileOutputStream(userSaveFile);
							ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
							
							outObjectStream.writeObject(MenuManager.currentUser);
							
							outObjectStream.close();
							outFileStream.close();
							System.out.println("Successfully wrote user data for user " 
									+ MenuManager.currentUser.getUserName());
						}
						catch(IOException e) {
							System.out.println("Error: Problem occured writing user data to object.");
						}
					}
					
					usernameEntry.setText("");
					MenuManager.switchMenu(MenuManager.PLAT_MENU);
				}
				else {
					System.out.println("Enter a username");
					usernameEntry.setText("");
				}
			}
		});
	}
}
