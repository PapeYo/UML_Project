package Conversation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		ippartner = "166.166.12.8";
		ipsender = "166.166.12.8";
		try {
			database_management.db_conv_manager.connect();
			ipreceiver = (InetAddress.getLocalHost()).toString();
			ipreceiver = ipreceiver.replaceAll(".*/", "");
			link = servSocket.accept();
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			message = in.readLine();
			timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println("Message recu = " + message);
			database_management.db_conv_manager.insertMessage(ippartner, ipsender, ipreceiver, message, timestamp);
			// afficher l'historique des messages
			database_management.db_conv_manager.selectALLmessages();
			database_management.db_conv_manager.disconnect();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}