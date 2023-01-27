package app_interface.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class Message extends VBox{

	private Label pseudo;
	private Text message;
	private Label date;
	
	public Message(int source, String Pseudo, String Message, String Date) {
		super();
		// source == 0 Me
		// source == 1 other
		
		
		pseudo = new Label(Pseudo);
		pseudo.setFont(Font.font("Arial" , FontWeight.BOLD, 12));
		pseudo.setStyle("-fx-text-fill: #226D68;");


		
		message = new Text(Message);
		message.setFont(Font.font("Arial" , FontWeight.NORMAL, 15));
		message.setStyle("-fx-fill: #D6955B;");
		
		date = new Label(Date);
		date.setFont(Font.font("Arial" , FontWeight.LIGHT, 9));
		date.setStyle("-fx-text-fill: #226D68;");


		message.setWrappingWidth(250);

		
		this.getChildren().addAll(pseudo,message,date);
		if (source == 0) {
			this.setAlignment(Pos.CENTER_RIGHT);
			message.setTextAlignment(TextAlignment.RIGHT);
		} else {
			this.setAlignment(Pos.CENTER_LEFT);
			message.setTextAlignment(TextAlignment.LEFT);
		}
		
	}
}
