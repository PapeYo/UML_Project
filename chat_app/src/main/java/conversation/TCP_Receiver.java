package conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

import database_management.db_conv_manager;
import setup.Constants;

/*
 * Thread running permanently while the application is launched.
 * Handles all TCP packets received (conversation messages with other users). 
 */
public class TCP_Receiver extends Thread {

	/* Attributes */
	protected String ippartner;
	protected String ipsender;
	protected String ipreceiver;
	protected Timestamp timestamp;
	protected static ServerSocket servSocket;
	protected String message;
	protected BufferedReader in;
	protected Socket link = null;
	
	
	/** Constructor */
	public TCP_Receiver() {
		start();
	}
	
	
	
	/**
	 * Creates the TCP_Receiver Server Socket with port number 3000.
	 * 
	 * @param portnumber=3000 is the port used to receive every TCP message
	 */
	private void connectPort(int portnumber) {
		try {
			servSocket = new ServerSocket(portnumber);
			System.out.println("TCP_receiver : Connected to port number " + portnumber);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	/**
	 * Receives a conversation message and calls the getMessageInfo function and adds it to our database.
	 * 
	 * @throws IOException
	 */
	private void receivePacket() throws IOException {
		link = servSocket.accept();
		System.out.println("TCP_receiver : Accepted");
		in = new BufferedReader(new InputStreamReader(link.getInputStream()));
		getMessageInfo();
		System.out.println("TCP_receiver : Message recu = " + message+ ", ipsender = "+ipsender+", myIP = "+Constants.get_LocalIP());
		if (!(ipsender.equals(Constants.get_LocalIP()))) {	//we only add the msg to the db if we didn't send it to ourselves
			System.out.println("TCP_Receiver : ajout du message dans la db");
			db_conv_manager.insertMessage(ippartner, ipsender, ipreceiver, message, timestamp); 
		}
	}
	
	
	
	/**
	 * Set the attributes with the message informations.
	 * 
	 * @throws IOException
	 */
	public void getMessageInfo() throws IOException {
		message = in.readLine();
		ipreceiver = Constants.get_LocalIP();
		ippartner = link.getInetAddress().getHostAddress();
		ipsender = ippartner;
		timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	
	
	public void run() {
		try {
			connectPort(Constants.TCP_RECEIVER_PORT);
			while(true) {
				System.out.println("TCP_receiver : Waiting to accept");
				receivePacket();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}