package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;

import Network.NetworkReceiverManager;
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
		inPacket = new DatagramPacket(buffer,buffer.length);
		System.out.println("inpacket created");
		dgramSocket.receive(inPacket);
		System.out.println("inpacket received");
		System.out.println("Sender address : " + inPacket.getAddress());
		analyzePacket(inPacket);
	}

	public void analyzePacket(DatagramPacket inPacket) throws SQLException, IOException {
		senderAddress = inPacket.getAddress();
		if (inPacket.getLength() > 0){
			String pseudo = new String(inPacket.getData(),0,inPacket.getLength());
			db_users_manager.updateUserTable(senderAddress.toString().replaceAll("/", ""), pseudo);
			// sendAnswer_RtoS(senderAddress);
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