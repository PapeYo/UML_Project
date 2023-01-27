package conversation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import database_management.db_conv_manager;
import setup.Constants;

/*
 * Thread running temporarily when necessary.
 * Handles all TCP packets sent (conversation messages with other users).
 */
public class TCP_Sender extends Thread {
	
	/* Attributes */
	protected String ippartner;
	protected String ipsender;
	protected String ipreceiver;
	protected Timestamp timestamp;
	protected String message;
	protected Socket link;
	protected PrintWriter out = null;
	
	
	
	/** Constructor */
	public TCP_Sender(String partnerAddress, String message) throws SocketException, UnknownHostException {
		this.ippartner = partnerAddress;
		this.ipsender = Constants.get_LocalIP();
		this.ipreceiver = this.ippartner;
		this.message = message;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		start();
	}
	
	
	
	public void run() {		
		try {
			link = new Socket(ippartner, Constants.TCP_RECEIVER_PORT);
			System.out.println(link);
			out = new PrintWriter(link.getOutputStream(),true);
			db_conv_manager.insertMessage(ippartner, ipsender, ipreceiver, message, timestamp);
			out.println(message);
			out.close();
			link.close();
		} catch (IOException e) {
				System.out.println(e);
		}
	}
}