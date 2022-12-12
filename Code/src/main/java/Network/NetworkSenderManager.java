package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import Connection.*;
import Setup.Constants;

public class NetworkSenderManager {
	
<<<<<<< HEAD
=======
	public void sendAnswer_RtoS(InetAddress senderAddress) throws IOException {
		DatagramSocket dgramSocket = new DatagramSocket();
		byte[] buffer = new byte[256];
		DatagramPacket answerPacket = new DatagramPacket(buffer, buffer.length, senderAddress, Constants.BROADCAST_PORT);
		dgramSocket.send(answerPacket);
		System.out.println("Answer packet sent");
		dgramSocket.close();
	}
	
>>>>>>> 9e7f340d983180d5a890383a68aeabf997cacf17
	public static void main(String arg[]) throws SocketException {
		UDP_Sender udp_sender = new UDP_Sender("Yohan");
	}
}
