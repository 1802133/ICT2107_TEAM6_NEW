package listener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTextArea;
import controller.Team6;

import json.json;

public class Listener {
	// HashMap for Ip Address & Group & Conversations
	json jSON = new json();
	public static String username;
	public final String groups = "groups";
	public final String users = "users";
	public final String convos = "convos";
	public final String profile = "profile";
	public final String address = "address";
	public final String grpTxt = "GRP:";
	public final String usrTxt = "USR:";
	public final String aduTxt = "ADU:";
	public final String rmuTxt = "RMU:";
	public final String chgTxt = "CHG:";
	public final String remTxt = "REM:";
	public final String sndTxt = "SND:";
	public final String uppTxt = "UPP:";
	public final String[] txtArray = {grpTxt, usrTxt, aduTxt, rmuTxt, chgTxt, remTxt, sndTxt, uppTxt};
	
	public static HashMap<String, String> nameIP = new HashMap<String, String>();
	public static HashMap<String, List<String>> userGrp = new HashMap<String, List<String>>();
	public static HashMap<String, List<String>> grpUser = new HashMap<String,List<String>>();
	public static HashMap<String, List<String>> grpConvo = new HashMap<String, List<String>>();
	public static HashMap<String, List<String>> userProf = new HashMap<String, List<String>>();
	
	public static List<String> userList = new ArrayList<String>();
	public static List<String> grpList = new ArrayList<String>();
	public MulticastSocket multicastSocket;
	public InetAddress multicastGroup;
	protected String temp;
	protected String[] tempList;
	// Well known Address
	public static final String IP = "230.1.1.1";
		
	public Listener(MulticastSocket mcs,InetAddress msg,String user){
		multicastSocket = mcs;
		multicastGroup = msg;
		username = user;
		
	}
	public String start(JTextArea textConvo) {
		try {
			multicastGroup = InetAddress.getByName(IP);
			multicastSocket = new MulticastSocket(6789);
			//Join
			multicastSocket.joinGroup(multicastGroup);
			//Send a joined message
			String message = username + " have joined";
			byte[] buf = message.getBytes();
			DatagramPacket dgpConnected = new DatagramPacket(buf,buf.length, multicastGroup,6789);
			multicastSocket.send(dgpConnected);
			//Create a new thread to keep listening for packets from the group
			new Thread(new Runnable() {
				@Override
				public void run() {
					byte buf1[] = new byte[1000];
					DatagramPacket dgpReceived = new DatagramPacket(buf1,buf1.length);
					while(true) {
						try {
							multicastSocket.receive(dgpReceived);
							byte[] receivedData = dgpReceived.getData();
							int length = dgpReceived.getLength();
							//Assumed we received string
							String msg = new String(receivedData, 0, length);
							String type = msg.substring(0,4);
							if(Arrays.asList(txtArray).contains(type)) {
								switchCase(type,msg,textConvo);
							}
						} catch (IOException ex) {
							ex.printStackTrace();
						}	
					}
				}
			}).start();
			return message;
	} catch (IOException ex) {
		ex.printStackTrace();
	}
		return username;
}
	protected void switchCase(String type,String msg, JTextArea textConvo) {
		// Add Group
		if(type.equals(grpTxt)) {
			System.out.println(msg);
			temp = msg.substring(4);
			tempList = temp.split(":");
			nameIP.put(tempList[0], tempList[1]);
			addHashGrp(type,tempList, textConvo);
			addHashUsr(type,tempList);
		}
		// Register new User
		if(type.equals(usrTxt)) {
			temp = msg.substring(4);
			tempList = temp.split(":");
			
			// To store path of image and bio
			List<String> userPage = new ArrayList<String>();
			// 0 is for profile picture, 1 is for bio
			
			userProf.put(tempList[0], userPage);
			textConvo.append(tempList[0] + " has been registered\n");
			//Adding online user to temp list
			for(String key: userProf.keySet()) {
				if(!userList.contains(key)) {
					userList.add(key);
				}
			}

			// Refresh user list for all clients
			Team6.userList.setListData(userList.toArray((new String[userList.size()])));
		}
		// Add User
		if(type.equals(aduTxt)) {
			temp = msg.substring(4);
			tempList = temp.split(":");
			addHashGrp(type,tempList, textConvo);
			addHashUsr(type,tempList);
			groupSettings();
			
		}
		// Remove User
		if(type.equals(rmuTxt)) {
			temp = msg.substring(4);
			tempList = temp.split(":");
			String user = tempList[1];
			String grp = tempList[0];
			removeUsr(grp,user);
		}
		// Change Group name
		if(type.equals(chgTxt)) {
			temp = msg.substring(4);
			tempList = temp.split(":");
			String newname = tempList[0];
			String oldname = tempList[1];
			changeGrp(newname,oldname);
		}
		// Leaving Group
		if(type.equals(remTxt)) {
			System.out.println("removing!");
			temp = msg.substring(4);
			tempList = temp.split(":");
			remHashGrp(tempList);
		}
		//Sending Message
		if(type.equals(sndTxt)) {
			System.out.println("sending!");
			temp = msg.substring(4);
			tempList = temp.split("SND:");
			msgGrp(tempList);
			
		}
		if(type.equals(uppTxt)) {
			System.out.println("Changing profile");
			temp = msg.substring(4);
			tempList = temp.split("~");
			changeProf(tempList);
		}
//		try {
//			json.JsonWriterController(convos, grpConvo, users, userGrp, groups, grpUser, address, nameIP, profile, userProf);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
	}
	protected void addHashGrp(String type, String[] grp, JTextArea textConvo) {
		//Create new group and automatically join
		if(type.equals("GRP:")) {
			//Getting list from username
			List<String> tempgrpList = userGrp.get(grp[2]);
			// Adding username with his respective groups that he is in
			if(tempgrpList == null) {
				tempgrpList = new ArrayList<String>();
				tempgrpList.add(grp[0]);
				userGrp.put(grp[2],tempgrpList);
			} else {
				tempgrpList.add(grp[0]);
			}
		}
		//Add new User
		if(type.equals("ADU:")) {
			List<String> groupList = userGrp.get(grp[1]);
			if(groupList == null) {
				groupList = new ArrayList<String>();
				groupList.add(grp[0]);
				userGrp.put(grp[1],groupList);
			} else {
				groupList.add(grp[0]);
			}
			// Refresh JList groups
			grpList = userGrp.get(grp[1]);
			if(grpList != null) {
				Team6.groupList.setListData(grpList.toArray((new String[grpList.size()])));
			}
//			textConvo.append(grp[1] + " has been added to group "+ grp[0]);
		}
		
		
	}
	protected void addHashUsr(String type, String[] usr) {
		if(type.equals("GRP:")) {
			// Getting userlist from groups
			List<String> usrList = grpUser.get(usr[0]);
			// Adding grps with list of users in it
			if(usrList == null) {
				usrList = new ArrayList<String>();
				usrList.add(usr[2]);
				grpUser.put(usr[0], usrList);
			} else {
				if(!usrList.contains(usr[2])) {
					usrList.add(usr[2]);
				}
				
			}
		}
		if(type.equals("ADU:")) {
			// Getting userlist from groups
			List<String> usrList = grpUser.get(usr[0]);
			// Adding grps with list of users in it
			if(usrList == null) {
				usrList = new ArrayList<String>();
				usrList.add(usr[1]);
				grpUser.put(usr[0], usrList);
			} else {
				usrList.add(usr[1]);
			}
		}
		
	}
	protected void removeUsr(String grp, String user) {
		//Remove user from userList based on group key
		List<String> usrList = grpUser.get(grp);
		usrList.remove(user);
		
		//Remove group from groupList based on user key
		List<String> grpList = userGrp.get(user);
		grpList.remove(grp);
	}
	protected void changeGrp(String newname, String oldname) {
		// TODO Auto-generated method stub
		/*
		 *  Remove keyvalue pair for old
		 *  Put keyvalue pair for new but with same ip
		 */
		String ip = nameIP.get(oldname);
		nameIP.remove(oldname);
		nameIP.put(newname, ip);
		/*
		 * Remove keyvalue pair for old
		 * Put keyvalue pair for new but with same usrList
		 */
		
		List<String> usrList = grpUser.get(oldname);
		grpUser.remove(oldname);
		grpUser.put(newname,usrList);
		
		/*
		 * Replace grpname for all users that belong to the group
		 */
		for(String user : usrList) {
			List<String> grpList = userGrp.get(user);
			int index = grpList.indexOf(oldname);
			grpList.set(index, newname);
		}
		/*
		 * Put all existing convos into new
		 */
		List<String> convoList = grpConvo.get(oldname);
		grpConvo.remove(oldname);
		grpConvo.put(newname,convoList);
		
		
	}
	public static void remHashGrp(String[] grp) {		
		//Remove user from userList based on group key
		List<String> usrList = grpUser.get(grp[1]);
		usrList.remove(grp[0]);
		
		//Remove group from groupList based on user key
		List<String> grpList = userGrp.get(grp[0]);
		grpList.remove(grp[1]);
	}
	
