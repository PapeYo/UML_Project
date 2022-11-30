package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import Setup.Constants;
// import database_management.db_users_manager;

public class UDP_Sender extends Thread{
	
	private static InetAddress bc_addr;
	private NetworkInterface nif;
	private static DatagramSocket dgramSocket;
	
	/* setup UDP with constructor */
	public void create_UDP_Sender() throws SocketException, UnknownHostException {
		nif = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
		bc_addr = nif.getInterfaceAddresses().get(0).getBroadcast();
		System.out.println("BroadCast address : " + bc_addr);
		dgramSocket = new DatagramSocket();
		System.out.println("datagram socket created");
	}
	 
	public static void broadcast_pseudo(String pseudo) throws IOException {
		// create pseudo message
		DatagramPacket outPacket = new DatagramPacket(pseudo.getBytes(),pseudo.length(),bc_addr, Constants.BROADCAST_PORT);
		System.out.println("datagram outpacket created");
		// send pseudo message
		dgramSocket.send(outPacket);
		System.out.println("outpacket sent");
	}
	
	public static void disconnect() throws IOException {
		// create disconnect message
		DatagramPacket outPacket = new DatagramPacket(null,-1,bc_addr, Constants.BROADCAST_PORT);
		System.out.println("Disconnection datagram outpacket created");
		// send disconnect message
		dgramSocket.send(outPacket);
		System.out.println("Disconnection message sent. Closing UDP Socket");
		//close dgramSocket
		dgramSocket.close();
	}
	
	public static void main(String args[]) throws IOException {
		// receive "pseudo received" confirmation
		byte[] buffer = new byte[256];
		DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
		System.out.println("waiting for answer");
		dgramSocket.receive(inPacket);
		String mesg = new String(inPacket.getData(),0,inPacket.getLength());
		System.out.println(mesg);
	}
}

// systeme de thread pour la réception (sur le sender) + dictionnaire (@IP,pseudo) + différencier déco, co, answer en émission UDP (1° string=0,1,2 ?) + DB (users)