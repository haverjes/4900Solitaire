package gamePlatform.menus;

import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JTable;

public class ShowStatsMenu extends Menu{

	private JTable statsTable;
	
	public ShowStatsMenu() {
		super();
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
				
		statsTable = new JTable();
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
	
	public void buildStatsTable() {
		List<Stats> userStats = MenuManager.currentUser.getUserStats();
		String[][] tableStrings = {};
		
		int i = 0;
		//TODO: Build table of user stats.
		for (Stats statsObj : userStats) {
			tableStrings[i] = statsObj.toStringArray();
		}
		
		statsTable = new JTable(tableStrings, Stats.headerStringArray());
	}

}
