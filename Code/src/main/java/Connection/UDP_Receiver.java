package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;
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
		System.out.println("Datagram socket created and listening on port " + portNumber);
	}

	private void receivePacket() throws IOException, SQLException {
		byte[] buffer = new byte[256];
		InetAddress localIP = NetworkInterface.getNetworkInterfaces().nextElement().getInterfaceAddresses().get(1).getAddress();
		System.out.println("Local address is : " + localIP);
		while(true) {
			inPacket = new DatagramPacket(buffer,buffer.length);
			System.out.println("Waiting for message");
			dgramSocket.receive(inPacket);
			System.out.println("New message received");
			InetAddress senderAddress = inPacket.getAddress();
			System.out.println("Sender address is : " + senderAddress);
			if (!(senderAddress.equals(localIP))) {
				System.out.println("Analyze in progress");
				analyzePacket(inPacket);
			} else {
				db_users_manager.selectALLusers();
			}
		}
	}

	public void analyzePacket(DatagramPacket inPacket) throws SQLException, IOException {
		senderAddress = inPacket.getAddress();
		String mesg = new String(inPacket.getData(),0,inPacket.getLength());
		System.out.println(mesg);
		if (mesg.startsWith("00/")){
			// someone is broadcasting his/her pseudo
			String pseudo = mesg.replaceAll("00/", "");
			System.out.println("Pseudo : " + pseudo + ".");
			db_users_manager.updateUserTable(senderAddress.toString().replaceAll("/", ""), pseudo);
			UDP_Sender.sendAnswer_RtoS(senderAddress);
		} else if (mesg.startsWith("01/")) {
			// someone has received my pseudo broadcast
			System.out.println("Accusé de réception reçu");
		} else if (mesg.startsWith("10/")) {
			// someone is disconnecting
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