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
	
	public final String userSaveFolders = Paths.get(".","UserSaveFolders").toString();
	public final String userSettingsFolder = Paths.get(".","UserSettings").toString();
	public final String userStatsFolders = Paths.get(".","UserStatistics").toString();
		
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
}
