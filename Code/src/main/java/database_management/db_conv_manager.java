package database_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class db_conv_manager extends db_manager {
	
	private static Connection connection;
	
	public static void insertMessage(String ippartner, String ipsender, String ipreceiver, String message, Timestamp timestamp) {
		String sql = "INSERT INTO conversation(ippartner,ipsender,ipreceiver,message,time)"
				+ " VALUES(?,?,?,?,?)";
		try {
			if (connection == null) {
				connection = connect();
			}			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, ippartner);
			pstmt.setString(2, ipsender);
			pstmt.setString(3, ipreceiver);
			pstmt.setString(4, message);
			pstmt.setTimestamp(5, timestamp);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void selectALLmessages() {
		String sql = "SELECT * FROM conversation";
		try {
			if (connection == null) {
				connection = connect();
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("time\t\t\t\tippartner\t\tipsender\t\tipreceiver\t\tmessage");
			while (rs.next()) {
				System.out.println(	rs.getTimestamp("time") + "\t\t" +
									rs.getString("ippartner") + "\t\t" +
									rs.getString("ipsender") + "\t\t" +
									rs.getString("ipreceiver") + "\t\t" +
									rs.getString("message"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
