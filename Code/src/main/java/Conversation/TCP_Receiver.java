
package Conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

import Setup.Constants;
import database_management.db_conv_manager;

public class TCP_Receiver extends Thread {

	String ippartner;
	String ipsender;
	String ipreceiver;
	Timestamp timestamp;
	ServerSocket servSocket;
	String message;
	BufferedReader in;
	Socket link = null;
	
	public TCP_Receiver() {
		start();
	}
	
	public void connectPort(int portnumber) {
		try {
			servSocket = new ServerSocket(portnumber);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void receivePacket() throws IOException {
		link = servSocket.accept();
		System.out.println("Accepted");
		// create message reader
		in = new BufferedReader(new InputStreamReader(link.getInputStream()));
		// read the conversation communication
		getMessageInfo();
		System.out.println("Message recu = " + message);
	}
	
	public void getMessageInfo() throws IOException {
		message = in.readLine();
		ipreceiver = Constants.get_LocalIP();
		ippartner = link.getInetAddress().getHostAddress();
		ipsender = ippartner;
		timestamp = new Timestamp(System.currentTimeMillis());
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
		// connection to database
		db_conv_manager.connect();
		// listens on port number 3000
		connectPort(Constants.TCP_RECEIVER_PORT);
		while(true) {
			System.out.println("Connected to port number " + Constants.TCP_RECEIVER_PORT);
			System.out.println("Waiting to accept");
			receivePacket();
			addToDb();
		}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}