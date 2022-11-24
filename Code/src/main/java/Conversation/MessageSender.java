package Conversation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalTime;

import database_management.db_conv_manager;

public class MessageSender {
	
	final static String serverHost = "localhost";
	static Socket link;
	static PrintWriter out = null;
	
	// 
	static String message;
	static String ippartner;
	static String ipsender;
	static String ipreceiver;
	static Timestamp timestamp;
	
	public static void getMessageInfo() throws IOException {
		// message = read from JFieldText
		ippartner = link.getInetAddress().getHostAddress();
		ipsender = getIPlocalhost();
		ipreceiver = ippartner;
		message = "il est " + LocalTime.now();
		timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public static String getIPlocalhost() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	public static void addToDb() {
		// add message to database
		db_conv_manager.insertMessage(ippartner, ipsender, ipreceiver, "Sender : " + message, timestamp);
		// display message history
		db_conv_manager.selectALLmessages();
		db_conv_manager.disconnect();
	}
	
	public static void main(String args[]) throws UnknownHostException {		
		try {
			link = new Socket("192.168.1.95", 7777);
			System.out.println(link);
			out = new PrintWriter(link.getOutputStream(),true);
			getMessageInfo();
			addToDb();
			out.println(message);
			out.close();
			link.close();
		} catch (IOException e) {
				System.out.println(e);
		}
	}
}