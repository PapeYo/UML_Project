package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import Setup.Constants;
import database_management.db_users_manager;

public class UDP_Sender extends Thread{
	
	private String pseudo;
	private InetAddress bc_addr;
	private String localIP;
	private DatagramSocket dgramSocket;
	
	public UDP_Sender(String pseudo) throws SocketException {
		this.pseudo = pseudo;
		start();
	}
	
	public void get_BcAddr() throws SocketException {
		Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
		while(networkInterfaceEnumeration.hasMoreElements()) {
			for(InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses()) {
				if(interfaceAddress.getAddress().isSiteLocalAddress()) {
					localIP = interfaceAddress.getAddress().getHostAddress();
					bc_addr = interfaceAddress.getBroadcast();
					System.out.println("Local IP : " + localIP);
					System.out.println("Broadcast address : " + bc_addr);
				}
			}
		}
	}
	
	/* setup UDP with constructor */
	public void create_UDP_Sender() throws SocketException {
		dgramSocket = new DatagramSocket(Constants.UDP_PORT);
		System.out.println("Datagram socket created and sending through port " + Constants.UDP_PORT);
	}
	 
	public void broadcast_pseudo() throws IOException {
		// create pseudo message
		String mesg = "00/" + pseudo;
		byte[] buf = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buf,buf.length,bc_addr, Constants.BROADCAST_PORT);
		System.out.println("Pseudo packet created");
		// send pseudo message
		dgramSocket.send(outPacket);
		System.out.println("Pseudo packet sent");
		db_users_manager.updateUserTable(localIP, pseudo);
	}
	
	public static void sendAnswer_RtoS(InetAddress senderAddress) throws IOException {
		DatagramSocket dgramSocket = new DatagramSocket(Constants.UDP_PORT);
		String mesg = "01/";
		byte[] buffer = mesg.getBytes();
		DatagramPacket answerPacket = new DatagramPacket(buffer, buffer.length, senderAddress, Constants.UDP_PORT);
		dgramSocket.send(answerPacket);
		System.out.println("Answer packet sent");
		dgramSocket.close();
	}
	
	public void disconnect() throws IOException {
		// create disconnect message
		String mesg = "10/";
		byte[] buffer = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buffer,buffer.length,bc_addr, Constants.BROADCAST_PORT);
		System.out.println("Disconnection datagram outpacket created");
		// send disconnect message
		dgramSocket.send(outPacket);
		System.out.println("Disconnection message sent. Closing UDP Socket");
		//close dgramSocket
		dgramSocket.close();
	}
	
	public static void sendChangePseudoMessage(InetAddress senderAddress) throws IOException {
		DatagramSocket dgramSocket = new DatagramSocket(Constants.UDP_PORT);
		// create changePseudo Message
		String mesg = "11/";
		byte[] buffer = mesg.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buffer,buffer.length,senderAddress, Constants.UDP_PORT);
		System.out.println("changePseudo datagram outpacket created");
		// send changePseudo Message
		dgramSocket.send(outPacket);
		System.out.println("changePseudo Message sent. Closing UDP Socket");
		//close dgramSocket
		dgramSocket.close();
	}
	
	public void run() {
		try {
			get_BcAddr();
			create_UDP_Sender();
			broadcast_pseudo();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}