package controller;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import listener.Listener;

public class deleteGroup {
	public deleteGroup() {
		
	}
	
	public void delete(MulticastSocket multicastSocket, InetAddress multicastGroup, String activeGrp, String userId) {
		try {
			msgGroup msg = new msgGroup();
			String tempIP = Listener.nameIP.get(activeGrp);
			multicastGroup = InetAddress.getByName(tempIP);
			multicastSocket = new MulticastSocket(6789);
			//Join
			multicastSocket.joinGroup(multicastGroup);
			String delmessage = userId + " has left the group";
			msg.msgLeave(multicastSocket, multicastGroup, delmessage);
			multicastSocket.leaveGroup(multicastGroup);
			//Update HashMap in Listener
			multicastGroup = InetAddress.getByName(Listener.IP);
			multicastSocket = new MulticastSocket(6789);
			multicastSocket.joinGroup(multicastGroup);
			String message = "REM:"+userId+":"+ activeGrp;
			
			byte[] buf = message.getBytes();
			DatagramPacket dgpConn = new DatagramPacket(buf, buf.length, multicastGroup, 6789);
			multicastSocket.send(dgpConn);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
//		try {
//			String msg = username + " is leaving " + group;
//			byte[] buf = msg.getBytes();
//			DatagramPacket dgpSend = new DatagramPacket(buf,buf.length,multicastGroup,6789);
//			multicastSocket.send(dgpSend);
//			multicastSocket.leaveGroup(multicastGroup);
//			btnJoin.setEnabled(true);
//			btnSend.setEnabled(false);
//			btnLeave.setEnabled(false);
//			
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
	}
}