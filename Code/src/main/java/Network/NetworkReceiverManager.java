package Network;

import java.io.IOException;
<<<<<<< HEAD
=======
import java.net.DatagramPacket;
import java.net.DatagramSocket;

>>>>>>> 9e7f340d983180d5a890383a68aeabf997cacf17
import Connection.*;

public class NetworkReceiverManager {
	
<<<<<<< HEAD
=======
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
	
>>>>>>> 9e7f340d983180d5a890383a68aeabf997cacf17
	public static void main(String arg[]) throws IOException {
		UDP_Receiver udp_receiver = new UDP_Receiver();
		receiveAnswer_RtoS();
	}
}
