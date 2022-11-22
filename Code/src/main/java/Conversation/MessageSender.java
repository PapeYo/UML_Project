package Conversation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalTime;

public class MessageSender {
	
	final static String serverHost = "localhost";
	static Socket link;
	static PrintWriter out = null;
	
	static String message;
	static String ippartner;
	static String ipsender;
	static String ipreceiver;
	static Timestamp timestamp;
	
	public static void getMessageInfo() throws IOException {
		// message = read from JFieldText
		message = "il est " + LocalTime.now();
		ipreceiver = ippartner;
		timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public static String getIPlocalhost() throws UnknownHostException {
		return (InetAddress.getLocalHost()).toString().replaceAll(".*/", "");
	}
	
	public static void main(String args[]) throws UnknownHostException {		
		try {
			link = new Socket(serverHost, 6667);
			out = new PrintWriter(link.getOutputStream(),true);
			ipsender = getIPlocalhost();
			getMessageInfo();
			out.println(message);
			String ipsender = (InetAddress.getLocalHost()).toString().replaceAll(".*/", "");
			out.println(ipsender);
			out.close();
			link.close();
		} catch (IOException e) {
				System.out.println(e);
		}
	}
}