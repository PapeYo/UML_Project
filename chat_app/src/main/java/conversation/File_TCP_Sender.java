package conversation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import database_management.db_conv_manager;
import setup.Constants;

public class File_TCP_Sender extends Thread {

	/* Attributes */
	protected String ippartner;
	protected String ipsender;
	protected String ipreceiver;
	protected Timestamp timestamp;
	protected String path;
	protected Socket link;
	protected PrintWriter out = null;
	private static DataOutputStream dataOutputStream = null;
    @SuppressWarnings("unused")
	private static DataInputStream dataInputStream = null;
	
	
	
	/* Constructor */
	public File_TCP_Sender(String partnerAddress, String path) throws SocketException, UnknownHostException {
		this.ippartner = partnerAddress;
		this.ipsender = Constants.get_LocalIP();
		this.ipreceiver = this.ippartner;
		this.path = path;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		start();
	}
	
	
	
	public static String choose_file() throws IOException {
		String res = null;
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter pdf_filter = new FileNameExtensionFilter(
		        "PDF files", "pdf");
	    FileNameExtensionFilter txt_filter = new FileNameExtensionFilter(
	        "Text files", "txt");
	    FileNameExtensionFilter img_filter = new FileNameExtensionFilter(
		        "Images", "jpg", "png", "jpeg", "gif");
	    chooser.setFileFilter(pdf_filter);
	    chooser.setFileFilter(txt_filter);
	    chooser.setFileFilter(img_filter);
	    int returnVal = chooser.showOpenDialog(chooser);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            chooser.getSelectedFile().getName());
	       System.out.println("URL fichier : " + chooser.getSelectedFile().getAbsolutePath());
	       res = chooser.getSelectedFile().getAbsolutePath();
	    }
		return res;
	}
	
	
	
	@SuppressWarnings("unused")
	private static void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        String filepath = path;
        String[] filepath_parts = filepath.split(Pattern.quote("\\"));
        String fileName = filepath_parts[filepath_parts.length-1];
        // send file size
        dataOutputStream.writeLong(file.length());  
        // send file name
        dataOutputStream.writeUTF(fileName);
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }
	
	
	
	public void run() {
		try (Socket socket = new Socket(ippartner,Constants.FILE_TCP_PORT)) {
            dataInputStream = new DataInputStream(socket.getInputStream());
			db_conv_manager.insertMessage(ippartner, ipsender, ipreceiver, path, timestamp);
            sendFile(this.path);
			out.close();
			link.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
