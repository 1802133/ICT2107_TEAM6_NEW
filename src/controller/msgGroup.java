package controller;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JTextArea;

import listener.Listener;

public class msgGroup {
	public msgGroup() {
		
	}
	public void msg(MulticastSocket multicastSocket,InetAddress multicastGroup,String groupIP,String group,String userId,String msg) {
		try {			
			// Send to Listener to put to HashMap
			multicastGroup = InetAddress.getByName(Listener.IP);
			multicastSocket = new MulticastSocket(6789);
			String concat = userId+":"+msg;
			String  message = "SND:"+group+"SND:"+concat;
			byte[] buf = message.getBytes();
			DatagramPacket dgpSend = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
			multicastSocket.send(dgpSend);
			// Join back group and send msg
			multicastGroup = InetAddress.getByName(groupIP);
			multicastSocket = new MulticastSocket(6789);
			message = userId+": "+msg;
			buf = message.getBytes();
			dgpSend = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
			multicastSocket.send(dgpSend);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public void msgLeave(MulticastSocket multicastSocket,InetAddress multicastGroup,String msg) {
		try {
			byte[] buf = msg.getBytes();
			DatagramPacket dgpSend = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
			multicastSocket.send(dgpSend);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
