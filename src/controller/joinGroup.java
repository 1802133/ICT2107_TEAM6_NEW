package controller;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JTextArea;

import listener.Listener;

public class joinGroup {
	public joinGroup() {
		
	}
	public void joinNew(MulticastSocket multicastSocket,InetAddress multicastGroup,String username, String group, JTextArea textConvo) {
		try {
			String tempIP = Listener.nameIP.get(group);
			
			multicastGroup = InetAddress.getByName(tempIP);
			multicastSocket = new MulticastSocket(6789);
			//Join
			multicastSocket.joinGroup(multicastGroup);
			//Send a joined message
			String message = username + " joined " + group;
			byte[] buf = message.getBytes();
			DatagramPacket dgpConnected = new DatagramPacket(buf,buf.length, multicastGroup,6789);
			multicastSocket.send(dgpConnected);
			newThread(multicastSocket,textConvo);
	} catch(Exception e) {
		e.printStackTrace();
	}

	}
	public void join(MulticastSocket multicastSocket,InetAddress multicastGroup, String group) {
		try {
			String tempIP = Listener.nameIP.get(group);
			
			multicastGroup = InetAddress.getByName(tempIP);
			multicastSocket = new MulticastSocket(6789);
			//Join
			multicastSocket.joinGroup(multicastGroup);
		} catch(Exception e) {
			
		}
	}
	public void newThread( MulticastSocket multicastSocket, JTextArea textConvo) {
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
						textConvo.append(msg + "\n");
				
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}).start();
	}
}