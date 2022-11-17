import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender {
	
	public static void main(String args[]) {
			
		final String serverHost = "localhost";
		Socket link;
		PrintWriter out = null;
		
		try {
			link = new Socket(serverHost, 6667);
			out = new PrintWriter(link.getOutputStream(),true);
			out.println("Message envoye");
			out.close();
			link.close();
			} catch (IOException e) {
				System.out.println(e);
			}
	}
}
