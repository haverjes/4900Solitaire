package gamePlatform.menus;

import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
		JScrollPane scrollPane = new JScrollPane(statsTable);
		contentPanel.add(scrollPane);
		
		back.setAlignmentX(CENTER_ALIGNMENT);
		contentPanel.add(back);
	}

	@Override
	protected void setActions() {
		super.setActions();
	}
	
	public void buildStatsTable() {
		List<Stats> userStats = MenuManager.currentUser.getUserStats();
		String[][] tableStrings = new String[userStats.size()][];
		
		int i = 0;
		//TODO: Build table of user stats.
		for (Stats statsObj : userStats) {
			System.out.println(i);
			tableStrings[i] = statsObj.toStringArray();
			i++;
		}
		
		DefaultTableModel data = new DefaultTableModel(tableStrings, Stats.headerStringArray());
		statsTable.setModel(data);
	}

}
