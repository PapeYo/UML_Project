package fr.insa.chat_app;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.ResourceBundle;
import database_management.db_users_manager;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Controller implements Initializable {
	
	public static Controller controller;
	
	private String myPseudo;

	// (adresse ip,indice du tab)
	public HashMap<String, Integer> openedConv;

	@SuppressWarnings("exports")
	public FXMLLoader convTabLoader;
	@SuppressWarnings("exports")
	public FXMLLoader convTabLoaderTest;
	Parent tabContent;
	Parent tabContentTest;

	private ObservableList<String> userListVector;

	private ListView<String> users;

	@SuppressWarnings("exports")
	@FXML
	public TabPane tabPane;

	@FXML
	protected ListView<String> userList;


	public void initialize(URL arg0, ResourceBundle arg1) {
		this.userListVector = FXCollections.observableArrayList();
		this.users = new ListView<String>();
		this.users.setItems(this.userListVector);
				
		// Loading necessaire pour les tab
		convTabLoader = new FXMLLoader(getClass().getResource("ConvTab.fxml"));
		convTabLoaderTest = new FXMLLoader(getClass().getResource("ConvTab.fxml"));

		System.out.println("Controller, affichage de userList à l'init :" + userList.getItems());
		loadAllPseudo();

		// HashMap qui va lier un pseudo et un l'indice de la tab dans le tabPane
		openedConv = new HashMap<String, Integer>();

		// EventListener qui verifie quel utilisateur est selectionné et ouvre la tab
		// correspondante.
		userList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				String ippartner;
				try {
					ippartner = db_users_manager.getUserIP(userList.getSelectionModel().getSelectedItem());
					openTab(ippartner);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void openTab(String ippartner) throws SQLException {
		
		String partnerPseudo = db_users_manager.getUserPseudo(ippartner);
		// Tab déja ouverte
		if (openedConv.containsKey(ippartner)) {
			tabPane.getSelectionModel().select(openedConv.get(ippartner));

			// Tab pas encore ouverte
		} else {
			// met le pseudo et sa clé dans le hashMap local
			openedConv.put(ippartner, tabPane.getTabs().size());

			convTabLoader = new FXMLLoader(getClass().getResource("ConvTab.fxml"));

			try {
				tabContent = convTabLoader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ConvTabController tabControl = convTabLoader.getController();
			tabControl.setLabel(partnerPseudo);
			tabControl.setIpReceiver(ippartner);
			System.out.println("Controller, receiverPseudo :"+ippartner);
			tabControl.setMyPseudo(myPseudo);
			tabControl.loadHistory();
			tabControl.setControl(controller);

			// Création de la tab
			Tab tab = new Tab(partnerPseudo);
			tab.setContent(tabContent);
			tabPane.getTabs().add(tab);

			// Met la selection sur la tab qui vient d'être ouverte
			tabPane.getSelectionModel().select(openedConv.get(ippartner));
		}
	}
	
	public void closeTab(String ip) {
		tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
		openedConv.remove(ip);
		System.out.println("sucess closing");
	}

	@FXML
	public void createGroupConv() {
		System.out.println("Controller : Group conversation creation");
	}

	@SuppressWarnings("exports")
	@FXML
	public void changePseudo(ActionEvent e) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPane.fxml"));
		Parent root;
		try {
			root = loader.load();
			LoginPaneController control = loader.getController();
			control.changeButtonName();
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			System.out.println("Controller : changement de pseudo, ancien pseudo =" + myPseudo);
			removePseudo(myPseudo);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void setPseudo(String pseudo) {
		myPseudo = pseudo;
	}

	@SuppressWarnings("exports")
	@FXML
	public void logout(ActionEvent e) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogoutPane.fxml"));
		Parent root;
		try {
			root = loader.load();
			LogoutPaneController control = loader.getController();
			control.setPseudo(myPseudo);
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			control.setStage(stage);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
			stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void loadAllPseudo() {
		System.out.println("Controller : Loading de tous les pseudos de la BDD");
		ArrayList<String> list = db_users_manager.listOfPseudo();
		ListIterator<String> li = list.listIterator();
		while (li.hasNext()) {
			System.out.println("controler : pseudo a ajouter =" + li.next());
			li.previous();
			addPseudo(li.next());
		}
	}

	public void addPseudo(String pseudo) {
		System.out.println("Controller: trying to add pseudo:" + pseudo);
		System.out.println("Controller : userList =" + userList.getItems());
	}

	public void removePseudo(String pseudo) {
		userList.getItems().remove(pseudo);
	}

	public static void setController(Controller control) {
		controller = control;
		
	}

}
