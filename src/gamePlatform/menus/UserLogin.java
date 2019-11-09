package gamePlatform.menus;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class UserLogin implements java.io.Serializable
{
	private String userName;
	private boolean hasSave;
	private String saveGameType;
	private File saveFile;
	private boolean showOnlyFavorites;
	private List<String> userFavorites;
		
	UserLogin()
	{
		this.userName = null;
		this.hasSave = false;
		this.saveGameType = null;
		this.saveFile = null;
		this.showOnlyFavorites = false;
		this.userFavorites = new LinkedList<>();		
	}
	
	UserLogin(String username)
	{
		this.userName = username;
		this.hasSave = false;
		this.saveGameType = null;
		this.saveFile = null;
		this.showOnlyFavorites = false;
		this.userFavorites = new LinkedList<>();
	}
		
	public String getUserName() {
		return userName;
	}

	public void setUserName(String currentUser) {
		this.userName = currentUser;
	}

	public boolean isHasSave() {
		return hasSave;
	}

	public void setHasSave(boolean hasSave) {
		this.hasSave = hasSave;
	}
	
	public String getSaveGameType() {
		return saveGameType;
	}
	
	public void setSaveGameType(String saveGameType) {
		this.saveGameType = saveGameType;
	}
	
	public File getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(File saveFile) {
		this.saveFile = saveFile;
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
