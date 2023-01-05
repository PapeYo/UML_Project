package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import Setup.Constants;
import database_management.db_users_manager;

public class UDP_Sender extends Thread{
	
	private String pseudo;
	private DatagramSocket dgramSocket;
	static String localIP;
	InetAddress bcIP;
	
	public UDP_Sender(String pseudo) throws SocketException {
		this.pseudo = pseudo;
		UDP_Sender.localIP = Constants.get_LocalIP();
		this.bcIP = Constants.get_BcIP();
		start();
	}
	
	/* setup UDP with constructor */
	public void create_UDP_Sender() throws SocketException {
		dgramSocket = new DatagramSocket(Constants.UDP_SENDER_PORT);
		System.out.println("Datagram socket created and sending through port " + Constants.UDP_SENDER_PORT);
	}
	 
	public void broadcast_pseudo() throws IOException {
		// create pseudo message
		String mesg = "00/" + pseudo;
		byte[] buf = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buf,buf.length,bcIP, Constants.UDP_RECEIVER_PORT);
		System.out.println("Pseudo packet created");
		// send pseudo message
		dgramSocket.send(outPacket);
		System.out.println("Pseudo packet sent");
		db_users_manager.updateUserTable(localIP, pseudo);
	}
	
	public static void sendAnswer_RtoS(InetAddress senderAddress) throws IOException, SQLException {
		DatagramSocket dgramSocket = new DatagramSocket(Constants.UDP_SENDER_PORT);
		String mesg = "01/" + db_users_manager.selectUser(localIP.replaceAll("/",""));
		byte[] buffer = mesg.getBytes();
		DatagramPacket answerPacket = new DatagramPacket(buffer, buffer.length, senderAddress, Constants.UDP_RECEIVER_PORT);
		dgramSocket.send(answerPacket);
		System.out.println("Answer packet sent");
		dgramSocket.close();
	}
	
	public void disconnect() throws IOException {
		// create disconnect message
		String mesg = "10/";
		byte[] buffer = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buffer,buffer.length,bcIP, Constants.UDP_RECEIVER_PORT);
		System.out.println("Disconnection datagram outpacket created");
		// send disconnect message
		dgramSocket.send(outPacket);
		System.out.println("Disconnection message sent. Closing UDP Socket");
		//close dgramSocket
		dgramSocket.close();
	}
	
	public static void sendChangePseudoMessage(InetAddress senderAddress) throws IOException {
		DatagramSocket dgramSocket = new DatagramSocket(Constants.UDP_SENDER_PORT);
		// create changePseudo Message
		String mesg = "11/";
		byte[] buffer = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buffer,buffer.length,senderAddress, Constants.UDP_RECEIVER_PORT);
		System.out.println("changePseudo datagram outpacket created");
		// send changePseudo Message
		dgramSocket.send(outPacket);
		System.out.println("changePseudo Message sent. Closing UDP Socket");
		//close dgramSocket
		dgramSocket.close();
	}
	
	public void run() {
		try {
			create_UDP_Sender();
			broadcast_pseudo();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}