package network;

import java.io.IOException;

import connection.*;
import conversation.File_TCP_Receiver;
import conversation.TCP_Receiver;

/*
 * Thread running permanently while the application is launched.
 * Handles all packets received (UDP and TCP). 
 */
public class NetworkReceiverManager {
	
	/**
	 * Starts both UDP and TCP receiver threads, running permanently.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static void init_receivers() throws IOException {
		UDP_Receiver udp_receiver = new UDP_Receiver();
		TCP_Receiver tcp_receiver = new TCP_Receiver();
		File_TCP_Receiver file_tcp_receiver = new File_TCP_Receiver();
	}
}