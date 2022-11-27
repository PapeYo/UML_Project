package database_management;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class db_creator {
	private static Connection connection;

	public static void createNewDatabase() {

        String url = "jdbc:sqlite:C:/Chat_App/chat_app_db.db";

        try {
        	connection = DriverManager.getConnection(url);
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	public static void createUsersTable() {
		String url = "jdbc:sqlite:C:/Chat_App/chat_app_db.db";
		
		String sql = "CREATE TABLE IF NOT EXISTS users (\n"
				+ "	ipaddress varchar(15) PRIMARY KEY,\n"
				+ " pseudo varchar(20) NOT NULL\n"
				+ ");";
		
		try {
			connection = DriverManager.getConnection(url);
			Statement stmt = connection.createStatement();
			stmt.execute(sql);
			System.out.println("Users Table has been created");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void createConvTable() {
		String url = "jdbc:sqlite:C:/Chat_App/chat_app_db.db";
		
		String sql = "CREATE TABLE IF NOT EXISTS conversation (\n"
				+ "	ippartner varchar(15) NOT NULL,\n"
				+ " ipsender varchar(15) NOT NULL,\n"
				+ " ipreceiver varchar(15) NOT NULL,\n"
				+ " message text NOT NULL,\n"
				+ " time timestamp NOT NULL,\n"
				+ " PRIMARY KEY(ippartner, time)"
				+ ");";
		
		try {
			connection = DriverManager.getConnection(url);
			Statement stmt = connection.createStatement();
			stmt.execute(sql);
			System.out.println("Conversation Table has been created");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String args[]) {
		createNewDatabase();
		createUsersTable();
		createConvTable();
	}
}
