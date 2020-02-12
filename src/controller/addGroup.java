package controller;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JTextArea;

import listener.Listener;

public class addGroup {
	//Using well-known address to send new group name and IP address to main threads
	joinGroup jG = new joinGroup();
	public addGroup() {
		
	}
	public void add(MulticastSocket multicastSocket,InetAddress multicastGroup,String userId,String group, String tempIP,  JTextArea textConvo) {
		try {
			
			multicastGroup = InetAddress.getByName(Listener.IP);
			multicastSocket = new MulticastSocket(6789);
			multicastSocket.joinGroup(multicastGroup);
			String message = "GRP:"+group+":"+tempIP+":"+userId;
			byte[] buf = message.getBytes();
			DatagramPacket dgpConnected = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
			multicastSocket.send(dgpConnected);
			textConvo.append("Group \""+ group + "\" has been created\n");
//			jG.join(multicastSocket, multicastGroup, userId, group, textConvo);
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	public void AddUser(MulticastSocket multicastSocket,InetAddress multicastGroup,String userId,String group) {
		try {
			multicastGroup = InetAddress.getByName(Listener.IP);
			multicastSocket = new MulticastSocket(6789);
			multicastSocket.joinGroup(multicastGroup);
			String message = "ADU:"+group+":"+userId;
			byte[] buf = message.getBytes();
			DatagramPacket dgpConnected = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
			multicastSocket.send(dgpConnected);
//			jG.join(multicastSocket, multicastGroup, group);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//User will automatically be defaulted to well-known address
	public void RemoveUser(MulticastSocket multicastSocket,InetAddress multicastGroup,String userId,String group, JTextArea textConvo) {
		try {
			multicastGroup = InetAddress.getByName(Listener.IP);
			multicastSocket = new MulticastSocket(6789);
			multicastSocket.joinGroup(multicastGroup);
			String message = "RMU:"+group+":"+userId;
			byte[] buf = message.getBytes();
			DatagramPacket dgpConnected = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
			multicastSocket.send(dgpConnected);
			jG.join(multicastSocket, multicastGroup, group);
			textConvo.append(userId + " has been removed from the group\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
