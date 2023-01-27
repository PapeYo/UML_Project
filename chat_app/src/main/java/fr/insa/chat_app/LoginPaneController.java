package fr.insa.chat_app;

import java.net.URL;
import java.util.ResourceBundle;

import connection.UDP_Sender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import network.NetworkSenderManager;

public class LoginPaneController implements Initializable{
	private Stage stage;
	private Scene scene;
	private Parent root;
	@FXML
	protected Button loginButton;
	@FXML
	protected Label comment;
	@FXML
	protected TextField pseudoField;
	protected String pseudo;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginButton.setDefaultButton(true);
		
	}

	
	@SuppressWarnings("exports")
	@FXML
	public void login(ActionEvent e) throws Exception {
		
		pseudo = pseudoField.getText();
		System.out.println("LoginPaneController : pseudo ="+pseudo);		
		if (pseudo!="") {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatInterface.fxml"));
			root = loader.load();
			System.out.println("LoginPaneController : root pour le pseudo ok");
			
			//send pseudo to main pane
			Controller control = loader.getController();
			System.out.println("LoginPaneController : controller pour le pseudo ok");
			control.setPseudo(pseudo);
			Controller.setController(control);
			
			NetworkSenderManager.udp_sender = new UDP_Sender(pseudo);
			
			//change pane-window
			stage = (Stage)((Node) e.getSource()).getScene().getWindow();
			System.out.println("LoginPaneController : stage pour le pseudo ok");
			
			scene = new Scene(root);
			stage.setScene(scene);
			
			
			
			//center the stage
			Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
			stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
		}
	}
	
	
	
	public void noticeWrongPseudo() {
		comment.setText("Choose another pseudo");
		pseudoField.setText("");
	}
	
	
	public void changeButtonName() {
		loginButton.setText("Change Pseudo");
	}



	
}