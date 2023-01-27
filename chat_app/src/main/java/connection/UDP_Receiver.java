package connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;

import app_interface.model.Message;
import database_management.db_users_manager;
import fr.insa.chat_app.Controller;
import fr.insa.chat_app.LoginPaneController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import setup.Constants;
import fr.insa.chat_app.Controller;

/*
 * Thread running permanently while the application is launched.
 * Handles all UDP packets received (connection, disconnection, pseudo updates).
 */
@SuppressWarnings("unused")
public class UDP_Receiver extends Thread {

	/* Attributes */
	protected DatagramSocket dgramSocket;
	protected DatagramPacket inPacket;
	protected InetAddress senderAddress;
	protected int senderPort;
	protected String pseudo;
	public static Stage stage;






	/** Constructor */
	public UDP_Receiver() {
		start();
	}
	
	

	/**
	 * Creates the UDP_Receiver Datagram Socket with port number 2999.
	 * 
	 * @param portNumber=2999 is the port used for receiving all UDP messages
	 * @throws SocketException
	 */
	public void createUDP_Receiver(int portNumber) throws SocketException {
		dgramSocket = new DatagramSocket(portNumber);
		System.out.println("UDP_receiver : Datagram socket created and listening on port " + portNumber);
	}
	
	
	
	/**
	 * Each time a UDP packet is received, gets the message sender's address and calls
	 * the function (analyzePacket) that analyzes the packet's content.
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	private void receivePacket() throws IOException, SQLException {
		byte[] buffer = new byte[256];
		System.out.println("UDP_receiver : Local address is : " + Constants.get_LocalIP());
		System.out.println("UDP_receiver : Broadcast address is : " + Constants.get_BcIP());
		while(true) {
			inPacket = new DatagramPacket(buffer,buffer.length);
			System.out.println("UDP_receiver : Waiting for message");
			dgramSocket.receive(inPacket);
			System.out.println("UDP_receiver : New message received");
			System.out.println("UDP_receiver : Analyze in progress");
			analyzePacket(inPacket);
		}
	}
	
	
	
	/**
	 * <i>Each message has the same pattern : "[01]{2}/[.]*"</i> <br><br>
	 * <b>There are multiple cases :</b> <br>
	 * <b>00/[.]* 	-></b> 	We receive someone's broadcast pseudo message, update the user table with their pseudo
	 * 				and send their back a message with our pseudo if they are not in our database.<br>
	 * <b>01/[.]* 	-></b> 	Someone's response to our previous broadcast pseudo message with their pseudo and add 
	 * 				it to our database. <br>
	 * <b>10/ 		-></b>  Someone is disconnecting so we remove them from our database. <br>
	 * <b>11/ 		-></b>  We chose a pseudo already chosen by someone connected so we need to change. 
	 * 
	 * @param inPacket is the packet that was received
	 * @throws SQLException
	 * @throws IOException
	 */
	public void analyzePacket(DatagramPacket inPacket) throws SQLException, IOException {
		Controller controller = new Controller();
		senderAddress = inPacket.getAddress();
		String saString = senderAddress.toString().replaceAll("/","");
		System.out.println("UDP_Receiver : Sender address is " + saString);
		String mesg = new String(inPacket.getData(),0,inPacket.getLength());
		System.out.println("UDP_receiver : " + mesg);
		
		/* Broadcast pseudo */
		if (mesg.startsWith("00/")){
			String pseudo = mesg.replaceFirst("00/", "");
			System.out.println("UDP_receiver : Pseudo : " + pseudo + ".");
			
			if (saString.equals(Constants.get_LocalIP())) {
				if (db_users_manager.existingUserWithIP(saString)) {
					System.out.println("UDP_receiver : modif de soi");
					Controller.controller.removePseudo(db_users_manager.getUserPseudo(saString));
					db_users_manager.updateUserTable(Constants.get_LocalIP(), pseudo);
					Controller.controller.addPseudo(pseudo);
				} else {
				System.out.println("UDP_receiver : ajout de soi");
				db_users_manager.updateUserTable(Constants.get_LocalIP(), pseudo);
				Controller.controller.addPseudo(pseudo);
				}
			}	
			else if (!((db_users_manager.existingUserWithPseudo(pseudo).equals(saString)) || (db_users_manager.existingUserWithPseudo(pseudo).equals("0.0.0.0")))) {
				System.out.println("UDP_receiver: pseudo pas unique, compliqué");
				UDP_Sender.sendChangePseudoMessage(senderAddress);
			}
			else {
				System.out.println("UDP_receiver: Ajout / update d'un user");
				if(db_users_manager.existingUserWithIP(saString)) {
					Controller.controller.removePseudo(db_users_manager.getUserPseudo(saString));
					db_users_manager.updateUserTable(saString, pseudo);
					Controller.controller.addPseudo(pseudo);
				} else {
					db_users_manager.updateUserTable(saString, pseudo);
					Controller.controller.addPseudo(pseudo);
					UDP_Sender.sendAnswer_RtoS(senderAddress);
				}
			}
		}
		
		/* Answer to broadcast */
		else if (mesg.startsWith("01/")) {
			System.out.println("UDP_receiver : Accusé de réception reçu");
			String pseudo = mesg.replaceFirst("01/", "");
			db_users_manager.updateUserTable(saString, pseudo);
			Controller.controller.addPseudo(pseudo);
		}
		
		/* Disconnection */
		else if (mesg.startsWith("10/")) {
			if (!(saString.equals(Constants.get_LocalIP()))){
				db_users_manager.removeUser(saString);
				System.out.println("UDP_Receiver, adresse à supprimer: "+saString);
				System.out.println("UDP_Receiver, pseudo à supprimer: "+db_users_manager.getUserPseudo(saString));
				Controller.controller.removePseudo(db_users_manager.getUserPseudo(saString));
			}
		}
		
		/* Invalid pseudo */
		else if (mesg.startsWith("11/")){
			//TO DO : InterfaceLogin.openWindow();
			System.out.println("UDP_receiver : En vrai là va falloir changer de pseudo gars");
			
			
			VBox loginPane;
			Scene scene = null;
			LoginPaneController loginPaneController;
			FXMLLoader loginPaneLoader = new FXMLLoader(getClass().getResource("LoginPane.fxml"));
			
			
	        try {
	        	
				loginPane = loginPaneLoader.load();
				scene = new Scene(loginPane);
				loginPaneController = loginPaneLoader.getController();
				System.out.println("App : Login pane loading sucess");
				
			} catch (IOException e) {
				System.out.println("App : Login pane loading failed");
				e.printStackTrace();
			}
	        
			/////////////////////////////////////////////////////////////////////////////////////////////
	        stage.setResizable(false);
	        stage.setTitle("CraquApp");
			
			stage.setScene(scene);
			stage.show();
			
			Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
			stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
		}
	}
	
	public static void setStage(Stage thestage) {
		stage = thestage;
	}

	
	
	public void run() {
		try {
			createUDP_Receiver(Constants.UDP_RECEIVER_PORT);
			receivePacket();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
