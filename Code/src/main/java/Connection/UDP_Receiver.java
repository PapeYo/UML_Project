package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;

import Network.NetworkReceiverManager;
<<<<<<< HEAD
import Network.NetworkSenderManager;
=======
>>>>>>> 9e7f340d983180d5a890383a68aeabf997cacf17
import Setup.Constants;
import database_management.db_users_manager;

public class UDP_Receiver extends Thread {

	DatagramSocket dgramSocket;
	DatagramPacket inPacket;
	InetAddress senderAddress;
	int senderPort;
	String pseudo;

	public UDP_Receiver() {
		start();
	}

	public void createUDP_Receiver(int portNumber) throws SocketException {
		dgramSocket = new DatagramSocket(portNumber);
		System.out.println("dgram socket created");
	}

	private void receivePacket() throws IOException, SQLException {
		byte[] buffer = new byte[256];
		while(true) {
			inPacket = new DatagramPacket(buffer,buffer.length);
			System.out.println("inpacket created");
			dgramSocket.receive(inPacket);
			System.out.println("inpacket received");
			System.out.println("Sender address : " + inPacket.getAddress());
			analyzePacket(inPacket);
		}
	}

	public void analyzePacket(DatagramPacket inPacket) throws SQLException, IOException {
		senderAddress = inPacket.getAddress();
		if (inPacket.getLength() > 0){
			String pseudo = new String(inPacket.getData(),0,inPacket.getLength());
			db_users_manager.updateUserTable(senderAddress.toString().replaceAll("/", ""), pseudo);
<<<<<<< HEAD
			UDP_Sender.sendAnswer_RtoS(senderAddress);
=======
			// sendAnswer_RtoS(senderAddress);
>>>>>>> 9e7f340d983180d5a890383a68aeabf997cacf17
		}
		else if (inPacket.getLength()==-1){
			db_users_manager.removeUser(senderAddress.toString().replaceAll("/", "")); //need a removeUser class
		}
	}
<<<<<<< HEAD
	
	/*
	 * public void receiveAnswer_RtoS() throws IOException { DatagramSocket
	 * dgramSocket = new DatagramSocket(); byte[] buffer = new byte[256];
	 * while(true) { DatagramPacket inPacket = new
	 * DatagramPacket(buffer,buffer.length);
	 * System.out.println("waiting for answer"); dgramSocket.receive(inPacket);
	 * String mesg = new String(inPacket.getData(),0,inPacket.getLength());
	 * System.out.println(mesg); } }
	 */
=======
>>>>>>> 9e7f340d983180d5a890383a68aeabf997cacf17

	public void run() {
		try {
			createUDP_Receiver(Constants.BROADCAST_PORT);
			receivePacket();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}