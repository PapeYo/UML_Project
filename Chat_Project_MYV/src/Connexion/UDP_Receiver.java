package Connexion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Setup.Constants;

public class UDP_Receiver {
	public static void main(String args[]) throws IOException {
	String pseudo = "moi";
	/* setup UDP */
	DatagramSocket dgramSocket = new DatagramSocket(Constants.BROADCAST_PORT);
	System.out.println("en vrai ça va");
	byte[] buffer = new byte[256];
	DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
	dgramSocket.receive(inPacket);
	InetAddress senderAddress = inPacket.getAddress();
	int senderPort = inPacket.getPort();
	System.out.println("en vrai ça va");
	String msg = new String(inPacket.getData(),0,inPacket.getLength());
	System.out.println(msg);
	DatagramPacket outPacket = new DatagramPacket(pseudo.getBytes(), pseudo.length(),
			senderAddress, senderPort);
	dgramSocket.send(outPacket);
	dgramSocket.close();
	}
}