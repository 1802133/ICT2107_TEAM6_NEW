package controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import json.json;
import listener.Listener;

import java.awt.FlowLayout;

// Package importsimport java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Team6 extends JFrame {
	
	MulticastSocket multicastSocket = null;
	InetAddress multicastGroup = null;
	// User Character Limit
	static final int userLIMIT = 8;
	
	// Temporary variables for userId, group, IP Address
	public static String userId = "You";
	String group, ip,activeGrp,activeUsr;
	private int x, y;
	Boolean grpSelected;
	String textDisplay;
	//Temp for user
	public static HashMap<String,String> checkGrp = new HashMap<String, String>();
	
	/*
	 * 1)Multicast Address of 230.1.1.1
	 * 	- User Registration id <8 characters, case sensitive, no space, cannot begin with number
	 * 2) User Profile is optional
	 * 3) Group Messaging
	 * 	- Group assigned a name by the user
	 * 	- No duplicate names
	 * 	- Assigned a new multicast address with 230.1.X.Y
	 * 4) Communication
	 * 	- User may have multiple groups
	 * 	- He can select the group he wants and display the chat
	 * 	- Shows Active
	 * 5) Membership
	 * 	- Add/Delete members
	 * 	- Change name of the group
	 * 	- Users can leave but can only be added by a member
	 * 	- Latest 10 messages will be shown within the group when user first joins
	 * 6) Storage
	 * 	- When user quit, all info (userid, profile, groupid, membership) saved to a local file
	 * 	- Retrieved when the user starts the program again (EXTENSION)
	 * 7) User Profile (EXTENSION) 
	 * 8) Extensions
	 * 	- Add to report if any additional features made
	 * 
	 */
	private JFrame f;
	private JTextArea textConvo;
	private JTextField txtRegister, txtSend, txtMgmt;
	private JButton btnRegister, btnCreate, btnEdit, btnDelete, btnSend,btnRefreshUsers, btnRefreshGrps,btnCreateGrp;
	private JPanel grpMgmt, users, grps, convo;
	private JScrollPane scroll;
	public static JList<Object> groupList, userList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Team6 frame = new Team6();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Team6() {
		f = new JFrame("WhatsChat");
		
		//1st Row
		btnRegister = new JButton("Register User");
		
		txtRegister = new JTextField();
		btnRegister.setBounds(30, 20, 116,30);
		txtRegister.setBounds(166, 20, 226 ,30);
		//2nd Row Panel with components and border
		grpMgmt = new JPanel();
		btnCreate = new JButton("Create");
		btnEdit = new JButton("Edit");
		btnDelete = new JButton("Delete");
		grpMgmt.setBounds(20,60,226,55);
		
		
		
		btnCreateGrp = new JButton("Add Group");
		txtMgmt = new JTextField();
		txtMgmt.setBounds(378,79,183,30);
		
		btnCreateGrp.setBounds(256,79,112,30);
		
		
		
		btnCreateGrp.setVisible(false);
		txtMgmt.setEnabled(false);
		
		grpMgmt.add(btnCreate);
		grpMgmt.add(btnEdit);
		grpMgmt.add(btnDelete);
		grpMgmt.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Group Management"));
		
		//3rd Row Multiple Panels with Text Area and border
		users = new JPanel();
		
		grps = new JPanel();
		groupList = new JList<Object>();
		grps.add(groupList);
		
		convo = new JPanel();
		textConvo = new JTextArea();
		textConvo.setEditable(false);
		textConvo.setLineWrap(true);
		textConvo.setColumns(23);
		textConvo.setRows(14);
		scroll = new JScrollPane (textConvo);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		convo.add(scroll);
		
		
		users.setBounds(20,120,136,253);
		grps.setBounds(166,120,136,253);
		convo.setBounds(312,120,281,253);
		users.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Online Users"));
		grps.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Groups"));
		convo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Conversation"));
		
		f.getContentPane().add(grpMgmt);
		f.getContentPane().add(users);
		f.getContentPane().add(btnCreateGrp);
		f.getContentPane().add(txtMgmt);
		users.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		userList = new JList<Object>();
		
		//4th Row
		btnSend = new JButton("Send Message");
		txtSend = new JTextField("");
		
		btnSend.setBounds(20,420,136,30);
		txtSend.setBounds(177,420,416,30);
		f.getContentPane().add(btnRegister);
		f.getContentPane().add(txtRegister);
		users.add(userList);
		f.getContentPane().add(grps);
		f.getContentPane().add(convo);
		f.getContentPane().add(btnSend);
		f.getContentPane().add(txtSend);
		
		f.getContentPane().setLayout(null);
		
		btnRefreshUsers = new JButton("Refresh Users");
		btnRefreshUsers.setBounds(30, 373, 116, 30);
		f.getContentPane().add(btnRefreshUsers);
		
		btnRefreshGrps = new JButton("Refresh Groups");
		btnRefreshGrps.setBounds(176, 373, 116, 30);
		f.getContentPane().add(btnRefreshGrps);
		f.setVisible(true);
		f.setSize(619,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(true);

		
		
		
		// Initializing other class
//		Listener l = new Listener(multicastSocket, multicastGroup, userId);
//		addGroup aG = new addGroup();
//		joinGroup jG = new joinGroup();
//		registerUser rU = new registerUser();
//		editOption eO = new editOption();
//		profilePage pO = new profilePage();
//		deleteGroup dG = new deleteGroup();
//		json jSON = new json();
//		msgGroup mG = new msgGroup();
		
//		groupList.setListData(l.userGrp.values().toArray((new String[l.userGrp.values().size()])));
		// Initialize size of JList groups and user respectively
		groupList.setPreferredSize(new Dimension(110,230));
		userList.setPreferredSize(new Dimension(110,230));
		
		textDisplay = l.start(textConvo);
		textConvo.append(textDisplay+"\n");
		
//		//TODO: Implement storage here
//		ArrayList<HashMap<String,?>> readFile = jSON.JsonRead(l.convos, l.users, l.groups, l.address, l.profile);
//		//ArrayList contains an array of hashmap
//		if (!readFile.isEmpty()) {
//			//need set the values of these to the other lists/fields
//			for (int i = 0; i < readFile.size(); i++) {
//				
//				if (readFile.get(i).containsKey(l.convos)) {
//					HashMap<String,HashMap<String,List<String>>> tempConvoHash = (HashMap<String, HashMap<String, List<String>>>) readFile.get(i);
//					l.grpConvo = tempConvoHash.get(l.convos);
//				}
//				if (readFile.get(i).containsKey(l.users)) {
//					HashMap<String,HashMap<String,List<String>>> tempUserGrpHash = (HashMap<String, HashMap<String, List<String>>>) readFile.get(i);
//					l.userGrp = tempUserGrpHash.get(l.users);
//				}
//				if (readFile.get(i).containsKey(l.groups)) {
//					HashMap<String,HashMap<String,List<String>>> tempGrpUserHash = (HashMap<String, HashMap<String, List<String>>>) readFile.get(i);
//					l.grpUser = tempGrpUserHash.get(l.groups);
//					
//				}
//				if (readFile.get(i).containsKey(l.address)) {
//					HashMap<String,HashMap<String,String>> tempAddressHash = (HashMap<String,HashMap<String,String>>) readFile.get(i);
//					l.nameIP = tempAddressHash.get(l.address);
//				}
//				if (readFile.get(i).containsKey(l.profile)) {
//					HashMap<String,HashMap<String,List<String>>> tempUserProfileHash = (HashMap<String, HashMap<String, List<String>>>) readFile.get(i);
//					l.userProf = tempUserProfileHash.get(l.profile);
//				}
//				
//			}
//		}
		
		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				String msg = txtSend.getText();
//				ip = l.nameIP.get(activeGrp);
//				if(grpSelected == false) {
//					System.out.println("no group selected");
//				}
//				else {	
//					txtSend.setText("");
//					mG.msg(multicastSocket,multicastGroup,ip,activeGrp,userId,msg);					
//				}				
			}
			
		});
		// Within Group Management
		btnCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
