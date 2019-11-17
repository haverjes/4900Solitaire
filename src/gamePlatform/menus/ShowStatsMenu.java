package gamePlatform.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import gamePlatform.main.Launcher;

public class ShowStatsMenu extends Menu{

	private JTable statsTable;
	
	public ShowStatsMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		//List<Stats> userStats = MenuManager.currentUser.getUserStats();
		
		//TODO: Build table of user stats.
		
		
		statsTable = new JTable(4,6);
		statsTable.setAlignmentX(CENTER_ALIGNMENT);
		statsTable.setAlignmentY(CENTER_ALIGNMENT);
		contentPanel.add(statsTable);
		
		back.setAlignmentX(CENTER_ALIGNMENT);
		contentPanel.add(back);
	}

	@Override
	protected void setActions() {
		super.setActions();
	}

}
