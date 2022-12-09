package udp;

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
		while(networkInterfaceEnumeration.hasMoreElements()){
			for (InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses()) {
				if (interfaceAddress.getAddress().isSiteLocalAddress()) {
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
		dgramSocket = new DatagramSocket();
		System.out.println("datagram socket created");
	}
	
	public void broadcast_pseudo() throws IOException {
		// create pseudo message
		byte[] buf = pseudo.getBytes();
		DatagramPacket outPacket = new DatagramPacket(buf,buf.length,bc_addr, Constants.BROADCAST_PORT);
		System.out.println("datagram outpacket created");
		// send pseudo message
		dgramSocket.send(outPacket);
		System.out.println("Pseudo packet sent");
		db_users_manager.updateUserTable(localIP, pseudo);
	}
	
	/*
	 * private void changePseudo(String pseudo) throws IOException {
	 * broadcast_pseudo(); String localhostIP =
	 * InetAddress.getLocalHost().getHostAddress();
	 * db_users_manager.updateUserTable(localhostIP, pseudo); }
	 */
	
	public void disconnect() throws IOException {
		// create disconnect message
		DatagramPacket outPacket = new DatagramPacket(null,-1,bc_addr, Constants.BROADCAST_PORT);
		System.out.println("Disconnection datagram outpacket created");
		// send disconnect message
		dgramSocket.send(outPacket);
		System.out.println("Disconnection message sent. Closing UDP Socket");
		//close dgramSocket
		dgramSocket.close();
	}
	
	public void get_answer() throws IOException {
		byte[] buffer = new byte[256];
		DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
		System.out.println("waiting for answer");
		dgramSocket.receive(inPacket);
		String mesg = new String(inPacket.getData(),0,inPacket.getLength());
		System.out.println(mesg);
	}

	public void run() {
		try {
			get_BcAddr();
			create_UDP_Sender();
			broadcast_pseudo();
			get_answer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
