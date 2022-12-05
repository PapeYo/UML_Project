package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import Setup.Constants;
import database_management.db_users_manager;

public class UDP extends Thread{
	
	static InetAddress senderAddress;
	static UDP_Receiver udp_Receiver = new UDP_Receiver();
    static UDP_Sender udp_Sender = new UDP_Sender();

    public static void main(String arg[]) throws SocketException, UnknownHostException {
		//Start thread udp_receiver & udp_sender (A REVOIR ?)
        udp_Receiver.start();
        udp_Receiver.createUDP_Receiver(Constants.UDP_PORT); //constante inexistante ??
		udp_Sender.start();
		udp_Sender.create_UDP_Sender();
		try {
			sendPseudo();
		} catch (IOException | SQLException e) {
			System.out.println(e.getMessage());
		}
    }
    
    private void changePseudo(String pseudo) throws IOException {
    	UDP_Sender.broadcast_pseudo(pseudo);
    	String localhostIP = InetAddress.getLocalHost().getHostAddress();
		db_users_manager.updateUserTable(localhostIP, pseudo);
    }
    
	private static void sendPseudo() throws IOException, SQLException {
		String localhostIP = InetAddress.getLocalHost().getHostAddress();
		String myPseudo = db_users_manager.selectUser(localhostIP); //need a selectUser class
		UDP_Sender.broadcast_pseudo(myPseudo);
		//launch HUB interface
	}

	public static void analyzePacket(DatagramPacket inPacket) throws SQLException {
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