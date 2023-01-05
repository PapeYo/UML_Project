package Network;

import java.io.IOException;
import Connection.*;
import Conversation.TCP_Receiver;

public class NetworkReceiverManager {
	
	public static void main(String arg[]) throws IOException {
		UDP_Receiver udp_receiver = new UDP_Receiver();
		TCP_Receiver tcp_receiver = new TCP_Receiver();
	}
}