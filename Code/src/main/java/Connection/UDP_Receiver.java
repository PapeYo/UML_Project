package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;

// import Setup.Constants;
// import database_management.db_users_manager;

public class UDP_Receiver extends Thread {

	static DatagramSocket dgramSocket;
	static DatagramPacket inPacket;
	static InetAddress senderAddress;
	static int senderPort;
	static String pseudo;

	public static void createUDP_Receiver(int portNumber) throws SocketException {
		dgramSocket = new DatagramSocket(portNumber);
		System.out.println("dgram socket created");
		createInPacket();
	}

	private static void createInPacket() {
		byte[] buffer = new byte[256];
		inPacket = new DatagramPacket(buffer,buffer.length);
		System.out.println("inpacket created");
	}

	private static void receivePacket() throws IOException, SQLException {
		while(true) {
			dgramSocket.receive(inPacket);
			System.out.println("inpacket received");
			UDP.analyzePacket(inPacket);
		}
	}
	
	public static void main(String args[], int portNumber) {
		try {
			createUDP_Receiver(portNumber);
			createInPacket();
			receivePacket();
		} catch (IOException | SQLException e) {
			System.out.println(e.getMessage());
		}
		

	}
}