package connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import database_management.db_users_manager;
import setup.Constants;

/*
 * Thread running temporarily when necessary.
 * Handles all UDP packets sent (broadcast pseudo, send pseudo, disconnection, change pseudo).
 */
public class UDP_Sender extends Thread{
	
	/* Attributes */
	private String pseudo;
	private static DatagramSocket dgramSocket;
	
	
	/** Constructor */
	public UDP_Sender(String pseudo) throws SocketException {
		this.pseudo = pseudo;
		start();
	}
	
	
	
	/**	 
	 * Creates the UDP_Sender datagram Socket with port number 2998.
	 * 
	 * @throws SocketException
	 */
	public static void create_UDP_Sender() throws SocketException {
		dgramSocket = new DatagramSocket(Constants.UDP_SENDER_PORT);
		System.out.println("UDP_sender : Datagram socket created and sending through port " + Constants.UDP_SENDER_PORT);
	}
	
	
	
	/**
	 * When we launch the application or change our pseudo, broadcasts it on the network.
	 * 
	 * @throws IOException
	 */
	public void broadcast_pseudo() throws IOException {
		String mesg = "00/" + pseudo;
		byte[] buf = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buf,buf.length,Constants.get_BcIP(), Constants.UDP_RECEIVER_PORT);
		System.out.println("UDP_sender : Pseudo packet created");
		dgramSocket.send(outPacket);
		System.out.println("UDP_sender : Pseudo packet sent");
	}
	
	
	
	/**
	 * Called by UDP_Receiver, replies to the broadcaster with our pseudo.
	 * 
	 * @param senderAddress
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void sendAnswer_RtoS(InetAddress senderAddress) throws IOException, SQLException {
		DatagramSocket dgramSocket = new DatagramSocket(Constants.UDP_SENDER_PORT);
		String mesg = "01/" + db_users_manager.getUserPseudo(Constants.get_LocalIP().replaceAll("/",""));
		byte[] buffer = mesg.getBytes();
		DatagramPacket answerPacket = new DatagramPacket(buffer, buffer.length, senderAddress, Constants.UDP_RECEIVER_PORT);
		dgramSocket.send(answerPacket);
		System.out.println("UDP_sender : Answer packet sent");
		dgramSocket.close();
	}
	
	
	
	/**
	 * Called when we close the application. <br>
	 * Notifies all users with a disconnection message.
	 * 
	 * @throws IOException
	 */
	public static void disconnect() throws IOException {
		create_UDP_Sender();
		String mesg = "10/";
		byte[] buffer = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buffer,buffer.length,Constants.get_BcIP(), Constants.UDP_RECEIVER_PORT);
		System.out.println("UDP_sender : Disconnection datagram outpacket created");
		dgramSocket.send(outPacket);
		System.out.println("UDP_sender : Disconnection message sent. Closing UDP Socket");
		dgramSocket.close();
		db_users_manager.deleteTable();
	}
	
	
	
	/**
	 * Called by UDP_Receiver when a pseudo received already exists in our database. <br>
	 * Sends message to the user so that they change their pseudo.
	 * 
	 * @param senderAddress is the sender's IP address
	 * @throws IOException
	 */
	public static void sendChangePseudoMessage(InetAddress senderAddress) throws IOException {
		DatagramSocket dgramSocket = new DatagramSocket(Constants.UDP_SENDER_PORT);
		String mesg = "11/";
		byte[] buffer = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buffer,buffer.length,senderAddress, Constants.UDP_RECEIVER_PORT);
		System.out.println("UDP_sender : changePseudo datagram outpacket created");
		dgramSocket.send(outPacket);
		System.out.println("UDP_sender : changePseudo Message sent. Closing UDP Socket");
		dgramSocket.close();
	}
	
	
	
	public void run() {
		try {
			create_UDP_Sender();
			broadcast_pseudo();
			dgramSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}