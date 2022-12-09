package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Connection.*;

public class NetworkReceiverManager {
	
	public static void receiveAnswer_RtoS() throws IOException {
		DatagramSocket dgramSocket = new DatagramSocket();
		byte[] buffer = new byte[256];
		while(true) {
			DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
			System.out.println("waiting for answer");
			dgramSocket.receive(inPacket);
			String mesg = new String(inPacket.getData(),0,inPacket.getLength());
			System.out.println(mesg);
		}
	}
	
	public static void main(String arg[]) throws IOException {
		UDP_Receiver udp_receiver = new UDP_Receiver();
		receiveAnswer_RtoS();
	}
}
