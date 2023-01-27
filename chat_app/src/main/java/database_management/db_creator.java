package database_management;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import setup.Constants;

/*
 * Handles the creation of the database during the first launch of the application on a machine.
 */
public class db_creator {
	
	/* Attributes */
	private static Connection connection;
	
	
	
	/**
	 * Creates the database (if not already created) at the specified URL in the project folder. <br>
	 * Database file located at "./chat_app_db.db"
	 */
	public static void createNewDatabase() {
        try {
        	connection = DriverManager.getConnection(Constants.db_url);
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("db_creator : The driver name is " + meta.getDriverName());
                System.out.println("db_creator : A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	
	
	/**
	 * Creates the users table if it has not been already created <br><br>
	 * <b> User = (ipaddress, pseudo) </b>, with : <br>
	 * ipaddress	-> String (PRIMARY KEY) <br>
	 * pseudo		-> String <br>
	 */
	public static void createUsersTable() {		
		String sql = "CREATE TABLE IF NOT EXISTS users (\n"
				+ "	ipaddress varchar(15) PRIMARY KEY,\n"
				+ " pseudo varchar(20) NOT NULL\n"
				+ ");";
		try {
			connection = DriverManager.getConnection(Constants.db_url);
			Statement stmt = connection.createStatement();
			stmt.execute(sql);
			System.out.println("db_creator : Users Table has been created");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	/**
	 * Creates the conversation table if it has not been already created. <br><br>
	 * <b>A database message=(ippartner,ipsender,ipreceiver,message,time)</b> with : <br>
	 * ippartner	-> String    (PRIMARY KEY)	IP of the user we are talking with <br>
	 * ipsender		-> String					IP of the user who sent me a message / My IP if I sent a message <br>
	 * ipreceiver	-> String					My IP if I received a message / IP of the user who received my message <br>
	 * message		-> String					Content of the message sent <br>
	 * time			-> timestamp (PRIMARY KEY)	The time the message has been sent / received
	 */
	public static void createConvTable() {		
		String sql = "CREATE TABLE IF NOT EXISTS conversation (\n"
				+ "	ippartner varchar(15) NOT NULL,\n"
				+ " ipsender varchar(15) NOT NULL,\n"
				+ " ipreceiver varchar(15) NOT NULL,\n"
				+ " message text NOT NULL,\n"
				+ " time timestamp NOT NULL,\n"
				+ " PRIMARY KEY(ippartner, time)"
				+ ");";
		try {
			connection = DriverManager.getConnection(Constants.db_url);
			Statement stmt = connection.createStatement();
			stmt.execute(sql);
			System.out.println("db_creator : Conversation Table has been created");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void createAll() {
		createNewDatabase();
		createUsersTable();
		createConvTable();
	}
}
