package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import Setup.Constants;
import database_management.db_users_manager;

// import Setup.Constants;
// import database_management.db_users_manager;

public class UDP_Receiver extends Thread {

	DatagramSocket dgramSocket;
	DatagramPacket inPacket;
	InetAddress senderAddress;
	int senderPort;
	String pseudo;

	public UDP_Receiver() {
		start();
	}

	// create UDP datagram socket
	public void createUDP_Receiver(int portNumber) throws SocketException {
		dgramSocket = new DatagramSocket(portNumber);
		System.out.println("dgram socket created");
	}

	// receive the username packet
	private void receivePacket() throws IOException, SQLException {
		byte[] buffer = new byte[256];
		inPacket = new DatagramPacket(buffer,buffer.length);
		System.out.println("inpacket created");
		dgramSocket.receive(inPacket);
		System.out.println("inpacket received");
		System.out.println("Sender address : " + inPacket.getAddress());
		analyzePacket(inPacket);
	}
	
	private void sendAnswer(InetAddress senderAddress) throws IOException {
		byte[] buffer = new byte[256];
		DatagramPacket answerPacket = new DatagramPacket(buffer,buffer.length,senderAddress, Constants.BROADCAST_PORT);
		System.out.println("datagram outpacket created");
		// send pseudo message
		dgramSocket.send(answerPacket);
		System.out.println("Answer packet sent");
	}

	// analyze the username packet : someone's connection or disconnection
	public void analyzePacket(DatagramPacket inPacket) throws SQLException, IOException {
		senderAddress = inPacket.getAddress();
		if (inPacket.getLength() > 0){
			//int senderPort = inPacket.getPort();
			String pseudo = new String(inPacket.getData(),0,inPacket.getLength());
			db_users_manager.updateUserTable(senderAddress.toString().replaceAll("/", ""), pseudo);
			sendAnswer(senderAddress);
		}
		else if (inPacket.getLength()==-1){
			db_users_manager.removeUser(senderAddress.toString().replaceAll("/", "")); //need a removeUser class
		}
	}

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