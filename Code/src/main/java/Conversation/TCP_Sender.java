package Conversation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;

import Setup.Constants;
import database_management.db_conv_manager;

public class TCP_Sender extends Thread {
	
	Socket link;
	PrintWriter out = null;
	
	String message;
	String ippartner;
	String ipsender;
	String ipreceiver;
	Timestamp timestamp;
	
	public TCP_Sender(String partnerAddress, String message) throws SocketException {
		this.ippartner = partnerAddress;
		this.ipsender = Constants.get_LocalIP();
		this.ipreceiver = this.ippartner;
		this.message = message;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		start();
	}
	
	public void addToDb() {
		// add message to database
		db_conv_manager.insertMessage(ippartner, ipsender, ipreceiver, message, timestamp);
		// display message history
		db_conv_manager.selectALLmessages();
		db_conv_manager.disconnect();
	}
	
	public void run() {		
		try {
			link = new Socket(ippartner, Constants.TCP_RECEIVER_PORT);
			System.out.println(link);
			out = new PrintWriter(link.getOutputStream(),true);
			addToDb();
			out.println(message);
			out.close();
			link.close();
		} catch (IOException e) {
				System.out.println(e);
		}
	}
}