package Conversation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;

public class MessageSender {
	
	public static void main(String args[]) throws UnknownHostException {
			
		final String serverHost = "localhost";
		Socket link;
		PrintWriter out = null;
		
		try {
			// InetAddress address = InetAddress.getLocalHost();
			link = new Socket(serverHost, 6667);
			out = new PrintWriter(link.getOutputStream(),true);
			out.println("il est " + LocalTime.now());
			out.close();
			link.close();
		} catch (IOException e) {
				System.out.println(e);
		}
	}
}