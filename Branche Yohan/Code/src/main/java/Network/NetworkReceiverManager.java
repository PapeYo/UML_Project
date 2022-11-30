package Network;

import Connection.UDP_Receiver;

public class NetworkReceiverManager {
	public static void main(String arg[]) {
		UDP_Receiver udp_receiver = new UDP_Receiver();
		udp_receiver.start();
	}
}
