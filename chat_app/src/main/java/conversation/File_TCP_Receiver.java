package conversation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import setup.Constants;

public class File_TCP_Receiver extends Thread {

	/* Attributes */
	private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
	protected static ServerSocket servSocket;
	protected Socket link = null;

    
    /** Constructor */
	public File_TCP_Receiver() {
		start();
	}
	
	
	private void connectPort (int portnumber) {
		try {
			servSocket = new ServerSocket(portnumber);
			System.out.println("File_TCP_receiver : Connected to port number " + portnumber);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * @throws Exception
	 */
	private void receiveFile() throws Exception{
		link = servSocket.accept();
		dataInputStream = new DataInputStream(link.getInputStream());
        dataOutputStream = new DataOutputStream(link.getOutputStream());
		int bytes = 0;        
        long size = dataInputStream.readLong();     // read file size
        String filename = dataInputStream.readUTF();   // read file name
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
    }
	
	public void run() {
		try {
			connectPort(Constants.FILE_TCP_PORT);
			while(true) {
				System.out.println("File_TCP_Receiver : Waiting to accept");
				receiveFile();
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}
