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
				connection = connect();
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
				connection = connect();
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("ipaddress \t\t pseudo");
			while (rs.next()) {
				System.out.println(	rs.getString("ipaddress") + "\t\t" +
						rs.getString("pseudo"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@SuppressWarnings("resource")
	public static void updateUserTable(String ipaddress, String pseudo) {
		String sql;
		PreparedStatement pstmt;
		try {
			if (connection == null) {
				connection = connect();
			}
			sql = "SELECT * FROM users WHERE ipaddress = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, ipaddress);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				sql = "INSERT INTO users(ipaddress,pseudo) VALUES(?,?)";
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, ipaddress);
				pstmt.setString(2, pseudo);
				pstmt.executeUpdate();
				System.out.println(ipaddress + " has been added and the pseudo is " + pseudo);
			} else {
				sql = "UPDATE users SET pseudo = ? WHERE ipaddress = ?";
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, pseudo);
				pstmt.setString(2, ipaddress);
				pstmt.executeUpdate();
				System.out.println(ipaddress + " has been updated and the new pseudo is " + pseudo);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String selectUser(String localhostIP) {
		String pseudo = "SELECT pseudo FROM users WHERE ipaddress = localhostIP";
		if (connection == null) connection = connect();
		return pseudo;
	}
}
