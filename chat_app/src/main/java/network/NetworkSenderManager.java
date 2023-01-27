package network;

import connection.*;
import conversation.File_TCP_Sender;
import conversation.TCP_Sender;

/*
 * Threads running only when necessary.
 * Handles all packets sent (UDP and TCP).
 */
public class NetworkSenderManager {
	
	public static UDP_Sender udp_sender;
	public static TCP_Sender tcp_sender;
	public static File_TCP_Sender file_tcp_sender;
}