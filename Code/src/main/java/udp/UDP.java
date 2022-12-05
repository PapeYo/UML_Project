package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.sql.SQLException;

import Setup.Constants;
import database_management.db_users_manager;

public class UDP extends Thread{
	
	InetAddress senderAddress;
	static UDP_Receiver udp_Receiver = new UDP_Receiver(Constants.UDP_PORT);
    static UDP_Sender udp_Sender = new UDP_Sender("Yohan");

    public static void main(String arg[]) throws IOException, SQLException {
		//Start thread udp_receiver & udp_sender (� REVOIR ?)
        // udp_Receiver.start();
        // udp_Receiver.createUDP_Receiver(Constants.UDP_PORT); //constante inexistante ??
		// udp_Sender.start();
		// udp_Sender.create_UDP_Sender();
    }
    
    private void changePseudo(String pseudo) throws IOException {
    	udp_Sender.broadcast_pseudo(pseudo);
    	String localhostIP = InetAddress.getLocalHost().getHostAddress();
		db_users_manager.updateUserTable(localhostIP, pseudo);
    }
    
	private static void sendPseudo(String myPseudo) throws IOException, SQLException {
		// KESKEKOI ????
		
		// String localhostIP = InetAddress.getLocalHost().getHostAddress();
		// String myPseudo = db_users_manager.selectUser(localhostIP); //need a selectUser class
		udp_Sender.broadcast_pseudo(myPseudo);
		//launch HUB interface
	}

	public void analyzePacket(DatagramPacket inPacket) throws SQLException {
		senderAddress = inPacket.getAddress();
		if (inPacket.getLength() > 0){
			//int senderPort = inPacket.getPort();
			String pseudo = new String(inPacket.getData(),0,inPacket.getLength());
			db_users_manager.updateUserTable(senderAddress.toString().replaceAll("/", ""), pseudo);
		}
		else if (inPacket.getLength()==-1){
			db_users_manager.removeUser(senderAddress.toString().replaceAll("/", "")); //need a removeUser class
		}
	}
}