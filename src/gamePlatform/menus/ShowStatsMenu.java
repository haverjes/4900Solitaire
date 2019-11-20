package gamePlatform.menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
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
		
		contentPanel.setLayout(new FlowLayout());
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridBagLayout());
		GridBagConstraints tablePanelConstraints = new GridBagConstraints();
		tablePanel.setBackground(new Color(0, 180, 0));
		tablePanelConstraints.gridx = 0;
		tablePanelConstraints.gridy = 0;
		tablePanelConstraints.weightx = 1;
		tablePanelConstraints.weighty = 1;
		tablePanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		statsTable = new JTable();
		statsTable.getTableHeader().setBackground(Color.lightGray);
		statsTable.setAlignmentX(CENTER_ALIGNMENT);
		statsTable.setAlignmentY(CENTER_ALIGNMENT);
		statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		statsTable.setBorder(BorderFactory.createCompoundBorder());
		statsTable.setForeground(Color.BLACK);
		statsTable.setGridColor(Color.gray);
		statsTable.setShowGrid(true);
        statsTable.setDefaultEditor(Object.class, null);
		JScrollPane scrollPane = new JScrollPane(statsTable);
		scrollPane.setBackground(new Color(0, 180, 0));
		tablePanel.add(scrollPane,tablePanelConstraints);
		
		tablePanelConstraints.insets = new Insets(10,0,0,0);
		tablePanelConstraints.gridy = 0;
		tablePanelConstraints.gridy = 1;
		tablePanelConstraints.fill = GridBagConstraints.NONE;
		tablePanel.add(back,tablePanelConstraints);
		
		contentPanel.add(tablePanel);
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
