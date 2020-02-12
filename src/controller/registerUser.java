package controller;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import listener.Listener;


public class registerUser {
	public registerUser() {
		
	}
	public void register(MulticastSocket multicastSocket,InetAddress multicastGroup,String userId) {
		try {
			//Using well-known address to send new group name and IP address to main threads
			multicastGroup = InetAddress.getByName(Listener.IP);
			multicastSocket = new MulticastSocket(6789);
			multicastSocket.joinGroup(multicastGroup);
			String message = "USR:"+userId;
			byte[] buf = message.getBytes();
			DatagramPacket dgpConnected = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
			multicastSocket.send(dgpConnected);
			
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}


}
