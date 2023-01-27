package database_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import setup.Constants;


/*
 * Handles connection to and disconnection from the database.
 */
public class db_manager {
	
	/* Attributes */
	private static Connection connection;
	
	
	
	/**
	 * Initiates a connection between the current thread and the database.
	 * 
	 * @return a connection used to access the database
	 */
	public static Connection connect() {
		try {
			connection = DriverManager.getConnection(Constants.db_url);
			System.out.println("db_manager : Connection is successful to the database " + Constants.db_url);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	
	/**
	 * Disconnects the current thread from the database.
	 * 
	 * @param connection is our access to the database (and update | read it)
	 */
	public static void disconnect(Connection connection) {
		try {
            if (connection != null) {
                connection.close();
                System.out.println("db_manager : Disconnection is successful.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
	}
}