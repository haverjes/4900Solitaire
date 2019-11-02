package UserLogin;
import java.io.*;
import java.util.*;

public class UserLogin {
	private String currentUser;
	private boolean hasSave;
	private String saveGameType;
	private File saveFile;
	private boolean showOnlyFavorites;
	private List<String> currentUserFavorites;
	
	private final static String userDataFile = "Users.csv";
	private final static String userDataDirectory = "UserData/";
		
	UserLogin(String username)
	{
		this.currentUser = username;
		
		String defaultInfo = ",false,null,null,none";
		File userDataCsv = new File(userDataDirectory+userDataFile);
		if(userDataCsv.isFile())
		{
			try {
				BufferedReader csvReader = new BufferedReader(new FileReader(userDataDirectory+userDataFile));
				boolean found = false;
				String line = csvReader.readLine();
				
				while (line != null & found == false) {
				    String[] lineValues = line.split(";");
				    // do something with the data
				    if(lineValues[0].equals(username)) {
				    	found = true;
				    	this.hasSave = Boolean.parseBoolean(lineValues[1]);
				    	this.saveGameType = lineValues[2];
				    	if(hasSave)
				    	{
				    		this.saveFile = new File("UserSaves/"+lineValues[3]);
				    	}
				    	else
				    	{
				    		this.saveFile = null;
				    	}
				    	if(!("none".equals(lineValues[4])))
				    	{
				    		this.currentUserFavorites = Arrays.asList(lineValues[4].split(","));
				    	}
				    	else
				    	{
				    		this.currentUserFavorites = new ArrayList<String>();
				    	}
				    }
				    csvReader.close();
				}
				if(!found)
				{
					BufferedWriter csvWriter = new BufferedWriter(new FileWriter(userDataCsv, true));
					csvWriter.newLine();
					csvWriter.write(username+defaultInfo);
					csvWriter.flush();
					csvWriter.close();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try {
				userDataCsv.createNewFile();
				BufferedWriter csvWriter = new BufferedWriter(new FileWriter(userDataCsv, true));
				csvWriter.newLine();
				csvWriter.write(username+defaultInfo);
				csvWriter.flush();
				csvWriter.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void UpdateUserDataFile()
	{
	    try {
	        // input the (modified) file content to the StringBuffer "input"
	        BufferedReader csvReader = new BufferedReader(new FileReader(userDataDirectory+userDataFile));
	        StringBuffer inputBuffer = new StringBuffer();
	        String line = csvReader.readLine();
	        
	        while (line != null) {
	        	// Logic for changing the one line here
	            inputBuffer.append(line);
	            inputBuffer.append('\n');
	        }
	        csvReader.close();

	        // write the new string with the replaced line OVER the same file
	        FileOutputStream fileOut = new FileOutputStream(userDataFile);
	        fileOut.write(inputBuffer.toString().getBytes());
	        fileOut.close();

	    } catch (Exception e) {
	        System.out.println("Problem reading file.");
	    }

	}
	
	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getCurrentGame() {
		return saveGameType;
	}

	public void setCurrentGame(String currentGame) {
		this.saveGameType = currentGame;
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

	public List<String> getCurrentUserFavorites() {
		return currentUserFavorites;
	}

	public void setCurrentUserFavorites(List<String> currentUserFavorites) {
		this.currentUserFavorites = currentUserFavorites;
	}
}
