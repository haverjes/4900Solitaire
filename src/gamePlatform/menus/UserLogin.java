package gamePlatform.menus;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class UserLogin implements java.io.Serializable
{
	private String userName;
	private String selectedGame;
	private boolean showOnlyFavorites;
	private List<String> userFavorites;
	
	public static final String userSaveFolders = Paths.get(".","UserSaveFolders").toString();
	public static final String userSettingsFolder = Paths.get(".","UserSettings").toString();
	public static final String userStatsFolders = Paths.get(".","UserStatistics").toString();
	
	
	UserLogin()
	{
		this.userName = null;
		this.selectedGame = null;
		this.showOnlyFavorites = false;
		this.userFavorites = new LinkedList<>();		
	}
	
	UserLogin(String username)
	{
		this.userName = username;
		this.selectedGame = null;
		this.showOnlyFavorites = false;
		this.userFavorites = new LinkedList<>();
	}
		
	public String getUserName() {
		return userName;
	}

	public void setUserName(String currentUser) {
		this.userName = currentUser;
	}
	
	public String getSelectedGame() {
		return selectedGame;
	}
	
	public void setSelectedGame(String selectedGame) {
		this.selectedGame = selectedGame;
	}
	
	public boolean isShowOnlyFavorites() {
		return showOnlyFavorites;
	}

	public void setShowOnlyFavorites(boolean showOnlyFavorites) {
		this.showOnlyFavorites = showOnlyFavorites;
	}

	public List<String> getUserFavorites() {
		return userFavorites;
	}

	public void setUserFavorites(List<String> newFavorites) {
		this.userFavorites = newFavorites;
	}
	
	public void addUserFavorite(String newFavorite) {
		this.userFavorites.add(newFavorite);
	}
	
	public void removeUserFavorite(String notFavorite) {
		this.userFavorites.remove(notFavorite);
	}
	
	public boolean isFavorite(String mightBeFavorite) {
		return this.userFavorites.contains(mightBeFavorite);
	}
	
	public String getUserSaveFolder() { return Paths.get(userSaveFolders,userName).toString(); }
	
	public String getUserStatsFolder() { return Paths.get(userStatsFolders,userName).toString(); }
	
	public List<File> getUserSaves() 
	{
		File folder = new File(getUserSaveFolder());
		File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".save"));
		return Arrays.asList(listOfFiles);
	}
	
	public List<Stats> getUserStats()
	{
		return getUserStats(false);
	}
	
	
	public List<Stats> getUserStats(boolean onlyFavs)
	{
		File folder = new File(getUserStatsFolder());
		File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".stats"));
		List<Stats> retStats = new ArrayList<Stats>();
		for(File curfile: listOfFiles)
		{
			
			try {
				FileInputStream inFileStream = new FileInputStream(curfile);
				ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
			
				Stats curStats = (Stats) inObjectStream.readObject();
				
				inObjectStream.close();
				inFileStream.close();
				
				if (!onlyFavs || userFavorites.contains(curStats.getGameType()))
				{
					retStats.add(curStats);
				}
			}
			catch(ClassNotFoundException | IOException e1) {
				
				System.out.println("Error: Problem occured while casting user data to object.");
			}
		}

		return retStats;
	}
	
	public Stats getGameStats(String gameType) 
	{
		for(Stats curStats: getUserStats())
		{
			if (curStats.getGameType() == gameType)
				return curStats;
		}
		return null;
	}
	
	public void saveUserSettings()
	{
		File userSaveFile = new File(Paths.get(UserLogin.userSettingsFolder,userName+".bin").toString());
		try {
			FileOutputStream outFileStream = new FileOutputStream(userSaveFile);
			ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
		
			outObjectStream.writeObject(this);
		
			outObjectStream.close();
			outFileStream.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}

	}
		
}
