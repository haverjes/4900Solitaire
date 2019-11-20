package gamePlatform.menus;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Stats implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String gameType;
	private int wins;
	private int losses;
	private double avgScore;
	private int highScore;
	private int fastestTime;
	
	public Stats()
	{
		this.wins = 0;
		this.losses = 0;
		this.avgScore = 0;
		this.highScore = 0;
		this.fastestTime = Integer.MAX_VALUE;
	}
	
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public double getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(int avgScore) {
		this.avgScore = avgScore;
	}
	public int getHighScore() {
		return highScore;
	}
	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
	public int getFastestTime() {
		return fastestTime;
	}
	public void setFastestTime(int fastestTime) {
		this.fastestTime = fastestTime;
	}
	
	public void updateStats(boolean win, int newScore, int newTime)
	{
		int total = wins+losses;
		avgScore = (avgScore*total + newScore)/(total+1);
			
		if(newScore > highScore) {
			highScore = newScore;
		}
		
		if (win) {
			wins +=1;
			if(newTime < fastestTime) {
				fastestTime = newTime;
			}
		}
		else {
			losses +=1;
		}
	}
	public double getWinRate()
	{
		return ((double) wins)/((double) losses);
	}
	
	
	// Table strings
	public String[] toStringArray()
	{
		String fastestTimeString = null;
		if (this.fastestTime == Integer.MAX_VALUE) {
			fastestTimeString = "";
		}
		else
		{
			fastestTimeString = String.valueOf(this.fastestTime);
		}
		
		String gameTypeFormatted = String.join(
				" ",this.gameType.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])"));
		DecimalFormat avgFormat = new DecimalFormat("0.00");
		return new String[] {gameTypeFormatted, String.valueOf(avgFormat.format(this.avgScore)), 
				fastestTimeString, String.valueOf(this.highScore), 
				String.valueOf(this.wins), String.valueOf(this.losses)};
	}
	
	public static String[] headerStringArray()
	{
		return new String[] {"Game Type", "Avg Score", 
				"Fastest Time", "High Score", 
				"Wins", "Losses"};
	}
	
}
