package controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import listener.Listener;

public class editOption {
	JFrame temp;
	JPanel panelTemp;
	JButton btnTemp;
	JLabel lblTemp, lblTemp1, lblTemp2;
	JTextField txtTemp;
	String selectedUser;
	JList<String> onlineUsr;
	addGroup aG = new addGroup();
	joinGroup jG = new joinGroup();
	MulticastSocket multicastSocket = null;
	InetAddress multicastGroup = null;
	public editOption() {
		
		
	}
	
	public void option(int option, String activeGrp, JTextArea textConvo) {
		
		switch(option) {
		// Add Users
		case 0:
			add(activeGrp,textConvo);
			break;
		// Remove Users
		case 1:
			remove(activeGrp, textConvo);
			break;
		// Change Group Name
		case 2:
			change(activeGrp,textConvo);
			break;
		}
		
		
	}
	public void add(String activeGrp, JTextArea textConvo) {
		temp = new JFrame("Add User");
		temp.setLayout(new FlowLayout());
		panelTemp = new JPanel();
		btnTemp = new JButton("Add User");
		
		// Getting size of Online Users that are not in group
		List<String> tempusrList = new ArrayList<String>();
		List<String> addList = new ArrayList<String>();
		for(String usr : Listener.userList) {
			tempusrList = Listener.userGrp.get(usr);
			if(tempusrList == null || tempusrList.contains(activeGrp)== false ) {
				addList.add(usr);
			}
		}
		String[] str = new String[addList.size()];
		// Populating into JList
		onlineUsr = new JList<String>(addList.toArray(str));
		panelTemp.add(onlineUsr);
		panelTemp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Online Users"));
		panelTemp.setBounds(20,20,200,300);
		btnTemp.setBounds(150,300,90,30);
		onlineUsr.setPreferredSize(new Dimension(120,270));
		temp.add(panelTemp,BorderLayout.SOUTH);
		temp.add(btnTemp);
		temp.setVisible(true);
		//Size of JFrame
		temp.setSize(300,500);
		temp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		
		
		onlineUsr.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(onlineUsr.getSelectedValue() != null) {
					selectedUser = onlineUsr.getSelectedValue();
				}
			}
			
		});
		
		btnTemp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(selectedUser != null) {
					try {
						aG.AddUser(multicastSocket, multicastGroup, selectedUser, activeGrp);
						textConvo.append("You have added " + selectedUser + " to " + activeGrp +"\n");
						List<String> tempusrList = new ArrayList<String>();
						List<String> addList = new ArrayList<String>();
						for(String usr : Listener.userList) {
							tempusrList = Listener.userGrp.get(usr);
							if(tempusrList == null || tempusrList.contains(activeGrp)== false ) {
								addList.add(usr);
							}
						}
						String[] str = new String[addList.size()];
						// Populating into JList
						onlineUsr.setListData(addList.toArray(str));
					} catch(Exception e1) {
						e1.printStackTrace();
					}
					
				} 
			}
			
		});
		
	}
	public void remove(String activeGrp, JTextArea textConvo){
		temp = new JFrame("Remove User");
		temp.getContentPane().setLayout(new FlowLayout());
		panelTemp = new JPanel();
		btnTemp = new JButton("Remove User");
		
		// Getting size of Grp Users
		String[] str = new String[Listener.grpUser.get(activeGrp).size()];
		// Populating into JList
		onlineUsr = new JList<String>(Listener.grpUser.get(activeGrp).toArray(str));
		panelTemp.add(onlineUsr);
		panelTemp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Users in "+ activeGrp));
		panelTemp.setBounds(20,20,200,300);
		btnTemp.setBounds(150,300,90,30);
		onlineUsr.setPreferredSize(new Dimension(120,270));
		temp.getContentPane().add(panelTemp,BorderLayout.SOUTH);
		temp.getContentPane().add(btnTemp);
		temp.setVisible(true);
		//Size of JFrame
		temp.setSize(300,500);
		temp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		onlineUsr.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(onlineUsr.getSelectedValue() != null) {
					selectedUser = onlineUsr.getSelectedValue();
				}
			}
			
		});
		
		btnTemp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(selectedUser != null) {
					aG.RemoveUser(multicastSocket, multicastGroup, selectedUser, activeGrp, textConvo);
					String[] str = new String[Listener.grpUser.get(activeGrp).size()];
					onlineUsr.setListData(Listener.grpUser.get(activeGrp).toArray(str));
				} 
			}
		});
	}
	public void change(String activeGrp, JTextArea textConvo){
		temp = new JFrame("Change Group Name");
		panelTemp = new JPanel();
		btnTemp = new JButton("Change");
		lblTemp = new JLabel("Current Name: ");
		lblTemp.setBounds(50, 38, 146, 26);
		lblTemp1 = new JLabel("New Name: ");
		lblTemp1.setBounds(80, 85, 116, 26);
		txtTemp = new JTextField();
		txtTemp.setBounds(208, 85, 151, 32);
		
		btnTemp.setBounds(177,144,103,35);
		temp.getContentPane().setLayout(null);
		temp.getContentPane().add(lblTemp);
		lblTemp2 = new JLabel(activeGrp);
		lblTemp2.setBounds(208, 38, 160, 26);
		temp.getContentPane().add(lblTemp2);
		temp.getContentPane().add(lblTemp1);
		temp.getContentPane().add(btnTemp);
		temp.getContentPane().add(txtTemp);
		temp.setVisible(true);
		//Size of JFrame
		temp.setSize(478,303);
		temp.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
		btnTemp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name = txtTemp.getText();
				
				if(name != "" && name != lblTemp2.getText() && name != Listener.nameIP.get(name)) {
					try {
						multicastGroup = InetAddress.getByName(Listener.IP);
						multicastSocket = new MulticastSocket(6789);
						//Join
						multicastSocket.joinGroup(multicastGroup);
						//Send a joined message
						String message = "CHG:" + name +":"+ activeGrp;
						byte[] buf = message.getBytes();
						DatagramPacket dgpConnected = new DatagramPacket(buf,buf.length, multicastGroup,6789);
						multicastSocket.send(dgpConnected);
						textConvo.append(lblTemp2.getText() + " name has been changed to \""+ name + "\"\n");
						lblTemp2.setText(name);
						jG.join(multicastSocket, multicastGroup, activeGrp);
						temp.dispose();
						
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

}
