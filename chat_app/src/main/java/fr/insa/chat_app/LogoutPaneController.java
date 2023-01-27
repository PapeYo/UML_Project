package fr.insa.chat_app;

import java.io.IOException;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LogoutPaneController implements Initializable  {
	
	protected String pseudo;
	
	@FXML
	protected Button yesLogoutButton;
	
	@FXML
	protected Button noLogoutButton;
	
	private Stage stage;
	
	public void setStage(@SuppressWarnings("exports") Stage stage) {
		this.stage = stage;
	}
	
	public void setPseudo(String p) {
		pseudo = p;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		noLogoutButton.setDefaultButton(true);
	}
	
	
	@SuppressWarnings("exports")
	@FXML
	public void yesLogout(ActionEvent e ) throws IOException {
		System.out.println("LogoutPane Controller : fermeture de la fenêtre");
		UDP_Sender.disconnect();
		App.closeApp();

	}
	
	@SuppressWarnings("exports")
	@FXML
	public void noLogout(ActionEvent e) {
		System.out.println("-----------------------------------------------------------------------------------");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatInterface.fxml"));
		Parent root;
		try {
			root = loader.load();
			Controller control = loader.getController();
			Controller.setController(control);
			
			control.setPseudo(pseudo);
			Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			
			Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
			stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}