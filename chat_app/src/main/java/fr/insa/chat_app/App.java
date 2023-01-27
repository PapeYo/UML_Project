package fr.insa.chat_app;
	
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import app_interface.model.Message;
import connection.UDP_Receiver;
import connection.UDP_Sender;
import database_management.db_creator;
import database_management.db_manager;
import database_management.db_users_manager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.NetworkReceiverManager;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;


@SuppressWarnings("unused")
public class App extends Application {
	
	
	@SuppressWarnings("exports")
	public VBox loginPane;
	public LoginPaneController loginPaneController;
	
	@SuppressWarnings("exports")
	public Connection connection;

	@SuppressWarnings({ "exports", "unused" })
	public void start(Stage primaryStage) throws SQLException, IOException {
		
		////////setup de la procédure de fermeture de la fenêtre
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		    	
		    	if (Controller.controller != null) {
		    		 try {
						UDP_Sender.disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					App.closeApp();
		    	} else {
		    		App.closeApp();	
		    	}
		   }
		});

		if (connection == null) connection = db_manager.connect();
		
		////////////////////////////////////////Login////////////////////////////////////////////
		db_creator.createAll();
		db_manager.disconnect(connection);
		NetworkReceiverManager.init_receivers();
		FXMLLoader loginPaneLoader = new FXMLLoader(getClass().getResource("LoginPane.fxml"));
		
		ArrayList<Message> MessageListTest = new ArrayList<Message>();
		
        try {
			loginPane = loginPaneLoader.load();
			loginPaneController = loginPaneLoader.getController();
			System.out.println("App : Login pane loading sucess");
			
		} catch (IOException e) {
			System.out.println("App : Login pane loading failed");
			e.printStackTrace();
		}
        
		/////////////////////////////////////////////////////////////////////////////////////////////
		primaryStage.setResizable(false);
        primaryStage.setTitle("CraquApp");
		Scene scene = new Scene(loginPane);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
		primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
		
		UDP_Receiver.setStage(primaryStage);
		
	}
	
	protected static void closeApp() {
		System.out.println("App : everything's okay, just quitting");
		Platform.exit();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
}