	public void msgGrp(String[] msgSend) {
		List<String> txtConvo = grpConvo.get(msgSend[0]);
		// Adding grps with concat msg
		if(txtConvo == null) {
			txtConvo = new ArrayList<String>();
			txtConvo.add(msgSend[1]);
			grpConvo.put(msgSend[0], txtConvo);
		} else {
			txtConvo.add(msgSend[1]);
		}
	}
	protected void changeProf(String[] profile) {
		List<String> imgBio = userProf.get(profile[0]);
		if(imgBio == null) {
			imgBio = new ArrayList<String>();
			imgBio.add(profile[1]);
			imgBio.add(profile[2]);
			userProf.put(profile[0], imgBio);
		} else {
			imgBio.add(profile[1]);
			imgBio.add(profile[2]);
		}
	}
	protected void groupSettings() {
		
		grpList = userGrp.get(Team6.userId);
		// These is for new users ina  group
		if(grpList != null) {
			for(String grp : grpList) {
				
				if(!Team6.checkGrp.containsKey(grp)) {
					Team6.checkGrp.put(grp,"0");
				}
//				if(!Team6.checkGrp.containsKey(grp) && templist.contains(Team6.userId)) {
//					Team6.checkGrp.put(grp,"3");
//				}
//				// If leave then added back/remove from group, update back to 0
//				else if(Team6.checkGrp.containsKey(grp) && !templist.contains(Team6.userId)) {
//					Team6.checkGrp.put(grp,"2");
//				}
				
			}
		}
		Team6.groupList.setListData(grpList.toArray((new String[grpList.size()])));
	}
	
	
}

