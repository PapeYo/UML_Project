package Network;

import java.net.SocketException;
import java.net.UnknownHostException;
import Connection.*;
import Conversation.TCP_Sender;

public class NetworkSenderManager {
	
	public static void main(String arg[]) throws SocketException, UnknownHostException {
		UDP_Sender udp_sender = new UDP_Sender("Yohan_Sender");
		TCP_Sender tcp_sender = new TCP_Sender("10.188.149.254","Coucouuuuu");
	}
}