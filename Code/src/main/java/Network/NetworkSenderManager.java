package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import Connection.*;
import Setup.Constants;

public class NetworkSenderManager {
	
	public void sendAnswer_RtoS(InetAddress senderAddress) throws IOException {
		DatagramSocket dgramSocket = new DatagramSocket();
		byte[] buffer = new byte[256];
		DatagramPacket answerPacket = new DatagramPacket(buffer, buffer.length, senderAddress, Constants.BROADCAST_PORT);
		dgramSocket.send(answerPacket);
		System.out.println("Answer packet sent");
		dgramSocket.close();
	}
	
	public static void main(String arg[]) throws SocketException {
		UDP_Sender udp_sender = new UDP_Sender("Yohan");
	}
}
