package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Setup.Constants;
import database_management.db_users_manager;

public class UDP extends Thread{
	
	static InetAddress senderAddress;
	static UDP_Receiver udp_Receiver = new UDP_Receiver();
    static UDP_Sender udp_Sender = new UDP_Sender();

	
    public static void main(String arg[]) {
		//Start thread udp_receiver & udp_sender (à REVOIR ?)
        udp_Receiver.start();
        udp_Receiver.createUDP_Receiver(UDP_PORT); //constante inexistante ??
		udp_Sender.start();
    }
    
    private void changePseudo(String pseudo) throws IOException {
    	udp_Sender.broadcast_pseudo(pseudo);
    	String localhostIP = InetAddress.getLocalHost().getHostAddress();
		db_users_manager.updateUserTable(localhostIP, pseudo);
    }
    
	private void sendPseudo() throws IOException {
		String localhostIP = InetAddress.getLocalHost().getHostAddress();
		String myPseudo = db_users_manager.selectUser(localhostIP); //need a selectUser class
		udp_Sender.broadcast_pseudo(myPseudo);
		//launch HUB interface
	}

	public static void analyzePacket(DatagramPacket inPacket) {
		senderAddress = inPacket.getAddress();
		if (inPacket.getLength() > 0){
			//int senderPort = inPacket.getPort();
			String pseudo = new String(inPacket.getData(),0,inPacket.getLength());
			db_users_manager.updateUserTable(senderAddress.toString().replaceAll("/", ""), pseudo);
		}
		else if (inPacket.getLength()==-1){
			db_users_manager.removeUser(senderAddress.toString()); //need a removeUser class
		}
	}
}