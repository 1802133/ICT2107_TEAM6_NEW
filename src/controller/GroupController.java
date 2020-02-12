package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.Iterator;

import variables.constantVariables;
import view.ErrorMessage;

public class GroupController {
	/*
	 * availAddress{IPAddress, true/false} true -> available, false -> unavailable
	 * groupsAddress{groupName: IPAddress}
	 * groupsUsers{groupName:List<Users>} Users -> users in the group
	 */
	protected static HashMap<String, Boolean> availAddress = new HashMap<String, Boolean>();
	protected static HashMap<String, String> groupsAddress = new HashMap<String, String>();
	protected static HashMap<String, List<String>> groupUsers = new HashMap<String, List<String>>();
	
	public GroupController() {
		//populates IPAddresses 
		populateIPAddress();
	}
	public GroupController(HashMap<String, Boolean> availAddress, HashMap<String, String> groupsAddress, HashMap<String,List<String>> groupUsers) {
		GroupController.availAddress = availAddress;
		GroupController.groupsAddress = groupsAddress;
		GroupController.groupUsers = groupUsers;
	}
	protected static void addGroup(String groupName, List<String> onlineUsers) {
		//TODO check where to get onlineUsers!!!
		//TODO implement code when new Group is created
		if (groupsAddress.containsKey(groupName)) {
			//group already exist
			ErrorMessage.errorBox("Group name already exist. Please choose a different name!", "Group Name Used");
		} else {
			//group dont exist
			//look for free IP
			String result = lookForAvailIP();
			if (result != null) {
				//AvailIP exists
				groupsAddress.put(groupName, result);
				availAddress.replace(result, false);
				//Adding all users to group
				groupUsers.put(groupName, onlineUsers);
			}else {
				//no available IP
				//message full IP
				ErrorMessage.errorBox("There are no more available address. Please leave a group!", "Addresses Full");
			}
		}
	}
	
	protected static void editGroup(String groupNameOld, String groupNameNew) {
		//TODO implement code when Group is edited
		String ipAddress = groupsAddress.get(groupNameOld);
		groupsAddress.remove(groupNameOld);
		groupsAddress.put(groupNameNew, ipAddress);
		List<String> users = groupUsers.get(groupNameOld);
		groupUsers.remove(groupNameOld);
		groupUsers.put(groupNameNew, users);
	}
	
	protected static void removeGroup(String groupName) {
		//TODO implement code when Group is removed or removed from group
		String ipAddress = groupsAddress.get(groupName);
		groupsAddress.remove(groupName);
		groupUsers.remove(groupName);
		availAddress.replace(ipAddress, true);
	}
	protected static void selectedGroup() {
		//TODO implement code when Group is selected
		
	}
	protected static void joinGroup(HashMap<String, Boolean> ipAddressNew, HashMap<String, String> groupAddressNew, HashMap<String, List<String>> groupUsersNew) {
		//TODO implement code when user added to a group
		//call when user receives invite
		for (String key: groupAddressNew.keySet()) {
			groupsAddress.putIfAbsent(key, groupAddressNew.get(key));
		}
		//assuming adder will first add user to his hashmap then send the updated hashmap 
		for (String key: groupUsersNew.keySet()) {
			groupUsers.putIfAbsent(key, groupUsers.get(key));
		}
		for (String key: ipAddressNew.keySet()) {
			availAddress.replace(key, false);
		}
	}
	protected static void addUserToGroup(String groupName, String userId) {
		//TODO implement code when adding User to Group
		List<String> users = groupUsers.get(groupName);
		users.add(userId);
		groupUsers.replace(groupName, users);
	}
	protected static void removeUserFromGroup(String groupName, String userId) {
		//TODO implement code when User is removed from Group
		List<String> users = groupUsers.get(groupName);
		users.remove(userId);
		groupUsers.replace(groupName, users);
	}
	protected static void refreshGroupList() {
		//TODO implement code to reflect changes made to Group
		//do we need this?
	}
	
	private static String lookForAvailIP() {
		for (Entry<String, Boolean> entry: availAddress.entrySet()) {
			if(entry.getValue()) {
				//true means IP is free
				return entry.getKey();
			}
		}
		return null;
	}
	
	private static void populateIPAddress() {
		for (int i = 1; i<256; i++) {
			for (int ii = 2; ii<256; ii++) {
				String tempAddress = constantVariables.getIpaddress() + Integer.toString(i) + "." + Integer.toString(ii);
				availAddress.put(tempAddress, true);
			}
		}
	}
}
