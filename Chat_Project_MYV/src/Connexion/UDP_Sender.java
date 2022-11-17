package Connexion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Setup.Constants;

public class UDP_Sender {
	public static void main(String args[]) throws IOException {
	//ex
	String msg="Hello";
	/* setup UDP */
	byte [] BC_IP = {10,1,(byte) 255,(byte) 255};
	InetAddress bc_addr;
	bc_addr = InetAddress.getByAddress(BC_IP);
	System.out.println("en vrai ça va");
	DatagramSocket dgramSocket = new DatagramSocket();
	System.out.println("en vrai ça va");
	DatagramPacket outPacket = new DatagramPacket(msg.getBytes(),msg.length(),bc_addr, Constants.BROADCAST_PORT);
	dgramSocket.send(outPacket);
	System.out.println("en vrai ça va");
	byte[] buffer = new byte[256];
	DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
	dgramSocket.receive(inPacket);
	String mesg = new String(inPacket.getData(),0,inPacket.getLength());
	System.out.println(mesg);
	
	dgramSocket.close();
	}
}

// systeme de thread pour la réception (sur le sender) + dictionnaire (@IP,pseudo) + différencier déco, co, answer en émission UDP (1° string=0,1,2 ?) + DB (users)