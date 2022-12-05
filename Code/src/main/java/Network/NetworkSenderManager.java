package Network;

import java.net.SocketException;

import udp.UDP_Sender;

public class NetworkSenderManager {
	public static void main(String arg[]) throws SocketException {
		UDP_Sender udp_sender = new UDP_Sender("PapéYo");
	}
}
