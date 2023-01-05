package Setup;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Constants{
	public static final int UDP_RECEIVER_PORT = 2999;
	public static final int UDP_SENDER_PORT = 2998;
	public static final int TCP_RECEIVER_PORT = 3000;
	
	public static String get_LocalIP() throws SocketException {
		String ip = null;
		Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
		while(networkInterfaceEnumeration.hasMoreElements()) {
			for(InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses()) {
				if(interfaceAddress.getAddress().isSiteLocalAddress()) {
					ip = interfaceAddress.getAddress().getHostAddress();
					System.out.println("Local IP : " + ip);
				}
			}
		}
		return ip;
	}
	
	public static InetAddress get_BcIP() throws SocketException {
		InetAddress bc = null;
		Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
		while(networkInterfaceEnumeration.hasMoreElements()) {
			for(InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses()) {
				if(interfaceAddress.getAddress().isSiteLocalAddress()) {
					bc = interfaceAddress.getBroadcast();
					System.out.println("Broadcast IP : " + bc);
				}
			}
		}
		return bc;
	}
}
