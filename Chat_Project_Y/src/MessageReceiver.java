import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageReceiver {
	
	public static void main(String args[]) {
			
		ServerSocket servSocket;
	    String input;
	    BufferedReader in;
	    Socket link = null;
		
		try {
			servSocket = new ServerSocket(6667);
			link = servSocket.accept();
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			input = in.readLine();
			System.out.println("Message recu = " + input);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
