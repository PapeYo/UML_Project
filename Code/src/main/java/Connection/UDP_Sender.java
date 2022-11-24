package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;

import Setup.Constants;
import database_management.db_users_manager;

public class UDP_Sender {
	public static void main(String args[]) throws IOException {
		String pseudo="PapeYo";
		/* setup UDP */
		
		// get localhost broadcast address
		InetAddress bc_addr;
		NetworkInterface nif = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
		bc_addr = nif.getInterfaceAddresses().get(0).getBroadcast();
		System.out.println("BroadCast address : " + bc_addr);
		
		// create pseudo message
		DatagramSocket dgramSocket = new DatagramSocket();
		System.out.println("datagram socket created");
		DatagramPacket outPacket = new DatagramPacket(pseudo.getBytes(),pseudo.length(),bc_addr, Constants.BROADCAST_PORT);
		System.out.println("datagram outpacket created");
		
		// send pseudo message
		dgramSocket.send(outPacket);
		System.out.println("outpacket sent");
		
		// receive "pseudo received" confirmation
		byte[] buffer = new byte[256];
		DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
		System.out.println("waiting for answer");
		dgramSocket.receive(inPacket);
		String mesg = new String(inPacket.getData(),0,inPacket.getLength());
		System.out.println(mesg);
		if (mesg.equals("Pseudo well received")) {
			String localhostIP = InetAddress.getLocalHost().toString().replaceAll(".*/", "");
			db_users_manager.updateUserTable(localhostIP, pseudo);
			db_users_manager.selectALLusers();
		}
		dgramSocket.close();
	}
}


// modif obtention localhostIP
// systeme de thread pour la réception (sur le sender) + dictionnaire (@IP,pseudo) + différencier déco, co, answer en émission UDP (1° string=0,1,2 ?) + DB (users)