//				btnCreateGrp.setVisible(true);
//				txtMgmt.setEnabled(true);
				
				
			}
			
		});
		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				//Outside Grp Management
//				btnCreateGrp.setVisible(false);
//				String[] options = {"Add User", "Remove User", "Change Group Name"};
//                int popup = JOptionPane.showOptionDialog(null, "Choose an Option: ",
//                        "Group Management - " + activeGrp,
//                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
//                eO.option(popup,activeGrp,textConvo);
//                
			}
		});
		// Leaving group by his own
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
//				if(grpSelected == false) {
//					System.out.println("no group selected");
//				}
//				else {		
//					dG.delete(multicastSocket,multicastGroup,activeGrp,userId);
//					textConvo.append("You have left group \""+ activeGrp+"\"");
//					l.grpList = l.userGrp.get(userId);
//					if(l.grpList != null) {
//						for(String grp : l.grpList) {
//							// newly join
//							if(!checkGrp.containsKey(grp)) {
//								checkGrp.put(grp, "0");
//							}
//							// If leave then added back, update back to 0
//							else if(checkGrp.containsKey(grp) && !l.grpUser.containsValue(userId)) {
//								checkGrp.put(grp, "2");
//							}
//							// Change name
//							else if(!checkGrp.containsKey(grp) && l.grpUser.containsValue(userId)) {
//								checkGrp.put(grp,"3");
//							}
//						}
//						groupList.setListData(l.grpList.toArray((new String[l.grpList.size()])));
//					} else {
//						groupList.setListData(l.grpList.toArray((new String[l.grpList.size()])));
//					}
//				}	
			}
			
		});
		// Creating New Grp
		btnCreateGrp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				group = txtMgmt.getText();
