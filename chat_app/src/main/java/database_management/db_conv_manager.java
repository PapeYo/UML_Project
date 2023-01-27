package database_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import app_interface.model.Message;

/*
 * Consists in a list of functions (using SQL commands) regarding the user table in a private database.
 */
public class db_conv_manager extends db_manager {
	
	/* Attributes */
	private static Connection connection;
	
	
	
	/**
	 * Adds a message to the database, using the given parameters.
	 * 
	 * @param ippartner
	 * @param ipsender
	 * @param ipreceiver
	 * @param message
	 * @param timestamp
	 */
	public static void insertMessage(String ippartner, String ipsender, String ipreceiver, String message, Timestamp timestamp) {
		String sql = "INSERT INTO conversation(ippartner,ipsender,ipreceiver,message,time)"
				+ " VALUES(?,?,?,?,?)";
		try {
			connection = connect();		
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, ippartner);
			pstmt.setString(2, ipsender);
			pstmt.setString(3, ipreceiver);
			pstmt.setString(4, message);
			pstmt.setTimestamp(5, timestamp);
			pstmt.executeUpdate();
			disconnect(connection);
			selectALLmessages();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	/**
	 * Finds then returns all of the database's messages 
	 */
	public static void selectALLmessages() {
		String sql = "SELECT * FROM conversation";
		try {
			connection = connect();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("db_conv_manager");
			System.out.println("time\t\t\t\tippartner\t\tipsender\t\tipreceiver\t\tmessage");
			while (rs.next()) {
				System.out.println(	rs.getTimestamp("time") + "\t\t" +
									rs.getString("ippartner") + "\t\t" +
									rs.getString("ipsender") + "\t\t" +
									rs.getString("ipreceiver") + "\t\t" +
									rs.getString("message"));
			}
			disconnect(connection);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static ArrayList<Message> selectConvWith(String ippartner) throws SQLException {
		ArrayList<Message> result = new ArrayList<Message>();
		
		String sql = "SELECT * FROM conversation WHERE ippartner = ? "  ;
		
		try {
			if (connection == null) connection = connect();
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1,ippartner);
			ResultSet rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				
				Message msg;
				if (rs.getString("ipsender") == rs.getString("ippartner")) {
					msg = new Message(1, db_users_manager.getUserPseudo(rs.getString("ipsender")), rs.getString("message"), rs.getTimestamp("time").toString() );
				}else {
					msg = new Message(0, db_users_manager.getUserPseudo(rs.getString("ipsender")), rs.getString("message"), rs.getTimestamp("time").toString() );
				}
				result.add(msg);
			}
			disconnect(connection);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return result;
	}
}