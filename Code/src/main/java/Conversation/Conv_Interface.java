package Conversation;

import javax.swing.*;

public class Conv_Interface {
	private static JFrame frame;
	private static JPanel textZone = new JPanel();
	private static JTextField message;
	
	private static void openWindow() {
		frame = new JFrame("Conversation");
		createMessageZone();
		frame.add(textZone);
		frame.setSize(1400, 800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private static void createMessageZone() {
		message = new JTextField();
		message.setBounds(20,40,50,60);
		textZone.setBounds(20,40,50,60);
		textZone.add(message);
	}
	
	public static void main(String args[]) {
		openWindow();
	}
}