//				if(!l.nameIP.containsKey(group)) {
//					do {
//						Random r = new Random();
//						x = r.nextInt((255-1) + 1) + 1;
//						y = r.nextInt((255-1) + 1) + 1;
//						ip = "230.1." + Integer.toString(x) + "." + Integer.toString(y);
//						System.out.println("IP Created for " + group +":"+ip);
//					} while(l.nameIP.containsValue(ip));
//					//Need do validation for IP and Address
//					aG.add(multicastSocket,multicastGroup,userId,group, ip, textConvo);
//					txtMgmt.setText("");
//					txtMgmt.setEnabled(false);
//					btnCreateGrp.setVisible(false);
//					
//					l.grpList = l.userGrp.get(userId);
//					if(l.grpList != null) {
//						for(String grp : l.grpList) {
//							if(!checkGrp.containsKey(grp)) {
//								checkGrp.put(grp, "0");
//							}
//							
//						}
//						groupList.setListData(l.grpList.toArray((new String[l.grpList.size()])));
//					}
//					System.out.println("Checkgrp in main"+checkGrp);
//					//Grp Mgmt
//					btnEdit.setEnabled(true);
//					btnDelete.setEnabled(true);
//				} else {
//					textConvo.append("Group Name has been used. Please enter a new Group Name\n");
//				}
//				if(group == "" || group == null) {
//					textConvo.append("Please enter a Group name");
//				}
				
			}
		});
		
		// Register as a new user
		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				userId = txtRegister.getText();
//				int length = userId.length();
//				if(!l.userList.contains(userId) || length == 0 || userId == "") {
//					
//					char ch = userId.charAt(0);
//					if (length > 8 || Character.isDigit(ch) || userId.contains(" ")) {
//						textConvo.append("User Id must not be more than 8 characters, must not contain any spaces and must not begin with a number\n");
//					} else {
//						
//						txtRegister.setText("");
//						txtRegister.setEnabled(false);
//						btnRegister.setEnabled(false);
//						rU.register(multicastSocket, multicastGroup, userId);
//						
////						userList.setListData(l.userList.toArray((new String[l.userList.size()])));
//						System.out.println("My User Id is " +userId +"\n");
//						l.grpList = l.userGrp.get(userId);
//						if(l.grpList == null) {
//							List<String> emptylist = new ArrayList<String>();
//							emptylist.add("");
//							Team6.groupList.setListData(emptylist.toArray((new String[emptylist.size()])));
//						}
//						
//					}
//				} else {
//					textConvo.append("Invalid User Id/User Id has been used. Please enter a new User Id\n ");
//				}
//				
			}
			
		});
		// JList Selection Listener
		groupList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
