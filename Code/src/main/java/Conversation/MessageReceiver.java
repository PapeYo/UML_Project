
package Conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import database_management.db_conv_manager;

public class MessageReceiver {

	static String ippartner;
	static String ipsender;
	static String ipreceiver;
	static Timestamp timestamp;
	static ServerSocket servSocket;
	static String message;
	static BufferedReader in;
	static Socket link = null;
	
	public static void connectPort(int portnumber) {
		try {
			servSocket = new ServerSocket(portnumber);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static String getIPlocalhost() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	public static void getMessageInfo() throws IOException {
		message = in.readLine();
		ipreceiver = getIPlocalhost();
		ippartner = link.getInetAddress().getHostAddress();
		ipsender = ippartner;
		timestamp = new Timestamp(System.currentTimeMillis());
	}

	public static void addToDb() {
		// add message to database
		db_conv_manager.insertMessage(ippartner, ipsender, ipreceiver, "Receiver : " + message, timestamp);
		// display message history
		db_conv_manager.selectALLmessages();
		db_conv_manager.disconnect();
	}

	public static void main(String args[]) {
		try {
			// connection to database
			db_conv_manager.connect();
			
			// listens on port number 7777
			connectPort(7777);
			System.out.println("Connected to port number 7777");
			System.out.println("Waiting to accept");
			link = servSocket.accept();
			System.out.println("Accepted");
			// create message reader
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			
			// read the conversation communication
			getMessageInfo();
			System.out.println("Message recu = " + message);
			
			addToDb();
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}