/**
 * @author Nathaniel Littleton
 */
package gamePlatform.menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gamePlatform.main.Launcher;

public class Menu extends JPanel {
	
	//public static final Dimension BUTTON_SIZE = new Dimension(100,50);
	
	protected JPanel contentPanel;
	protected JButton back;
	protected JLabel title;
	
	public Menu() {
		initComponents();
		setActions();
		setBackground(new Color(0, 180, 0));
	}
	
	
	/**
	 * Initializes all swing components of the menu.  Each subclass is responsible for it own components.
	 * Since most menus use a back button, it has been initialized here, but not added to the contentPanel.
	 */
	protected void initComponents() {
		this.setLayout(new BorderLayout());
		
		title = new JLabel("Solitaire", JLabel.CENTER);
		title.setFont(new Font("Helvetica", Font.PLAIN, 100));
		title.setPreferredSize(new Dimension(200,200));
		this.add(title, BorderLayout.PAGE_START);
		
		contentPanel = new JPanel();
		contentPanel.setBackground(new Color(0,0,0,0));
		this.add(contentPanel, BorderLayout.CENTER);
		
		back = new JButton("Back");
		
	}
	
	/**
	 * Adds ActionListeners for all appropriate components.
	 * Since most of the menus use a back button, it has had its function set here.
	 */
	protected void setActions() {
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MenuManager.switchMenu(MenuManager.prevMenu);
			}
		});
	}
	
}
