package setup;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/*
 * Contains all constants (port number, local IP address, etc) or functions to get them
 */
public class Constants{
	
	/*
	 *  Constants
	 */
	
	/* The UDP port the machine is listening to */
	public static final int UDP_RECEIVER_PORT = 2999;
	/* The UDP port the machine is sending from */
	public static final int UDP_SENDER_PORT = 2998;
	/* the TCP port the machine is listening to */
	public static final int TCP_RECEIVER_PORT = 3000;
	/* The UDP port the machine is listening to */
	public static final int FILE_TCP_PORT = 3001;
	/* The database URL in the project folder (relative route : "./chat_app_db.db") */
	public static final String db_url = "jdbc:sqlite:chat_app_db.db";
	
	
	
	
	/**
	 * Returns the IP address of our machine (equivalent to an id, we suppose that one IP address = 1 user)
	 * 
	 * @return an ipaddress (String)
	 * @throws UnknownHostException
	 */
	public static String get_LocalIP() throws SocketException {
		String ip = null;
		Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
		while(networkInterfaceEnumeration.hasMoreElements()) {
			for(InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses()) {
				if(interfaceAddress.getAddress().isSiteLocalAddress()) {
					ip = interfaceAddress.getAddress().getHostAddress();
					return ip;
				}
			}
		}
		return ip;
	}
	
	
	
	
	/**
	 * Returns the broadcast address related to our IP address
	 * 
	 * @return an ipaddress (String)
	 * @throws SocketException
	 */
	public static InetAddress get_BcIP() throws SocketException {
		InetAddress bc = null;
		Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
		while(networkInterfaceEnumeration.hasMoreElements()) {
			for(InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses()) {
				if(interfaceAddress.getAddress().isSiteLocalAddress()) {
					bc = interfaceAddress.getBroadcast();
					return bc;
				}
			}
		}
		return bc;
	}
}
