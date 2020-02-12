package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageController {
	protected static HashMap<String, List<String>> messages = new HashMap<String, List<String>>();

	public MessageController() {
		
	}
	
	public MessageController(HashMap<String, List<String>> messages) {
		MessageController.messages = messages;
	}
	
	protected static void sendMessage(String groupName, String msg) {
		//TODO implement code when message is sent
		List<String> messageList = new ArrayList<String>();
		if (messages.containsKey(groupName)) {
			messageList = messages.get(groupName);
			messageList.add(msg);
			messages.replace(groupName, messageList);
		} else {
			messageList.add(msg);
			messages.put(groupName, messageList);
		}
	}
}
