package database_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class db_users_manager extends db_manager {
	private static Connection connection;

	public static void insertUser(String ipaddress, String pseudo) {
		String sql = "INSERT INTO users(ipaddress,pseudo) VALUES(?,?)";
		try {
			if (connection == null) {
				connection = db_conv_manager.connect();
			}
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, ipaddress);
			pstmt.setString(2, pseudo);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void selectALLusers() {
		String sql = "SELECT * FROM users";
		try {
			if (connection == null) {
				connection = db_conv_manager.connect();
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(	rs.getString("ipaddress") + "\t" +
						rs.getString("pseudo"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
