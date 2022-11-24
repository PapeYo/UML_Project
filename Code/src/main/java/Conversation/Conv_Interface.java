package Conversation;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Conv_Interface extends JFrame {

	public Conv_Interface(String pseudo) {
		// name of the frame
		super("Conversation with " + pseudo);
		
		// create the send button
		JButton sendButton = new JButton("Send");
		
		// create the area to type the message
		JTextArea message_area = new JTextArea();
		message_area.setColumns(20);
		message_area.setRows(5);

		JScrollPane messageScrollPane = new JScrollPane(message_area);
		messageScrollPane.setPreferredSize(new Dimension(250,100));

		JPanel message_pannel = new JPanel();
		message_pannel.add(messageScrollPane);
		message_pannel.add(sendButton);

		getContentPane().add(message_pannel);

		// set the parameters of the frame
		setSize(500,500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2); // place the window at the center of the screen
	}

	public static void main(String[] args) {
		JFrame frame = new Conv_Interface("Michel");
	}
}
