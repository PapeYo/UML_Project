package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Setup.Constants;

public class UDP_Receiver1 {
	public static void main(String args[]) throws IOException {
	String pseudo = "moi";
	/* setup UDP */
	DatagramSocket dgramSocket = new DatagramSocket(Constants.BROADCAST_PORT);
	System.out.println("dgram socket created");
	byte[] buffer = new byte[256];
	DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
	System.out.println("inpacket created");
	dgramSocket.receive(inPacket);
	System.out.println("inpacket received");
	InetAddress senderAddress = inPacket.getAddress();
	int senderPort = inPacket.getPort();
	System.out.println("ca va 4");
	String msg = new String(inPacket.getData(),0,inPacket.getLength());
	System.out.println(msg);
	DatagramPacket outPacket = new DatagramPacket(pseudo.getBytes(), pseudo.length(),
			senderAddress, senderPort);
	dgramSocket.send(outPacket);
	dgramSocket.close();
	}
}