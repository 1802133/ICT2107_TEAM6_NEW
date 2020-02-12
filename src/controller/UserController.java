package controller;

import java.util.HashMap;
import java.util.List;

import view.ErrorMessage;

public class UserController {
	protected static HashMap<String, List<String>> usersProfile = new HashMap<String, List<String>>();
	//TODO implement mac address
	public UserController() {
		
	}
	public UserController(HashMap<String, List<String>> usersProfile) {
		UserController.usersProfile = usersProfile;
	}
	protected static void registerUser(String userName) {
		//TODO implement code to register new User
		if (checkUsernameIsValid(userName)) {
			//valid username
			if (checkUsernameIsNotTaken(userName)) {
				//username is not taken
				usersProfile.put(userName, null);
			}else {
				//username is taken
				ErrorMessage.errorBox("Username in use. Please use another Username", "Used Username");
			}
		}else {
			//username is invalid
			ErrorMessage.errorBox("Username is invalid. Username should at most be 8 characters with no spaces and numbers!", "Invalid Username");
		}
	}
	protected static void renameUser(String oldUsername, String userNameNew) {
		//TODO implement code to renameUser
		if (checkUsernameIsValid(userNameNew)) {
			//valid username
			if (checkUsernameIsNotTaken(userNameNew)) {
				//username is not taken
				List<String> usersInfo = usersProfile.get(oldUsername);
				usersProfile.remove(oldUsername);
				usersProfile.put(userNameNew, usersInfo);
			}else {
				//username is taken
				ErrorMessage.errorBox("Username in use. Please use another Username", "Used Username");
			}
		}else {
			//username is invalid
			ErrorMessage.errorBox("Username is invalid. Username should at most be 8 characters with no spaces and numbers!", "Invalid Username");
		}
	}
	protected static boolean checkUsernameIsValid(String userName) {
		//TODO implement code to check if Username is valid
		
		return false;
	}
	protected static boolean checkUsernameIsNotTaken(String userName) {
		//TODO implement code to check if Username is Taken
		return false;
	}
	
}
