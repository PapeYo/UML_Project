 package database_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * Consists in a list of functions (using SQL commands) regarding the user table in a private database.
 */
public class db_users_manager extends db_manager {
	
	/* Attributes */
	private static Connection connection;

	
	
	/**
	 * Inserts a user who broadcast or sent back a pseudo in a private database, along with their ipaddress.
	 * 
	 * @param ipaddress
	 * @param pseudo associated with the ipaddress
	 */
	public static void insertUser(String ipaddress, String pseudo) {
		String sql = "INSERT INTO users(ipaddress,pseudo) VALUES(?,?)";
		try {
			connection = connect();
			System.out.println("db_users_manager : insertUser");
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, ipaddress);
			pstmt.setString(2, pseudo);
			pstmt.executeUpdate();
			disconnect(connection);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	
	
	/**
	 * Returns the list of users in the database (ipaddress | pseudo).
	 */
	public static void selectALLusers() {
		String sql = "SELECT * FROM users";
		try {
			connection = connect();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("db_users_manager : selectALLusers");
			System.out.println("ipaddress \t\tpseudo");
			while (rs.next()) {
				System.out.println(	rs.getString("ipaddress") + "\t\t" +
						rs.getString("pseudo"));
			}
			disconnect(connection);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	/**
	 * @return
	 */
	public static ArrayList<String> listOfPseudo() {
		String sql = "SELECT pseudo FROM users";
		ArrayList<String> list = new ArrayList<String>();
		System.out.println("db_users_manager: entrée dans listOfPseudo");
		try {
			connection = connect();
			System.out.println("db_users_manager : listOfPseudo");
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String p = rs.getString("pseudo");
				System.out.println("pseudo:" + p);
				list.add(p);
			}
			disconnect(connection);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("db_users_manager : liste des users");
		for (int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
		return list;
	}
	
	
	
	/**
	 * Updates the pseudo of an already existing user (recognized by their ipaddress).
	 * 
	 * @param ipaddress
	 * @param pseudo associated with the ipaddress, has probably changed
	 */
	@SuppressWarnings("resource")
	public static void updateUserTable(String ipaddress, String pseudo) {
		String sql;
		PreparedStatement pstmt;
		try {
			connection = connect();
			System.out.println("db_users_manager : updateUserTable");
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
			disconnect(connection);
			selectALLusers();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	
	
	/**
	 * Returns the pseudo of a user if they exist and null if they don't (using the parameter ipaddress).
	 * 
	 * @param ipaddress
	 * @return <i>a pseudo | null</i>
	 * @throws SQLException
	 */
	public static String getUserPseudo(String ipaddress) throws SQLException {
		String sql = "SELECT pseudo FROM users WHERE ipaddress = ?";
		String res;
		PreparedStatement pstmt;
		connection = connect();
		System.out.println("db_users_manager : getUserPseudo");
		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, ipaddress);
		ResultSet rs = pstmt.executeQuery();
		res = rs.getString("pseudo");
		disconnect(connection);
		return res;
	}
	
	
	/**
	 * Calls selectUser to look for a user given their ipaddress, returns true if found, false else.
	 * 
	 * @param ipaddress
	 * @return <i>true | false</i>
	 * @throws SQLException
	 */
	public static boolean existingUserWithIP(String ipaddress) throws SQLException {
		if (getUserPseudo(ipaddress)==null) {
			return false;
		} else {
			return true;
		}
	}

	
	/**
	 * Given a pseudo, returns the ipaddress of a user (if such a user exists)
	 * 
	 * @param pseudo
	 * @return
	 * @throws SQLException
	 */
	public static String getUserIP(String pseudo) throws SQLException {
		String res;
		String sql = "SELECT ipaddress FROM users WHERE pseudo = ?";
		PreparedStatement pstmt;
		connection = connect();
		System.out.println("db_users_manager : getUserIp");
		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, pseudo);
		ResultSet rs = pstmt.executeQuery();
		res=rs.getString("ipaddress");		
		disconnect(connection);
		return res;
	}
	
	
	/**
	 * Given a pseudo, looks for an existing user in the database. <br>
	 * If one exists, selects their ipaddress, else returns "0.0.0.0" (which cannot be an ipaddress).
	 * 
	 * @param pseudo
	 * @return a String (ipaddress)
	 * @throws SQLException
	 */
	public static String existingUserWithPseudo(String pseudo) throws SQLException {
		String sql = "SELECT ipaddress FROM users WHERE pseudo=?";
		String res;
		PreparedStatement pstmt;
		connection = connect();
		System.out.println("db_users_manager : existingPseudo");		
		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, pseudo);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			res = rs.getString("ipaddress");
		} else {
			res = "0.0.0.0";
		}
		disconnect(connection);
		return res;
	}
	
	
	
	/**
	 * Removes a user for the database after a disconnection message, using their ipaddress
	 * 
	 * @param ipuser of the user who must be removed from the database
	 * @throws SQLException
	 */
	public static void removeUser(String ipaddress) throws SQLException {
		String sql = "DELETE FROM users WHERE ipaddress = ?";
		PreparedStatement pstmt;
		connection = connect();
		System.out.println("db_users_manager : removeUser");
		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, ipaddress);
		pstmt.executeUpdate();
		disconnect(connection);
		//selectALLusers();
	}



	public static void deleteTable() {
		String sql= "DROP TABLE users";
		connection = connect();
        try {
    		Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}