//				if(groupList.getValueIsAdjusting() == false) {
//					activeGrp = (String) groupList.getSelectedValue();
//					
//					grpSelected = true;
//					// For newly join
//					if(checkGrp.get(activeGrp)== "0") {
//						textConvo.setText("You are in group " + activeGrp + "\n");
//						jG.joinNew(multicastSocket, multicastGroup, userId, activeGrp, textConvo);
//						checkGrp.put(activeGrp, "1");
//					}
//					// For those who have join
//					if(checkGrp.get(activeGrp)== "1"){
//						jG.join(multicastSocket, multicastGroup, activeGrp);
//						List<String> convoList = l.grpConvo.get(activeGrp);
//						
//						if(convoList == null || convoList.size() == 0) {
//							textConvo.setText("You are in group " + activeGrp+"\n");
//						}
//						else {
////							Collections.reverse(convoList);
//							System.out.println("Length of convo:"+convoList.size());
//							if (convoList.size()>10) {
//								textConvo.setText("");
//								for (int i = 0; i < 10;i++) {
//									textConvo.append(convoList.get(i)+ "\n");
//								}
//							} else {
//								textConvo.setText("");
//								for (int i = 0; i < convoList.size();i++) {
//									textConvo.append(convoList.get(i)+ "\n");
//								}
//							}
//							
//						}
//					}
//					// For those who got kicked/left the group
//					if(checkGrp.get(activeGrp)== "2") {
//						jG.join(multicastSocket, multicastGroup, activeGrp);
//						List<String> convoList = l.grpConvo.get(activeGrp);
//						if(convoList == null || convoList.size() == 0) {
//							textConvo.setText("You are in group " + activeGrp +"\n");
//						}
//						else {
//							//Reverse the list to get from the back
////							Collections.reverse(convoList);
//							textConvo.setText("");
//							for (int i = 0; i < convoList.size();i++) {
//								textConvo.append(convoList.get(i)+ "\n");
//							}
//							
//						}
//					}
//					// For those that change name
//					if (checkGrp.get(activeGrp) == "3") {
//						jG.join(multicastSocket, multicastGroup, activeGrp);
//						List<String> convoList = l.grpConvo.get(activeGrp);
//						if(convoList == null || convoList.size() == 0) {
//							textConvo.setText("You are in group " + activeGrp+"\n");
//						}
//						else {
//							//Reverse the list to get from the back
////							Collections.reverse(convoList);
//							textConvo.setText("");
//							for (int i = 0; i < convoList.size()-1;i++) {
//								textConvo.append(convoList.get(i)+ "\n");
//							}
//						}
//					}
//					System.out.println(activeGrp +"selected");
//				}
			}
			
		});
		userList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
//				if(userList.getValueIsAdjusting() == false) {
//					activeUsr = (String) userList.getSelectedValue();
//					pO.changeProf(activeUsr, userId);
//				}
			}
			
		});
		
//		//Refreshing List
//		btnRefreshGrps.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				
//				l.grpList = l.userGrp.get(userId);
//				
//				if(l.grpList != null) {
//					for(String grp : l.grpList) {
//						//Change name
//						List<String> templist = new ArrayList<String>();
//						templist = l.grpUser.get(grp);
//						if(!checkGrp.containsKey(grp) && templist.contains(userId)) {
//							checkGrp.put(grp, "3");
//						}
//						// If leave then added back/removed from group, update back to 0
//						else if(checkGrp.containsKey(grp) && !templist.contains(userId)) {
//							checkGrp.put(grp, "2");
//						}
//						// Doesnt exist at all and new
//						else {
//							checkGrp.put(grp,"0");
//						}
//					}
//					groupList.setListData(l.grpList.toArray((new String[l.grpList.size()])));
//				}
//				
//			}
//			
//		});
//		btnRefreshUsers.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				if (l.userList !=null) {
//					userList.setListData(l.userList.toArray((new String[l.userList.size()])));
//				}
//				
//			}
//			
//		});
	}
}
